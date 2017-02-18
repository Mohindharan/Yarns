package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mako on 2/9/2017.
 */

public class formadapter extends RecyclerView.Adapter <formadapter.ViewHolder> {
        private List<Order> dataset;
        private List<String> OrderKey;
        private String formtype;
        String type;
        Context context;

        public formadapter(List<Order> dataset, List<String> mDatakey, FragmentActivity activity, String formType) {
            this.dataset =dataset;
            this.context=activity;
            this.OrderKey =mDatakey;
            this.formtype=formType;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmation_list_item, parent, false);
            return new formadapter.ViewHolder(view);
        }



    @Override
        public void onBindViewHolder(formadapter.ViewHolder holder, int position) {
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
                Intent intent = new Intent(context,UploadForm.class);
                intent.putExtra("type",formtype);
                intent.putExtra("key",OrderKey.get(getLayoutPosition()));
                intent.putExtra("invoice",dataset.get(getLayoutPosition()).getInvoice());
                context.startActivity(intent);
            }
        }
    }


