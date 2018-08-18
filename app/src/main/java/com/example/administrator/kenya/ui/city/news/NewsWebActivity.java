package com.example.administrator.kenya.ui.city.news;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Comment;
import com.example.administrator.kenya.classes.News;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnCommentChooseListener;
import com.example.administrator.kenya.ui.main.CommentDetailsActivity;
import com.example.administrator.kenya.ui.main.CommentDialog;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class NewsWebActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.news_web)
    WebView newsWeb;
    @Bind(R.id.bar_comment)
    AutoRelativeLayout barComment;
    private News news;

    //==============================评论变量====================================================================
    private List<Comment> commentListc;
    private PopupWindow popupWindow;
    private int cpageNum = 1;
    @Bind(R.id.commentViews)
    AutoLinearLayout commentViews;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @Bind(R.id.ll)
    AutoLinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.news_details));
        initdata();


    }

    private void initPullToRefreshLayout() {
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);
        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(this));
        pullToRefreshLayout.setFooterView(new MyFootRefreshView(this));
        pullToRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
            }

            @Override
            public void loadMore() {
                loadComments();
            }
        });


    }

    private void loadComments() {
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/comments/findCommentsByGoodsId")
                .addParams("thingsId", String.valueOf(news.getNewsid()))
                .addParams("thingsType", "news")
                .addParams("pn", String.valueOf(cpageNum))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        pullToRefreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000") && pullToRefreshLayout != null) {
                                pullToRefreshLayout.setCanLoadMore(true);
                            } else if (jsonObject.getString("code").equals("040")) {
                                //没有下一页
                                pullToRefreshLayout.setCanLoadMore(false);
                            }
                            cpageNum++;
                            commentListc = JSON.parseArray(jsonObject.getString("result"), Comment.class);
                            refreshComment();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pullToRefreshLayout.finishLoadMore();
                    }
                });
    }

    private void send_comment(final TextView comment_text) {
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        OkHttpUtils
                .post()
                .url(AppConstants.BASE_URL + "/kenya/comments/saveComments")
                .addParams("userId", User.getInstance().getUserId())
                .addParams("commentText", comment_text.getText().toString())
                .addParams("thingsId", String.valueOf(news.getNewsid()))
                .addParams("thingsType", "news")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        log(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000") && pullToRefreshLayout != null) {
                                Comment comment = JSON.parseObject(jsonObject.getString("result"), Comment.class);
                                if (commentListc == null || commentListc.size() == 0) {
                                    commentListc = new ArrayList<Comment>();
                                    commentListc.add(comment);
                                    commentViews.removeAllViews();
                                } else {
                                    commentListc.add(0, comment);
                                }
//                                addItemView(comment,0 , true);
                                refreshComment();
                                toast(getString(R.string.post_success));
                                comment_text.setText("");
                                popupWindow.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }


    private void deleteComment(final String commentId, final int position) {
        new CommentDialog(this, new OnCommentChooseListener() {
            @Override
            public void delet(String message) {
                OkHttpUtils.post()
                        .url(AppConstants.BASE_URL + "/kenya/comments/deleteComments")
                        .addParams("thingsType", "news")
                        .addParams("thingsId", String.valueOf(news.getNewsid()))
                        .addParams("commentsId", commentId)
                        .addParams("loginId", User.getInstance().getUserId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                e.printStackTrace();
                                toast(getString(R.string.delete_failed));
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                log(response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("code").equals("000") && pullToRefreshLayout != null) {
                                        commentListc.remove(position);
                                        refreshComment();
                                        toast(getString(R.string.delete_successfully));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }).show();
    }


    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.comment_edit, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        final TextView comment_text = popContentView.findViewById(R.id.comment_text);
        TextView comment_send = popContentView.findViewById(R.id.comment_send);

        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_text.getText().length() == 0) {
                    toast(getString(R.string.please_enter));
                } else {
                    send_comment(comment_text);
                }
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftInput(comment_text.getWindowToken());
                backgroundAlpaha(1f);
            }
        });
    }

    private void addNoComment() {
        View itemView = View.inflate(this, R.layout.none_comment, null);
        commentViews.addView(itemView);
    }

    private void addItemView(final Comment comment, final int position, boolean top) {
        View itemView = View.inflate(this, R.layout.item_comment, null);
        final TextView name = itemView.findViewById(R.id.name);
        final TextView text = itemView.findViewById(R.id.text);
        TextView date = itemView.findViewById(R.id.date);
        TextView reply = itemView.findViewById(R.id.reply);
        ImageView avatar = itemView.findViewById(R.id.avatar);
        final TextView unfold = itemView.findViewById(R.id.unfold);
        name.setText(comment.getIssuer().getUserName());
        text.setText(comment.getCommentText());
        date.setText(comment.getCreateTime());
        text.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这个回调会调用多次，获取完行数记得注销监听
                text.getViewTreeObserver().removeOnPreDrawListener(this);

                if (text.getLineCount() >= 3) {
                    unfold.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        if (!comment.getPageSize().equals("0"))
            reply.setText(comment.getPageSize() + getString(R.string.reply));
        Glide.with(this)
                .load(AppConstants.BASE_URL + comment.getIssuer().getUserPortrait())
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
        unfold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setMaxLines(16);
                unfold.setVisibility(View.GONE);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", comment);
                bundle.putString("id", String.valueOf(news.getNewsid()));
                bundle.putString("thingsType", "news");
                startActivity(CommentDetailsActivity.class, bundle);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (User.getInstance().getUserId().equals(comment.getIssuer().getUserId())) {
                    deleteComment(commentListc.get(position).getId(), position);
                }
                return false;
            }
        });

        if (top) {
            commentViews.addView(itemView, 0);
        } else {
            commentViews.addView(itemView);
        }

    }

    private void refreshComment() {
        commentViews.removeAllViews();
        if (commentListc != null && commentListc.size() != 0) {
            for (int i = 0; i < commentListc.size(); i++) {
                addItemView(commentListc.get(i), i, false);
            }
        } else {
            addNoComment();
            pullToRefreshLayout.setCanLoadMore(false);
        }
    }

    //==========================================评论模块 END  ==========================================================
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

            //==========================================评论模块=================================================================
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            initPopupWindow();
            initPullToRefreshLayout();
            loadComments();
            barComment.setVisibility(View.VISIBLE);
            commentViews.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.image_comment)
    public void onViewClicked2() {
        popupWindow.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(500, InputMethodManager.HIDE_NOT_ALWAYS);
        backgroundAlpaha(0.5f);
    }
}
