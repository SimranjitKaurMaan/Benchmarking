package db_SQLite;

import services.Forecast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ForecastDataSource
{
	private SQLiteDatabase mDatabase;

	private ForecastSQLiteHelper mForecastSQLiteHelper;

	private Context mContext;

	public static final String TAG = ForecastDataSource.class.getSimpleName();

	int n = 0;

	long t1, t2;

	public ForecastDataSource(Context context)
	{
		mContext = context;
		mForecastSQLiteHelper = new ForecastSQLiteHelper(mContext);
	}

	// open database
	public void open() throws SQLException
	{
		mDatabase = mForecastSQLiteHelper.getWritableDatabase();

	}

	// close database

	public void close()
	{
		mDatabase.close();

	}

	// insert
	public void insert(Forecast forecast)
	{
		Log.d(TAG, "Inserting temperatures in table started .... ");
		t1 = System.currentTimeMillis();
		mDatabase.beginTransaction();
		try
		{
			for (Forecast.HourData hour : forecast.hourly.data)
			{
				ContentValues values = new ContentValues();
				values.put(ForecastSQLiteHelper.COLUMN_TEMPERATURE, hour.temperature);
				mDatabase.insert(ForecastSQLiteHelper.TABLE_TEMPERATURES, null, values);
				n++;
			}
			mDatabase.setTransactionSuccessful();
		}
		finally
		{
			mDatabase.endTransaction();
		}

		t2 = System.currentTimeMillis();
		Log.d(TAG, "Inserting temperatures in table complete, time taken : " + (t2 - t1) + ", rows inserted : " + n);
	}

	// select
	public Cursor selectAllTemperatures()
	{

		t1 = System.currentTimeMillis();
		Cursor cursor = mDatabase.query(ForecastSQLiteHelper.TABLE_TEMPERATURES, new String[] { ForecastSQLiteHelper.COLUMN_TEMPERATURE }, null, // where
																																					// clause
				null, // where params
				null, // group by
				null, // having
				null // orderby
				);
		t2 = System.currentTimeMillis();
		Log.d(TAG, "Selecting all temperatures in table complete, time taken : " + (t2 - t1));

		return cursor;
	}

	// update
	public int updateTemperatures(double newTemp)
	{
		Log.d(TAG, "Updating all temperatures in table started .... ");
		t1 = System.currentTimeMillis();
		ContentValues values = new ContentValues();
		values.put(ForecastSQLiteHelper.COLUMN_TEMPERATURE, newTemp);
		int rowsUpdated = mDatabase.update(ForecastSQLiteHelper.TABLE_TEMPERATURES, values, null, null);

		t2 = System.currentTimeMillis();
		Log.d(TAG, "Updating all temperatures in table complete, time taken : " + (t2 - t1) + ", rows updated : " + rowsUpdated);

		return rowsUpdated;
	}

	// delete
	public void deleteAll()
	{

		Log.d(TAG, "Deleting all temperatures in table started .... ");
		t1 = System.currentTimeMillis();
		mDatabase.delete(ForecastSQLiteHelper.TABLE_TEMPERATURES, null, // whereclause
				null // whereargs
				);
		t2 = System.currentTimeMillis();
		Log.d(TAG, "Deleting all temperatures in table complete, time taken : " + (t2 - t1) + ", rows deleted : " + n);
		n = 0;
	}

}
