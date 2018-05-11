package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.interfaces.OnPersonalIntroductionsListener;

/**
 * Created by 123 on 2018/5/10.
 */

public class PersonalIntroductionsDialog extends Dialog {

    private TextView information;
    private TextView yes;
    private TextView no;
    private OnPersonalIntroductionsListener onPersonalIntroductionsListener;

    public PersonalIntroductionsDialog(Context context) {
        super(context, R.style.FullScreenDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_introductions_dialog);

        information = (TextView) findViewById(R.id.information);
        yes = (TextView) findViewById(R.id.yes);
        no = (TextView) findViewById(R.id.no);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalIntroductionsDialog.this.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPersonalIntroductionsListener != null)
                    onPersonalIntroductionsListener.success(information.getText().toString());
                PersonalIntroductionsDialog.this.dismiss();
            }
        });
        Window window = getWindow();
        window.setWindowAnimations(R.style.take_photo_anim);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        if (wlp.gravity == Gravity.BOTTOM)
            wlp.y = 0;
        window.setAttributes(wlp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setOnPersonalIntroductionsListener(OnPersonalIntroductionsListener onPersonalIntroductionsListener) {
        this.onPersonalIntroductionsListener = onPersonalIntroductionsListener;
    }
}
