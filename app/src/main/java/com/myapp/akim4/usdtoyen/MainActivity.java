package com.myapp.akim4.usdtoyen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;

    public void convertUsd(View view) {

        EditText editText = (EditText) findViewById(R.id.usdAmount);

        String amountInDollars = editText.getText().toString();

        if (amountInDollars.matches("")) {

            Toast.makeText(this,"Please enter an USD amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            double amountInDollarsDouble = Double.parseDouble(amountInDollars);

            double amountInYenDouble = amountInDollarsDouble * 113.45;

            String amountInYenString = String.format("%.2f", amountInYenDouble);

            Toast.makeText(this, "$" + amountInDollars + " Dollars equals to ¥" + amountInYenString + " Yen", Toast.LENGTH_LONG).show();
        }
    }


    public void convertYen(View view) {

        EditText editText = (EditText) findViewById(R.id.yenAmount);

        String amountInYen = editText.getText().toString();

        if(amountInYen.matches("")){

            Toast.makeText(this,"Please enter a YEN amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            double amountInYenDouble = Double.parseDouble(amountInYen);

            double amountInDollarsDouble = amountInYenDouble * 0.0088;

            String amountInDollarsString = String.format("%.2f", amountInDollarsDouble);

            Toast.makeText(this, "¥" + amountInYen + " Yen equals to $" + amountInDollarsString + " Dollars", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
