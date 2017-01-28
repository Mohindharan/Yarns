package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_product extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText name_ev,description_ev,rate_ev;
    product p;
    String key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_product);
        initUI();

    }

    private void initUI() {
        p= new product();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);
        name_ev=(EditText)findViewById(R.id.product_name);
        description_ev=(EditText)findViewById(R.id.description);
        rate_ev=(EditText)findViewById(R.id.rate);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                checkData();
                break;
        }

        return(super.onOptionsItemSelected(item));
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
        DatabaseReference ref = database.getReference().child("product");
        key = ref.push().getKey();
        ref.child(key).setValue(p);
        Toast.makeText(this,"saved",Toast.LENGTH_SHORT).show();
        moveup();
    }
    public void moveup(){
        finish();
    }

}