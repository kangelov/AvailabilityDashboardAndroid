package com.qualicom.availabilitydashboard.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Environment;
import com.qualicom.availabilitydashboard.vo.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangelov on 2015-09-01.
 */
class EnvironmentDBHelper {

    private EnvironmentDBHelper() {
        //do not instantiate
    }

    public static List<Environment> getAllEnvironments(SQLiteDatabase db) {
        Cursor cursor = db.query(DBHelper.TABLE_ENVIRONMENT,
                new String[]{DBHelper.COL_ENVIRONMENT_ID, DBHelper.COL_ENVIRONMENT_NAME, DBHelper.COL_ENVIRONMENT_STATUS},
                null,
                null,
                null, null, DBHelper.COL_ENVIRONMENT_NAME + " ASC");
        ArrayList<Environment> envList = new ArrayList<Environment>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long envId = cursor.getInt(0);
                    String name = cursor.getString(1);
                    Status status = Status.valueOf(cursor.getString(2));
                    if (!TextUtils.isEmpty(name)) {
                        envList.add(new Environment(name, status, ServiceDBHelper.getServiceByEnvironmentID(db, envId)));
                    }
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("Query Database", "Error querying Environment table.", e);
            throw e;
        } finally {
            cursor.close(); //db gets closed by the classes creating it.
        }
        return envList;
    }

    public static void insertEnvironments(SQLiteDatabase db, List<Environment> envList) {
        if (envList != null) {
            for (Environment env : envList) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.COL_ENVIRONMENT_NAME, env.getName());
                values.put(DBHelper.COL_ENVIRONMENT_STATUS, env.getStatus() != null ? env.getStatus().name() : Status.UNKNOWN.name());
                try {
                    db.beginTransaction();
                    long envId = db.insertWithOnConflict(DBHelper.TABLE_ENVIRONMENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    ServiceDBHelper.insertServicesByEnvID(db, env.getServices(), envId);
                    db.setTransactionSuccessful();
                } catch (SQLiteException e) {
                    Log.e("Write Database", "Error writing Environment table for environment with name " + env.getName() + ".", e);
                    throw e;
                } finally {
                    db.endTransaction();
                }
            }
        }
    }

    public static void deleteAllEnvironments(SQLiteDatabase db) {
        ServiceDBHelper.deleteAllServices(db);
        db.delete(DBHelper.TABLE_ENVIRONMENT, null, null);
    }

}
