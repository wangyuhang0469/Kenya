package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.kenya.R;

/**
 * Created by 123 on 2018/6/8.
 */

public class YesOrNoDialog extends Dialog {

    private Button positiveButton, negativeButton;
    private TextView message;
    private YesOrNoDialogCallback callback;

    //定义“确定/取消”的操作
    public enum ClickedButton {POSITIVE, NEGATIVE}

    public YesOrNoDialog(Context context) {
        super(context, R.style.FullScreenDialog);
        setCustomDialog();
       // getWindow().setLayout((int) (AppConstant.SCRRENWIDTH * 0.7), (int) (AppConstant.SCREENHEIGHT * 0.3));
    }

    public void setCallback(YesOrNoDialogCallback callback) {
        this.callback = callback;
    }

    //具体的设置Dialog
    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.yes_or_no_dialog, null);
        message = (TextView) mView.findViewById(R.id.title);
        positiveButton = (Button) mView.findViewById(R.id.acceptbtn);
        negativeButton = (Button) mView.findViewById(R.id.refusebtn);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickButton(ClickedButton.POSITIVE,"");
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickButton(ClickedButton.NEGATIVE,"");
            }
        });

        //ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setContentView(mView);
    }

    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {
    }


    public void setMeesage(String mes) {
        message.setText(mes);
    }

    //定义回调的接口
    public interface YesOrNoDialogCallback {
        public void onClickButton(ClickedButton button,String message);
    }

}
