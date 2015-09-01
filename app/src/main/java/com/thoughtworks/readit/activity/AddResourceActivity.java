package com.thoughtworks.readit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thoughtworks.readit.R;
import com.thoughtworks.readit.domain.Resource;
import com.thoughtworks.readit.network.RestService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddResourceActivity extends AppCompatActivity {

    private WebView webView;
    private android.support.v7.widget.Toolbar toolbar;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_resource);
        init();
        webView = (WebView) findViewById(R.id.addResourceWebView);
        webView.loadUrl("file:///android_asset/www/addResource.html");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AddResourceJSObject(), "AddResource");
    }

    private class AddResourceJSObject {
        @JavascriptInterface
        public void onSave(String message) {
            Gson resourceGson = new Gson();
            Resource resource = resourceGson.fromJson(message, Resource.class);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://readit.thoughtworks.com")
                    .build();

            RestService service = restAdapter.create(RestService.class);

            service.shareResource(resource, new Callback<Resource>() {
                @Override
                public void success(Resource resource, Response response) {
                    Toast.makeText(AddResourceActivity.this, "Successfully added!", Toast.LENGTH_LONG).show();

                    Intent addResourceIntent = new Intent(AddResourceActivity.this, MainActivity.class);
                    startActivity(addResourceIntent);
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(AddResourceActivity.this, "Please try later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void readIntentContent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String url = bundle.getString(Intent.EXTRA_TEXT);
            String title = bundle.getString(Intent.EXTRA_SUBJECT);
            renderContent(title, url);
        }
    }

    private void renderContent(String title, String url) {
        webView.loadUrl("javascript:addView.renderInput(\"" + title + "\",\"" + url + "\")");
    }

    private void init() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addContent);
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new ShareClickListener());
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            readIntentContent();
            renderShareButton();
        }
    }

    private void renderShareButton()
    {
        shareButton.setVisibility(View.VISIBLE);
    }

    private class ShareClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            webView.loadUrl("javascript:addView.viewModel.addResource()");
        }
    }

}