package com.ujuzy.ujuzy.activities;

import android.app.ProgressDialog;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ujuzy.ujuzy.R;

public class Webview2Activity extends AppCompatActivity {

    String webview_url = "";
    String page_title = "";

    private WebView myWebView;
    private ImageView backBtn;
    private ProgressDialog progressDialog;
    private TextView toolbaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service_web2);

        initWindows();
        initGetExtra();
        intWebView();
        initBactBtn();
        initToolbarTv();

    }


    @Override
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
      /*  Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }*/
    }

    private void initToolbarTv()
    {
        toolbaTv = (TextView) findViewById(R.id.toolbarTv);
        toolbaTv.setText("Create Service");
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
        Window window = Webview2Activity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(ContextCompat.getColor( Webview2Activity.this,R.color.colorPrimaryDark));

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

    }


    private void intWebView()
    {

       /* myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(webview_url);
        myWebView.setWebViewClient(new WebViewClient());*/

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressDialog = new ProgressDialog(Webview2Activity.this);
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

                Toast.makeText(Webview2Activity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                //finish();

            }

        });

        myWebView.loadUrl(webview_url);
    }

    private void closePage() {
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
