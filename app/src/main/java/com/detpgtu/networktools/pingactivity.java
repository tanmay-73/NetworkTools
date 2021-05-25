package com.detpgtu.networktools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.stealthcopter.networktools.IPTools;
import com.stealthcopter.networktools.Ping;
import com.stealthcopter.networktools.ping.PingResult;
import com.stealthcopter.networktools.ping.PingStats;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class pingactivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView resultText;
    private EditText editIpAddress;
    private ScrollView scrollView;
    private Button pingButton;
    private String text;
    private int textint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);
        resultText = findViewById(R.id.resultText);
        editIpAddress = findViewById(R.id.editIpAddress1);
        scrollView = findViewById(R.id.scrollView1);
        pingButton = findViewById(R.id.pingButton);
        InetAddress ipAddress = IPTools.getLocalIPv4Address();
        if (ipAddress != null){
            editIpAddress.setText(ipAddress.getHostAddress());
        }
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        findViewById(R.id.pingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            doPing();
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
    private void doPing() throws Exception {
        String ipAddress = editIpAddress.getText().toString();
        if (TextUtils.isEmpty(ipAddress)) {
            appendResultsText("Invalid Ip Address");
            return;
        }
        setEnabled(pingButton, false);
        // Perform a single synchronous ping
        PingResult pingResult = null;
        try {
            pingResult = Ping.onAddress(ipAddress).setTimeOutMillis(1000).doPing();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            appendResultsText(e.getMessage());
            setEnabled(pingButton, true);
            return;
        }
        appendResultsText("Pinging Address: " + pingResult.getAddress().getHostAddress());
        appendResultsText("HostName: " + pingResult.getAddress().getHostName());
        appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
        // Perform an asynchronous ping
        Ping.onAddress(ipAddress).setTimeOutMillis(1000).setTimes(textint).doPing(new Ping.PingListener() {
            @Override
            public void onResult(PingResult pingResult) {
                if (pingResult.isReachable) {
                    appendResultsText(String.format("%.2f ms", pingResult.getTimeTaken()));
                } else {
                    appendResultsText(getString(R.string.hint_ip_address));
                }
            }
            @Override
            public void onFinished(PingStats pingStats) {
                appendResultsText(String.format("Pings: %d, Packets lost: %d",
                        pingStats.getNoPings(), pingStats.getPacketsLost()));
                appendResultsText(String.format("Min/Avg/Max Time: %.2f/%.2f/%.2f ms",
                        pingStats.getMinTimeTaken(), pingStats.getAverageTimeTaken(), pingStats.getMaxTimeTaken()));
                setEnabled(pingButton, true);
            }
            @Override
            public void onError(Exception e) {
                // TODO: STUB METHOD
                setEnabled(pingButton, true);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        textint= Integer.parseInt(text);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}