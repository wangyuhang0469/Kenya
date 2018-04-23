package com.example.administrator.kenya.ui.myself;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.ui.myself.myrelease.MyReleaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends BaseFragment {


    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;

    public MyselfFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        ButterKnife.bind(this, view);
        title.setText("个人中心");
        back.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.myName, R.id.myAddress, R.id.myRelease, R.id.myCar, R.id.myPoint, R.id.myVallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myName:
                break;
            case R.id.myAddress:
                break;
            case R.id.myRelease:
                startActivity(MyReleaseActivity.class,null);
                break;
            case R.id.myCar:
                break;
            case R.id.myPoint:
                break;
            case R.id.myVallet:
                break;
        }
    }
}
