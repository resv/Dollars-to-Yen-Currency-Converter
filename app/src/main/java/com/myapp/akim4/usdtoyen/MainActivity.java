package com.myapp.akim4.usdtoyen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdView mAdView;

    public void convertUsd(View view) {

        EditText editText = (EditText) findViewById(R.id.usdAmount);

        String amountInDollarsString = editText.getText().toString();

        if (amountInDollarsString.matches("")) {

            Toast.makeText(this,"Enter $ (USD) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            double amountInDollarsDouble = Double.parseDouble(amountInDollarsString);

            double amountInYenDouble = amountInDollarsDouble * 113.45;

            String amountInYenString = String.format("%.2f", amountInYenDouble);

            Toast.makeText(this, "$" + amountInDollarsString + " (USD) equals to ¥" + amountInYenString + " (YEN)", Toast.LENGTH_LONG).show();
        }
    }


    public void convertYen(View view) {

        EditText editText = (EditText) findViewById(R.id.yenAmount);

        String amountInYenString = editText.getText().toString();

        if(amountInYenString.matches("")){

            Toast.makeText(this,"Enter ¥ (YEN) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            double amountInYenDouble = Double.parseDouble(amountInYenString);

            double amountInDollarsDouble = amountInYenDouble * 0.0088;

            String amountInDollarsString = String.format("%.2f", amountInDollarsDouble);

            Toast.makeText(this, "¥" + amountInYenString + " (YEN) equals to $" + amountInDollarsString + " (USD)", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-9665161606825012~7384360412");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
