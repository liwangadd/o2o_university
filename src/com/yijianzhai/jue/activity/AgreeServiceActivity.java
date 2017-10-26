package com.yijianzhai.jue.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yijianzhai.jue.R;


public class AgreeServiceActivity extends Activity {
	    private static final String LOG_TAG = "WebViewDemo";
	    private String URL;

	    private WebView mWebView;

	    private Handler mHandler = new Handler();

	    @Override
	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.agreeservice);
	        mWebView = (WebView) findViewById(R.id.wv);

	        WebSettings webSettings = mWebView.getSettings();
	        webSettings.setSavePassword(false);
	        webSettings.setSaveFormData(false);
	        webSettings.setJavaScriptEnabled(true);
	        webSettings.setSupportZoom(false);

	        /*mWebView.setWebChromeClient(new MyWebChromeClient());*/
	        Init();
	        mWebView.loadUrl(URL);
	    }
	    
	    public void Init(){
	    	Intent intent = getIntent();
	    	URL = intent.getStringExtra("url");
	    }
	    
}
