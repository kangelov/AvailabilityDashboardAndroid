package com.qualicom.availabilitydashboard.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Settings;

/**
 * Created by kangelov on 2015-08-31.
 */
class SettingsDBHelper {

    public static final String SETTINGS_KEY_URI = "uri";
    public static final String SETTINGS_KEY_USERNAME = "username";
    public static final String SETTINGS_KEY_PASSWORD = "password";

    private SettingsDBHelper() {
        //Don't instantiate
    }

    public static Settings getSettings(SQLiteDatabase db) {
        String uri = getSettingsValue(db, SETTINGS_KEY_URI);
        if (TextUtils.isEmpty(uri))
            return null;
        String username = getSettingsValue(db, SETTINGS_KEY_USERNAME);
        String password = getSettingsValue(db, SETTINGS_KEY_PASSWORD);
        return new Settings(uri, username, password);
    }

    public static void setSettings(SQLiteDatabase db, Settings settings) {
        db.beginTransaction();
        try {
            setSettingsValue(db, SETTINGS_KEY_URI, settings.getUri());
            setSettingsValue(db, SETTINGS_KEY_USERNAME, settings.getUsername());
            setSettingsValue(db, SETTINGS_KEY_PASSWORD, settings.getPassword());
            db.setTransactionSuccessful();
            Log.i("Write Database", "New settings saved successfully.");
        } catch (SQLiteException e) {
            Log.i("Write Database", "New settings will now be discarded.");
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    private static void setSettingsValue(SQLiteDatabase db, String key, String value) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COL_SETTINGS_KEY, key);
        values.put(DBHelper.COL_SETTINGS_VALUE, value);
        try {
            db.insertWithOnConflict(DBHelper.TABLE_SETTINGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLiteException e) {
            Log.e("Write Database", "Error writing Settings table for key " + key + " with value " + value + ".");
            throw e;
        }
    }


    private static String getSettingsValue(SQLiteDatabase db, String key) {
        Cursor cursor = db.query(DBHelper.TABLE_SETTINGS,
                new String[]{DBHelper.COL_SETTINGS_KEY, DBHelper.COL_SETTINGS_VALUE},
                DBHelper.COL_SETTINGS_KEY + " = ?",
                new String[]{key},
                null, null, null);
        String value = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    value = cursor.getString(1); //Value column.
                } while (cursor.moveToNext()); //there should be only one.
            }
        } catch (SQLiteException e) {
            Log.e("Query Database", "Error querying Settings table for key " + key + ".", e);
            throw e;
        } finally {
            cursor.close(); //db gets closed by the classes creating it.
        }
        return value;
    }


}
