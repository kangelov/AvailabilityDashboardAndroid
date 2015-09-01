package com.qualicom.availabilitydashboard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Environment;
import com.qualicom.availabilitydashboard.vo.ListEntry;
import com.qualicom.availabilitydashboard.vo.Settings;

import java.util.List;

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
            try {
                db.execSQL(sql);
            } catch (SQLiteException e) {
                Log.e("Create Database", e.getMessage(), e);
                throw e;
            }
        }
        //Now load sample data for me to look at.
        Log.i("Sample Data", "About to load some sample data into SQLite. This should be removed when development is complete.");
        try {
            db.beginTransaction();
            setAllEnvironments(ListEntry.dummyEntries, db);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e("Sample Data", e.getMessage(), e);
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String sql : DBHelper.getUpgradeSQLs(oldVersion, newVersion)) {
            Log.i("Upgrade Database", sql);
            try {
                db.execSQL(sql);
            } catch (SQLiteException e) {
                Log.e("Upgrade Database", e.getMessage(), e);
                throw e;
            }
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

    public void setSettings(Settings settings) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            SettingsDBHelper.setSettings(db, settings);
        } catch (SQLiteException e) {
            Log.e("Write Settings", e.getMessage(), e);
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }

    public List<Environment> getAllEnvironments() {
        SQLiteDatabase db = null;
        List<Environment> envList = null;
        try {
            db = this.getReadableDatabase(); //we only read, so readable database.
            envList = EnvironmentDBHelper.getAllEnvironments(db);
        } catch (SQLiteException e) {
            Log.e("Query Environment", e.getMessage(), e);
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        return envList;
    }

    public void setAllEnvironments(List<Environment> envList) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            setAllEnvironments(envList, db);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e("Write Environment", e.getMessage(), e);
        } finally {
            try {
                db.endTransaction();
                db.close();
            } catch (Exception e) {
            }
        }
    }

    private void setAllEnvironments(List<Environment> envList, SQLiteDatabase db) {
        EnvironmentDBHelper.deleteAllEnvironments(db);
        EnvironmentDBHelper.insertEnvironments(db, envList);
    }

}
