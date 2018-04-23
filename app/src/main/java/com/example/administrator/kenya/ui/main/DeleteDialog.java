package com.example.administrator.kenya.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.interfaces.DeleteSuccessfulListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


public class DeleteDialog extends Dialog{

    private TextView information;
    private TextView yes;
    private TextView no;
    private String url;
    private Map<String,String> params;
    private DeleteSuccessfulListener deleteSuccessfulListener;

    public DeleteDialog(@NonNull Context context,String url, Map<String,String> params) {
        super(context, R.style.FullScreenDialog);
        this.url = url;
        this.params = params;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dialog);

        information =(TextView) findViewById(R.id.information);
        yes =(TextView) findViewById(R.id.yes);
        no =(TextView) findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialog.this.dismiss();
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


    private void delete(){
        information.setText("删除中...");

        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                        DeleteDialog.this.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("000")){
                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                if (deleteSuccessfulListener != null)
                                    deleteSuccessfulListener.success();
                            }else {
                                Toast.makeText(getContext(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                            DeleteDialog.this.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void setDeleteSuccessfulListener(DeleteSuccessfulListener deleteSuccessfulListener){
        this.deleteSuccessfulListener = deleteSuccessfulListener;
    }

}
