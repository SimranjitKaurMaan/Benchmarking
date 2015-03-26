package ui;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.Forecast;
import services.ForecastService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.purpleberrylabs.benchmarkingdb.R;

import db_SQLite.ForecastDataSource;

public class MainActivity extends ActionBarActivity
{

	public static final String TAG = MainActivity.class.getSimpleName();

	protected ForecastDataSource mDataSource;

	protected Button mInsertButton;

	protected Button mSelectButton;

	protected Button mUpdateButton;

	protected Button mDeleteButton;

	protected double[] mTemperatures;

	

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDataSource = new ForecastDataSource(MainActivity.this);

		initialiseViews();

		setOnClickListeners();

	}

	private void setOnClickListeners()
	{
		mInsertButton.setOnClickListener(onInsertClickListener);
		mSelectButton.setOnClickListener(onSelectClickListener);
		mUpdateButton.setOnClickListener(onUpdateClickListener);
		mDeleteButton.setOnClickListener(onDeleteClickListener);

	}

	private void initialiseViews()
	{

		
		mInsertButton = (Button) findViewById(R.id.insertButton);
		mSelectButton = (Button) findViewById(R.id.selectButton);
		mUpdateButton = (Button) findViewById(R.id.updateButton);
		mDeleteButton = (Button) findViewById(R.id.deleteButton);
	}
   
	OnClickListener onDeleteClickListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			mDataSource.deleteAll();
			
		}
	};
	
	
	OnClickListener onInsertClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			loadForecastData();

		}
	};

	OnClickListener onSelectClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			startActivity(new Intent(MainActivity.this, ViewForecastActivity.class));

		}
	};

	OnClickListener onUpdateClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			mDataSource.updateTemperatures(100);

		}
	};

	protected void loadForecastData()
	{
		ForecastService service = new ForecastService();
		service.loadForecastData(mForecastCallback);
	}

	protected Callback<Forecast> mForecastCallback = new Callback<Forecast>()
	{
		@Override
		public void success(Forecast forecast, Response response)
		{
			mTemperatures = new double[forecast.hourly.data.size()];
			for (int i = 0; i < forecast.hourly.data.size(); i++)
			{
				mTemperatures[i] = forecast.hourly.data.get(i).temperature;
				Log.v(TAG, "Temp " + i + ": " + mTemperatures[i]);
			}

			mDataSource.insert(forecast);
			enableOtherButtons();
		}

		@Override
		public void failure(RetrofitError error)
		{
			Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	};

	
	
	private void enableOtherButtons()
	{
		mSelectButton.setEnabled(true);
		mUpdateButton.setEnabled(true);
		mDeleteButton.setEnabled(true);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mDataSource.open();

	}

	@Override
	public void onPause()
	{
		super.onPause();
		mDataSource.close();
	}

}
