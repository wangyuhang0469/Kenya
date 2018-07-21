package com.example.administrator.kenya.ui.city.used;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Comment;
import com.example.administrator.kenya.classes.Goods;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.tools.GlideImageLoader;
import com.example.administrator.kenya.ui.main.CallPhoneDialog;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.PreviewDialog;
import com.example.administrator.kenya.view.MyFootRefreshView;
import com.example.administrator.kenya.view.MyHeadRefreshView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

public class GoodsDetailsActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.goodsprice)
    TextView goodsprice;
    @Bind(R.id.goodsdesc)
    TextView goodsdesc;
    @Bind(R.id.goodsusername)
    TextView goodsusername;
    @Bind(R.id.goodsphone)
    TextView goodsphone;
    @Bind(R.id.goodsname)
    TextView goodsname;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.goods_detail_address)
    TextView goodsDetailAddress;
    @Bind(R.id.commentViews)
    AutoLinearLayout commentViews;
    @Bind(R.id.ll)
    AutoLinearLayout ll;
    @Bind(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    private Goods goods;

    private List<Comment> commentListc;
    private PopupWindow popupWindow;
    private int cpageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        title.setText(getResources().getString(R.string.goods_detail));

        initPopupWindow();


        goods = (Goods) getIntent().getExtras().getSerializable("goods");
        initBanner(goods.getImageUrlList());
        goodsname.setText(goods.getGoodsname());
        goodsprice.setText("KSh " + goods.getGoodsprice());
        goodsdesc.setText(goods.getGoodsdesc());
        goodsusername.setText(goods.getGoodsusername());
        goodsphone.setText(getResources().getString(R.string.phone_no_) + goods.getGoodsphone());
        goodsDetailAddress.setText(goods.getGoodsaddress());

        Glide.with(this).load(AppConstants.BASE_URL + goods.getUser().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);

        loadComments();


        log(goods.getGoodsid());
    }

    private void init(){
        pullToRefreshLayout.setCanRefresh(false);
        pullToRefreshLayout.setCanLoadMore(false);
//        pullToRefreshLayout.setHeaderView(new MyHeadRefreshView(this));
//        pullToRefreshLayout.setFooterView(new MyFootRefreshView(this));
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
        toast("aaa");
        OkHttpUtils.post()
                .url(AppConstants.BASE_URL + "/kenya/comments/findCommentsByGoodsId")
                .addParams("thingsId", goods.getGoodsid())
                .addParams("thingsType", "goods")
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
                            if (jsonObject.getString("code").equals("000")) {
                                pullToRefreshLayout.setCanLoadMore(true);
                            } else if (jsonObject.getString("code").equals("040")) {
                                //没有下一页
                                pullToRefreshLayout.setCanLoadMore(false);
                            }
                            cpageNum ++ ;
                            commentListc = JSON.parseArray(jsonObject.getString("result"), Comment.class);
                            if (commentListc != null && commentListc.size() != 0) {
                                initItemView();
                            } else {
                                addNoComment();
                            }
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
                .addParams("thingsId", goods.getGoodsid())
                .addParams("thingsType", "goods")
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
                            if (jsonObject.getString("code").equals("000")) {
                                toast("评论成功");
                                comment_text.setText("");
                                popupWindow.dismiss();
                                loadingDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initPopupWindow() {
        final View popContentView = LayoutInflater.from(this).inflate(R.layout.comment_edit, null);
        popupWindow = new PopupWindow(popContentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        final TextView comment_text = popContentView.findViewById(R.id.comment_text);
        TextView comment_send = popContentView.findViewById(R.id.comment_send);

        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment_text.getText().length() == 0) {
                    toast("请输入");
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

    private void initItemView() {
        for (int i = 0; i < commentListc.size(); i++) {
            final Comment comment = commentListc.get(i);
            View itemView = View.inflate(this, R.layout.item_comment, null);
            TextView name = itemView.findViewById(R.id.name);
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
                reply.setText("共" + comment.getPageSize() + "条回复");
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
                    toast("ahhhhh");
                }
            });
            commentViews.addView(itemView);
        }
    }

    private void initBanner(List<String> imageUrlList) {
        banner.setImages(imageUrlList).setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                new PreviewDialog(GoodsDetailsActivity.this, goods.getImageUrlList(), position).show();
            }
        });
    }

    @OnClick({R.id.back, R.id.image_comment, R.id.call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.image_comment:
                popupWindow.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
                backgroundAlpaha(0.5f);
                break;
            case R.id.call:
                new CallPhoneDialog(this, goods.getGoodsphone()).show();
                break;
        }
    }
}
