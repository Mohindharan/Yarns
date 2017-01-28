package com.mako.srikrishnayarns;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
 * Created by Mako on 1/27/2017.
 */

public class selectContact extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CONTACT = 1;
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
        init();
        setContentView(R.layout.select_contact);
        this.setTitle("Select "+Category);
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

    private void init() {
        int category=getIntent().getIntExtra("category",1);
        if(category==1)
            this.Category="Buyer";
        else if(category==2)
            this.Category="Sellers";
        else if(category==3)
            this.Category="Transport";
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Category);
        mDatabase.keepSynced(true);
        mDatabase.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


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
    private void getContact(){
        Intent intent = new Intent(this, CreateContact.class);
        intent.putExtra("type",pos);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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
                askForContactPermission();
            }
        });
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                getContact();
            }
        }
        else{
            getContact();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            Intent returnIntent = new Intent();
        returnIntent.putExtra("key", data.getStringExtra("key"));
        returnIntent.putExtra("person",data.getStringExtra("person"));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this,"permission Denied",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
