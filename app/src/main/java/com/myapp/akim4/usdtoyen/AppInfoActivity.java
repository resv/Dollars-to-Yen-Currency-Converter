package com.myapp.akim4.usdtoyen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static java.util.jar.Pack200.Packer.ERROR;

public class AppInfoActivity extends AppCompatActivity {

    private AdView mAdView;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        //---------------------------------------------------------------//
        //RATE APP BUTTON
            Button rateAppButton = (Button) findViewById(R.id.rateAppButton);
            rateAppButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });

        //SHARE APP BUTTON
        Button shareAppButton = (Button) findViewById(R.id.shareAppButton);
        shareAppButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Dollars to Yen Currency Converter, Google Play Link: http://play.google.com/store/apps/details?id=" + getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this app that converts Dollars and Yen!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        //CONTACT DEV BUTTON
        Button contactDevButton = (Button) findViewById(R.id.contactDevButton);
        contactDevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Android@AtomKim.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "APPLICATION : DOLLARS TO YEN, (Version Code: " + versionCode + ", Version Name: " + versionName + ")");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AppInfoActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //ABOUT DEV BUTTON
        Button aboutDevButton = (Button) findViewById(R.id.aboutDevButton);

        aboutDevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.atomkim.com"));
                startActivity(intent);
            }
        });

        //---------------------------------------------------------------//
        //INFO VERSION TEXTFIELD

        TextView  appVersion = (TextView) findViewById(R.id.appVersion);
        String versionConcat = ("Version " + versionCode + " / " + versionName);

        appVersion.setText(versionConcat);


        //---------------------------------------------------------------//
        //AD INFO

        // ADBMOD INSTANTIATION
        MobileAds.initialize(this, "ca-app-pub-9665161606825012/3253543710");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
