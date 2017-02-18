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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class paymentFragment extends Fragment {
    RecyclerView re;
    View v;
    ProgressDialog progress;
    paymentAdapter cc;

    private List<Order> dataset=new ArrayList<>();
    private List<String> mDatakey=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.payment_fragment_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        getActivity().setTitle("Payments to be made");
        init();
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false);
        progress.show();
        loaddata();

    }

    public void loaddata() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("order");
        db.orderByChild("remaining").startAt(1).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void init() {
        re=(RecyclerView)v.findViewById(R.id.my_recycler_view);
        re.setLayoutManager(new LinearLayoutManager(getContext()));
        cc =new paymentAdapter(dataset,mDatakey,getActivity(),paymentFragment.this);
        re.setAdapter(cc);

    }
}
