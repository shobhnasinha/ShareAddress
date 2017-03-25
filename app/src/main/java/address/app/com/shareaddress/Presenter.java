package address.app.com.shareaddress;

import android.content.Context;

public class Presenter implements AddressListContract {

    private AddressListContract.AddressList view;
    private AddressListContract.AddressFetcher addressFetcher;

    Presenter(AddressListContract.AddressList view, AddressListContract.AddressFetcher addressFetcher) {
        this.view = view;
        this.addressFetcher = addressFetcher;
    }


    @Override
    public void getAddressList(Context context) {
        addressFetcher.getAddressFromDb(context, new OnDbRequestCallback() {
                    @Override
                    public void setData(Address address) {
                        view.setAddress(address);
                    }
                }
        );
    }
}
