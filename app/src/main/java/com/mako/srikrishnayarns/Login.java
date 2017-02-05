package com.mako.srikrishnayarns;



import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ShareCompat;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Login extends BaseDriveActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
       // startActivityForResult(Intent.createChooser(intent, "Select Picture"), 77);
        startActivity(new Intent(Login.this,MainActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 77 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();


            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                        .setText("Share PDF doc")
                        .setType("application/pdf")
                        .setChooserTitle("hele")
                        .setStream(uri )
                        .getIntent()
                        .setPackage("com.google.android.apps.docs");
                startActivity(shareIntent);

        }
    }
}
