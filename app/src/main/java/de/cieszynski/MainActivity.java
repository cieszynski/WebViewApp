package de.cieszynski;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewCompat;

public class MainActivity extends AppCompatActivity {

    private WebViewAssetLoader mAssetLoader;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //getWindow().setNavigationBarColor(Color.TRANSPARENT);*/

        mWebView = new WebView(this);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setBackgroundColor(Color.parseColor("#FFFBE6"));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                return mAssetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        mAssetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/", new WebViewAssetLoader.AssetsPathHandler(this))
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this))
                .build();

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            Uri path = new Uri.Builder()
                    .scheme("https")
                    .authority(WebViewAssetLoader.DEFAULT_DOMAIN)
                    .appendPath("basil.html")
                    .build();
            mWebView.loadUrl(path.toString());
        }

        PackageInfo webViewPackageInfo = WebViewCompat.getCurrentWebViewPackage(this);
        Log.d("XXX", "WebView version: " + webViewPackageInfo.versionName.startsWith("82"));
        setContentView(mWebView);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

/*    @Override
    protected void onResume() {
        super.onResume();

        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(label, icon, colorPrimary);
        ((Activity)this).setTaskDescription(taskDescription);
    }*/
}
