package com.mako.srikrishnayarns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 2/9/2017.
 */
public class E1_forms extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progress;
    RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<Order> dataset=new ArrayList<>();
    private List<String> mDatakey=new ArrayList<>();
    formadapter cc;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forms_container,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        mRecyclerView =(RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cc =new formadapter(dataset,mDatakey,getActivity(),"e1Form");
        mRecyclerView.setAdapter(cc);
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false);
        progress.show();
        loadData();
    }

    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("order");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("e1Form").equalTo(false).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataset.clear();
                mDatakey.clear();
                for(DataSnapshot single:dataSnapshot.getChildren()){
                    dataset.add(single.getValue(Order.class));
                    mDatakey.add(single.getKey().toString());
                }
                cc.notifyDataSetChanged();
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
