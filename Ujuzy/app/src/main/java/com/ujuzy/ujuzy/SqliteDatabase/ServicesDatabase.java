package com.ujuzy.ujuzy.SqliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ujuzy.ujuzy.model.Constants;
import com.ujuzy.ujuzy.model.Datum;

import java.util.ArrayList;
import java.util.List;

public class ServicesDatabase extends SQLiteOpenHelper
{

    public ServicesDatabase(Context context) {
        super(context, Constants.DATABASE.DB_NAME,null, Constants.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(Constants.DATABASE.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL(Constants.DATABASE.DROP_QUERY);
        this.onCreate(sqLiteDatabase);
    }

    public void addServicesToSqlDB(Datum service)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.DATABASE.ID, service.getId());
        values.put(Constants.DATABASE.SERVICE_NAME, service.getServiceName());
        values.put(Constants.DATABASE.SERVICE_DETAILS, service.getServiceDetails());
        values.put(Constants.DATABASE.PHOTO_URL, service.getImages().get(0).toString());

        sqLiteDatabase.insert(Constants.DATABASE.TABLE_NAME, null, values);
        sqLiteDatabase.close();
    }

    public List<Datum> getServicesFromSqlDB()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(Constants.DATABASE.GET_SERVICE_QUERY, null);

        List<Datum> serviceList = new ArrayList<>();

        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {

                do {

                    Datum service = new Datum();
                    service.setServiceName(cursor.getColumnName(cursor.getColumnIndex(Constants.DATABASE.SERVICE_NAME)));
                    service.setServiceDetails(cursor.getColumnName(cursor.getColumnIndex(Constants.DATABASE.SERVICE_DETAILS)));

                } while (cursor.moveToNext());

            }

        }

        return serviceList;
    }
}
