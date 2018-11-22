package com.myapp.akim4.usdtoyen;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //START OF INITIALIZING GLOBAL VARIABLES
    private AdView mAdView;
    public Double usdJSON;
    public Double jpyJSON;

//---------------------------------------------------------------------------------------//

    // START OF USD BASE, LOOKS FOR JPY JSON API CODE
    public class downloadUSDJsonData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // ALLOWS YOU TO WRITE CODE/GIVE INSTRUCTIONS AFTER ABOVE CODE HAS FINISHED RUNNING (s can be renamed to anything)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //CREATE JSON OBJECT VARIABLE FROM THE JDON DATA
                JSONObject jsonObject = new JSONObject(s);

                //CREATE JSON OBJECT OF SPECIFIC DATA BY PARAMETER
                JSONObject rateInfo = jsonObject.getJSONObject("rates");

                //CREATE JSON ARRAY TO EXTRACT DATA
                JSONArray rateArr = rateInfo.toJSONArray(rateInfo.names());

                //EXTRACT ELEMENT FROM JSON ARRAY
                jpyJSON = rateArr.getDouble(17);

                    //INITIALIZING liveJPYValue TEXTVIEW
                    TextView liveJPYValue = (TextView) findViewById(R.id.liveJPYValue);

                    //CREATING STRING WITH JSON VALUE, THEN CONCAT INTO ONE STRING
                    String liveJPYValueString = String.valueOf(jpyJSON);
                    String jpyValuePrefix = ("$1 = ¥" + liveJPYValueString);

                    //SET VALUE USING THE CONCAT STRINGS
                    liveJPYValue.setText(jpyValuePrefix);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // END OF USD BASE, LOOKS FOR JPY JSON API CODE

//---------------------------------------------------------------------------------------//

    // START OF JPY BASE, LOOKS FOR USD JSON API CODE
    public class downloadJPYJsonData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // ALLOWS YOU TO WRITE CODE/GIVE INSTRUCTIONS AFTER ABOVE CODE HAS FINISHED RUNNING (s can be renamed to anything)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //CREATE JSON OBJECT VARIABLE FROM THE JSON DATA
                JSONObject jsonObject = new JSONObject(s);

                //CREATE JSON OBJECT OF SPECIFIC DATA BY PARAMETER
                JSONObject rateInfo = jsonObject.getJSONObject("rates");

                //CREATE JSON ARRAY TO EXTRACT DATA
                JSONArray rateArr = rateInfo.toJSONArray(rateInfo.names());

                //EXTRACT ELEMENT FROM JSON ARRAY FOR CONVERSION FUNCTION
                usdJSON = rateArr.getDouble(30);

                    //INITIALIZING liveUSDValue TEXTVIEW
                    TextView liveUSDValue = (TextView) findViewById(R.id.liveUSDValue);

                    //CREATING STRING WITH JSON VALUE, THEN CONCAT INTO ONE STRING
                    String liveUSDValueString = String.valueOf(usdJSON);
                    String usdValuePrefix = ("¥1 = $" + liveUSDValueString);

                    //SET VALUE USING THE CONCAT STRINGS
                    liveUSDValue.setText(usdValuePrefix);

                //---------------------------------------------------------------//
                    //DATE JSON API
                        //CREATE STRING VARIABLE FROM THE JSON DATA, (CAST TO STRING)
                        String dateJSON = jsonObject.getString("date");

                        //INITIALIZES liveDateValue TEXTVIEW AND CREATE DATE STRING VALUE
                        TextView liveDateValue = (TextView) findViewById(R.id.liveDateValue);

                        //CREATING STRING WITH JSON VALUE, THEN CONCAT INTO ONE STRING
                        String liveDateValueString = String.valueOf(dateJSON);
                        String dateValuePrefix = ("Last updated on: " + liveDateValueString);

                        //SET VALUE USING THE CONCAT STRINGS
                        liveDateValue.setText(dateValuePrefix);
                //---------------------------------------------------------------//

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // END OF JPY BASE, LOOKS FOR USD JSON API CODE

