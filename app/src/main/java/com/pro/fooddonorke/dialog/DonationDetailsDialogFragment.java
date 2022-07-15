package com.pro.fooddonorke.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pro.fooddonorke.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonationDetailsDialogFragment extends DialogFragment {
  public static final String TAG = DonationDetailsDialogFragment.class.getSimpleName();
  private static final String ARG_EMAIL = "Charity email";
  private String email;

  @BindView(R.id.btn_add)
  FloatingActionButton btnAdd;
  @BindView(R.id.btn_send_mail)
  Button btnSendMail;
  @BindView(R.id.donation_input)
  TextInputLayout donationInput;
  @BindView(R.id.donation_inputs)
  ChipGroup donationInputs;

  public DonationDetailsDialogFragment() {
  }

  public static DonationDetailsDialogFragment newInstance(String email){
    DonationDetailsDialogFragment fragment = new DonationDetailsDialogFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ARG_EMAIL, email);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    assert getArguments() != null;
    email = getArguments().getString(ARG_EMAIL);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_donation_details_dialog, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    btnAdd.setOnClickListener(addButton -> {
      String input = Objects.requireNonNull(donationInput.getEditText()).getText().toString();

      if (input.isEmpty()){
        Toast.makeText(getContext(), R.string.empty_input, Toast.LENGTH_SHORT).show();
      } else {
        populateDonationInputs(input);
        clearInput();
      }
    });

    btnSendMail.setOnClickListener(mailButton -> {
      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      String subject = "An intent to donate";
      StringBuilder body = new StringBuilder("Hello, \n We would like to donate the following: ");
      int no = 1;

      List<String> donationItems = getDonationItems(donationInputs);

      if (donationItems.isEmpty()){
        Toast.makeText(getContext(), R.string.empty_input, Toast.LENGTH_SHORT).show();
      } else {
        for (String donationItem: donationItems) {
          body.append(String.format(Locale.ENGLISH, "\n %d %s", no++ ,donationItem));
        }

        body.append(String.format(Locale.ENGLISH, "\n\n Kind regards, \n %s.", Objects.requireNonNull(user).getDisplayName()));

        sendMail(email, subject, body.toString());
      }
    });
  }

  private List<String> getDonationItems(ChipGroup chipGroup){
    int count = chipGroup.getChildCount();
    List<String> donationInputs = new ArrayList<>();

    for (int i = 0; i < count ; i++) {
      Chip input = (Chip) chipGroup.getChildAt(i);

      String donationItem = input.getText().toString();
      donationInputs.add(donationItem);
    }

    return donationInputs;
  }

  private void sendMail(String email, String subject, String body){
    dismiss();
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setType("message/rfc822"); // Only email apps handle this intent
    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(Intent.EXTRA_TEXT, body);
    startActivity(Intent.createChooser(emailIntent, "Choose an email client: "));
  }

  private void populateDonationInputs(String text) {
    Chip chip = new Chip(requireContext());
    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.InputChipStyle);
    chip.setChipDrawable(chipDrawable);
    chip.setText(text);
    donationInputs.addView(chip);
  }

  private void clearInput(){
    Objects.requireNonNull(donationInput.getEditText()).setText("");
  }
}
