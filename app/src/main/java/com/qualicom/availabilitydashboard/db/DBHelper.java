package com.qualicom.availabilitydashboard.db;

/**
 * Created by kangelov on 2015-08-31.
 */
class DBHelper {

    public final static String TABLE_ENVIRONMENT = "Environment";
    public final static String COL_ENVIRONMENT_ID = "_id";
    public final static String COL_ENVIRONMENT_NAME = "name";
    public final static String COL_ENVIRONMENT_STATUS = "status";
    public final static String TABLE_SERVICE = "Service";
    public final static String COL_SERVICE_ID = "_id";
    public final static String COL_SERVICE_NAME = "name";
    public final static String COL_SERVICE_STATUS = "status";
    public final static String COL_SERVICE_ENVID = "env";
    public final static String TABLE_NODE = "Node";
    public final static String COL_NODE_ID = "_id";
    public final static String COL_NODE_NAME = "name";
    public final static String COL_NODE_STATUS = "status";
    public final static String COL_NODE_RESPONSE = "response";
    public final static String COL_NODE_VERSION = "version";
    public final static String COL_NODE_SERVICEID = "service";
    public final static String TABLE_SETTINGS = "Settings";
    public final static String COL_SETTINGS_KEY = "key";
    public final static String COL_SETTINGS_VALUE = "value";
    private final static String CREATE_ENVIRONMENT_SQL =
            "CREATE TABLE " + TABLE_ENVIRONMENT + " (" +
                    COL_ENVIRONMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ENVIRONMENT_NAME + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                    COL_ENVIRONMENT_STATUS + " TEXT NOT NULL) ";
    private final static String CREATE_SERVICE_SQL =
            "CREATE TABLE " + TABLE_SERVICE + " (" +
                    COL_SERVICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_SERVICE_NAME + " TEXT NOT NULL, " +
                    COL_SERVICE_STATUS + " TEXT NOT NULL, " +
                    COL_SERVICE_ENVID + " INTEGER NOT NULL REFERENCES " + TABLE_ENVIRONMENT + "(" + COL_ENVIRONMENT_ID + ") ON DELETE CASCADE, " +
                    "CONSTRAINT uniqueServiceToEnvironment UNIQUE(" + COL_SERVICE_NAME + "," + COL_SERVICE_ENVID + ") ON CONFLICT REPLACE " +
                    ") ";
    private final static String CREATE_NODE_SQL =
            "CREATE TABLE " + TABLE_NODE + " (" +
                    COL_NODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NODE_NAME + " TEXT NOT NULL, " +
                    COL_NODE_STATUS + " TEXT NOT NULL, " +
                    COL_NODE_RESPONSE + " TEXT NOT NULL, " +
                    COL_NODE_VERSION + " TEXT NOT NULL, " +
                    COL_NODE_SERVICEID + " INTEGER NOT NULL REFERENCES " + TABLE_SERVICE + "(" + COL_SERVICE_ID + ") ON DELETE CASCADE, " +
                    "CONSTRAINT uniqueNodeToService UNIQUE(" + COL_NODE_NAME + ", " + COL_NODE_SERVICEID + ") ON CONFLICT REPLACE " +
                    ") ";
    private final static String CREATE_SETTINGS_SQL =
            "CREATE TABLE " + TABLE_SETTINGS + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_SETTINGS_KEY + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, " +
                    COL_SETTINGS_VALUE + " TEXT) ";

    private final static String[] CREATE_DB_SQL = new String[]{
            CREATE_SETTINGS_SQL,
            CREATE_ENVIRONMENT_SQL,
            CREATE_SERVICE_SQL,
            CREATE_NODE_SQL
    };

    private final static String DROP_SETTINGS_SQL = "DROP TABLE " + TABLE_SETTINGS;
    private final static String DROP_NODE_SQL = "DROP TABLE " + TABLE_NODE;
    private final static String DROP_SERVICE_SQL = "DROP TABLE " + TABLE_SERVICE;
    private final static String DROP_ENVIRONMENT_SQL = "DROP TABLE " + TABLE_ENVIRONMENT;

    private final static String[] UPGRADE_DB_SQL = new String[]{
            DROP_SETTINGS_SQL,
            DROP_NODE_SQL,
            DROP_SERVICE_SQL,
            DROP_ENVIRONMENT_SQL,
            CREATE_SETTINGS_SQL,
            CREATE_ENVIRONMENT_SQL,
            CREATE_SERVICE_SQL,
            CREATE_NODE_SQL
    };

    private DBHelper() {
        //Don't instantiate.
    }

    public static String[] getCreateSQLs() {
        return CREATE_DB_SQL;
    }

    public static String[] getUpgradeSQLs(int oldVersion, int newVersion) {
        return UPGRADE_DB_SQL; //it's a brand new app, so tables are just discarded and recreated always.
    }

}
