package com.rk.myapps.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    EditText bill_amount_edittext;
    EditText tip_percent_edittext;
    EditText no_of_ppl_edittext;
    TextView tip_amount_textview;
    TextView total_amount_textview;
    TextView each_person_pay_textview;

    Button calculate_tip;
    FloatingActionButton fab;

    final String DEFAULT_VALUE1 = "0";

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setCollapsible(true);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setLogo(R.drawable.square_icon);
        setSupportActionBar(toolbar);

        bill_amount_edittext = (EditText) findViewById(R.id.bill_amount);
        tip_percent_edittext = (EditText) findViewById(R.id.tip_percent);
        tip_amount_textview = (TextView) findViewById(R.id.tip_amount);
        total_amount_textview = (TextView) findViewById(R.id.total_amount);
        no_of_ppl_edittext = (EditText) findViewById(R.id.no_of_ppl);
        each_person_pay_textview = (TextView) findViewById(R.id.each_person_pay);
        //fab = (FloatingActionButton) findViewById(R.id.sendmail_fab);
        calculate_tip = (Button) findViewById(R.id.calculate);

        //Set default values
        tip_amount_textview.setText(DEFAULT_VALUE1);
        total_amount_textview.setText(DEFAULT_VALUE1);
        each_person_pay_textview.setText(DEFAULT_VALUE1);

        /**
         * Version2: Save calculation functionality
         */
//        fab.setImageResource(R.mipmap.sendmail);
//        fab.setVisibility(View.INVISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar/toolbar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /**
             * Version2: Add settings menu
             */
//            case R.id.action_settings: {
//                Intent settings = new Intent(this, SettingsActivity.class);
//                startActivity(settings);
//                return true;
//            }
            case R.id.action_about: {
                Intent about_page = new Intent(this, AboutActivity.class);
                startActivity(about_page);
                return true;
            }
            case R.id.action_reset: {
                bill_amount_edittext.setText("");
                tip_percent_edittext.setText("");
                no_of_ppl_edittext.setText("");
                tip_amount_textview.setText("0");
                total_amount_textview.setText("0");
                each_person_pay_textview.setText("0");
                return true;
            }
            case R.id.action_share: {
                String shareContent = "Bill amount: " + bill_amount_edittext.getText() + "\nTip percentage: " + tip_percent_edittext.getText() + "%"
                        + "\nNumber of people: " + no_of_ppl_edittext.getText() + "\nTip amount: " + tip_amount_textview.getText()
                        + "\nTotal amount: " + total_amount_textview.getText()
                        + "\nEach Person Pays: " + each_person_pay_textview.getText();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                try {
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.sendmail_header)));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_mail_client_toast), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //Calculate Tip
    public void calculateTip(View view) {
        double billAmount = 0.0;
        double tipPercent = 0.0;
        double noOfPpl = 0.0;
        double tipAmount = 0.0;
        double totalAmount = 0.0;
        double eachPersonPay = 0.0;

        DecimalFormat numberFormat = new DecimalFormat("#.00");

        //If bill amount is not entered, no further calculation should be done
        if (bill_amount_edittext.getText().toString().length() <= 0) {
            showToast();
        } else {
            billAmount = Double.parseDouble(bill_amount_edittext.getText().toString());

            if (tip_percent_edittext.getText().toString().length() <= 0) {
                tip_percent_edittext.setText("0");
            } else {
                tipPercent = Double.parseDouble(tip_percent_edittext.getText().toString());
            }

            if (no_of_ppl_edittext.getText().toString().length() <= 0) {
                no_of_ppl_edittext.setText("1");
            } else {
                noOfPpl = Integer.parseInt(no_of_ppl_edittext.getText().toString());
            }

            tipAmount = (billAmount * tipPercent) / 100;
            if (tipAmount != 0) {
                tip_amount_textview.setText(numberFormat.format(tipAmount));
            } else {
                tip_amount_textview.setText(DEFAULT_VALUE1);
            }

            totalAmount = billAmount + tipAmount;
            total_amount_textview.setText(numberFormat.format(totalAmount));

            if (noOfPpl > 1) {
                eachPersonPay = totalAmount / noOfPpl;
                each_person_pay_textview.setText(Double.toString(eachPersonPay));
            } else {
                each_person_pay_textview.setText(numberFormat.format(totalAmount));
            }
        }
    }

    public void showToast() {
        Toast.makeText(getApplicationContext(), getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resume the AdView.
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();

        super.onDestroy();
    }
}
