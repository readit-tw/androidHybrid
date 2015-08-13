package com.readithybrid.thoughtworks;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    protected void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webContent);
        webView.loadUrl("file:///android_asset/www/list.html");
        webView.setWebViewClient(new DefaultWebViewClient());
        webView.setWebChromeClient(new BridgeWCClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class BridgeWCClient extends WebChromeClient {

        @Override
        public boolean onJsPrompt(WebView view, String url, String title, String message, JsPromptResult result) {
            Log.d("", message);
            if (title.equals("BRIDGE_KEY")) {

                return true;
            }
            return super.onJsPrompt(view, url, title, message, result);
        }

    }

    private class DefaultWebViewClient extends WebViewClient
    {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("", "--onPageFinished--");
            listLoad();
        }
    }

    private void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private boolean validate(String userName, String password) {
        return true;
    }

    private void listLoad() {
        webView.loadUrl("javascript:onListLoad([{title:'Docker'},{title:'Internet of things'},{title:'My life'},{title:'5 points'}," +
                "{title:'Docker'},{title:'Internet of things'},{title:'My life'},{title:'5 points'}," +
                "{title:'Docker'},{title:'Internet of things'},{title:'My life'},{title:'5 points'}])");
    }

}