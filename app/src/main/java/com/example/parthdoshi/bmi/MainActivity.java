package com.example.parthdoshi.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, PopupMenu.OnMenuItemClickListener {

    Button btnCalculate, btnViewHistory;
    TextView tvTitle, tvHtMsg, tvWtMsg, tvWtUnits, tvLoc, tvTemp;
    Spinner spnHtFt, spnHtIn;
    EditText etWt;
    ImageButton ibMenu;

    GoogleApiClient gac;
    Location loc;

    public static DatabaseHandler db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnViewHistory = (Button) findViewById(R.id.btnViewHistroy);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvHtMsg = (TextView) findViewById(R.id.tvHtMsg);
        tvWtMsg = (TextView) findViewById(R.id.tvWtMsg);
        tvWtUnits = (TextView) findViewById(R.id.tvWtUnits);
        tvLoc = (TextView) findViewById(R.id.tvLoc);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        spnHtFt = (Spinner) findViewById(R.id.spnHtFt);
        spnHtIn = (Spinner) findViewById(R.id.spnHtIn);
        etWt = (EditText) findViewById(R.id.etWt);
        ibMenu = (ImageButton) findViewById(R.id.ibMenu);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addOnConnectionFailedListener(this);
        builder.addConnectionCallbacks(this);
        gac = builder.build();

        spnHtFt.setPrompt("Feet");
        spnHtIn.setPrompt("Inches");

        db = new DatabaseHandler(this);

        sp = getSharedPreferences("p1", MODE_PRIVATE);

        String name = sp.getString("n", "");
        tvTitle.setText("Welcome\n" + name);

        ArrayList<Integer> HtFt = new ArrayList<>();
        HtFt.add(0);
        HtFt.add(1);
        HtFt.add(2);
        HtFt.add(3);
        HtFt.add(4);
        HtFt.add(5);
        HtFt.add(6);
        HtFt.add(7);
        HtFt.add(8);
        HtFt.add(9);
        HtFt.add(10);

        ArrayAdapter<Integer> adapterHtFt = new ArrayAdapter<Integer>(this, android.R.layout.select_dialog_item, HtFt);

        spnHtFt.setAdapter(adapterHtFt);

        ArrayList<Integer> HtIn = new ArrayList<>();
        HtIn.add(0);
        HtIn.add(1);
        HtIn.add(2);
        HtIn.add(3);
        HtIn.add(4);
        HtIn.add(5);
        HtIn.add(6);
        HtIn.add(7);
        HtIn.add(8);
        HtIn.add(9);
        HtIn.add(10);
        HtIn.add(11);

        ArrayAdapter<Integer> adapterHtIn = new ArrayAdapter<Integer>(this, android.R.layout.select_dialog_item, HtIn);

        spnHtIn.setAdapter(adapterHtIn);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((etWt.getText().toString().length() == 0) || etWt.getText().toString().equals(".")){
                    etWt.setError("Please Enter your Weight");
                    etWt.requestFocus();
                    return;
                }

                int htfti = spnHtFt.getSelectedItemPosition();
                int htft = Integer.parseInt(spnHtFt.getItemAtPosition(htfti).toString());
                int htini = spnHtIn.getSelectedItemPosition();
                int htin = Integer.parseInt(spnHtIn.getItemAtPosition(htini).toString());

                htin = htin + (htft * 12);
                double htmd = htin * 0.0254;
                float htm = (float)htmd;
                float wt = Float.parseFloat(etWt.getText().toString());
                if (wt <= 0){
                    etWt.setError("Please Enter your Weight");
                    etWt.requestFocus();
                    return;
                }
                float bmi = wt / (htm * htm);

                spnHtFt.setSelection(0);
                spnHtIn.setSelection(0);
                etWt.setText("");
                etWt.requestFocus();

                Intent i = new Intent(MainActivity.this, ResultActivity.class);
                i.putExtra("b",bmi);
                startActivity(i);
            }
        });

        btnViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

    }

    public void showPopup (View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.m1);
        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gac != null)
            gac.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gac != null) {
            gac.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc!=null)
        {
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            Geocoder g = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> la = g.getFromLocation(lat, lon, 1);
                android.location.Address add = la.get(0);
                String msg = add.getLocality();
                tvLoc.setText("     " + msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Enable your GPS!", Toast.LENGTH_SHORT).show();
        }
        String c = tvLoc.getText().toString();

        String url = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + c +"&appid=0673a71ee1dea7d1a2c3a0ba6e4c0de7";

        MyTask t = new MyTask();
        t.execute(url);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent i = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(i);
                return true;
            case R.id.contact:
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse("https://www.linkedin.com/in/parthketandoshi"));
                startActivity(j);
                return true;
            default:
                return false;
        }
    }

    class MyTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... params) {
            double temp = 0.0;
            String line, json;
            line = "";
            json = "";

            try
            {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.connect();

                InputStreamReader isr = new InputStreamReader(con.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                while ((line = br.readLine()) != null)
                {
                    json = json + line + "\n";
                }

                JSONObject o = new JSONObject(json);
                JSONObject p = o.getJSONObject("main");
                temp = p.getDouble("temp");
            }
            catch(Exception e)
            {

            }
            return temp;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvTemp.setText(aDouble + "       ");
        }
    }
}

