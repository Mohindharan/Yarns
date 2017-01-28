package com.mako.srikrishnayarns;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Mako on 1/25/2017.
 */
public class editcontact extends Fragment implements View.OnClickListener {
    TextView charge_tx;
    Switch charge;
    Button adduser,more,setAddress, setbankbtn;
    EditText email_ev,bank_ev,charge_ev,name_ev,buyer_type_ev,companny;
    EditText ph[]=new EditText[3];
    RecyclerView address_Card,bank_card;
    Toolbar toolbar;
    LinearLayout ll;
    MyUtils anime;
    String ContactType[] = { "Sellers","Buyer","Transport" };
    Spinner sp_type;
    List<String> fullAddress = new ArrayList<String>();
    List<String> fullBank = new ArrayList<String>();
    private CustomAdapter AddressAdapter;
    private CustomAdapter BankAdapter;
    private RecyclerView.LayoutManager mLayoutManager,layoutManager;
    Person person;
    View v;
    String key;
    public editcontact(Person person, String key) {
        this.person=person;
        this.fullAddress=person.fullAddress;
        this.fullBank=person.fullBank;
        this.key=key;
    }
    public void initUI(){
        email_ev =(EditText)v.findViewById(R.id.email);
        companny =(EditText)v.findViewById(R.id.company_name);
        setAddress=(Button) v.findViewById(R.id.set_address);
        address_Card=(RecyclerView)v.findViewById(R.id.address_list);
        bank_card=(RecyclerView)v.findViewById(R.id.bank_list);
        name_ev=(EditText)v.findViewById(R.id.name_ev);
        bank_ev=(EditText)v.findViewById(R.id.bank_ev);
        buyer_type_ev=(EditText)v.findViewById(R.id.buyer_type);
        charge_ev=(EditText)v.findViewById(R.id.charge_value);
        ph[0]=(EditText)v.findViewById(R.id.phone_1);
        ph[1]=(EditText)v.findViewById(R.id.phone_2);
        ph[2]=(EditText)v.findViewById(R.id.phone_3);
        charge_tx=(TextView)v.findViewById(R.id.chargetype);
        ll=(LinearLayout)v.findViewById(R.id.more_layout);
        charge=(Switch)v.findViewById(R.id.switch_charge);
        setbankbtn =(Button)v.findViewById(R.id.set_bank);
        sp_type=(Spinner)v.findViewById(R.id.contatType);
        more=(Button)v.findViewById(R.id.more);
        setbankbtn.setOnClickListener(this);
        more.setOnClickListener(this);
        anime = new MyUtils();
        anime.SlideUP(ll,getContext());
        ArrayAdapter<String> adapterType= new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, ContactType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapterType);
        setAddress.setOnClickListener(this);
        sp_type.setSelection(Integer.parseInt(person.contactType));
        charge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!charge.isChecked())
                    charge_tx.setText("Service Percentage");
                else
                    charge_tx.setText("Service Amount");
            }
        });
        charge_ev.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String str= charge_ev.getText().toString();
                if(!charge.isChecked()&&!str.isEmpty()){
                    int num=Integer.parseInt(str);
                    if(num>100) {
                        charge_ev.setError("percentage value must be less than 100");
//                        LinearLayout ll=(LinearLayout)v.findViewById(R.id.rd);
                      Toast.makeText(getActivity(), "percentage value must be less than 100",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setdata();
    }
    private void setAddress(String str) {

        if(!str.isEmpty()){
            TranslateAnimation animate = new TranslateAnimation(0,0,50,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            address_Card.setVisibility(View.VISIBLE);
            fullAddress.add(str);
            AddressAdapter = new CustomAdapter(getActivity(), fullAddress,true);
            address_Card.setAdapter(AddressAdapter);
            AddressAdapter.notifyDataSetChanged();

        }

    }
    private void setBank(String str){
        if(!str.isEmpty()){
            TranslateAnimation animate = new TranslateAnimation(0,0,50,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            bank_card.setVisibility(View.VISIBLE);
            fullBank.add(str);
            BankAdapter = new CustomAdapter( getContext(), fullBank, false);
            bank_card.setAdapter(BankAdapter);
            BankAdapter.notifyDataSetChanged();
            bank_ev.setText("");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save:
              checkdata();
                break;
        }
        return(super.onOptionsItemSelected(item));
    }
    private void setdata() {
        address_Card.setHasFixedSize(true);
        bank_card.setHasFixedSize(true);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll=(LinearLayout) v.findViewById(R.id.not_for_tras);
                EditText ed =(EditText)v.findViewById(R.id.buyer_type);
                String str= ContactType[position];
//                Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
                if (str==ContactType[2])
                    ll.setVisibility(View.GONE);
                else
                    ll.setVisibility(View.VISIBLE);
                if(str==ContactType[1])
                    ed.setVisibility(View.VISIBLE);
                else
                    ed.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AddressAdapter = new editcontact.CustomAdapter(getContext(), fullAddress,true);
        BankAdapter = new editcontact.CustomAdapter(getContext(), fullBank,false);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        address_Card.setLayoutManager(mLayoutManager);
        bank_card.setLayoutManager(layoutManager);
        address_Card.setAdapter(AddressAdapter);
        bank_card.setAdapter(BankAdapter);


        name_ev.setText(person.getName());
        if(person.getCompany()!=null)
            companny.setText(person.getCompany().toString());
        if(person.phone.size()>=1)
        ph[0].setText(person.phone.get(0));
        if(person.phone.size()>=2)
        ph[1].setText(person.phone.get(1));
        if(person.phone.size()>=3)
        ph[2].setText(person.phone.get(2));
        if (person.getEmail()!=null)
            email_ev.setText(person.getEmail());

        if(person.getChargeValue()!=null)
            charge_ev.setText(person.getChargeValue());
        charge.setChecked(person.percentage);
        if (person.getBuyertype()!=null)
            buyer_type_ev.setText(person.getBuyertype());
    }
    public void checkdata() {
        if (name_ev.getText().toString().isEmpty()){
            name_ev.setError("You must enter a name to set");
        }
        else if (!charge_ev.getText().toString().isEmpty()&&!charge.isChecked() && Integer.parseInt(charge_ev.getText().toString()) > 100){
                charge_ev.setError("percentage value must be less than 100");
        }
        else {
            getData(person);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View view=inflater.inflate(R.layout.edit_contact,container,false);
                return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                moreFields(v);
                break;
            case R.id.set_address:
                PromptAddress();
                break;
            case R.id.set_bank:
                addBank();
                break;
        }


    }

    private void getData(Person person) {
        //name numbers email_ev address bank value or percentage bool
        List<String> phone = new ArrayList<>();
        int NumberOfPhone=0;
        int type;

        person.setName(name_ev.getText().toString());
        person.setEmail(email_ev.getText().toString());
        type=sp_type.getSelectedItemPosition();

        person.setContactType(String.valueOf(type));
        for(int i=0;i<3;i++){
            if(!ph[i].getText().toString().isEmpty()){
                phone.add(ph[i].getText().toString());
                NumberOfPhone++;
            }
        }
        person.setPhone(phone);
        person.setNumOfPhone(String.valueOf(NumberOfPhone));
        if(type==1)//buyer
        {
            person.setBuyertype(buyer_type_ev.getText().toString());
            if (charge.isChecked()){
                person.setPercentage(false);
                person.setChargeValue(charge_ev.getText().toString());
            }
            else {
                person.setPercentage(true);
                person.setChargeValue(charge_ev.getText().toString());
            }
        }
        if(type!=2)// not transport
        {
            person.setCompany(companny.getText().toString());
            person.setFullAddress(fullAddress);
            person.setFullBank(fullBank);
        }



            Toast.makeText(getActivity(),"Contact Saved",Toast.LENGTH_SHORT).show();
        update(person);
                moveUp();

    }




    private void update(Person person) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        int contactType = sp_type.getSelectedItemPosition();
        String str= ContactType[contactType];
        DatabaseReference myRef = database.getReference().child(str);
        myRef.child(key).setValue(person);
    }

    private void moveUp() {

//        getActivity().getSupportFragmentManager().popBackStack();
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
        ft.replace(R.id.content_frame, new displayContact(key,person.getName(),ContactType[sp_type.getSelectedItemPosition()]));
        ft.commit();
    }


    private void addBank() {
        setBank(bank_ev.getText().toString());
    }

    public void moreFields(View v){
        anime.SlideDown(ll,v.getContext());
        more.setVisibility(View.GONE);
    }
    public void PromptAddress(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.setaddress, null);
        final EditText userInput1 = (EditText) dialogView.findViewById(R.id.edit1);
        final EditText userInput2 = (EditText) dialogView.findViewById(R.id.edit2);
        final EditText userInput3 = (EditText) dialogView.findViewById(R.id.edit3);
        final EditText userInput4 = (EditText) dialogView.findViewById(R.id.edit4);
        final EditText userInput5 = (EditText) dialogView.findViewById(R.id.edit5);
        dialogBuilder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, null);
        dialogBuilder.setNegativeButton("add later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        final AlertDialog d = dialogBuilder.create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!userInput1.getText().toString().isEmpty()||
                                !userInput2.getText().toString().isEmpty()||
                                !userInput3.getText().toString().isEmpty()||
                                !userInput4.getText().toString().isEmpty()||
                                !userInput5.getText().toString().isEmpty()){
                            String str=""+userInput1.getText()+"\n"+userInput2.getText()+"\n"+userInput3.getText()+"\n"+userInput4.getText()+"\n"+userInput5.getText();
                            setAddress(str);
                            setAddress.setText("Add More Address");
                            d.dismiss();
                        }
                        else{
                            Toast.makeText(v.getContext(),"please fill all the details",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        d.show();
    }

    private class CustomAdapter extends RecyclerView.Adapter<editcontact.CustomAdapter.ViewHolder> {

        private Context context;
        List<String> text;
        boolean isaddress;

        public CustomAdapter(Context context, List<String> Address, boolean isaddress) {
            this.text=Address;
            this.context=context;
            // Log.d("",text.get(0));
            this.isaddress=isaddress;
        }

        @Override
        public editcontact.CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card, parent, false);

            return new editcontact.CustomAdapter.ViewHolder(view,isaddress);
        }

        @Override
        public void onBindViewHolder(editcontact.CustomAdapter.ViewHolder holder, int position) {
            holder.mTextView.setText(text.get(position));
        }


        @Override
        public int getItemCount() {
            {
                return text.size();
            }


        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View v, final boolean isaddress) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.card_text);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getContext());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.just_a_edit_view, null);
                        final EditText userInput1 = (EditText) dialogView.findViewById(R.id.edit_alert);
                        builder.setView(dialogView);
                        if(isaddress)
                        builder.setTitle("Edit Address");
                        else
                        builder.setTitle("Edit Bank");
                        final int pos=getLayoutPosition();
                        userInput1.setText(text.get(pos));

                        builder.setPositiveButton("set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isaddress) {
                                    fullAddress.remove(pos);
                                    setAddress(userInput1.getText().toString());
                                }
                                else{
                                    fullBank.remove(pos);
                                    setBank(userInput1.getText().toString());
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                });
            }
        }
    }
}

