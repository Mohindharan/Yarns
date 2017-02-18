package com.mako.srikrishnayarns;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Mako on 2/9/2017.
 */
public class UploadForm extends Activity {
    FloatingActionButton fab;
    String type,invoice,date,key;
    boolean isset=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        date= DateFormat.getDateTimeInstance().format(new Date());
        Intent in=getIntent();
        key=in.getStringExtra("key");
        type=in.getStringExtra("type");
        invoice=String.valueOf(in.getIntExtra("invoice",0));
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                String[] mimeTypes = {"application/pdf"};
                chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 77);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 77 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String filePath = uri.getLastPathSegment();
            String exe=filePath.substring(filePath.lastIndexOf(".")+1);
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setText("Share PDF doc")
                    .setType("application/pdf")
                    .setSubject(""+invoice+"_"+type+"_"+date.substring(0,12)+"."+exe)
                    .setStream(uri)
                    .getIntent()
                    .setPackage("com.google.android.apps.docs");
            startActivityForResult(shareIntent,55);

        }
        if (requestCode==55&&resultCode==Activity.RESULT_OK){
            Toast.makeText(this,"successfully uploaded",Toast.LENGTH_SHORT).show();
            DatabaseReference dv= FirebaseDatabase.getInstance().getReference().child("order").child(key).child(type);
            dv.setValue(true);
//            finish();
            onBackPressed();
        }

        if (requestCode==55&&resultCode==Activity.RESULT_CANCELED){
            Toast.makeText(this,"not okay",Toast.LENGTH_SHORT).show();
        }

    }

}
