package net.ddns.andrewnetwork.ludothornsoundbox.ui.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.CommonUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.PhoneUtils;

import java.util.ArrayList;

public class WebActivity extends BaseActivity {
    public static final String KEY_WEB_LINK = "KEY_WEB_LINK";
    public static final int REQUEST_WEB = 3033;
    private WebView mWebView;
    private ArrayList<String> searchHistory = new ArrayList<>();

    public static void newInstance(Activity activity, String link) {

        Intent webPatreonIntent = new Intent(activity, WebActivity.class);
        webPatreonIntent.putExtra(KEY_WEB_LINK, link);
        activity.startActivityForResult(webPatreonIntent, WebActivity.REQUEST_WEB);
        activity.overridePendingTransition( R.anim.slide_out_up, R.anim.do_nothing );
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        if(!PhoneUtils.isPhoneConnected(this)) {
            CommonUtils.showDialog(this, "Non sembra tu sia connesso ad internet." +
                    "", (dialog, which) -> finish(), false);
            return;
        }

        mWebView = new WebView(this);

        mWebView.loadUrl(getUrl());

        searchHistory.add(getUrl());

        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setGeolocationEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                searchHistory.add(url);

                return true;
            }
        });
        setContentView(mWebView);
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
        if (intent != null && intent.getExtras() != null && intent.getExtras().getString(KEY_WEB_LINK) != null) {
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

    @Override
    public void onBackPressed() {

        if(canGoBack()) {
            searchHistory.remove(searchHistory.size()-1);

            String url = searchHistory.get(searchHistory.size()-1);


            mWebView.loadUrl(url);
        } else {
            finish();
            overridePendingTransition(R.anim.do_nothing, R.anim.slide_right);
        }
    }

    private boolean canGoBack() {
        return searchHistory.size() > 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_web_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                return true;
            case R.id.action_open_browser:
                CommonUtils.showDialog(this, "Apri nel tuo browser di sistema?", (dialog, which) -> {
                    Intent webExternalIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(searchHistory.get(searchHistory.size()-1)));
                    startActivity(webExternalIntent);
                    dialog.dismiss();
                }, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.do_nothing, R.anim.slide_in_up);
    }
}