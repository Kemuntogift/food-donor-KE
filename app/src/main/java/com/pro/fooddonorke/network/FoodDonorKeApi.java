package com.pro.fooddonorke.network;

import com.pro.fooddonorke.models.CharitiesSearchResponse;
import com.pro.fooddonorke.models.Charity;
import com.pro.fooddonorke.models.CharityDetailResponse;
import com.pro.fooddonorke.models.RequestsSearchResponse;
import com.pro.fooddonorke.utilities.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodDonorKeApi {
  @GET("charities/locations")
  Call<CharitiesSearchResponse> getCharitiesByLocation(@Query(Constants.LOCATION_QUERY_PARAMETER) String location);

  @GET("requests/locations")
  Call<RequestsSearchResponse> getRequestsByLocation(@Query(Constants.LOCATION_QUERY_PARAMETER) String location);

  @GET("charities/:id")
  Call<CharityDetailResponse> getCharityDetails(@Path("id") String id);
}
