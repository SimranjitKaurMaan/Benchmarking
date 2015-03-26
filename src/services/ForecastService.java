package services;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class ForecastService
{

	private static final String API_URL = "https://api.forecast.io/";

	public interface WeatherService
	{
	        @GET("/forecast/{key}/{latitude},{longitude}")
	        public void getForecastAsync(
	                @Path("key") String key,
	                @Path("latitude") String lat,
	                @Path("longitude") String longitude,
	                Callback<Forecast> callback
	        );
	}

	// loading forecast data from forecast.io
	public void loadForecastData(Callback<Forecast> callback)
	{
         RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_URL).build();
       
         WeatherService service = restAdapter.create(WeatherService.class);
         service.getForecastAsync("929d214ed2dfd3705ff1ee66c1d5c3be","37.8267","-122.423", callback);
                                  
	}

}