//---------------------------------------------------------------------------------------//

    // START OF (USD) CONVERSION
    public void convertUsd(View view) {

        // GETS INPUT VALUE OF DOLLARS AMOUNT FROM USER
        EditText editText = (EditText) findViewById(R.id.usdAmount);

        // CONVERTS DOLLARS AMOUNT INTO A STRING FOR USABILITY
        String amountInDollarsString = editText.getText().toString();

        // CHECK IF NULL VALUES WAS GIVEN
        if (amountInDollarsString.matches("")) {

            // TOAST TO USER IF USD CONVERT BUTTON WAS PRESSED WITH NULL VALUES
            Toast.makeText(this, "Enter $ (USD) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            // CAST USD AMOUNT TO A DOUBLE FOR USABILITY
            double amountInDollarsDouble = Double.parseDouble(amountInDollarsString);

            // FORMULA FOR USD AMOUNT INTO YEN AMOUNT DOUBLE
            double amountInYenDouble = amountInDollarsDouble * jpyJSON;

            // CONVERT YEN AMOUNT INTO A STRING FOR USABILITY, ADD DECIMAL WITH LIMIT TO THE THOUSANDTHS PLACE
            String amountInYenString = String.format("%.2f", amountInYenDouble);

            // TOAST USD TO JPY CONVERSION RESULT TO USER
            Toast.makeText(this, "$" + amountInDollarsString + " (USD) equals to ¥" + amountInYenString + " (JPY)", Toast.LENGTH_LONG).show();

            //RECENT (USD) CONVERSION TEXTVIEW
            TextView recentConversionValue = (TextView) findViewById(R.id.recentConversionValue);
            String recentConversionValueString = ("$" + amountInDollarsString +  " = ¥" + amountInYenString);
            recentConversionValue.setText(recentConversionValueString);
        }
    }
    // END OF (USD) CONVERSION

//---------------------------------------------------------------------------------------//

    // START OF (JPY) CONVERSION
    public void convertYen(View view) {

        // GETS INPUT VALUE OF YEN AMOUNT FROM USER
        EditText editText = (EditText) findViewById(R.id.yenAmount);

        // CONVERTS YEN AMOUNT INTO A STRING FOR USABILITY
        String amountInYenString = editText.getText().toString();

        // CHECK IF NULL VALUES WAS GIVEN
        if (amountInYenString.matches("")) {

            // TOAST TO USER IF JPY CONVERT BUTTON WAS PRESSED WITH NULL VALUES
            Toast.makeText(this, "Enter ¥ (JPY) amount to convert", Toast.LENGTH_SHORT).show();

        } else {

            // CAST YEN AMOUNT TO A DOUBLE FOR USABILITY
            double amountInYenDouble = Double.parseDouble(amountInYenString);

            // FORMULA FOR YEN AMOUNT INTO DOLLARS AMOUNT DOUBLE
            double amountInDollarsDouble = amountInYenDouble * usdJSON;

            // CONVERT DOLLARS AMOUNT INTO A STRING FOR USABILITY, ADD DECIMAL WITH LIMIT TO THE THOUSANDTHS PLACE
            String amountInDollarsString = String.format("%.2f", amountInDollarsDouble);

            // TOAST JPY TO USD CONVERSION RESULT TO USER
            Toast.makeText(this, "¥" + amountInYenString + " (JPY) equals to $" + amountInDollarsString + " (USD)", Toast.LENGTH_SHORT).show();

                //RECENT (JPY) CONVERSION TEXTVIEW
                TextView recentConversionValue = (TextView) findViewById(R.id.recentConversionValue);
                String recentConversionValueString = ("¥" + amountInYenString +  " = $" + amountInDollarsString);
                recentConversionValue.setText(recentConversionValueString);
        }
    }
    // END OF (JPY) CONVERSION

//---------------------------------------------------------------------------------------//

    //ONCREATE
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //START OF JSON INSTANTIATION, ENTER API URLS
                //USD BASE JSON
                downloadUSDJsonData getUSDRate = new downloadUSDJsonData();
                getUSDRate.execute("https://ratesapi.io/api/latest?base=USD");

                //JPY BASE JSON (AND DATE)
                downloadJPYJsonData getJPYRate = new downloadJPYJsonData();
                getJPYRate.execute("https://ratesapi.io/api/latest?base=JPY");

            // ADBMOD INSTANTIATION
            MobileAds.initialize(this, "ca-app-pub-9665161606825012/3253543710");
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
    }


