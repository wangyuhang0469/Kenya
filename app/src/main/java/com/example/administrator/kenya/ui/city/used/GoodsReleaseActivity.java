package com.example.administrator.kenya.ui.city.used;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsReleaseActivity extends BaseActivity {

    @Bind(R.id.title)
    TextView title;

    private static final int REQUEST_CODE = 123;
    @Bind(R.id.brief)
    EditText brief;
    private ArrayList<String> mResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_release);
        ButterKnife.bind(this);

        title.setText("发布");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;

                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for (String result : mResults) {
                    sb.append(result).append("\n");
                }
                brief.setText(sb.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.back, R.id.release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.release:
                // start multiple photos selector
                Intent intent = new Intent(GoodsReleaseActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
// min size of image which will be shown; to filter tiny images (mainly icons)
                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }
}
