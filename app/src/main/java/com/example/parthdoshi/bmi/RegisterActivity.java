package com.example.parthdoshi.bmi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    TextView tvPersonalDerails;
    EditText etPDName, etPDAge, etPDContact;
    Button btnRegister;
    RadioGroup rgSex;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvPersonalDerails = (TextView)findViewById(R.id.tvPersonalDetails);
        etPDName = (EditText)findViewById(R.id.etPDName);
        etPDAge = (EditText)findViewById(R.id.etPDAge);
        etPDContact = (EditText)findViewById(R.id.etPDContact);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        rgSex = (RadioGroup)findViewById(R.id.rgSex);
        sp = getSharedPreferences("p1", MODE_PRIVATE);

        String n = sp.getString("n","");
        int a = sp.getInt("a",0);
        String c = sp.getString("c","");
        String s = sp.getString("s","");

        if ((n.length() != 0) || (a != 0) || (c.length() != 0) || (s.length() != 0)) {

            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        } else {

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (etPDName.getText().toString().length() == 0) {
                        etPDName.setError("Name is not Entered!");
                        etPDName.requestFocus();
                        return;
                    }

                    if (etPDAge.getText().toString().length() == 0) {
                        etPDAge.setError("Age is not Entered!");
                        etPDAge.requestFocus();
                        return;
                    }

                    if (etPDContact.getText().toString().length() < 10){
                        etPDContact.setError("Contact is not Entered! / Invalid Input!");
                        etPDContact.requestFocus();
                        return;
                    }

                    if (rgSex.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(RegisterActivity.this, "Choose option for Gender", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String name = etPDName.getText().toString();
                    int age = Integer.parseInt(etPDAge.getText().toString());
                    String contact = etPDContact.getText().toString();

                    int id = rgSex.getCheckedRadioButtonId();
                    RadioButton rb = rgSex.findViewById(id);
                    String sex = rb.getText().toString();

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("n", name);
                    editor.putInt("a", age);
                    editor.putString("c", contact);
                    editor.putString("s", sex);
                    editor.commit();

                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
