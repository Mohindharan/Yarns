package com.mako.srikrishnayarns;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mako on 2/1/2017.
 */

public class confirmation_list extends Fragment implements View.OnClickListener {
    static View v;
    FloatingActionButton fabadd;
    RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    LinearLayout filterLayout,from_to_layout;
    ProgressDialog progress;
    private DatabaseReference mDatabase;
    confirmationAdapter cc;
    private List<Order> dataset;
    private List<String> mDatakey;
    Spinner fliterAttr;
    EditText setvalue;
    TextView fromFliter,toFliter;
    Button search;
    public static boolean toggle=true;
    boolean save=false;
    int pos;

    String attrValue[]={"billdate","deliveydate","grand_total","buyer","seller","typeOfSale","typeofpayment"};
    MyUtils anime = new MyUtils();
    private ProgressBar spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirmation_list,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.display_confirmation,menu);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        setHasOptionsMenu(true);
        dataset =new ArrayList<Order>();
        mDatakey=new ArrayList<String>();
        loadData();

        filterLayout=(LinearLayout)v.findViewById(R.id.filter_layout);
        String attr[]={"Bill date","Delivery date","Total Amount","Buyer","Seller","Type Of Sale","Type of Payment"};
        fliterAttr=(Spinner) v.findViewById(R.id.fliterType);
        ArrayAdapter<String> adapterType= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, attr);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fliterAttr.setAdapter(adapterType);
        anime.SlideUP(filterLayout,getActivity());
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Loading");
            progress.setMessage("Syncing ");
            progress.setCancelable(false);
            progress.show();
        getActivity().setTitle("Manage Conformation");
        ((MainActivity)getActivity()).setlighttoolbarcolor();
        mRecyclerView =(RecyclerView)v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }




    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("order");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("billdate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataset.clear();
                mDatakey.clear();

                for(DataSnapshot single:dataSnapshot.getChildren()){
                    dataset.add(single.getValue(Order.class));
                    mDatakey.add(single.getKey().toString());
                }

                initialiseUI();
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.filter_btn:
                if (toggle) {
                    anime.SlideDown(filterLayout, getActivity());
                    toggle=false;
                }
                else {
                    anime.SlideUP(filterLayout, getActivity());
                    toggle=true;
                }

                break;
            case R.id.sort_btn:
                Collections.reverse(dataset);
                Collections.reverse(mDatakey);
                cc.notifyDataSetChanged();
                break;
        }


        return super.onOptionsItemSelected(item);

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

    protected void initialiseUI(){

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fromFliter=(TextView)v.findViewById(R.id.from_filter);
        toFliter=(TextView)v.findViewById(R.id.to_fliter);
        search=(Button)v.findViewById(R.id.search_btn);
        setvalue=(EditText)v.findViewById(R.id.value_filter);
        from_to_layout=(LinearLayout)v.findViewById(R.id.from_to_layout);
        cc =new confirmationAdapter(dataset,mDatakey,getActivity());
        mRecyclerView.setAdapter(cc);
        fabadd=(FloatingActionButton)v.findViewById(R.id.productadd);
        final TextView tc=(TextView)v.findViewById(R.id.valtc);
        fliterAttr.setSelection(0);
        fliterAttr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == 1) {
                    from_to_layout.setVisibility(View.VISIBLE);
                    setvalue.setVisibility(View.GONE);
                    tc.setVisibility(View.GONE);
                }
                else if (position==2){
                    setvalue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    from_to_layout.setVisibility(View.GONE);
                    tc.setVisibility(View.VISIBLE);
                    setvalue.setVisibility(View.VISIBLE);
                }else {
                    setvalue.setInputType(InputType.TYPE_CLASS_TEXT);
                    from_to_layout.setVisibility(View.GONE);
                    tc.setVisibility(View.VISIBLE);
                    setvalue.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fabadd.setOnClickListener(this);
        fromFliter.setOnClickListener(this);
        toFliter.setOnClickListener(this);
        toggle=true;
        search.setOnClickListener(this);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.productadd:

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction() ;
                ft.setCustomAnimations(R.anim.pull_in_right,R.anim.push_out_left);
                ft.replace(R.id.content_frame,new Create_Order());
                ft.commit();

                break;
            case R.id.from_filter:
                DialogFragment newFragment1 = new fromDatePickerFragment();
                newFragment1.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.to_fliter:
                DialogFragment newFragment2 = new toDatePickerFragment();
                newFragment2.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.search_btn:
                filterout();
                break;
        }
    }

    private void filterout() {
        anime.SlideUP(filterLayout,getActivity());
        int pos=fliterAttr.getSelectedItemPosition();
        String attr=attrValue[pos];
        if(pos==1||pos==0)//date
        {
           int from= getdate(fromFliter.getText().toString());
           int to= getdate(toFliter.getText().toString());
           mDatabase.orderByChild(attr).startAt(from).endAt(to).addListenerForSingleValueEvent(new ValueEventListener() {

               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   dataset.clear();
                   mDatakey.clear();

                   for(DataSnapshot single:dataSnapshot.getChildren()){
                       dataset.add(single.getValue(Order.class));
                       mDatakey.add(single.getKey().toString());
                   }
                   cc.notifyDataSetChanged();
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

        }
        else if(pos==2)//total
        {
            int str=Integer.parseInt(setvalue.getText().toString());
            mDatabase.orderByChild(attr).equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataset.clear();
                    mDatakey.clear();

                    for(DataSnapshot single:dataSnapshot.getChildren()){
                        dataset.add(single.getValue(Order.class));
                        mDatakey.add(single.getKey().toString());
                    }
                    cc.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            String str=setvalue.getText().toString();
            mDatabase.orderByChild(attr).equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataset.clear();
                    mDatakey.clear();

                    for(DataSnapshot single:dataSnapshot.getChildren()){
                        dataset.add(single.getValue(Order.class));
                        mDatakey.add(single.getKey().toString());
                    }
                    cc.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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
                TextView ed=(TextView)v.findViewById(R.id.from_filter);

            ed.setText(new StringBuilder()
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
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView ed=(TextView)v.findViewById(R.id.to_fliter);

            ed.setText(new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(day).append("-").append(month + 1).append("-")
                    .append(year).append(" "));
        }
    }
}
