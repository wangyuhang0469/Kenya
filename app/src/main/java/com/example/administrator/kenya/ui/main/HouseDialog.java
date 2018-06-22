package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.ui.city.buyhouse.BuyHouseInfoActivity;
import com.example.administrator.kenya.ui.city.house.HouseInfoActivity;

/**
 * Created by 123 on 2018/6/8.
 */

public class HouseDialog extends Dialog {

    TextView house_dialog_house;
    TextView house_dialog_office_building;
    TextView house_dialog_workshop;

    public HouseDialog(@NonNull Context context) {
        super(context, R.style.FullScreenDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_dialog);
        house_dialog_house = findViewById(R.id.house_dialog_house);
        house_dialog_office_building = findViewById(R.id.house_dialog_office_building);
        house_dialog_workshop = findViewById(R.id.house_dialog_workshop);
        house_dialog_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HouseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("housetype", "住宅");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                HouseDialog.this.dismiss();
            }
        });
        house_dialog_office_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HouseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("housetype", "写字楼");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                HouseDialog.this.dismiss();
            }
        });
        house_dialog_workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HouseInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("housetype", "厂房");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
                HouseDialog.this.dismiss();
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
