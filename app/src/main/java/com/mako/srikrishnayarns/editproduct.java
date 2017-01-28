package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mako on 1/28/2017.
 */
public class editproduct extends Fragment {
    View v;
    EditText name_ev,description_ev,rate_ev;
    product p;
    String key;
    public editproduct(product p, String key) {
        this.p=p;
        this.key=key;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.product_edit,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void initUI() {
        setHasOptionsMenu(true);
        getActivity().setTitle("Edit Product");
        name_ev=(EditText)v.findViewById(R.id.product_name);
        description_ev=(EditText)v.findViewById(R.id.description);
        rate_ev=(EditText)v.findViewById(R.id.rate);
        name_ev.setText(p.getName());
        description_ev.setText(p.getDescription());
        rate_ev.setText(String.valueOf(p.getRate()));


    }
    private void checkData() {
        if(name_ev.getText().toString().isEmpty())
            name_ev.setError("Product name is Mandatory");
        else if(rate_ev.getText().toString().isEmpty())
            rate_ev.setError("Rate of the product is Mandatory");
        else
            getData();
    }

    private void getData() {
        p.setName(name_ev.getText().toString());
        p.setDescription(description_ev.getText().toString());
        p.setRate(Integer.parseInt(rate_ev.getText().toString()));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("product").child(key);
        ref.setValue(p);
        Toast.makeText(getActivity(),"saved",Toast.LENGTH_SHORT).show();
        moveup();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save:
                checkData();
                break;
        }
        return(super.onOptionsItemSelected(item));
    }

    private void moveup() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
