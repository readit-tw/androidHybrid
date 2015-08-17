package com.thoughtworks.readit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.gson.Gson;
import com.thoughtworks.readit.activity.AddResourceActivity;
import com.thoughtworks.readit.domain.Resource;
import com.thoughtworks.readit.network.RestService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ImageButton addResourceButton;
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

        addResourceButton =  (ImageButton)findViewById(R.id.addResourceButton);

        addResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addResourceIntent = new Intent(MainActivity.this, AddResourceActivity.class);
                startActivity(addResourceIntent);
            }
        });

        webView = (WebView) findViewById(R.id.webContent);
        webView.loadUrl("file:///android_asset/www/listResource.html");

        webView.getSettings().setJavaScriptEnabled(true);

        loadResources();
    }

    private void loadResources() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://readit.thoughtworks.com")
                .build();

        RestService service = restAdapter.create(RestService.class);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        service.listResources(new Callback<List<Resource>>() {
            @Override
            public void success(List<Resource> resources, Response response) {

                // TODO - get rid of hard coded stuff
//                resources = new ArrayList<>();
//                Resource r = new Resource();
//                r.setLink("google.com");
//                r.setTitle("Search");
//                resources.add(r);

                Gson gson = new Gson();
                String json = gson.toJson(resources);

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