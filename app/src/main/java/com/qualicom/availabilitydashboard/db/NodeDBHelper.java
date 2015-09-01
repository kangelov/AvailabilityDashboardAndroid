package com.qualicom.availabilitydashboard.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.qualicom.availabilitydashboard.vo.Node;
import com.qualicom.availabilitydashboard.vo.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangelov on 2015-09-01.
 */
class NodeDBHelper {

    private NodeDBHelper() {
        //Do not instantiate.
    }

    public static List<Node> getNodesByServiceID(SQLiteDatabase db, long serviceId) {
        Cursor cursor = db.query(DBHelper.TABLE_NODE,
                new String[]{DBHelper.COL_NODE_NAME, DBHelper.COL_NODE_STATUS, DBHelper.COL_NODE_VERSION, DBHelper.COL_NODE_RESPONSE},
                DBHelper.COL_NODE_SERVICEID + " = ?",
                new String[]{Long.toString(serviceId)},
                null, null, DBHelper.COL_NODE_NAME + " ASC");
        ArrayList<Node> nodeList = new ArrayList<Node>();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    Status status = Status.valueOf(cursor.getString(1));
                    String version = cursor.getString(2);
                    String response = cursor.getString(3);
                    if (!TextUtils.isEmpty(name)) {
                        nodeList.add(new Node(name, status, response, version));
                    }
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.e("Query Database", "Error querying Node table for serviceId " + serviceId + ".", e);
            throw e;
        } finally {
            cursor.close(); //db gets closed by the classes creating it.
        }
        return nodeList;
    }

    public static void insertNodesByServiceID(SQLiteDatabase db, List<Node> nodeList, long serviceId) {
        if (nodeList != null && serviceId > -1) {
            for (Node node : nodeList) {
                ContentValues values = new ContentValues();
                values.put(DBHelper.COL_NODE_NAME, node.getName());
                values.put(DBHelper.COL_NODE_STATUS, node.getStatus() != null ? node.getStatus().name() : Status.UNKNOWN.name());
                values.put(DBHelper.COL_NODE_RESPONSE, node.getResponse());
                values.put(DBHelper.COL_NODE_VERSION, node.getVersion());
                values.put(DBHelper.COL_NODE_SERVICEID, Long.toString(serviceId));
                try {
                    db.insertWithOnConflict(DBHelper.TABLE_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (SQLiteException e) {
                    Log.e("Write Database", "Error writing Node table for serviceId " + serviceId + " for node with name " + node.getName() + ".", e);
                    throw e;
                }
            }
        }
    }

    public static void deleteAllNodes(SQLiteDatabase db) {
        db.delete(DBHelper.TABLE_NODE, null, null);
    }

}
