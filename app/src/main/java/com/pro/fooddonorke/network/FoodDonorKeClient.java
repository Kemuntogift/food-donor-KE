package com.pro.fooddonorke.network;

import com.pro.fooddonorke.utilities.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodDonorKeClient {
  private static Retrofit retrofit = null;

  public static FoodDonorKeApi getClient(){
    if (retrofit == null){
      retrofit = new Retrofit.Builder()
              .baseUrl(Constants.API_BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit.create(FoodDonorKeApi.class);
  }
}
