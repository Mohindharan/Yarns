package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Mako on 1/29/2017.
 */
public class productItemAdapter extends RecyclerView.Adapter<productItemAdapter.ViewGroup> {
    List<product> productList;
    Context context;
    Create_Order fragment;
    public productItemAdapter(Context activity, List<product> mdataset,Create_Order fragment) {
        this.productList=mdataset;
        this.context=activity;
        this.fragment=fragment;
    }

    @Override
    public ViewGroup onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.product_list_item,parent,false);
        return new ViewGroup(v);

    }
    public int getTotal(){
        int total=0;
        for (int i = 0; i <productList.size() ; i++) {
            total=total+productList.get(i).getRate()*productList.get(i).getQuantity();
        }
        return total;
    }
    @Override
    public void onBindViewHolder(ViewGroup holder, int position) {
        holder.name.setText(productList.get(position).getName());
        holder.rate.setText(productList.get(position).getRate()+"x"+productList.get(position).getQuantity());
        holder.amt.setText(String.valueOf(productList.get(position).getRate()*productList.get(position).getQuantity()));


    }
    public  List<product> getProductList(){
        return productList;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewGroup extends RecyclerView.ViewHolder {
        TextView name;
        TextView rate;
        TextView amt;
        ImageView remove;
        public ViewGroup(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.item_name);
            rate=(TextView)itemView.findViewById(R.id.rate);
            amt=(TextView)itemView.findViewById(R.id.amt);
            remove=(ImageView)itemView.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productList.remove(getLayoutPosition());
                    notifyDataSetChanged();
                    fragment.setTotal();
                }
            });
        }
    }
}
