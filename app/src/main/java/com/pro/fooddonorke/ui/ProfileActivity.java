package com.pro.fooddonorke.ui;

import static android.graphics.ImageDecoder.decodeBitmap;
import static android.provider.MediaStore.Images.Media.getBitmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pro.fooddonorke.dialog.SetAvatarDialogFragment;
import com.pro.fooddonorke.interfaces.ImageCaptureListener;
import com.pro.fooddonorke.models.ProfileData;
import com.pro.fooddonorke.R;
import com.pro.fooddonorke.utilities.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements ImageCaptureListener {
    public static final String TAG = ProfileActivity.class.getSimpleName();
    TextInputLayout name, phone, location, description;
    Button updateButton;
    ImageView profileImage;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root;
    private ValueEventListener profileDataListener;
    private boolean storagePermission = false;
    private SetAvatarDialogFragment setAvatarDialogFragment;
    private StorageReference mStorageReference;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ActivityResultLauncher<String[]> requestStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Log.d(TAG, "Requesting storage permissions");
                result.forEach((key, value) -> {
                    if (value) {
                        Log.d(TAG, String.format("User has allowed permission %s", key));
                    }
                });
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        updateButton = findViewById(R.id.updateButton);
        profileImage = findViewById(R.id.profileImage);

        addAnimations();

        String userId = user.getUid();
        root = db.getReference(Constants.FIREBASE_CHILD_PROFILE).child(userId);
        mStorageReference = FirebaseStorage.getInstance().getReference(Constants.FOOD_DONORS).child(userId).child(Constants.STORAGE_AVATAR);

        setProfileData();

        updateButton.setOnClickListener(view -> updateProfileData());
        profileImage.setOnClickListener(view -> openAvatarDialog());
    }

    private void verifyStoragePermissions() {
        Log.d(TAG, "Asking user for storage permissions.");
        String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA };

        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED
        ) {
            storagePermission = true;
        } else {
            // Request for permissions
            requestStoragePermission.launch(permissions);
        }
    }


    private void updateProfileData(){
        // Gather input
        String profileName = Objects.requireNonNull(name.getEditText()).getText().toString();
        String profilePhone = Objects.requireNonNull(phone.getEditText()).getText().toString();
        String profileLocation = Objects.requireNonNull(location.getEditText()).getText().toString();
        String profileDescription = Objects.requireNonNull(description.getEditText()).getText().toString();
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();

        Task<Uri> uploadTask = mStorageReference.putBytes(compressImage(bitmap))
                .addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Log.d(TAG, "Upload progress: " + progress);
                    Toast.makeText(getApplicationContext(), String.format("Upload progress: %d", (int) progress), Toast.LENGTH_SHORT).show();
                })
                .addOnPausedListener(snapshot -> {
                    Log.d(TAG, "Upload paused");
                    Toast.makeText(getApplicationContext(), R.string.paused, Toast.LENGTH_SHORT).show();
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return mStorageReference.getDownloadUrl();
                })
                .addOnCompleteListener(this, insertTask -> {
                    if (insertTask.isSuccessful()){
                        updateFirebaseProfile(insertTask.getResult().toString(), profileName);

                        ProfileData profileData = new ProfileData(profilePhone, profileLocation, profileDescription);

                        root.setValue(profileData).addOnCompleteListener(ProfileActivity.this, updateTask -> {
                            if (updateTask.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, Objects.requireNonNull(updateTask.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }

    private void updateFirebaseProfile(String imageUrl, String name){
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(imageUrl))
                .build();

        user.updateProfile(changeRequest)
                .addOnCompleteListener(this, updateTask -> {
                    if (updateTask.isSuccessful()){
                        Log.d(TAG, "Firebase profile updated");
                    } else {
                        Log.e(TAG, "Firebase profile not updated", updateTask.getException());
                    }
                });
    }

    private byte[] compressImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void setProfileData(){
        profileDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ProfileData profileData = snapshot.getValue(ProfileData.class);

                    Objects.requireNonNull(name.getEditText()).setText(profileData != null ? Objects.requireNonNull(user).getDisplayName() : "");
                    Objects.requireNonNull(phone.getEditText()).setText(profileData != null ? profileData.getPhone() : "");
                    Objects.requireNonNull(location.getEditText()).setText(profileData != null ? profileData.getLocation() : "");
                    Objects.requireNonNull(description.getEditText()).setText(profileData != null ? profileData.getDescription() : "");
                    Glide.with(getApplicationContext()).asBitmap().load(Objects.requireNonNull(user).getPhotoUrl()).placeholder(R.drawable.ic_baseline_account_circle).into(profileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching profile data", error.toException());
            }
        };
    }

    private void addAnimations(){
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(profileImage);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(name);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(phone);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(location);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(description);

        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(updateButton);

    }

    private void openAvatarDialog(){
        if (storagePermission){
            // Initialize fragment manager that's responsible for adding, replacing, removing fragments dynamically
            FragmentManager fragmentManager = getSupportFragmentManager();
            // Initialize the theme selection fragment
            setAvatarDialogFragment = new SetAvatarDialogFragment();
            // Display the theme selection fragment
            setAvatarDialogFragment.show(fragmentManager, "Set avatar dialog fragment");
        } else {
            verifyStoragePermissions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        root.addValueEventListener(profileDataListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        root.removeEventListener(profileDataListener);
    }

    @Override
    public void getImageUri(Uri uri) {
        Glide.with(this).asBitmap().load(uri).placeholder(R.drawable.ic_baseline_account_circle).into(profileImage);
        setAvatarDialogFragment.dismiss();
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Glide.with(this).asBitmap().load(bitmap).placeholder(R.drawable.ic_baseline_account_circle).into(profileImage);
        setAvatarDialogFragment.dismiss();
    }
}