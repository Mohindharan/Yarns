package com.mako.srikrishnayarns;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mako on 1/29/2017.
 */
public class productItemAdapter extends RecyclerView.Adapter<productItemAdapter.ViewGroup> {
    List<product> productList;
    Context context;
    public productItemAdapter(Context activity, List<product> mdataset) {
        this.productList=mdataset;
        this.context=activity;
    }

    @Override
    public ViewGroup onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.product_list_item,parent,false);
        return new ViewGroup(v);

    }

    @Override
    public void onBindViewHolder(ViewGroup holder, int position) {
        holder.name.setText(productList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewGroup extends RecyclerView.ViewHolder {
        TextView name;
        public ViewGroup(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.item_name);

        }
    }
}
