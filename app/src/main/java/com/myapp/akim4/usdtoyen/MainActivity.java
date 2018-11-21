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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //START OF INITIALIZING GLOBAL VARIABLES
    private AdView mAdView;
    static String dateJSON;
    static private Double usdJSON;
    static private Double jpyJSON;
    public String jpyValuePrefix;
    public String usdValuePrefix;

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

            //TEST TO CHECK IF JSON DATA HAS BEEN FETCHED, COMMENT OR DELETE OUT FOR RELEASE
            //Log.i("JSON", s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONObject rateInfo = jsonObject.getJSONObject("rates");

                JSONArray rateArr = rateInfo.toJSONArray(rateInfo.names());

                jpyJSON = rateArr.getDouble(17);

                    //INITIALIZING liveJPYValue TEXTVIEW DURING POST EXECUTE
                    TextView liveJPYValue = (TextView) findViewById(R.id.liveJPYValue);
                    String liveJPYValueString = String.valueOf(jpyJSON);

                        //IN ORDER TO CONCAT IN setText WE ENCAPSULATE STRINGS
                        jpyValuePrefix = ("$1 = ¥" + liveJPYValueString);
                        liveJPYValue.setText(jpyValuePrefix);

                //FOR LOG TESTING PURPOSES
                    //Double jpyObj = rateArr.getDouble(17);
                    //String jpyRate = String.valueOf(jpyObj);
                    //Log.i("JSON", jpyRate);

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

            //TEST TO CHECK IF JSON DATA HAS BEEN FETCHED, COMMENT OR DELETE OUT FOR RELEASE
            //Log.i("JSON", s);
//
            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONObject rateInfo = jsonObject.getJSONObject("rates");

                JSONArray rateArr = rateInfo.toJSONArray(rateInfo.names());

                usdJSON = rateArr.getDouble(30);

                TextView liveUSDValue = (TextView) findViewById(R.id.liveUSDValue);
                String liveUSDValueString = String.valueOf(usdJSON);
                liveUSDValue.setText(liveUSDValueString);

                    //INITIALIZES THE TEXTVIEW AND INSERTS THE JSON DATE VALUE
                    TextView liveDateValue = (TextView) findViewById(R.id.liveDateValue);
                    String liveDateValueString = String.valueOf(dateJSON);

                    //IN ORDER TO CONCAT IN setText WE ENCAPSULATE STRINGS
                    usdValuePrefix = ("¥1 = $" + liveUSDValueString);
                    liveUSDValue.setText(usdValuePrefix);

                    //LIVE DATE VALUE, ONLY NEEDED ONCE SO IT EXISTS IN THE JPY ASYNC
                        //ENCAPSULATES THE DATE VALUE INTO A VARIABLE
                        dateJSON = jsonObject.getString("date");

                //FOR LOG TESTING PURPOSES
                    //Double usdObj = rateArr.getDouble(30);
                    //String usdRate = String.valueOf(usdObj);
                    //Log.i("JSON", usdRate);
                // Actual JSON data fetching
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

                //JPY BASE JSON
                downloadJPYJsonData getJPYRate = new downloadJPYJsonData();
                getJPYRate.execute("https://ratesapi.io/api/latest?base=JPY");

            // ADBMOD INSTANTIATION
            MobileAds.initialize(this, "ca-app-pub-9665161606825012/3253543710");
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


        }
    }


