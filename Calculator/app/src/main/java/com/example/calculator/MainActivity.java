package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView m_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_textView = findViewById(R.id.textView);
        m_textView.setTextColor(Color.BLUE);
        m_textView.setText("Hello Android");

        Switch switch1 = findViewById(R.id.switch1);
        switch1.setText("This is a switch");
        switch1.setChecked(true);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m_textView.setText(isChecked ? "Hello Android" : "Hello World");
            }
        });



        m_textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }
}
