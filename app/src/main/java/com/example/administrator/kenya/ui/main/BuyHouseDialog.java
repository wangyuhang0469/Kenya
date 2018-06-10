package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.ui.city.buyhouse.BuyHouseInfoActivity;

import butterknife.Bind;

/**
 * Created by 123 on 2018/6/8.
 */

public class BuyHouseDialog extends Dialog {

    TextView buy_house_dialog_newhouse;
    TextView buy_house_dialog_oldhouse;

    public BuyHouseDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_house_dialog);
        buy_house_dialog_newhouse = findViewById(R.id.buy_house_dialog_newhouse);
        buy_house_dialog_oldhouse = findViewById(R.id.buy_house_dialog_oldhouse);
        buy_house_dialog_newhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BuyHouseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("housetype", "新房");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                BuyHouseDialog.this.dismiss();

            }
        });
        buy_house_dialog_oldhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BuyHouseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("housetype", "二手房");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                BuyHouseDialog.this.dismiss();
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
