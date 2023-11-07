package edu.uiuc.cs427app;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapWebActivity extends BaseActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_webview);

        String cityName = getIntent().getStringExtra("city_name");
        String lat = getIntent().getStringExtra("lat");
        String log = getIntent().getStringExtra("log");

        webView = findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");

        //source of the html files
        //https://developers.google.com/maps/documentation/javascript/examples/advanced-markers-html-simple

        webView.loadUrl("file:///android_asset/index.html?m=m&lat=" + lat + "&log=" + log + "&city=" + cityName);
    }
}
