package com.mako.srikrishnayarns;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class Dashboard extends Fragment implements View.OnClickListener {
    View view;
    FloatingActionButton BuyerBtn,SellerBtn,Transportbtn,payment_btn;
    LinearLayout createOrderBtn,manage_product,allconformation,forms;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.app_name);
        SellerBtn= (FloatingActionButton)view.findViewById(R.id.sellerbtn);
        Transportbtn= (FloatingActionButton)view.findViewById(R.id.transportbtn);
        payment_btn= (FloatingActionButton)view.findViewById(R.id.payment_btn);
        BuyerBtn= (FloatingActionButton)view.findViewById(R.id.buyerbtn);
        createOrderBtn= (LinearLayout) view.findViewById(R.id.create_order_btn);
        manage_product= (LinearLayout) view.findViewById(R.id.manage_product);
        allconformation= (LinearLayout) view.findViewById(R.id.all_conformation);
        forms= (LinearLayout) view.findViewById(R.id.forms);
        forms.setOnClickListener(this);
        BuyerBtn.setOnClickListener(this);
        manage_product.setOnClickListener(this);
        SellerBtn.setOnClickListener(this);
        payment_btn.setOnClickListener(this);
        Transportbtn.setOnClickListener(this);
        allconformation.setOnClickListener(this);
        createOrderBtn.setOnClickListener(this);
        ((MainActivity)getActivity()).setdarktoolbarcolor();
        ((MainActivity)getActivity()).isdash=true;
    }
    public void setFragment(Fragment fragment){
        ((MainActivity)getActivity()).isdash=false;
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null) ;
            ft.setCustomAnimations(R.anim.pull_in_right,R.anim.push_out_left);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sellerbtn:
                setFragment(new Contact_List(0));
                break;
            case  R.id.buyerbtn:
                setFragment(new Contact_List(1));
                break;
            case R.id.transportbtn:
                setFragment(new Contact_List(2));
                break;
            case R.id.create_order_btn:
                setFragment(new Create_Order());
                break;
            case R.id.manage_product:
                setFragment(new Product_list());
                break;
            case R.id.all_conformation:
                setFragment(new confirmation_list());
                break;
            case R.id.forms:
                setFragment(new Review_form());
                break;
            case R.id.payment_btn:
                setFragment(new paymentFragment());
                break;
        }
    }
}
