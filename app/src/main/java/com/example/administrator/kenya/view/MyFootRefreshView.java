package com.example.administrator.kenya.view;

/**
 * Created by Administrator on 2018/5/17.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.jwenfeng.library.pulltorefresh.view.FooterView;

/**
 * 当前类注释:
 * 作者：jinwenfeng on 2016/12/6 13:49
 * 邮箱：823546371@qq.com
 * QQ： 823546371
 * 公司：南京穆尊信息科技有限公司
 * © 2016 jinwenfeng
 * © 版权所有，未经允许不得传播
 */
public class MyFootRefreshView extends FrameLayout implements FooterView {

    private TextView tv;
    private ImageView arrow;
    private ProgressBar progressBar;

    public MyFootRefreshView(Context context) {
        this(context,null);
    }

    public MyFootRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFootRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_header,null);
        addView(view);
        tv = (TextView) view.findViewById(R.id.header_tv);
        arrow = (ImageView) view.findViewById(R.id.header_arrow);
        progressBar = (ProgressBar) view.findViewById(R.id.header_progress);
    }

    @Override
    public void begin() {

    }

    @Override
    public void progress(float progress, float all) {
        float s = progress / all;
        if (s >= 0.9f){
            arrow.setRotation(0);
        }else{
            arrow.setRotation(180);
        }
        if (progress >= all-10){
            tv.setText(getContext().getString(R.string.load_more));
        }else{
            tv.setText(getContext().getString(R.string.load_more));
        }
    }

    @Override
    public void finishing(float progress, float all) {

    }

    @Override
    public void loading() {
        arrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tv.setText(getContext().getString(R.string.loading));
    }

    @Override
    public void normal() {
        arrow.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        tv.setText(getContext().getString(R.string.load_more));
    }

    @Override
    public View getView() {
        return this;
    }
}