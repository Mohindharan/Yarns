package com.mako.srikrishnayarns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viethoa.RecyclerViewFastScroller;
import com.viethoa.models.AlphabetItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/28/2017.
 */

public class Product_list extends Fragment {
    View view;
    FloatingActionButton fabadd;
    RecyclerView mRecyclerView;
    RecyclerViewFastScroller fastScroller;
    private LinearLayoutManager linearLayoutManager;
    ProgressDialog progress;
    private DatabaseReference mDatabase;
    int pos;
    private List<String> mDataArray;
    private List<String> mDatakey;
    private List<AlphabetItem> mAlphabetItems;
    private ProgressBar spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.product_list,container,false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        mDataArray=new ArrayList<String>();
        mDatakey=new ArrayList<String>();
        loadData();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        getActivity().setTitle("Product List");
        mRecyclerView =(RecyclerView)view.findViewById(R.id.my_recycler_view);
        fastScroller=(RecyclerViewFastScroller)view.findViewById(R.id.fast_scroller);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void loadData() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("product");
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataArray.clear();
                mDatakey.clear();

                for(DataSnapshot single:dataSnapshot.getChildren()){
                    mDataArray.add(single.child("name").getValue().toString());
                    mDatakey.add(single.getKey().toString());
                }
                initialiseData();
                initialiseUI();
                progress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    protected void initialiseData() {
        //Recycler view 0
        //Alphabet fast scroller data

        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }
    protected void initialiseUI(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new ProductAdapter(mDataArray,mDatakey,getActivity()));
        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setUpAlphabet(mAlphabetItems);
        fabadd=(FloatingActionButton)view.findViewById(R.id.productadd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Create_product.class);
                startActivityForResult(intent,22);
            }
        });
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
