package com.mako.srikrishnayarns;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/27/2017.
 */
public class ActivityOverview extends Fragment {
    ProgressDialog progress;
    private DatabaseReference mDatabase;
    confirmationAdapter cc;
    RecyclerView mRecyclerView;
    private List<Order> dataset;
    private List<String> mDatakey;
    String key,type;
    TextView rm;
    View v;

    String ContactType[] = { "Sellers","Buyer","Transport" };

    public ActivityOverview(String key, String category) {
        this.key=key;

       if (category==ContactType[0])
           this.type="sellerKey";
        else if(category==ContactType[1])
           this.type="buyerKey";
        else {
           this.type="transportKey";
       }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        dataset =new ArrayList<Order>();
        mDatakey=new ArrayList<String>();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false);
        progress.show();
        getActivity().setTitle("Manage Conformation");
        ((MainActivity)getActivity()).setlighttoolbarcolor();
        mRecyclerView =(RecyclerView)v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activities_ovetview,container,false);
        return v;
    }
    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("order");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild(type).equalTo(key).addValueEventListener(new ValueEventListener() {
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

    private void initialiseUI() {
        rm=(TextView)v.findViewById(R.id.rm);
        if (dataset.size()>0)
            rm.setVisibility(View.GONE);
        cc =new confirmationAdapter(dataset,mDatakey,getActivity());
        mRecyclerView.setAdapter(cc);
    }
}
