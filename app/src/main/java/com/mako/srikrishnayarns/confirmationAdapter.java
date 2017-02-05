package com.mako.srikrishnayarns;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mako on 2/1/2017.
 */

public class confirmationAdapter extends RecyclerView.Adapter <confirmationAdapter.ViewHolder> {
    private List<Order> dataset;

    private List<String> OrderKey;


    Context context;

    public confirmationAdapter(List<Order> dataset, List<String> mDatakey, FragmentActivity activity) {
        this.dataset =dataset;
        this.context=activity;
        this.OrderKey =mDatakey;

    }

    @Override
    public confirmationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmation_list_item, parent, false);
        return new confirmationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(confirmationAdapter.ViewHolder holder, int position) {
        holder.tx.setText(dataset.get(position).getBuyer().toString());
        holder.seller.setText(dataset.get(position).getSeller());
        holder.date.setText(getDate(dataset.get(position).getBilldate()));
        holder.money.setText("Rs "+dataset.get(position).getGrand_total());

    }
    public String getDate(int date){
        int year=2000,month=00,day=00;
        day=date%100;
        date=date/100;
        month=date%100;
        date=date/100;
        year=year+date;
        return ""+day+"/"+month+"/"+year;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx,seller,date,money;
        public ViewHolder(View view) {
            super(view);
            tx = (TextView) view.findViewById(R.id.buyer_name_tx);
            seller=(TextView)view.findViewById(R.id.seller_name_tx);
            date=(TextView)view.findViewById(R.id.date);
            money=(TextView)view.findViewById(R.id.total);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction ft =  ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
            ft.replace(R.id.content_frame, new Display_order(dataset.get(getLayoutPosition()),OrderKey.get(getLayoutPosition())));
            ft.commit();
        }
    }
}
