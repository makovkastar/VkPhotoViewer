package com.melnykov.vkphotoviewer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.melnykov.vkphotoviewer.R;
import com.melnykov.vkphotoviewer.util.Auth;
import com.melnykov.vkphotoviewer.util.Constants;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String authUrl = Auth.getAuthorizationUrl(Constants.API_ID);
        final WebView wvLogin = (WebView) findViewById(R.id.wvLogin);
        wvLogin.setWebViewClient(new VkWebViewClient());
        wvLogin.loadUrl(authUrl);
    }

    
    private class VkWebViewClient extends WebViewClient {
    	 public boolean shouldOverrideUrlLoading(WebView view, String url) {
             boolean result = true;
             if (url != null && url.startsWith(Constants.CALLBACK_URL)) {
                 if (Constants.DEBUG) Log.v(TAG, url);
                 if (url.contains("error")) {
                     setResult(RESULT_CANCELED);
                     finish();
                 } else {
                 	// Get token and verifier
                     String accessToken = Auth.getAccessToken(url);
                     String userId = Auth.getUserId(url);

                     Intent intent = getIntent();
                     intent.putExtra(Constants.IEXTRA_ACCESS_TOKEN, accessToken);
                     intent.putExtra(Constants.IEXTRA_USER_ID, userId);

                     setResult(RESULT_OK, intent);
                     finish();
                 }
             } else {
                 result = super.shouldOverrideUrlLoading(view, url);
             }
             return result;
         }
    }
}
