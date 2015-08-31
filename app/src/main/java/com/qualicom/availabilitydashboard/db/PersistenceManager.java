package com.qualicom.availabilitydashboard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Settings;

/**
 * Created by kangelov on 2015-08-31.
 */
public class PersistenceManager extends SQLiteOpenHelper {

    private final static String DBNAME = "AvailabilityDashboard";
    private final static int DBVERSION = 1;

    public PersistenceManager(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : DBHelper.getCreateSQLs()) {
            Log.i("Create Database", sql);
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String sql : DBHelper.getUpgradeSQLs(oldVersion, newVersion)) {
            Log.i("Upgrade Database", sql);
            db.execSQL(sql);
        }
    }

    public Settings getSettings() {
        SQLiteDatabase db = null;
        Settings settings = null;
        try {
            db = this.getReadableDatabase(); //we only read, so readable database.
            settings = SettingsDBHelper.getSettings(db);
        } catch (SQLiteException e) {
            Log.e("Query Settings", e.getMessage(), e);
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        return settings;
    }

}
