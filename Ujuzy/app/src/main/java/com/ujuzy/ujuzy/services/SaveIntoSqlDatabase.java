package com.ujuzy.ujuzy.services;

import android.os.AsyncTask;

import com.ujuzy.ujuzy.SqliteDatabase.ServicesDatabase;
import com.ujuzy.ujuzy.model.Datum;

public class SaveIntoSqlDatabase extends AsyncTask<Datum, Datum, Boolean>
{

    ServicesDatabase sqliteDatabase;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Datum... data) {
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

   /* @Override
    protected void onProgressUpdate(Datum... values) {
        super.onProgressUpdate(values);

        ServicesDatabase sqliteDatabase = new ServicesDatabase(this);
        sqliteDatabase.addServicesToSqlDB(values[0]);
    }*/
}
