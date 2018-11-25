package com.myapp.akim4.usdtoyen;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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

                //---------------------------------------------------------------//
                    //liveJPYValue TEXTVIEW (OLD METHOD FOR COLORATION, COMMENTED OUT)
                    //INITIALIZING liveJPYValue TEXTVIEW
    //                    TextView liveJPYValue = (TextView) findViewById(R.id.liveJPYValue);
                    //BREAKING UP STRINGS FOR COLORATION IN STRINGS.XML, CREATING STRING WITH JSON VALUE, THEN CONCAT INTO ONE STRING
    //                    String liveJPYValuePrefix = ("$1");
    //                    String equals = " = ";
    //                    String liveJPYValueString = ("¥" + String.valueOf(jpyJSON));
    //                    String liveJPYValueStringFinal = liveJPYValuePrefix + equals + liveJPYValueString;
                    //SET VALUE USING THE CONCAT STRINGS
    //                    liveJPYValue.setText(liveJPYValueStringFinal);

                    //liveJPYValue TEXTVIEW (NEW METHOD FOR COLORATION)
                    TextView liveJPYValue = (TextView) findViewById(R.id.liveJPYValue);

                    //INITIALIZING BUILDER THAT WILL ACCEPT APPENDS
                    SpannableStringBuilder builder = new SpannableStringBuilder();

                    //SETTING COLOR FOR JPY PREFIX THEN APPENDS
                    String liveJPYValuePrefix = ("$1");
                    SpannableString USDSpannable = new SpannableString(liveJPYValuePrefix);
                    USDSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, liveJPYValuePrefix.length(), 0);
                    builder.append(USDSpannable);

                    //APPENDING "=" (NO COLOR)
                    String equals = " = ";
                    builder.append(equals);

                    //SETTING COLOR FOR JPY STRINGS THEN APPENDS
                    String liveJPYValueString = ("¥" + String.valueOf(jpyJSON));
                    SpannableString JPYSpannable= new SpannableString(liveJPYValueString);
                    JPYSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, JPYSpannable.length(), 0);
                    builder.append(JPYSpannable);

                    //SET VALUE FOR APPENDED STRINGS (APPENDER OBJECT NAME IS BUILDER)
                    liveJPYValue.setText(builder, TextView.BufferType.SPANNABLE);

                        //---------------------------------------------------------------//
                        //DATE JSON API (ONLY NEEDED ONCE)
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

                //---------------------------------------------------------------//
//                //liveUSDValue TEXTVIEW (OLD METHOD FOR COLORATION, COMMENTED OUT)
//                    //INITIALIZING liveUSDValue TEXTVIEW
//                        TextView liveUSDValue = (TextView) findViewById(R.id.liveUSDValue);
//                    //BREAKING UP STRINGS FOR COLORATION IN STRINGS.XML, CREATING STRING WITH JSON VALUE, THEN CONCAT INTO ONE STRING
//                        String liveUSDValuePrefix = ("¥1");
//                        String equals = " = ";
//                        String liveUSDValueString = ("$" + String.valueOf(usdJSON));
//                        String liveUSDValueStringFinal = liveUSDValuePrefix + equals + liveUSDValueString;
//                    //SET VALUE USING THE CONCAT STRINGS
//                        liveUSDValue.setText(liveUSDValueStringFinal);

                //liveUSDValue TEXTVIEW (NEW METHOD FOR COLORATION)
                TextView liveUSDValue = (TextView) findViewById(R.id.liveUSDValue);

                //INITIALIZING BUILDER THAT WILL ACCEPT APPENDS
                SpannableStringBuilder builder = new SpannableStringBuilder();

                //SETTING COLOR FOR JPY PREFIX THEN APPENDS
                String liveUSDValuePrefix = ("¥1");
                SpannableString JPYSpannable = new SpannableString(liveUSDValuePrefix);
                JPYSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, liveUSDValuePrefix.length(), 0);
                builder.append(JPYSpannable);

                //APPENDING "=" (NO COLOR)
                String equals = " = ";
                builder.append(equals);

                //SETTING COLOR FOR JPY STRINGS THEN APPENDS
                String liveUSDValueString = ("$" + String.valueOf(usdJSON));
                SpannableString USDSpannable= new SpannableString(liveUSDValueString);
                USDSpannable.setSpan(new ForegroundColorSpan(Color.BLUE), 0, USDSpannable.length(), 0);
                builder.append(USDSpannable);

                //SET VALUE FOR APPENDED STRINGS (APPENDER OBJECT NAME IS BUILDER)
                liveUSDValue.setText(builder, TextView.BufferType.SPANNABLE);

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

            //CLEAR INPUT VALUE
            editText.setText("");
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

                String amountInYenStringWithSymbol = ("¥" + amountInYenString);
                String amountInDollarStringsWithSymbol = ("$" + amountInDollarsString);

                String recentConversionValueString = (amountInYenStringWithSymbol + " = " + amountInDollarStringsWithSymbol);

                recentConversionValue.setText(recentConversionValueString);

                //CLEAR INPUT VALUE
                editText.setText("");
        }
    }
    // END OF (JPY) CONVERSION

//---------------------------------------------------------------------------------------//
//NAVIGATION FOR NEW ACTIVITIES

        //DATE INFO ACTIVITY METHOD TO OPEN
            public void openDateInfoActivity(){
                Intent intent = new Intent(this, DateInfoActivity.class);
                startActivity(intent);
            }

        //APP INFO ACTIVITY METHOD TO OPEN
        public void openAppInfoActivity(){
            Intent intent = new Intent(this, AppInfoActivity.class);
            startActivity(intent);
        }

//---------------------------------------------------------------------------------------//
//ONCREATE

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //---------------------------------------------------------------//
            //START OF JSON INSTANTIATION, ENTER API URLS

                //USD BASE JSON
                downloadUSDJsonData getUSDRate = new downloadUSDJsonData();
                getUSDRate.execute("https://ratesapi.io/api/latest?base=USD");

                //JPY BASE JSON (AND DATE)
                downloadJPYJsonData getJPYRate = new downloadJPYJsonData();
                getJPYRate.execute("https://ratesapi.io/api/latest?base=JPY");

            //---------------------------------------------------------------//
            //"Enter" KEY STARTS CONVERSION AND CLOSES KEYBOARD

                //FOR USD CONVERSION
                final EditText usdKeyboard = (EditText) findViewById(R.id.usdAmount);
                usdKeyboard.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            convertUsd(null);
                            usdKeyboard.setText("");
                        }
                        return true;
                    }
                });

                //FOR JPY CONVERSION
                final EditText jpyKeyboard = (EditText) findViewById(R.id.yenAmount);
                jpyKeyboard.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView editText, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            convertYen(null);
                            jpyKeyboard.setText("");
                        }
                        return true;
                    }
                });

            //---------------------------------------------------------------//
            //MORE INFO BUTTONS

                //DATE INFO BUTTON
                Button dateInfoButton = findViewById(R.id.dateInfoButton);
                dateInfoButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        openDateInfoActivity();
                    }
                });

                //APP INFO BUTTON
                Button appInfoButton = findViewById(R.id.appInfoButton);
                appInfoButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        openAppInfoActivity();
                    }
                });

                //SHARE APP BUTTON
                Button shareAppButton = (Button) findViewById(R.id.shareAppButton);
                shareAppButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Here is the share content body";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    }
                });

            //---------------------------------------------------------------//
            //AD INFO

                // ADBMOD INSTANTIATION
                MobileAds.initialize(this, "ca-app-pub-9665161606825012/3253543710");
                mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
        }
    }


