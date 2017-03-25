package address.app.com.shareaddress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Address data;
    private ViewHolder viewHolder;

    RecyclerViewAdapter(Address w) {
        this.data = w;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName, streetName, streetNumber, postalCode;

        ViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.place);
            streetName = (TextView) itemView.findViewById(R.id.street);
            streetNumber = (TextView) itemView.findViewById(R.id.number);
            postalCode = (TextView) itemView.findViewById(R.id.postcode);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data != null) {
            viewHolder.cityName.setText(data.city);
            viewHolder.postalCode.setText(data.postalCode);
            viewHolder.streetName.setText(data.streetName);
            viewHolder.streetNumber.setText(data.streetNumber);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : 1;
    }

    void setData(Address data) {
        this.data = data;
    }
}

