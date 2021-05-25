package com.detpgtu.networktools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class about extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about1);
        TextView textView=findViewById(R.id.ab);
        textView.setText("I created this app for my design engineering subject project implementation code is\n"+"Available On Github You can clone\n" +
                "and add your own new utility or maybe make the UI better\n" +
                "\n" +
                "Best Regards\n" +
                ":) Tanmay ");
    }
}
