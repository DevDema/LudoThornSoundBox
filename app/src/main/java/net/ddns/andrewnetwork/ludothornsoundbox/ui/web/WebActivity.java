package net.ddns.andrewnetwork.ludothornsoundbox.ui.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;

import java.util.Objects;

public class WebActivity extends BaseActivity {
    public static final String KEY_WEB_LINK = "KEY_WEB_LINK";
    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getUrl());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        this.setContentView(mWebView);
    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected int getFragmentContainerView() {
        return R.id.content_web;
    }

    private String getUrl() {
        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null && intent.getExtras().getString(KEY_WEB_LINK) != null) {
            return intent.getExtras().getString(KEY_WEB_LINK);
        }

        throw new IllegalArgumentException("No Link found for WebActivity");
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}