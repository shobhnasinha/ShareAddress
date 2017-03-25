package address.app.com.shareaddress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static address.app.com.shareaddress.AddressDatabase.CITY_NAME;
import static address.app.com.shareaddress.AddressDatabase.POSTAL_CODE;
import static address.app.com.shareaddress.AddressDatabase.STREET_NAME;
import static address.app.com.shareaddress.AddressDatabase.STREET_NUMBER;

public class AddressDbManager {

    AddressDatabase dbHelper;
    Context context;
    private SQLiteDatabase database;

    AddressDbManager(Context c) {
        context = c;
        dbHelper = new AddressDatabase(context, AddressDatabase.ADDRESS_TABLE_NAME, null, 2);
    }

    public AddressDbManager open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    private void close() {
        dbHelper.close();
    }

    boolean insertCity(Address address) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_NAME, address.getCity());
        contentValues.put(AddressDatabase.STREET_NAME, address.getStreetName());
        contentValues.put(AddressDatabase.STREET_NUMBER, address.getStreetNumber());
        contentValues.put(AddressDatabase.POSTAL_CODE, address.getPostalCode());
        database.insert(AddressDatabase.ADDRESS_TABLE_NAME, null, contentValues);
        close();
        return true;
    }

    Cursor getCityData(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = new String[]{CITY_NAME, STREET_NAME, STREET_NUMBER, POSTAL_CODE};
        return db.query(AddressDatabase.ADDRESS_TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }
}

