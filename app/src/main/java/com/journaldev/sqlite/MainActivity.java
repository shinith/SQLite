package com.journaldev.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    WebView webview;
    LinearLayout LoaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        webview = (WebView) findViewById(R.id.webview);
        LoaderView = findViewById(R.id.loaderView);
        webview.setWebViewClient(new WebClient());
        webview.setWebChromeClient(new ACWebchromeClient());
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);
        webview.loadUrl("file:///android_asset/pdtForm.html");
        webview.setVerticalScrollBarEnabled(false);
        webview.getSettings().setAppCachePath("/data/data/" + getPackageName() + "/cache");
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface");
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


    }

    public class ACWebchromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress >= 100) {
                LoaderView.setVisibility(View.GONE);
            } else {
                LoaderView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    class WebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("sticket_success.html")) {

            } else {
                view.loadUrl(url);
            }
            return true;
        }
    }

}
