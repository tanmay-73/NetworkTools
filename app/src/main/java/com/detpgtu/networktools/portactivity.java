package com.detpgtu.networktools;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.stealthcopter.networktools.IPTools;
import com.stealthcopter.networktools.PortScan;

import java.net.InetAddress;
import java.util.ArrayList;

public class portactivity extends AppCompatActivity {
    private TextView resultText;
    private EditText editIpAddress;
    private ScrollView scrollView;
    private Button portScanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port);


        resultText = findViewById(R.id.resultText1);
        editIpAddress = findViewById(R.id.editIpAddress2);
        scrollView = findViewById(R.id.scrollView2);
        portScanButton = findViewById(R.id.portbtn);

        InetAddress ipAddress = IPTools.getLocalIPv4Address();
        if (ipAddress != null) {
            editIpAddress.setText(ipAddress.getHostAddress());
        }

        findViewById(R.id.portbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPortScan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
        private void doPortScan () throws Exception {
            String ipAddress = editIpAddress.getText().toString();
            if (TextUtils.isEmpty(ipAddress)) {
                appendResultsText("Invalid Ip Address");
                setEnabled(portScanButton, true);
                return;
            }
            setEnabled(portScanButton, false);
            appendResultsText("PortScanning IP: " + ipAddress);
            ArrayList<Integer> openPorts = PortScan.onAddress(ipAddress).setPort(21).setMethodTCP().doScan();
            final long startTimeMillis = System.currentTimeMillis();
            PortScan portScan = PortScan.onAddress(ipAddress).setPortsAll().setMethodTCP().doScan(new PortScan.PortListener() {
                @Override
                public void onResult(int portNo, boolean open) {
                    if (open) appendResultsText("Open: " + portNo);
                }

                @Override
                public void onFinished(ArrayList<Integer> openPorts) {
                    appendResultsText("Open Ports: " + openPorts.size());
                    appendResultsText("Time Taken: " + ((System.currentTimeMillis() - startTimeMillis) / 1000.0f));
                    setEnabled(portScanButton, true);
                }
            });
        }
    private void appendResultsText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultText.append(text + "\n");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });
    }
    private void setEnabled(final View view, final  boolean enabled)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.setEnabled(enabled);
                }
            }
        });
    }
}
