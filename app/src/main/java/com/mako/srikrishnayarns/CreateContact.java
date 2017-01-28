package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/19/2017.
 */
public class CreateContact  extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_CONTACT = 111;
    TextView charge_tx;
    Switch charge;
    Button adduser,more,setAddress, setbankbtn;
    EditText email_ev,bank_ev,charge_ev,name_ev,company_ev,buyer_type_ev;
    EditText ph[]=new EditText[3];
    RecyclerView address_Card,bank_card;
    Toolbar toolbar;
    LinearLayout ll;
    MyUtils anime;
    Person person;
    String key;
    String ContactType[] = { "Sellers","Buyer","Transport" };
    Spinner sp_type;
    List<String> fullAddress = new ArrayList<String>();
    List<String> fullBank = new ArrayList<String>();
    private horizontalAdapter AddressAdapter,BankAdapter;
    private RecyclerView.LayoutManager mLayoutManager,layoutManager;
    private static boolean import_Contact=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contact);
        initUI();
        Intent intent = getIntent();
        int pos=intent.getIntExtra("type",0);
        ArrayAdapter<String> adapterType= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ContactType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapterType);
        sp_type.setSelection(pos);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addbook:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                import_Contact=true;
                break;
            case R.id.more:
                moreFields(v);
                break;
            case R.id.set_address:
                PromptAddress();
                break;
            case R.id.set_bank:
                setBank();
                break;

        }
    }
    private void setAddress(String str) {

        if(!str.isEmpty()){
            TranslateAnimation animate = new TranslateAnimation(0,0,50,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            address_Card.setVisibility(View.VISIBLE);
            fullAddress.add(str);
            AddressAdapter = new horizontalAdapter( this, fullAddress, true);
            address_Card.setAdapter(AddressAdapter);
            AddressAdapter.notifyDataSetChanged();

        }

    }
    private void setBank(){
        String str=bank_ev.getText().toString();
        if(!str.isEmpty()){
            TranslateAnimation animate = new TranslateAnimation(0,0,50,0);
            animate.setDuration(500);
            animate.setFillAfter(true);
            bank_card.setVisibility(View.VISIBLE);
            fullBank.add(str);
            BankAdapter = new horizontalAdapter( this, fullBank, false);
            bank_card.setAdapter(BankAdapter);
            BankAdapter.notifyDataSetChanged();
            bank_ev.setText("");
        }
    }
    public void PromptAddress(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
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
    public void initUI(){
        email_ev =(EditText)findViewById(R.id.email);
        setAddress=(Button) findViewById(R.id.set_address);
        address_Card=(RecyclerView)findViewById(R.id.address_list);
        bank_card=(RecyclerView)findViewById(R.id.bank_list);
        name_ev=(EditText)findViewById(R.id.name_ev);
        bank_ev=(EditText)findViewById(R.id.bank_ev);
        buyer_type_ev=(EditText)findViewById(R.id.buyer_type);
        charge_ev=(EditText)findViewById(R.id.charge_value);
        company_ev=(EditText)findViewById(R.id.company_name);
        ph[0]=(EditText)findViewById(R.id.phone_1);
        ph[1]=(EditText)findViewById(R.id.phone_2);
        ph[2]=(EditText)findViewById(R.id.phone_3);
        adduser=(Button)findViewById(R.id.addbook);
        charge_tx=(TextView)findViewById(R.id.chargetype);
        ll=(LinearLayout)findViewById(R.id.more_layout);
        charge=(Switch)findViewById(R.id.switch_charge);
        setbankbtn =(Button)findViewById(R.id.set_bank);
        sp_type=(Spinner)findViewById(R.id.contatType);
        more=(Button)findViewById(R.id.more);
        adduser.setOnClickListener(this);
        setAddress.setOnClickListener(this);
        setbankbtn.setOnClickListener(this);
        more.setOnClickListener(this);
        anime = new MyUtils();
        anime.SlideUP(ll,this);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout ll=(LinearLayout)findViewById(R.id.not_for_tras);
                EditText ed =(EditText)findViewById(R.id.buyer_type);
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

            }
        });
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cancel);

        //PopButton
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        AddressAdapter = new horizontalAdapter( this,fullAddress,true);
        BankAdapter = new horizontalAdapter( this,fullBank,false);
        address_Card.setLayoutManager(mLayoutManager);
        bank_card.setLayoutManager(layoutManager);
        address_Card.setAdapter(AddressAdapter);
        bank_card.setAdapter(BankAdapter);
        //Layout manager for the Recycler View
        address_Card.setHasFixedSize(true);
        bank_card.setHasFixedSize(true);

    }
    public void moreFields(View v){
        anime.SlideDown(ll,this);
        more.setVisibility(View.GONE);
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

    private void checkData(){
        if(name_ev.getText().toString().isEmpty())
            name_ev.setError("You must enter a name to set");
        else if (!charge_ev.getText().toString().isEmpty()){
            if(!charge.isChecked()&&Integer.parseInt(charge_ev.getText().toString())>100)
                charge_ev.setError("percentage value must be less than 100");
        }
        else
            setData();
    }
    private void setData() {
        person= new Person();
        getData(person);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        int contactType = sp_type.getSelectedItemPosition();
        String str= ContactType[contactType];
        DatabaseReference ref = database.getReference().child(str);
        key = ref.push().getKey();
        ref.child(key).setValue(person);
    }
    private void getData(Person person) {
    //name numbers email_ev address bank value or percentage bool
         List<String> phone = new ArrayList<>();
        int NumberOfPhone=0;
        int type;

        person.setName(name_ev.getText().toString());
        person.setEmail(email_ev.getText().toString());
        type=sp_type.getSelectedItemPosition();
        person.setCompany(company_ev.getText().toString());
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
            person.setFullAddress(fullAddress);
            person.setFullBank(fullBank);
        }
        if(!import_Contact)
            saveComtact(person);
        moveUp();
    }
    private void saveComtact(Person person) {

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);

        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);


        intent.putExtra(ContactsContract.Intents.Insert.NAME, person.getName());
        if(person.getEmail()!=null) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, person.getEmail());
        }
        if(person.phone.get(0)!=null)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, person.phone.get(0));
        if(person.phone.get(1)!=null)
        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE,person.phone.get(1));
        if(person.phone.get(2)!=null)
        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE,person.phone.get(2));

        startActivity(intent);
    }
    private void moveUp(){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("key", key);
                returnIntent.putExtra("person",person.name);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
    }

@Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT) :
               // contactPicked(data);
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                        String nameStr = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                        name_ev.setText(nameStr);
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = null;
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                            int i=0;
                            while (numbers.moveToNext()) {
                                num=numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                ph[i].setText(num);
                                i++;
                            }
                            if(i>1)
                                more.performClick();
                        }
                        ContentResolver cr = getContentResolver();
                        Cursor emailCur = cr.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (emailCur.moveToNext()) {
                            // This would allow you get several email addresses
                            // if the email addresses were stored in an array
                            String email = emailCur.getString(
                                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                            email_ev.setText(email);
                        }
                    }
                    c.close();
                }

                break;

        }
    }
}
