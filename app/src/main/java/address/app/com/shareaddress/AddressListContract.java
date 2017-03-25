package address.app.com.shareaddress;

import android.content.Context;

interface AddressListContract {

    interface AddressList {
        void setAddress(Address address);
    }

    interface AddressFetcher {
        void getAddressFromDb(Context context, OnDbRequestCallback onDbRequestCallback);
    }

    void getAddressList(Context context);

    interface OnDbRequestCallback {
        void setData(Address address);
    }

}
