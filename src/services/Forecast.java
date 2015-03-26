package services;

import java.util.List;

public class Forecast
{

	public  HourlyForecast hourly;

	public class HourlyForecast
	{
		public List<HourData> data;
	}

	public class HourData
	{
		public Double temperature;
	}

}
