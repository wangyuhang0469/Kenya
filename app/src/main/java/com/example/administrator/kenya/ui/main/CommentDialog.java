package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.interfaces.OnCommentChooseListener;
import com.example.administrator.kenya.interfaces.OnSexSelectListener;


public class CommentDialog extends Dialog{


    private TextView delete;
    private OnCommentChooseListener onCommentChooseListener;

    public CommentDialog(@NonNull Context context, OnCommentChooseListener onCommentChooseListener) {
        super(context, R.style.FullScreenDialog);
        this.onCommentChooseListener = onCommentChooseListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog);

        delete = (TextView) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommentChooseListener.delet("ture");
                CommentDialog.this.dismiss();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
