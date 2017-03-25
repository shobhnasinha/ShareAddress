package address.app.com.shareaddress;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements AddressListContract.AddressList {

    private AddressListContract.AddressFetcher addressFetcher;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBar();
        addressFetcher = new AddressFetchImpl();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddressFormActivity();
            }
        });

        findViewById(R.id.addMoreButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddressFormActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Presenter presenter = new Presenter(this, addressFetcher);
        presenter.getAddressList(this);
        adapter = new RecyclerViewAdapter(null);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.address_list);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    private void startAddressFormActivity() {
        Intent intent = new Intent(getApplicationContext(), AddressFormActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
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
    public void setAddress(Address address) {
        if (address != null){
            adapter.setData(address);
            adapter.notifyDataSetChanged();
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.addMoreButton).setVisibility(View.VISIBLE);
        }
    }
}
