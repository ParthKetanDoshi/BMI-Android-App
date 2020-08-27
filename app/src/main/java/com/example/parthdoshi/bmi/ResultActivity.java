package com.example.parthdoshi.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ResultActivity extends AppCompatActivity {

    TextView tvWelcomeR, tvBMIValR, tvBMIMsgR, tvRangeUR, tvRangeNR, tvRangeOWR, tvRangeOR;
    Button btnBackR, btnShareR, btnSaveR;
    String bmiRMsg;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvWelcomeR = (TextView)findViewById(R.id.tvWelcomeR);
        tvBMIValR = (TextView)findViewById(R.id.tvBMIValR);
        tvBMIMsgR = (TextView)findViewById(R.id.tvBMIMsgR);
        tvRangeUR = (TextView)findViewById(R.id.tvRangeUR);
        tvRangeNR = (TextView)findViewById(R.id.tvRangeNR);
        tvRangeOWR = (TextView)findViewById(R.id.tvRangeOWR);
        tvRangeOR = (TextView)findViewById(R.id.tvRangeOR);
        btnBackR = (Button) findViewById(R.id.btnBackR);
        btnSaveR = (Button) findViewById(R.id.btnSaveR);
        btnShareR = (Button) findViewById(R.id.btnShareR);

        sp = getSharedPreferences("p1", MODE_PRIVATE);

        final String name = sp.getString("n", "");
        final int age = sp.getInt("a",0);
        final String contact = sp.getString("c","");
        final String sex = sp.getString("s","");
        tvWelcomeR.setText("Welcome\n" + name);

        Bundle data = getIntent().getExtras();
        final Float bmi = data.getFloat("b");

        tvBMIValR.setText("Your BMI is " + bmi);

        if (bmi <= 18.5) {

            bmiRMsg = "You are Underweight";
            tvBMIMsgR.setText(bmiRMsg);
            tvRangeUR.setTextColor(Color.parseColor("#EAB543"));
            tvRangeUR.setTypeface(null, Typeface.BOLD_ITALIC);
            tvRangeUR.setTextSize(23);

        } else if ((bmi > 18.5) && (bmi <= 25)) {

            bmiRMsg = "You are Normal";
            tvBMIMsgR.setText(bmiRMsg);
            tvRangeNR.setTextColor(Color.parseColor("#EAB543"));
            tvRangeNR.setTypeface(null, Typeface.BOLD_ITALIC);
            tvRangeNR.setTextSize(23);

        } else if ((bmi > 25) && (bmi <= 30)) {

            bmiRMsg = "You are Overweight";
            tvBMIMsgR.setText(bmiRMsg);
            tvRangeOWR.setTextColor(Color.parseColor("#EAB543"));
            tvRangeOWR.setTypeface(null, Typeface.BOLD_ITALIC);
            tvRangeOWR.setTextSize(23);

        } else {

            bmiRMsg = "You are Obese";
            tvBMIMsgR.setText(bmiRMsg);
            tvRangeOR.setTextColor(Color.parseColor("#EAB543"));
            tvRangeOR.setTypeface(null, Typeface.BOLD_ITALIC);
            tvRangeOR.setTextSize(23);

        }

        btnSaveR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss,");
                String datetime = sdf.format(calendar.getTime());
                MainActivity.db.insert(datetime, bmi);
                Toast.makeText(ResultActivity.this, "Data Updated to your History!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
             }
        });

        btnShareR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Name: " + name + "\nAge: " + age + "\nContact: " + contact + "\nSex: " + sex + "\nBMI: " + bmi + "\n" + bmiRMsg;
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(i);
            }
        });

    }
}
