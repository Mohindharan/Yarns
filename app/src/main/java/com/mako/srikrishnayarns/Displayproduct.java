package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mako on 1/28/2017.
 */
public class Displayproduct extends Fragment {
    product p;
    private DatabaseReference mDatabase;
    TextView name,description,rate;
    String key;
    View v;
    public  boolean firsetload=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.displayproduct,container,false);
        return v;
    }
    public Displayproduct(String key){
        this.key=key;

    }
    public void initUI(){
        name=(TextView)v.findViewById(R.id.product_name);
        description=(TextView) v.findViewById(R.id.description);
        rate=(TextView) v.findViewById(R.id.rate);
        setHasOptionsMenu(true);
    }
    public void setdata()
    {
        getActivity().setTitle(p.getName().toString());
        name.setText(p.getName().toString());
        description.setText(p.getDescription().toString());
        rate.setText(String.valueOf(p.getRate()));


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        loadData();

    }



    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("product").child(key);
        mDatabase.keepSynced(true);
//        Log.d("sdfvdsf", key);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    p = (product) dataSnapshot.getValue(product.class);

                    setdata();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.display_contact, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_contact:
                delete();
                break;
            case R.id.edit_contact:
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
                ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
                ft.replace(R.id.content_frame, new editproduct(p,key));
                ft.commit();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void delete(){
        Toast.makeText(getActivity(),"Contact deleted",Toast.LENGTH_SHORT).show();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
    }

}
