package com.detpgtu.networktools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.stealthcopter.networktools.ARPInfo;
import com.stealthcopter.networktools.IPTools;
import com.stealthcopter.networktools.WakeOnLan;

import java.io.IOException;
import java.net.InetAddress;

public class wolactivity extends AppCompatActivity {
    private TextView resultText;
    private EditText editIpAddress;
    private ScrollView scrollView;
    private Button wolButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wol);
        resultText = findViewById(R.id.resultText4);
        editIpAddress = findViewById(R.id.editIpAddress1);
        scrollView = findViewById(R.id.scrollView4);
        wolButton = findViewById(R.id.wolButton);
        InetAddress ipAddress = IPTools.getLocalIPv4Address();
        if (ipAddress != null) {
            editIpAddress.setText(ipAddress.getHostAddress());
        }
        findViewById(R.id.wolButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doWakeOnLan();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
        private void appendResultsText(final String text){
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
        private void setEnabled ( final View view, final boolean enabled){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (view != null) {
                        view.setEnabled(enabled);
                    }
                }
            });
        }
        private void doWakeOnLan () throws IllegalArgumentException {
            String ipAddress = editIpAddress.getText().toString();

            if (TextUtils.isEmpty(ipAddress)) {
                appendResultsText("Invalid Ip Address");
                return;
            }
            setEnabled(wolButton, false);
            appendResultsText("IP address: " + ipAddress);
            String macAddress = ARPInfo.getMACFromIPAddress(ipAddress);
            if (macAddress == null) {
                appendResultsText("Could not fromIPAddress MAC address, cannot send WOL packet without it.");
                setEnabled(wolButton, true);
                return;
            }
            appendResultsText("MAC address: " + macAddress);
            appendResultsText("IP address2: " + ARPInfo.getIPAddressFromMAC(macAddress));
            try {
                WakeOnLan.sendWakeOnLan(ipAddress, macAddress);
                appendResultsText("WOL Packet sent");
            } catch (IOException e) {
                appendResultsText(e.getMessage());
                e.printStackTrace();
            } finally {
                setEnabled(wolButton, true);
            }
        }
    }
