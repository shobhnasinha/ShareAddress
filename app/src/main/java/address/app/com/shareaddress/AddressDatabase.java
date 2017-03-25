package address.app.com.shareaddress;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class AddressDatabase extends SQLiteOpenHelper {

    static final String ADDRESS_TABLE_NAME = "address";
    static final String CITY_NAME = "city";
    static final String STREET_NAME = "street";
    static final String STREET_NUMBER = "number";
    static final String POSTAL_CODE = "postcode";

    private static final String CREATE_TABLE_COMMAND =
            "CREATE TABLE "
                    + ADDRESS_TABLE_NAME
                    + " ("
                    + CITY_NAME + ", "
                    + STREET_NAME + ", "
                    + STREET_NUMBER + ", "
                    + POSTAL_CODE
                    + ")";

    AddressDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS city");
        onCreate(sqLiteDatabase);
    }
}
