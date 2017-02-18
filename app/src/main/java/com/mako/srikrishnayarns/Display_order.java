package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/31/2017.
 */

public class Display_order extends Fragment {
    View v;
    DatabaseReference Ref;
    Order order;
    boolean firstLoad=true;
    List<product> mdataset = new ArrayList<>();
    productitemreAdapter adapter;
    TextView buyer_tv, seller_tv, transport_tv, addItem, Total_tv,in_date,es_date,grandtotal_tv,items,discount_tv,adj_tv,ship_tv,advance_amt_tv,payment_type,type_of_sale,invoice;
    String key;
    private RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.display_order,container,false);
        return v;
    }
    public Display_order(Order s, String key){
        this.order=s;
        this.key=key;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initUi();
    }
    public void delete(){
       final DatabaseReference fd=FirebaseDatabase.getInstance().getReference().child("order").child(key);
        fd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fd.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
        ft.replace(R.id.content_frame, new confirmation_list());
        ft.commit();
    }

    private void initUi() {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).setlighttoolbarcolor();
        getActivity().setTitle("confirmation");
        buyer_tv = (TextView) v.findViewById(R.id.select_buyer);
        items= (TextView) v.findViewById(R.id.items);
        addItem = (TextView) v.findViewById(R.id.addItem);
        in_date = (TextView) v.findViewById(R.id.invoice_date);
        es_date = (TextView) v.findViewById(R.id.estimated_date);
        seller_tv = (TextView) v.findViewById(R.id.select_seller);
        transport_tv = (TextView) v.findViewById(R.id.select_trasport);
        Total_tv = (TextView) v.findViewById(R.id.total);
        grandtotal_tv = (TextView) v.findViewById(R.id.grand_total);
        discount_tv = (TextView) v.findViewById(R.id.discount);
        adj_tv = (TextView) v.findViewById(R.id.adjustments);
        ship_tv=(TextView) v.findViewById(R.id.shipping);
        invoice=(TextView) v.findViewById(R.id.invoice);
        advance_amt_tv=(TextView) v.findViewById(R.id.advance_amt);
        type_of_sale=(TextView)v.findViewById(R.id.type_of_sale);
        payment_type=(TextView)v.findViewById(R.id.payment_type);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //should pass data
        setdata();
    }
    public void setdata(){
        invoice.setText(String.valueOf(order.getInvoice()));
        buyer_tv.setText(order.getBuyer());
        seller_tv.setText(order.getSeller());
        transport_tv.setText(order.getTransport());
        type_of_sale.setText(order.getTypeOfSale());
        in_date.setText(getdate(order.getBilldate()));
        es_date.setText(getdate(order.getDeliveydate()));
        Total_tv.setText(String.valueOf(order.getTotal()));
        discount_tv.setText(String.valueOf(order.getDiscount()));
        adj_tv.setText(String.valueOf(order.getAdjustments()));
        ship_tv.setText(String.valueOf(order.getShippping()));
        payment_type.setText(String.valueOf(order.getTypeofpayment()));
        advance_amt_tv.setText(String.valueOf(order.getAdvanceamt()));
        grandtotal_tv.setText(String.valueOf(order.getGrand_total()));
        mdataset=order.getProductList();
        adapter = new productitemreAdapter(getActivity(), mdataset);
        mRecyclerView.setAdapter(adapter);

    }
    public String getdate(int d){
        String temp="";
        int day,month,year=2000;
        day=d%100;
        d=d/100;
        month=d%100;
        d=d/100;
        year=year+d;
        temp=""+day+"-"+month+"-"+year;
        return temp;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_contact:
                setFragment(new Create_Order(order,key));
                break;
            case R.id.delete_contact:
                delete();
                break;
            case R.id.send_invoice:
                setFragment(new InvoiceFragment(order));
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    public void setFragment(Fragment fragment){

        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null) ;
            ft.setCustomAnimations(R.anim.pull_in_right,R.anim.push_out_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.display_order,menu);

    }


}
