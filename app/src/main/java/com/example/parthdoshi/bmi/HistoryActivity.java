package com.example.parthdoshi.bmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistory = (TextView)findViewById(R.id.tvHistory);

        String data = MainActivity.db.view();

        if (data.length() == 0)
            tvHistory.setText("\n \n No Records to show");
        else
            tvHistory.setText(data);
    }
}
