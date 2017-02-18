package com.mako.srikrishnayarns;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Mako on 2/1/2017.
 */

public class paymentAdapter extends RecyclerView.Adapter <paymentAdapter.ViewHolder> {
    private List<Order> dataset;
    private List<String> OrderKey;
    paymentFragment act;

    Context context;

    public paymentAdapter(List<Order> dataset, List<String> mDatakey, FragmentActivity activity, paymentFragment paymentFragment) {
        this.dataset =dataset;
        this.context=activity;
        this.OrderKey =mDatakey;
        act=paymentFragment;

    }

    @Override
    public paymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list_item, parent, false);
        return new paymentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(paymentAdapter.ViewHolder holder, int position) {
        holder.tx.setText(dataset.get(position).getBuyer().toString());
        holder.seller.setText(dataset.get(position).getSeller());
        holder.date.setText(getDate(dataset.get(position).getBilldate()));
        holder.money.setText("Rs "+dataset.get(position).getGrand_total());
        holder.invoice.setText("No :"+dataset.get(position).getInvoice());

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
        private TextView tx,seller,date,money,invoice;
        public ViewHolder(View view) {
            super(view);
            tx = (TextView) view.findViewById(R.id.buyer_name_tx);
            seller=(TextView)view.findViewById(R.id.seller_name_tx);
            date=(TextView)view.findViewById(R.id.date);
            money=(TextView)view.findViewById(R.id.total);
            invoice=(TextView)view.findViewById(R.id.invoice);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            new AlertDialog.Builder(context)
                    .setTitle("Payment")
                    .setMessage("has amount "+dataset.get(getLayoutPosition()).getRemaining()+" has been paid?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference dv= FirebaseDatabase.getInstance().getReference().child("order").child(OrderKey.get(getLayoutPosition())).child("remaining");
                            dv.setValue(0);
                           act.loaddata();

                        }

                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
