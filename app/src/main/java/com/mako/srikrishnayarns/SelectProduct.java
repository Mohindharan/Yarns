package com.mako.srikrishnayarns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
 * Created by Mako on 1/29/2017.
 */
public class SelectProduct extends AppCompatActivity {    private static final int PERMISSION_REQUEST_CONTACT = 1;
    String Category;
    FloatingActionButton fabadd;
    RecyclerView mRecyclerView;
    RecyclerViewFastScroller fastScroller;
    private LinearLayoutManager linearLayoutManager;
    String category;
    ProgressDialog progress;
    private DatabaseReference mDatabase;
    int pos;
    private List<String> mDataArray;
    private List<String> mDatakey;
    private List<AlphabetItem> mAlphabetItems;
    private ProgressBar spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_contact);
        this.setTitle("Select Item");
        loadData();
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        mRecyclerView =(RecyclerView)findViewById(R.id.my_recycler_view);
        fastScroller=(RecyclerViewFastScroller)findViewById(R.id.fast_scroller);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
    private void loadData() {

        mDataArray=new ArrayList<String>();
        mDatakey=new ArrayList<String>();
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

    private void getnewItem() {
    }
    protected void initialiseUI(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactSelectAdapter contactAda= new contactSelectAdapter(mDataArray,mDatakey,category,this);
        mRecyclerView.setAdapter(contactAda);
        fastScroller.setRecyclerView(mRecyclerView);
        fastScroller.setUpAlphabet(mAlphabetItems);
        fabadd=(FloatingActionButton)findViewById(R.id.useradd);
        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectProduct.this,Create_product.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}
