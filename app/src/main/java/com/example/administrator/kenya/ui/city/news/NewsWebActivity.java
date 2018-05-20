package com.example.administrator.kenya.ui.city.news;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.constants.AppConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsWebActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.news_web)
    WebView newsWeb;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.news_details));
        initdata();
    }

    public void initdata() {
        String content = getIntent().getExtras().getString("content");
        if (content != null && !content.equals("")) {
            loadView(content);
            title.setText("Details");
//            newsWeb.loadUrl( content);
        } else {
            news = (News) getIntent().getExtras().getSerializable("news");
            loadView(AppConstants.BASE_URL + news.getNewstext());
//            newsWeb.loadUrl(AppConstants.BASE_URL + news.getNewstext());
        }

        newsWeb.getSettings().setDefaultTextEncodingName("GB2312");
        WebSettings settings = newsWeb.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
    }

    private void loadView(String urlStr) {
        WebSettings settings = newsWeb.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        newsWeb.loadUrl(urlStr);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
