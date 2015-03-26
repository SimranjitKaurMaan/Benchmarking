package ui;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.purpleberrylabs.benchmarkingdb.R;

import db_SQLite.ForecastDataSource;
import db_SQLite.ForecastSQLiteHelper;

public class ViewForecastActivity extends ListActivity
{

	protected ForecastDataSource mDataSource;

	protected ArrayList<BigDecimal> mTemperatures;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_forecast);
		mDataSource = new ForecastDataSource(ViewForecastActivity.this);
		mTemperatures = new ArrayList<BigDecimal>();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mDataSource.open();

		Cursor cursor = mDataSource.selectAllTemperatures();
		updateList(cursor);

	}

	private void updateList(Cursor cursor)
	{
		mTemperatures.clear();

		cursor.moveToFirst();
		int i = cursor.getColumnIndex(ForecastSQLiteHelper.COLUMN_TEMPERATURE);
		while (!cursor.isAfterLast())
		{
		   double temperature = cursor.getDouble(i);
		   mTemperatures.add(new BigDecimal(temperature,MathContext.DECIMAL32));
           cursor.moveToNext();
		}

		ArrayAdapter<BigDecimal> adapter = new ArrayAdapter<>(ViewForecastActivity.this, android.R.layout.simple_list_item_1, mTemperatures);

		setListAdapter(adapter);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mDataSource.close();
	}
}
