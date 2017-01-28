package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Create_Order extends Fragment implements View.OnClickListener{
    TextView buyer_tv,seller_tv,transport_tv,addItem;
    private RecyclerView mRecyclerView;
    List<product> mdataset=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    productItemAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Order order=new Order();
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_order,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        v=view;
        super.onViewCreated(view, savedInstanceState);
        buyer_tv=(TextView)v.findViewById(R.id.select_buyer);
        addItem=(TextView)v.findViewById(R.id.addItem);
        seller_tv=(TextView)v.findViewById(R.id.select_seller);
        transport_tv=(TextView)v.findViewById(R.id.select_trasport);
        buyer_tv.setOnClickListener(this);
        seller_tv.setOnClickListener(this);
        addItem.setOnClickListener(this);
        transport_tv.setOnClickListener(this);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
        adapter=new productItemAdapter(getActivity(),mdataset);
        mRecyclerView.setAdapter(adapter);//should pass data
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_buyer:
                Intent intent1 = new Intent(getContext(),selectContact.class);
                intent1.putExtra("category",1);
                startActivityForResult(intent1,1);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.select_seller:
                Intent intent2 = new Intent(getContext(),selectContact.class);
                intent2.putExtra("category",2);
                startActivityForResult(intent2,2);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.select_trasport:
                Intent intent3 = new Intent(getContext(),selectContact.class);
                intent3.putExtra("category",3);
                startActivityForResult(intent3,3);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.addItem:
                Intent intent4 = new Intent(getContext(),SelectCurrentProduct.class);
                startActivityForResult(intent4,4);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode== Activity.RESULT_OK) {
                    order.setBuyer(data.getStringExtra("person"));
                    order.setBuyerKey(data.getStringExtra("key"));
                    buyer_tv.setText(order.getBuyer());
                }
                break;
            case 2:
                if(resultCode== Activity.RESULT_OK) {
                    order.setSeller(data.getStringExtra("person"));
                    order.setSellerKey(data.getStringExtra("key"));
                    seller_tv.setText(order.getSeller());
                }
                break;
            case 3:
                 if(resultCode== Activity.RESULT_OK) {
                order.setTransport(data.getStringExtra("person"));
                order.setTransportKey(data.getStringExtra("key"));
                transport_tv.setText(order.getTransport());
            }
                break;
            case 4:
                if(resultCode== Activity.RESULT_OK) {
                    product ap = new product();
                    ap.setName(data.getStringExtra("name"));
                    ap.setRate(data.getIntExtra("rate",0));
                    ap.setCount(data.getIntExtra("count",0));
                    ap.setQuantity(data.getIntExtra("quantity",0));
                    mdataset.add(ap);
                    adapter=new productItemAdapter(getActivity(),mdataset);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(),data.getStringExtra("name"),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
