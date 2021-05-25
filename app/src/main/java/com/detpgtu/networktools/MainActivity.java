package com.detpgtu.networktools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stealthcopter.networktools.IPTools;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button pingb = findViewById(R.id.pingbtn);
        Button portb = findViewById(R.id.portbtn);
        Button wolbtn = findViewById(R.id.Wolbtn);
        Button subnetbtn = findViewById(R.id.subnetbtn);
        TextView textView1 = findViewById(R.id.privateip);
        TextView textView = findViewById(R.id.privateipd);
        InetAddress ip = IPTools.getLocalIPv4Address();
        if (ip != null){
            textView1.setText(ip.getHostAddress());
        }
        textView.setText("Your Device's IP Address");
        pingb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), pingactivity.class);
                startActivity(intent);
            }
        });
        wolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), wolactivity.class);
                startActivity(intent);
            }
        });
        subnetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), subnetactivity.class);
                startActivity(intent);
            }
        });
        portb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), portactivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item2:
                Intent i = new Intent(getApplicationContext(), about.class);
                startActivity(i);
                return true;
            case R.id.item1:
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(getString(R.string.github_url)));
                startActivity(i1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }


