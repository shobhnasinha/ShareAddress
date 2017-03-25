package address.app.com.shareaddress;

import android.content.Context;

import java.util.ArrayList;

interface AddressListContract {

    interface AddressList {
        void setAddress(ArrayList<Address> address);
    }

    interface AddressFetcher {
        void getAddressFromDb(Context context, OnDbRequestCallback onDbRequestCallback);
    }

    void getAddressList(Context context);

    interface OnDbRequestCallback {
        void setData(ArrayList<Address> address);
    }

}
