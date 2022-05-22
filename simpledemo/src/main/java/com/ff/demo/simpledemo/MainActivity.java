package com.ff.demo.simpledemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView errorTipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorTipView = findViewById(R.id.errorTip);
        findViewById(R.id.btnJumpVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNetfixJump("http://www.netflix.com/watch/80212762");
            }
        });
        findViewById(R.id.btnJumpTvVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNetfixJump("https://www.netflix.com/title/81072852");
            }
        });

        findViewById(R.id.btnGetUA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserAgent();
            }
        });
        findViewById(R.id.btnSlingPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doJumpWebViewPage(1);
            }
        });
    }

    private void doJumpWebViewPage(int type) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("webUrl", "https://watch.sling.com/1/asset/cc1085690f7b4f6cb00fcd1ec8794546/watch");
        startActivity(intent);
    }

    private void doNetfixJump(String link) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setData(Uri.parse(link));
        intent.setComponent(new ComponentName(
                "com.netflix.mediaclient",
                "com.netflix.mediaclient.ui.launch.NetflixComLaunchActivity"
        ));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        boolean hasError;

        try {
            startActivity(intent);
            hasError = false;
        } catch (Exception e) {
            e.printStackTrace();
            errorTipView.setText("netfix:" + e);
            hasError = true;
        }
        if (hasError) {
            Intent errorIntent = new Intent();
            errorIntent.setAction("android.intent.action.VIEW");
            errorIntent.setData(Uri.parse(link));
            errorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                startActivity(errorIntent);
            } catch (Exception e) {
                e.printStackTrace();
                errorTipView.setText("webview :" + e);
            }
        }
    }

    /**
     * 更改UA
     */
    private void updateUA() {
        WebView webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        String ua = settings.getUserAgentString();

        String newUa = ua;
        settings.setUserAgentString(newUa);
    }

    /**
     * 获取应用内webview的UA
     * <p>
     * https://www.ip138.com/useragent/
     */
    @SuppressLint("ObsoleteSdkInt")
    public String getUserAgent() {
        Context context = getApplicationContext();
        String userAgent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Throwable ignored) {
            }
            if (userAgent == null) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuilder sb = new StringBuilder();
        if (userAgent != null) {
            for (int i = 0, length = userAgent.length(); i < length; i++) {
                char c = userAgent.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", (int) c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}