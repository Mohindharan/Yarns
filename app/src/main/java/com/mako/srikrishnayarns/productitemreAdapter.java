package com.mako.srikrishnayarns;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mako on 1/29/2017.
 */
public class productitemreAdapter extends RecyclerView.Adapter<productitemreAdapter.ViewGroup> {
    List<product> productList;
    Context context;

    public productitemreAdapter(Context activity, List<product> mdataset) {
        this.productList=mdataset;
        this.context=activity;

    }

    @Override
    public ViewGroup onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.product_list_re_item,parent,false);
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
        holder.name.setText(productList.get(position).getName()+"("+productList.get(position).getCount()+")");
        holder.rate.setText(productList.get(position).getRate()+"x"+productList.get(position).getQuantity());
        holder.amt.setText(String.valueOf(productList.get(position).getRate()*productList.get(position).getQuantity()));


    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewGroup extends RecyclerView.ViewHolder {
        TextView name;
        TextView rate;
        TextView amt;
        public ViewGroup(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.item_name);
            rate=(TextView)itemView.findViewById(R.id.rate);
            amt=(TextView)itemView.findViewById(R.id.amt);
        }
    }
}
