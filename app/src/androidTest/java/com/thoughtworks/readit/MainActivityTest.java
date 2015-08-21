package com.thoughtworks.readit;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.thoughtworks.readit.activity.MainActivity;

import junit.framework.Assert;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private ImageButton addResourceButton;
    private WebView webView;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent launchIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(launchIntent, null, null);
        MainActivity activity = getActivity();

        addResourceButton = (ImageButton) activity.findViewById(R.id.addResourceButton);
        webView = (WebView) activity.findViewById(R.id.webContent);
    }

    public void testonStartLayoutShouldHaveButton() {
        Assert.assertNotNull(addResourceButton);
        Assert.assertNotNull(webView);
    }

    public void testButtonOnClickShouldStartAddResourceActivity() {
        addResourceButton.performClick();
        final Intent passedIntent = getStartedActivityIntent();
        assertNotNull("Intent is not null",passedIntent);
    }
}
