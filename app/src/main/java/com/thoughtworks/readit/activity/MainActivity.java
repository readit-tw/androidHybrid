package com.thoughtworks.readit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thoughtworks.readit.R;
import com.thoughtworks.readit.domain.Resource;
import com.thoughtworks.readit.network.RestService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addResourceButton;
    private WebView webView;
    private ProgressBar progressBar;
    private android.support.v7.widget.Toolbar toolbar;
    private final String LIST_ITEM_CLICK = "LIST_ITEM_CLICK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    protected void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        addResourceButton = (FloatingActionButton) findViewById(R.id.addResourceButton);

        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addResourceIntent = new Intent(MainActivity.this, AddResourceActivity.class);
                startActivity(addResourceIntent);
            }
        });

        webView = (WebView) findViewById(R.id.webContent);
        webView.loadUrl("file:///android_asset/www/resourceList.html");
        webView.setWebChromeClient(new BridgeWCClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadResources();
        }
    }

    private class BridgeWCClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (message.equals(LIST_ITEM_CLICK)) {
                Log.d("", defaultValue);
                String _url = defaultValue;
                openWebContent(_url);
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    private void openWebContent(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void loadResources() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://readit.thoughtworks.com")
                .build();

        RestService service = restAdapter.create(RestService.class);

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        service.listResources(new Callback<List<Resource>>() {
            @Override
            public void success(List<Resource> resources, Response response) {

                // TODO - get rid of hard coded stuff
                Gson gson = new Gson();
                String json = gson.toJson(resources);

                Log.d("", "Content Loaded" + json);

                webView.loadUrl("javascript:onListLoad(" + json + ")");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "Please try later!", Toast.LENGTH_LONG);
            }
        });
    }

    private void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

}