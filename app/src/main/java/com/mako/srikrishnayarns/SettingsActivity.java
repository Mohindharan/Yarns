package com.mako.srikrishnayarns;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Mako on 2/11/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_settings);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimary)));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();


        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }

    public class SettingsFragment extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener {
       DatabaseReference db;
        String invoice_no;
        EditTextPreference invoice_Pref;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.user_settings);
             invoice_Pref = (EditTextPreference) findPreference("invoice");
            invoice_Pref.setSummary("Loading");
            db = FirebaseDatabase.getInstance().getReference().child("invoice");

            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    invoice_no=dataSnapshot.getValue().toString();
                    invoice_Pref.setSummary(invoice_no);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("invoice")) {
                db = FirebaseDatabase.getInstance().getReference().child("invoice");
                Preference pref = findPreference(key);
                pref.setSummary(sharedPreferences.getString(key, ""));
                db.setValue(sharedPreferences.getString(key, ""));
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
