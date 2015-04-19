package in.kubryant.shout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import in.kubryant.andhoclib.src.AndHocMessage;

/**
 * Created by camherringshaw on 4/18/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "messages.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FeedReaderContract.SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void clear() {
//        Seriously, don't do this
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DROP TABLE "+FeedReaderContract.FeedEntry.TABLE_NAME);
    }

    public void insertMessage(AndHocMessage message) {
        Log.d("FeedReader", "Logging message: "+message.get("msg"));
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_UUID, message.get("uuid"));
        values.put(FeedReaderContract.FeedEntry.COLUMN_USERNAME, "");
        values.put(FeedReaderContract.FeedEntry.COLUMN_MESSAGE, message.get("msg"));

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
//        db.insertOrThrow(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }
    public Set<AndHocMessage> getAllMessages() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_TIMESTAMP;
        Set<AndHocMessage> messages = new HashSet<>();

        Cursor c = db.rawQuery("SELECT * from " + FeedReaderContract.FeedEntry.TABLE_NAME, null);
        if(c.moveToFirst()) {
            do {
                Map<String, String> messageMap = new HashMap<String, String>();
                int count = c.getColumnCount();
                for(int i=0; i<count; i++) {
                    messageMap.put(c.getColumnName(i), c.getString(i));
                }
                AndHocMessage message = new AndHocMessage(messageMap);
                messages.add(message);
            }while(c.moveToNext());
        }
        if(c != null && !c.isClosed()) {
            c.close();
        }
        return messages;
    }
}