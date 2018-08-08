package com.ujuzy.ujuzy.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.ujuzy.ujuzy.R;

public class WebViewActivity extends AppCompatActivity {

    String webview_url = "";
    String page_title = "";

    private WebView myWebView;
    private ImageView backBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service_web);

        initWindows();
        initGetExtra();
        intWebView();
        initBactBtn();

    }

    private void initGetExtra()
    {
        webview_url = getIntent().getStringExtra("webview_url");
        page_title = getIntent().getStringExtra("page_title");
    }

    private void initBactBtn()
    {
        backBtn = (ImageView) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWindows()
    {
        Window window = WebViewActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( WebViewActivity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }


    private void intWebView()
    {

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(webview_url);
        myWebView.setWebViewClient(new WebViewClient());

        /*myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Toast.makeText(WebViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                //finish();

            }
        });
        myWebView.loadUrl(webview_url);*/
    }

    /*@Override
    public void onBackPressed()
    {
        if (myWebView.canGoBack())
        {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }

    }*/
}
