package com.thoughtworks.readit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.thoughtworks.readit.MainActivity;
import com.thoughtworks.readit.R;
import com.thoughtworks.readit.domain.Resource;
import com.thoughtworks.readit.network.RestService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

public class AddResourceActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_resource);

        webView = (WebView) findViewById(R.id.addResourceWebView);
        webView.loadUrl("file:///android_asset/www/resource.html");
        webView.setWebChromeClient(new BridgeWCClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private class BridgeWCClient extends WebChromeClient {

        @Override
        public boolean onJsPrompt(WebView view, String url, String title, String message, JsPromptResult result) {
            Log.d("", message);

            Gson resourceGson = new Gson();
            Resource resource = resourceGson.fromJson(message, Resource.class);

            if (title.equals("ADD_RESOURCE")) {

                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint("http://readit.thoughtworks.com")
                        .build();

                RestService service = restAdapter.create(RestService.class);

                service.shareResource(resource, new Callback<Resource>() {
                    @Override
                    public void success(Resource resource, Response response) {
                        // TODO -  Show success message - test ?
                        Toast.makeText(AddResourceActivity.this, "Successfully added!", Toast.LENGTH_LONG);

                        Intent addResourceIntent = new Intent(AddResourceActivity.this, MainActivity.class);
                        startActivity(addResourceIntent);
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                return true;
            }
            return super.onJsPrompt(view, url, title, message, result);
        }

    }
}
