package address.app.com.shareaddress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

public class AddressFormActivity extends AppCompatActivity {

    AddressDbManager db;
    private LocationHelper helper;
    AppCompatEditText place, street, number, postcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_form);
        setToolBar();
        helper = new LocationHelper(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            helper.buildGoogleApiClient();
        }
        helper.checkLocationPermission();
        helper.inflateMapFragment();
        place = (AppCompatEditText) findViewById(R.id.place_name);
        street = (AppCompatEditText) findViewById(R.id.street_name);
        postcode = (AppCompatEditText) findViewById(R.id.postcode_name);
        number = (AppCompatEditText) findViewById(R.id.street_number);

        place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helper.setCityName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        street.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helper.setStreetNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helper.setPostcode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helper.setStreetNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done: {
                db = new AddressDbManager(this);
                db.insertCity(new Address(helper.getCityName(), helper.getStreetName(), helper.getStreetNumber(), helper.getPostcode()));
                onBackPressed();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        helper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        if (helper.getGoogleApiClient() != null && helper.getGoogleApiClient().isConnected())
            helper.getGoogleApiClient().disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        helper.onActivityResult(requestCode, resultCode, data);
    }

    void updateAddress(Address address) {
        place.setText(address.city);
        postcode.setText(address.postalCode);
        number.setText(address.streetNumber);
        street.setText(address.streetName);
    }
}

