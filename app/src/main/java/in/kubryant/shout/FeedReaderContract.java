package in.kubryant.shout;

import android.provider.BaseColumns;

/**
 * Created by camherringshaw on 4/18/15.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_UUID = "msgId";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_MESSAGE = "msg";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER_PRIMARY_KEY," +
                    FeedEntry.COLUMN_UUID + " TEXT," +
                    FeedEntry.COLUMN_USERNAME + " TEXT," +
                    FeedEntry.COLUMN_MESSAGE + " TEXT," +
                    FeedEntry.COLUMN_TIMESTAMP + " TEXT" +
                    " )";
    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}