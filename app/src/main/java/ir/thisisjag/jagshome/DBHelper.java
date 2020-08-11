package ir.thisisjag.jagshome;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Table Name
    static final String TABLE_NAME = "Devices";

    // Table columns
    static final String _ID = "_id";
    static final String IDd = "DeviceID";
    static final String J = "j";
    static final String SUBID = "SubID";
    static final String STATUS = "RelayStatus";
    static final String NAME = "RelayName";

    // Database Information
    private static final String DB_NAME = "Devices";

    // database version
    private static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY  , "
            + IDd + " INTEGER, "
            + J + " INTEGER, "
            + SUBID + " INTEGER, "
            + STATUS +" INTEGER, "
            + NAME + " TEXT"+")";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
