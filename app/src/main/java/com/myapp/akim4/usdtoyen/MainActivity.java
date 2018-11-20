package com.myapp.akim4.usdtoyen;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //START OF INITIALIZING GLOBAL VARIABLES

        //ADMOB VARIABLE
        private AdView mAdView;
        private String date;
        private String usdRate;
        private String jpyRate;


    //END OF INITIALIZATION OF GLOBAL VARIABLES



    // START OF JSON API CODE

    static class DownloadJsonData extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls){
            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try{

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        // ALLOWS YOU TO WRITE CODE/GIVE INSTRUCTIONS AFTER ABOVE CODE HAS FINISHED RUNNING
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //TEST TO CHECK IF JSON DATA HAS BEEN FETCHED, COMMENT OR DELETE OUT FOR RELEASE
            Log.i("JSON", s);
        }
    }








    // END OF JSON API CODE







    // START OF (USD) CONVERSION
    public void convertUsd(View view) {

        // GETS INPUT VALUE OF DOLLARS AMOUNT FROM USER
        EditText editText = (EditText) findViewById(R.id.usdAmount);

        // CONVERTS DOLLARS AMOUNT INTO A STRING FOR USABILITY
        String amountInDollarsString = editText.getText().toString();

        // CHECK IF NULL VALUES WAS GIVEN
        if (amountInDollarsString.matches("")) {

            // TOAST TO USER IF USD CONVERT BUTTON WAS PRESSED WITH NULL VALUES
            Toast.makeText(this,"Enter $ (USD) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            // CAST USD AMOUNT TO A DOUBLE FOR USABILITY
            double amountInDollarsDouble = Double.parseDouble(amountInDollarsString);

            // FORMULA FOR USD AMOUNT INTO YEN AMOUNT DOUBLE
            double amountInYenDouble = amountInDollarsDouble * 113.45;

            // CONVERT YEN AMOUNT INTO A STRING FOR USABILITY, ADD DECIMAL WITH LIMIT TO THE THOUSANDTHS PLACE
            String amountInYenString = String.format("%.2f", amountInYenDouble);

            // TOAST USD TO JPY CONVERSION RESULT TO USER
            Toast.makeText(this, "$" + amountInDollarsString + " (USD) equals to ¥" + amountInYenString + " (JPY)", Toast.LENGTH_LONG).show();
        }
    }
    // END OF (USD) CONVERSION

    //-------------------------------------------------------------------------------------//

    // START OF (JPY) CONVERSION
    public void convertYen(View view) {

        // GETS INPUT VALUE OF YEN AMOUNT FROM USER
        EditText editText = (EditText) findViewById(R.id.yenAmount);

        // CONVERTS YEN AMOUNT INTO A STRING FOR USABILITY
        String amountInYenString = editText.getText().toString();

        // CHECK IF NULL VALUES WAS GIVEN
        if(amountInYenString.matches("")){

            // TOAST TO USER IF JPY CONVERT BUTTON WAS PRESSED WITH NULL VALUES
            Toast.makeText(this,"Enter ¥ (JPY) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            // CAST YEN AMOUNT TO A DOUBLE FOR USABILITY
            double amountInYenDouble = Double.parseDouble(amountInYenString);

            // FORMULA FOR YEN AMOUNT INTO DOLLARS AMOUNT DOUBLE
            double amountInDollarsDouble = amountInYenDouble * 0.0088;

            // CONVERT DOLLARS AMOUNT INTO A STRING FOR USABILITY, ADD DECIMAL WITH LIMIT TO THE THOUSANDTHS PLACE
            String amountInDollarsString = String.format("%.2f", amountInDollarsDouble);

            // TOAST JPY TO USD CONVERSION RESULT TO USER
            Toast.makeText(this, "¥" + amountInYenString + " (JPY) equals to $" + amountInDollarsString + " (USD)", Toast.LENGTH_SHORT).show();
        }
    }
    // END OF (JPY) CONVERSION



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // JSON INSTANTIATION, ENTER API URL
        DownloadJsonData getJPYRate = new DownloadJsonData();
        getJPYRate.execute("https://ratesapi.io/api/latest?base=JPY");

        DownloadJsonData getUSDRate = new DownloadJsonData();
        getUSDRate.execute("https://ratesapi.io/api/latest?base=USD");


        // ADBMOD INSTANTIATION
        MobileAds.initialize(this, "ca-app-pub-9665161606825012/3253543710");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
