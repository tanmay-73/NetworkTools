package com.detpgtu.networktools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.stealthcopter.networktools.SubnetDevices;
import com.stealthcopter.networktools.subnet.Device;
import java.util.ArrayList;

public class subnetactivity extends AppCompatActivity {
    private TextView resultText;
    private ScrollView scrollView;
    private Button subnetDevicesButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet);
        resultText = findViewById(R.id.resultText1);
        scrollView = findViewById(R.id.scrollView3);
        subnetDevicesButton = findViewById(R.id.subnetbtn);
        findViewById(R.id.subnetbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            findSubnetDevices();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
    private void setEnabled(final View view, final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.setEnabled(enabled);
                }
            }
        });
    }
    private void findSubnetDevices() {
        setEnabled(subnetDevicesButton, false);
        final long startTimeMillis = System.currentTimeMillis();
        SubnetDevices subnetDevices = SubnetDevices.fromLocalAddress().findDevices(new SubnetDevices.OnSubnetDeviceFound() {
            @Override
            public void onDeviceFound(Device device) {
                appendResultsText("Device: " + device.ip+" "+ device.hostname);
            }
            @Override
            public void onFinished(ArrayList<Device> devicesFound) {
                float timeTaken =  (System.currentTimeMillis() - startTimeMillis)/1000.0f;
                appendResultsText("Devices Found: " + devicesFound.size());
                appendResultsText("Finished "+timeTaken+" s");
                setEnabled(subnetDevicesButton, true);
            }
        });
    }
}

