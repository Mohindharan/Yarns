package com.mako.srikrishnayarns;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Create_Order extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {
    TextView buyer_tv, seller_tv, transport_tv, addItem, Total_tv,in_date,es_date,grandtotal_tv,items;
    EditText discount_tv,adj_tv,ship_tv,advance_amt_tv,payment_type,type_of_sale;
    private RecyclerView mRecyclerView;
    CheckBox advance_cb;
    List<product> mdataset = new ArrayList<>();
    productItemAdapter adapter;
    Order order = new Order();
    private int year;
    private int month;
    private int day;
    String key;
    Order bae ;
    static final int DATE_PICKER_ID = 1111;
    static View v;
    Button b;
    boolean firstload=true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_order, container, false);
    }

    public void showDatePickerDialog(View v) {
        android.support.v4.app.DialogFragment newFragment = new android.support.v4.app.DialogFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save){
            setGrandtotal();
            getData();

           if(checkdata())
                setdata();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        v = view;
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        buyer_tv = (TextView) v.findViewById(R.id.select_buyer);
        items= (TextView) v.findViewById(R.id.items);
        addItem = (TextView) v.findViewById(R.id.addItem);
        in_date = (TextView) v.findViewById(R.id.invoice_date);
        es_date = (TextView) v.findViewById(R.id.estimated_date);
        seller_tv = (TextView) v.findViewById(R.id.select_seller);
        transport_tv = (TextView) v.findViewById(R.id.select_trasport);
        Total_tv = (TextView) v.findViewById(R.id.total);
        grandtotal_tv = (TextView) v.findViewById(R.id.grand_total);
        discount_tv = (EditText) v.findViewById(R.id.discount);
        adj_tv = (EditText) v.findViewById(R.id.adjustments);
        ship_tv=(EditText) v.findViewById(R.id.shipping);
        advance_amt_tv=(EditText) v.findViewById(R.id.advance_amt);
        type_of_sale=(EditText)v.findViewById(R.id.type_of_sale);
        payment_type=(EditText)v.findViewById(R.id.payment_type);
        advance_cb=(CheckBox)v.findViewById(R.id.checkBox);
        buyer_tv.setOnClickListener(this);
        seller_tv.setOnClickListener(this);
        addItem.setOnClickListener(this);
        transport_tv.setOnClickListener(this);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new productItemAdapter(getActivity(), mdataset, Create_Order.this);
        mRecyclerView.setAdapter(adapter);//should pass data
        in_date.setOnClickListener(this);
        es_date.setOnClickListener(this);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Show current date
        advance_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    advance_amt_tv.setEnabled(true);
                    advance_amt_tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                    //grandtotal =total-advance;
                }
                else{
                    advance_amt_tv.setEnabled(false);
                    advance_amt_tv.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.Blue_Grey));
                }
            }
        });
        in_date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
        es_date.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));
        discount_tv.setOnFocusChangeListener(this);
        adj_tv.setOnFocusChangeListener(this);
        ship_tv.setOnFocusChangeListener(this);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save,menu);
    }

    public void setTotal() {
        Total_tv.setText(String.valueOf(adapter.getTotal()));
    }
    public boolean checkdata(){
        boolean bugfree=true;
        if(order.getProductList().size()==0) {
            items.setError("you need to have only one item");
            Toast.makeText(getActivity(),"you need to have only one item",Toast.LENGTH_SHORT).show();
            bugfree=false;
        }
        if (payment_type.getText().toString().isEmpty())
        {
            payment_type.setError("you need to enter the payment type");
            Toast.makeText(getActivity(),"you need to enter the payment type",Toast.LENGTH_SHORT).show();
            bugfree=false;
        }
        if(type_of_sale.getText().toString().isEmpty())
        {
            type_of_sale.setError("you enter the type of sale");
            Toast.makeText(getActivity(),"you enter the type of sale",Toast.LENGTH_SHORT).show();
            bugfree=false;
        }
        return bugfree;
    }
    public void setdata(){
        DatabaseReference fd=FirebaseDatabase.getInstance().getReference().child("order");
        fd.keepSynced(true);
        key = fd.push().getKey();
        fd.child(key).setValue(order);
    }
    public void loadData(String str){
        DatabaseReference fd=FirebaseDatabase.getInstance().getReference().child("order").child(str);
        fd.keepSynced(true);


        fd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(firstload) {
                    bae = dataSnapshot.getValue(Order.class);
                    firstload=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getData(){
        String temp;
        order.setProductList(adapter.getProductList());
        order.setBilldate(getdate(in_date.getText().toString()));
        order.setDeliveydate(getdate(es_date.getText().toString()));
        order.setTotal(adapter.getTotal());
        temp=discount_tv.getText().toString();
        if(!temp.isEmpty())
            order.setDiscount(Integer.parseInt(temp));
        temp=adj_tv.getText().toString();
        if(!temp.isEmpty())
            order.setAdjustments(Integer.parseInt(temp));
        temp=ship_tv.getText().toString();
        if(!temp.isEmpty())
            order.setShippping(Integer.parseInt(temp));

        temp=advance_amt_tv.getText().toString();
        if (advance_cb.isChecked()) {
            order.setAdvance(true);
            if (!temp.isEmpty())
                order.setAdvanceamt(Integer.parseInt(temp));
        }
        temp=grandtotal_tv.getText().toString();
        if (!temp.isEmpty())
            order.setGrand_total(Integer.parseInt(temp));
        order.setTypeofpayment(payment_type.getText().toString());
        order.setTypeOfSale(type_of_sale.getText().toString());
    }

    private int getdate(String str) {
        int day,year,month,date;
        str=str.replace(" ","");
        year=Integer.parseInt(str.substring(str.length()-2));
        year=Integer.parseInt(str.substring(str.length()-2));
        str=str.substring(0,str.length()-5);
        String temp;
        temp=str.substring(str.length()-1);
        str=str.substring(0,str.length()-1);
        if(!str.substring(str.length()-1).equals("-")) {
            temp = temp.concat(String.valueOf(str.charAt(str.length() - 2)));
            str=str.substring(0,str.length()-1);
        }
        str=str.substring(0,str.length()-1);
        month=Integer.parseInt(temp);
        str="0"+str;
        day=Integer.parseInt(str);
        date=year*10000+month*100+day;
        return date;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_buyer:
                Intent intent1 = new Intent(getContext(), selectContact.class);
                intent1.putExtra("category", 1);
                startActivityForResult(intent1, 1);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.select_seller:
                Intent intent2 = new Intent(getContext(), selectContact.class);
                intent2.putExtra("category", 2);
                startActivityForResult(intent2, 2);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.select_trasport:
                Intent intent3 = new Intent(getContext(), selectContact.class);
                intent3.putExtra("category", 3);
                startActivityForResult(intent3, 3);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.addItem:
                Intent intent4 = new Intent(getContext(), SelectCurrentProduct.class);
                startActivityForResult(intent4, 4);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
            case R.id.invoice_date:

                DialogFragment newFragment1 = new fromDatePickerFragment();
                newFragment1.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.estimated_date:
                DialogFragment newFragment2 = new toDatePickerFragment();
                newFragment2.show(getActivity().getSupportFragmentManager(), "datePicker");

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    order.setBuyer(data.getStringExtra("person"));
                    order.setBuyerKey(data.getStringExtra("key"));
                    buyer_tv.setText(order.getBuyer());
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    order.setSeller(data.getStringExtra("person"));
                    order.setSellerKey(data.getStringExtra("key"));
                    seller_tv.setText(order.getSeller());
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    order.setTransport(data.getStringExtra("person"));
                    order.setTransportKey(data.getStringExtra("key"));
                    transport_tv.setText(order.getTransport());
                }
                break;
            case 4:
                if (resultCode == Activity.RESULT_OK) {
                    product ap = new product();
                    ap.setName(data.getStringExtra("name"));
                    ap.setRate(Integer.parseInt(data.getStringExtra("rate")));
                    ap.setCount(Integer.parseInt(data.getStringExtra("count")));
                    ap.setQuantity(Integer.parseInt(data.getStringExtra("qua")));
                    mdataset.add(ap);
                    adapter.notifyDataSetChanged();
                    setTotal();
                }
                break;
        }
    }
    public void setGrandtotal(){
        int tot=0,dis=0,adj=0,shipping=0,ad=0;
         tot=Integer.parseInt(Total_tv.getText().toString());
        if(discount_tv.getText().length()!=0)
        dis=Integer.parseInt(discount_tv.getText().toString());
        if(adj_tv.getText().length()!=0)
            adj=Integer.parseInt(adj_tv.getText().toString());
        if(ship_tv.getText().length()!=0)
        shipping=Integer.parseInt(ship_tv.getText().toString());
        if(advance_cb.isChecked())
            if(advance_amt_tv.getText().length()!=0)
            ad=Integer.parseInt(advance_amt_tv.getText().toString());
        String ans= String.valueOf(tot+adj+shipping-dis-ad);

        grandtotal_tv.setText(ans);
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.discount:
                    setGrandtotal();
                break;
            case R.id.adjustments:
                    setGrandtotal();
                break;
            case R.id.shipping:
                    setGrandtotal();
                break;
        }
    }

    public static class fromDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView in_date= (TextView) v.findViewById(R.id.invoice_date);

            in_date.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));

        }
    }
    public static class toDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView es_date= (TextView) v.findViewById(R.id.estimated_date);
            es_date.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }
    }
}