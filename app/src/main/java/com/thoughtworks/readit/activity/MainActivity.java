package com.thoughtworks.readit.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
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

        webView = (WebView) findViewById(R.id.webContent);
        webView.loadUrl("file:///android_asset/www/resourceList.html");
        webView.addJavascriptInterface(new JSObject(), "ListView");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                loadResources();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://readit.thoughtworks.com")
                    .build();

            RestService service = restAdapter.create(RestService.class);

            service.searchListResources(query, new Callback<List<Resource>>() {
                @Override
                public void success(List<Resource> resources, Response response) {
                    Gson gson = new Gson();
                    String json = gson.toJson(resources);

                    Log.d("", "Content Loaded" + json);
                    webView.loadUrl("javascript:onListLoad(" + json + ")");
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(MainActivity.this, "Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            init();
        }
    }

    protected void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.drawable.app_icon);
        setSupportActionBar(toolbar);

        addResourceButton = (FloatingActionButton) findViewById(R.id.addResourceButton);

        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addResourceIntent = new Intent(MainActivity.this, AddResourceActivity.class);
                startActivity(addResourceIntent);
            }
        });

    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadResources();
        }
    }

    private void openWebContent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void shareContent(String title, String url) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide what to do with it.
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(intent);
    }

    private void loadResources() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://readit.thoughtworks.com")
                .build();

        RestService service = restAdapter.create(RestService.class);

        service.listResources(new Callback<List<Resource>>() {
            @Override
            public void success(List<Resource> resources, Response response) {

                Gson gson = new Gson();
                String json = gson.toJson(resources);

                Log.d("", "Content Loaded" + json);

                webView.loadUrl("javascript:onListLoad(" + json + ")");
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MainActivity.this, "Please try later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    private class JSObject {
        @JavascriptInterface
        public void onItemClick(String link) {
            openWebContent(link);
        }

        @JavascriptInterface
        public void onShareClick(String title, String link) {
            shareContent(title, link);
        }

    }
}