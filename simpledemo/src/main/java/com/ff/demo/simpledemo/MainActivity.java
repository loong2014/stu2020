package com.ff.demo.simpledemo;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
}