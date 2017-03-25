package address.app.com.shareaddress;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;

public class AddressFetchImpl implements AddressListContract.AddressFetcher {
    @Override
    public void getAddressFromDb(final Context context, final AddressListContract.OnDbRequestCallback onDbRequestCallback) {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                AddressDbManager db = new AddressDbManager(context);
                Cursor cursor = db.getCityData(null, null, null, null, null);
                if (cursor == null) {
                    return null;
                }
                if (cursor.getCount() == 0) {
                    cursor.close();
                    return null;
                }
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                    ArrayList<Address> list = new ArrayList<>();
                    do {
                        String cityName = cursor.getString(cursor.getColumnIndex(AddressDatabase.CITY_NAME));
                        String streetName = cursor.getString(cursor.getColumnIndex(AddressDatabase.STREET_NAME));
                        String streetNumber = cursor.getString(cursor.getColumnIndex(AddressDatabase.STREET_NUMBER));
                        String postalCode = cursor.getString(cursor.getColumnIndex(AddressDatabase.POSTAL_CODE));
                        Address address = new Address();
                        address.city = cityName;
                        address.postalCode = postalCode;
                        address.streetName = streetName;
                        address.streetNumber = streetNumber;
                        list.add(address);
                    } while (cursor.moveToNext());
                    onDbRequestCallback.setData(list);
                } else {
                    onDbRequestCallback.setData(null);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }.execute();
    }
}
