package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Create_Order extends Fragment implements View.OnClickListener{
    TextView buyer_tv;
    String buyerKey;
    Order order=new Order();
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_order,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        v=view;
        super.onViewCreated(view, savedInstanceState);
        buyer_tv=(TextView)v.findViewById(R.id.select_buyer);
        buyer_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_buyer:
                Intent intent = new Intent(getContext(),selectContact.class);
                intent.putExtra("category",1);
                startActivityForResult(intent,22);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.stay);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 22:
                if(resultCode== Activity.RESULT_OK) {
                    order.setBuyer(data.getStringExtra("person"));
                    order.setBuyerKey(data.getStringExtra("key"));
                    buyer_tv.setText(order.getBuyer());
                }
                break;
        }
    }
}
