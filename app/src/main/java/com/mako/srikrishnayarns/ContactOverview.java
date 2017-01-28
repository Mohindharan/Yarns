package com.mako.srikrishnayarns;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ContactOverview extends Fragment {
    private String key;
    private String category;
    private DatabaseReference mDatabase;
    private ProgressDialog progress;
    private Person person;
    private View v;
    boolean percentage;
    private TextView name_tv, email_tv,charge_value_tv,buyer_type_tv,company_tv;
    private RecyclerView address_Card, bank_card;
    private CustomAdapter AddressAdapter,BankAdapter;
    private RecyclerView.LayoutManager mLayoutManager, layoutManager;
    List<String> fullAddress = new ArrayList<>();
    List<String> fullBank = new ArrayList<>();
    public static boolean firsetload=true;


    public ContactOverview(String s, String category) {
        this.key = s;
        this.category = category;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        firsetload=true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;

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
                ft.replace(R.id.content_frame, new editcontact(person,key));
                ft.commit();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {

        firsetload=true;
        name_tv = (TextView) v.findViewById(R.id.contact_name_tx);
        company_tv = (TextView) v.findViewById(R.id.company_name_tx);
        email_tv = (TextView) v.findViewById(R.id.contact_email_tx);
        buyer_type_tv = (TextView) v.findViewById(R.id.buyer_type_tv);

        charge_value_tv = (TextView) v.findViewById(R.id.service_value_tv);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.list_phone);
        address_Card = (RecyclerView) v.findViewById(R.id.address_list);
        bank_card = (RecyclerView) v.findViewById(R.id.card_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new phoneAdapter());
        if(person.getCompany()!=null)
        company_tv.setText(person.getCompany());
        if (person.getEmail()!=null)
        email_tv.setText(person.getEmail());
       if (!person.getEmail().isEmpty()){
        email_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{person.getEmail()});
                try {
                    startActivity(Intent.createChooser(i, "Send mail"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email applications installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
       }
        if(category.equals("Transport")){
            company_tv.setVisibility(View.GONE);
        }
        if (!category.equals("Transport")) {
            LinearLayout ll= (LinearLayout)v.findViewById(R.id.not_for_tras);
            ll.setVisibility(View.VISIBLE);
            if(person.getChargeValue()!=null) {
                    charge_value_tv.setText(person.getChargeValue());


            }

            if(person.getCompany()!=null)
                company_tv.setText(person.getCompany());

            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            AddressAdapter = new CustomAdapter(getContext(), person.getFullAddress());
            BankAdapter = new CustomAdapter(getContext(), person.getFullBank());
            address_Card.setLayoutManager(mLayoutManager);
            bank_card.setLayoutManager(layoutManager);
            address_Card.setAdapter(AddressAdapter);
            bank_card.setAdapter(BankAdapter);
            AddressAdapter.notifyDataSetChanged();
            BankAdapter.notifyDataSetChanged();
        }
        if (category.equals("Buyer")){

            LinearLayout ll= (LinearLayout)v.findViewById(R.id.buyer_only);
            ll.setVisibility(View.VISIBLE);
           if(person.getBuyertype()!=null){
             buyer_type_tv.setText(person.getBuyertype());
           }
            if (person.isPercentage())
                charge_value_tv.setText(person.getChargeValue()+"%");
            else
                charge_value_tv.setText(person.getChargeValue());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_overview,container,false);
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing ");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        loadData();
        return view;
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
        String ContactType[] = { "Sellers","Buyer","Transport" };
        FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
        int e =0;
        for (int i = 0; i < ContactType.length; i++) {
            if(ContactType[i].equals(category))
              e=i;
        }
        ft.replace(R.id.content_frame, new Contact_List(e));
        ft.commit();
    }
    private void loadData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(category).child(key);
        mDatabase.keepSynced(true);
        Log.d(category, key);

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(firsetload) {
                        person = dataSnapshot.getValue(Person.class);
                        //Log.d(category, key + " " + person.getName());
                        initUI();
                        firsetload=false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            progress.dismiss();


    }

    private class phoneAdapter extends RecyclerView.Adapter<phoneAdapter.ViewHolder> {
        List<String> phoneNumber = person.getPhone();


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.phone_tx.setText(phoneNumber.get(position));
        }

        @Override
        public int getItemCount() {
            return phoneNumber.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView phone_tx;
            ImageButton message_btn;

            public ViewHolder(View view) {
                super(view);
                phone_tx = (TextView) view.findViewById(R.id.text_name);
                message_btn=(ImageButton)view.findViewById(R.id.message_btn);
                message_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber.get(getLayoutPosition())));
                        startActivity(intent);
                    }
                });
                phone_tx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String number = phoneNumber.get(getLayoutPosition());
                        Uri call = Uri.parse("tel:" + number);
                        Intent surf = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(surf);

                    }
                });
            }
        }
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private Context context;
        List<String> text;

        public CustomAdapter(Context context, List<String> Address) {
            this.text=Address;
            this.context=context;
           // Log.d("",text.get(0));

        }

        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_card, parent, false);
            view.setMinimumWidth(parent.getMeasuredWidth());
            return new CustomAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
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

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.card_text);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getContext());
                        builder.setTitle("Copy to clipBoard");
                        builder.setMessage(text.get(getLayoutPosition()));
                        builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager  myClipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                                ClipData myClip=ClipData.newPlainText("text", text.get(getLayoutPosition()));
                                myClipboard.setPrimaryClip(myClip);
                                Toast.makeText(getActivity(),"copied to clipboard",Toast.LENGTH_SHORT).show();
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
