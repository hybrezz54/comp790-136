package com.hamzahch.lab8;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MainActivity extends AppCompatActivity {

    private static Pattern VALID_IPV4_PATTERN = null;
    private static Pattern VALID_IPV6_PATTERN = null;
    private static Pattern VALID_NUM_PATTERN = null;
    private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
    private static final String ipv6Pattern = "([0-9a-f]{1,4}:{1,2}){4,7}([0-9a-f]){1,4}";
    private static final String numPattern = "(\\d+)";

    static {
        try {
            VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
            VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
            VALID_NUM_PATTERN = Pattern.compile(numPattern, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            //logger.severe("Unable to compile pattern", e);
        }
    }

    private EditText editIpv4, editIpv6, editTx, editRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editIpv4 = findViewById(R.id.editIp4);
        editIpv6 = findViewById(R.id.editIp6);
        editTx = findViewById(R.id.editTx);
        editRx = findViewById(R.id.editRx);

        runCommandOnce();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runCommandMultiple();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void runCommandOnce() {
        try {
            // exec cmd
            Process p = Runtime.getRuntime().exec("ifconfig");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // parse cmd
            int i = 0;
            int w = -1;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                Log.e("LAB8", line);

                if (line.contains("wlan0"))
                    w = i;

                // wlan0
                if (i == w + 1) {
                    Matcher ip4Matcher = VALID_IPV4_PATTERN.matcher(line);
                    if (ip4Matcher.find())
                        editIpv4.setText(ip4Matcher.group());
                }

                // ipv4
                if (i == w + 2) {
                    Matcher ip6Matcher = VALID_IPV6_PATTERN.matcher(line);
                    if (ip6Matcher.find())
                        editIpv6.setText(ip6Matcher.group());
                }

                // rx packets
                if (i == w + 4) {
                    Matcher numMatcher = VALID_NUM_PATTERN.matcher(line);
                    if (numMatcher.find())
                        editRx.setText(numMatcher.group());
                }

                // tx packets
                if (i == w + 5) {
                    Matcher numMatcher = VALID_NUM_PATTERN.matcher(line);
                    if (numMatcher.find())
                        editTx.setText(numMatcher.group());
                }

                i++;
            }

            br.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void runCommandMultiple() {
        try {
            // exec cmd
            Process p = Runtime.getRuntime().exec("ifconfig");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // parse cmd
            int i = 0;
            int w = -1;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.contains("wlan0"))
                    w = i;

                // rx packets
                if (i == w + 4) {
                    Matcher numMatcher = VALID_NUM_PATTERN.matcher(line);
                    if (numMatcher.find())
                        editRx.setText(numMatcher.group());
                }

                // tx packets
                if (i == w + 5) {
                    Matcher numMatcher = VALID_NUM_PATTERN.matcher(line);
                    if (numMatcher.find())
                        editTx.setText(numMatcher.group());
                }

                i++;
            }

            br.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}