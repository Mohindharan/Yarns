package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SelectCurrentProduct extends AppCompatActivity {
    TextView select_tv,rate;
    EditText count,quantity;

    String key;
    product p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_current_product);
        initUI();
    }
    private void initUI() {
        select_tv=(TextView) findViewById(R.id.select_Product);
        rate=(TextView) findViewById(R.id.selected_rate);
        count=(EditText) findViewById(R.id.count);
        quantity=(EditText) findViewById(R.id.quantity);
        select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCurrentProduct.this,SelectProduct.class);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

    }

    private void loadData() {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("product").child(key);
        mDatabase.keepSynced(true);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                p = dataSnapshot.getValue(product.class);
                setdata();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setdata() {
        select_tv.setText(p.getName());
        rate.setText(String.valueOf(p.getRate()));

    }
    private void checkData() {
        if (count.getText().toString().isEmpty()){
            count.setError("Count quality required");
            Toast.makeText(this,"Count quality required",Toast.LENGTH_SHORT).show();
        }
        else if (quantity.getText().toString().isEmpty()){
            quantity.setError("Quantity is required");
            Toast.makeText(this,"Quantity is required",Toast.LENGTH_SHORT).show();
        }
        else
            updateData();
    }

    private void updateData() {
        Toast.makeText(this,"saved ",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent();
        intent.putExtra("name",select_tv.getText());
        intent.putExtra("rate",rate.getText().toString());
        intent.putExtra("count",count.getText().toString());
        intent.putExtra("qua",quantity.getText().toString());
//        Log.d("rate",String.valueOf(quantity.getText().toString()));
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save)
        checkData();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if(resultCode== Activity.RESULT_OK){
            key=data.getStringExtra("key");
            loadData();
            }
        }
    }

}
