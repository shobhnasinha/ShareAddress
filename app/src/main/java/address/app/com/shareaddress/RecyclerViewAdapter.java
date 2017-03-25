package address.app.com.shareaddress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Address> data;
    private ViewHolder viewHolder;

    RecyclerViewAdapter(ArrayList<Address> w) {
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
            viewHolder.cityName.setText(data.get(position).city);
            viewHolder.postalCode.setText(data.get(position).postalCode);
            viewHolder.streetName.setText(data.get(position).streetName);
            viewHolder.streetNumber.setText(data.get(position).streetNumber);
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
        return data == null ? 0 : data.size();
    }

    void setData(ArrayList<Address> data) {
        this.data = data;
    }
}

