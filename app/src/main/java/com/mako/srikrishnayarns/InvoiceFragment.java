package com.mako.srikrishnayarns;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFragment extends Fragment implements View.OnClickListener{
    private static final int REQUEST_WRITE_PERMISSION = 786;
    String emailid="";
    RecyclerView re;
    List<String> Emailid=new ArrayList<>();
    Order order;
    List<product> dataset;
    productitemreAdapter adapter;
    View v;
    TextView addmore,email,total;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.send_invoice,container,false);
        requestPermission();
        return v;
    }
    public InvoiceFragment(Order order){
        this.order=order;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;
        getActivity().setTitle("Send Invoice as Email");
        init();
        initdata();
        getEmail();
    }
    public void getEmail(){
        String buyerkey=" ";
        if(order.getBuyerKey()!=null)
        buyerkey=order.getBuyerKey();
        String sellerKey=" ";
        if(order.getSellerKey()!=null)
        sellerKey=order.getSellerKey();
        DatabaseReference buyer= FirebaseDatabase.getInstance().getReference().child("Buyer").child(buyerkey).child("email");
        DatabaseReference seller= FirebaseDatabase.getInstance().getReference().child("Sellers").child(sellerKey).child("email");
        buyer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = (String) dataSnapshot.getValue();
                if (temp != null) {
                    Emailid.add(temp);
                    setemail();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seller.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = (String) dataSnapshot.getValue();
                if (temp != null) {
                    Emailid.add(temp);
                    setemail();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void setemail(){
        String temp="";
        for (int i=0;i<Emailid.size();i++){
            if (i==0)
            temp=Emailid.get(i);
            else
            temp=temp+" , "+Emailid.get(i);
        }
        email.setText(temp);
    }
    private void initdata() {
        dataset = order.getProductList();
        adapter = new productitemreAdapter(getActivity(), dataset);
        re.setAdapter(adapter);
        total.setText(""+order.getGrand_total());
        email.setText("loading");

    }
    public void getmoreemail(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.email_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);

        dialogBuilder.setTitle("enter a email address");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (isValidEmail(edt.getText().toString())){
                    Emailid.add(edt.getText().toString());
                    setemail();
                }
                else {
                    Toast.makeText(getActivity(), "enter a valid id", Toast.LENGTH_SHORT).show();
                    getmoreemail();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private void init() {
        addmore=(TextView)v.findViewById(R.id.addmore);
        email=(TextView)v.findViewById(R.id.email);
        total=(TextView)v.findViewById(R.id.total);
        re = (RecyclerView) v.findViewById(R.id.item_list_recycler);
        re.setHasFixedSize(true);
        re.setLayoutManager(new LinearLayoutManager(getActivity()));
        addmore.setOnClickListener(this);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addmore:
                getmoreemail();
                break;
        }
    }
}

