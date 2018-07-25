package com.example.administrator.kenya.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.ChildComment;
import com.example.administrator.kenya.classes.Comment;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnCommentChooseListener;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class CommentDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.commentViews)
    AutoLinearLayout commentViews;
    @Bind(R.id.ll1)
    AutoLinearLayout ll;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;


    //==============================评论变量====================================================================
    private List<ChildComment> childCommentList;
    private PopupWindow popupWindow;
    private TextView popupWindow_tv;
    private int cpageNum = 1;
    private String toUserName = "";
    private String toUserId = "";

    private Comment comment;
    private String id;
    private String thingsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Bundle bundle = getIntent().getExtras();
        comment = (Comment) bundle.getSerializable("comment");
        id = bundle.getString("id");
        thingsType = bundle.getString("thingsType");
        title.setText(comment.getPageSize() + getString(R.string.reply));

        initTop();

        //==========================================评论模块=================================================================
        initPopupWindow();
        initPullToRefreshLayout();
        loadComments();

    }

    private void initTop() {
        name.setText(comment.getIssuer().getUserName());
        text.setText(comment.getCommentText());
        date.setText(comment.getCreateTime());
        Glide.with(this)
                .load(AppConstants.BASE_URL + comment.getIssuer().getUserPortrait())
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
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
                .url(AppConstants.BASE_URL + "/kenya/comments/findUserCommentsByGoodsId")
                .addParams("thingsId", id)
                .addParams("thingsType", thingsType)
                .addParams("pn", String.valueOf(cpageNum))
                .addParams("commentId", comment.getId())
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
                            if (jsonObject.getString("code").equals("000") && pullToRefreshLayout !=null) {
                                pullToRefreshLayout.setCanLoadMore(true);
                            } else if (jsonObject.getString("code").equals("040")) {
                                //没有下一页
                                pullToRefreshLayout.setCanLoadMore(false);
                            }
                            cpageNum++;
                            childCommentList = JSON.parseArray(jsonObject.getString("result"), ChildComment.class);
                            refreshComment();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pullToRefreshLayout.finishLoadMore();
                    }
                });
    }

    private void send_comment(final TextView comment_text) {
        log("commentid = " + comment.getId() + "uid = " + User.getInstance().getUserId());
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        OkHttpUtils
                .post()
                .url(AppConstants.BASE_URL + "/kenya/comments/saveUserComments")
                .addParams("loginId", User.getInstance().getUserId())//loginId
                .addParams("childCommentText", comment_text.getText().toString())//childCommentText
                .addParams("id", comment.getId())//id
                .addParams("thingsId", id)//thingsId
                .addParams("thingsType", thingsType)//thingsType
                .addParams("toUserId", toUserId) //toUserId
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
                            if (jsonObject.getString("code").equals("000") && pullToRefreshLayout !=null) {
                                ChildComment childComment = JSON.parseObject(jsonObject.getString("result"), ChildComment.class);
                                if (childCommentList ==null || childCommentList.size() == 0) {
                                    childCommentList = new ArrayList<ChildComment>();
                                    childCommentList.add(childComment);
                                    commentViews.removeAllViews();
                                }else {
                                    childCommentList.add(0, childComment);
                                }
//                                addItemView(childComment, 0 , true);
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

    private void deleteComment(final String childCommentId, final int position ){
        new CommentDialog(this, new OnCommentChooseListener() {
            @Override
            public void delet(String message) {
                OkHttpUtils.post()
                        .url(AppConstants.BASE_URL + "/kenya/comments/deleteUserComments")
                        .addParams("thingsType" , thingsType)
                        .addParams("thingsId" , id)
                        .addParams("commentsId" , comment.getId())
                        .addParams("loginId" , User.getInstance().getUserId())
                        .addParams("userCommentsId" , childCommentId)
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
                                    if (jsonObject.getString("code").equals("000") && pullToRefreshLayout !=null) {
//                                        childCommentList.remove(position);
//                                        View view = commentViews.getChildAt(position);
//                                        commentViews.removeView(view);
                                        childCommentList.remove(position);
                                        refreshComment();
                                        toast(getString(R.string.delete_successfully) );
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
        popupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);

        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow_tv = popContentView.findViewById(R.id.comment_text);
        TextView comment_send = popContentView.findViewById(R.id.comment_send);

        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow_tv.getText().length() == 0) {
                    toast(getString(R.string.please_enter_real_name));
                } else {
                    send_comment(popupWindow_tv);
                }
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha(1f);
            }
        });
    }

    private void addNoComment() {
        View itemView = View.inflate(this, R.layout.none_comment, null);
        commentViews.addView(itemView);
    }

    private void addItemView(final ChildComment childComment, final int position , boolean top) {
        final View itemView = View.inflate(this, R.layout.item_child_comment, null);
        final TextView name = itemView.findViewById(R.id.name);
        final TextView text = itemView.findViewById(R.id.text);
        TextView date = itemView.findViewById(R.id.date);
        ImageView avatar = itemView.findViewById(R.id.avatar);
        final TextView unfold = itemView.findViewById(R.id.unfold);
        name.setText(childComment.getIssuer().getUserName());
        if (childComment.getToUsername()!= null && !childComment.getToUsername().equals("")) {
            text.setText( Html.fromHtml(getString(R.string.reply) + "<font color='#4f7dae'>@" + childComment.getToUsername() + "</font>:"+childComment.getChildCommentText() ));
        }else {
            text.setText( childComment.getChildCommentText());
        }
        date.setText(childComment.getCreateTime());
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
        Glide.with(this)
                .load(AppConstants.BASE_URL + childComment.getIssuer().getUserPortrait())
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
                if (User.getInstance().getUserId().equals(childComment.getIssuer().getUserId())){
                    deleteComment(childComment.getId() , position );
                }else {
                    if (!toUserName.equals(childComment.getIssuer().getUserName()))
                        popupWindow_tv.setText("");
                    toUserName = childComment.getIssuer().getUserName();
                    toUserId = childComment.getIssuer().getUserId();
                    popupWindow_tv.setHint(getString(R.string.reply) + "@" + toUserName);
                    popupWindow.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(500, InputMethodManager.HIDE_NOT_ALWAYS);
                    backgroundAlpaha(0.5f);
                }
            }
        });
        if (top) {
            commentViews.addView(itemView, 0);
        } else {
            commentViews.addView(itemView);
        }

    }

    private void refreshComment(){
        commentViews.removeAllViews();
        if (childCommentList != null && childCommentList.size() != 0) {
            for (int i = 0; i < childCommentList.size(); i++) {
                addItemView(childCommentList.get(i), i , false);
            }
        } else {
            addNoComment();
            pullToRefreshLayout.setCanLoadMore(false);
        }
    }


    @OnClick({R.id.bar_main_comment, R.id.bar_edit})
    public void onViewClicked(View view) {
        if (!toUserName.equals(comment.getIssuer().getUserName()))
            popupWindow_tv.setText("");
        toUserName = "";
        toUserId = "";
        popupWindow_tv.setHint(getString(R.string.reply)+":");
        popupWindow.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(500, InputMethodManager.HIDE_NOT_ALWAYS);
        backgroundAlpaha(0.5f);
    }

    //==========================================评论模块 END  ==========================================================

    @OnClick(R.id.back)
    public void onViewClicked2() {
        finish();
    }
}
