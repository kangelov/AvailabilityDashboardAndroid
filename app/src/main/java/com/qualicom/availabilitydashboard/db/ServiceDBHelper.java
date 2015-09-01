package com.qualicom.availabilitydashboard.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Service;
import com.qualicom.availabilitydashboard.vo.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangelov on 2015-09-01.
 */
class ServiceDBHelper {

    private ServiceDBHelper() {
        //do not instantiate
    }

    public static List<Service> getServiceByEnvironmentID(SQLiteDatabase db, long envId) {
        Cursor cursor = db.query(DBHelper.TABLE_SERVICE,
                new String[]{DBHelper.COL_SERVICE_ID, DBHelper.COL_SERVICE_NAME, DBHelper.COL_SERVICE_STATUS},
                DBHelper.COL_SERVICE_ENVID + " = ?",
                new String[]{Long.toString(envId)},
                null, null, DBHelper.COL_SERVICE_NAME + " ASC");
        ArrayList<Service> serviceList = new ArrayList<Service>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    long serviceId = cursor.getLong(0);
                    String name = cursor.getString(1);
                    Status status = Status.valueOf(cursor.getString(2));
                    if (!TextUtils.isEmpty(name)) {
                        serviceList.add(new Service(name, status, NodeDBHelper.getNodesByServiceID(db, serviceId)));
                    }
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("Query Database", "Error querying Service table for envId " + envId + ".", e);
            throw e;
        } finally {
            cursor.close(); //db gets closed by the classes creating it.
        }
        return serviceList;
    }

    public static void insertServicesByEnvID(SQLiteDatabase db, List<Service> serviceList, long envId) {
        if (serviceList != null && envId > -1) {
            for (Service service : serviceList) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.COL_SERVICE_NAME, service.getName());
                values.put(DBHelper.COL_SERVICE_STATUS, service.getStatus() != null ? service.getStatus().name() : Status.UNKNOWN.name());
                values.put(DBHelper.COL_SERVICE_ENVID, Long.toString(envId));
                try {
                    db.beginTransaction();
                    long serviceId = db.insertWithOnConflict(DBHelper.TABLE_SERVICE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    NodeDBHelper.insertNodesByServiceID(db, service.getNodes(), serviceId);
                    db.setTransactionSuccessful();
                } catch (SQLiteException e) {
                    Log.e("Write Database", "Error writing Service table for envId " + envId + " for service with name " + service.getName() + ".", e);
                    throw e;
                } finally {
                    db.endTransaction();
                }
            }
        }
    }

    public static void deleteAllServices(SQLiteDatabase db) {
        NodeDBHelper.deleteAllNodes(db);
        db.delete(DBHelper.TABLE_SERVICE, null, null);
    }

}
