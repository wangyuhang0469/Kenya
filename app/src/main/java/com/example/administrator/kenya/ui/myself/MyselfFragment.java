package com.example.administrator.kenya.ui.myself;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.kenya.R;
import com.example.administrator.kenya.base.BaseFragment;
import com.example.administrator.kenya.classes.MessageEvent;
import com.example.administrator.kenya.classes.MessageEvent2;
import com.example.administrator.kenya.classes.User;
import com.example.administrator.kenya.constants.AppConstants;
import com.example.administrator.kenya.interfaces.OnReUsernameSuccessfulListener;
import com.example.administrator.kenya.model.image_selector.MultiImageSelectorActivity;
import com.example.administrator.kenya.ui.main.LoadingDialog;
import com.example.administrator.kenya.ui.main.ReUsernameDialog;
import com.example.administrator.kenya.ui.myself.aboutus.AboutUsActivity;
import com.example.administrator.kenya.ui.myself.myrelease.MyInformationActivity;
import com.example.administrator.kenya.ui.myself.myrelease.MyReleaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyselfFragment extends BaseFragment {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.my_center_name)
    TextView myCenterName;
    @Bind(R.id.avatar)
    CircleImageView avatar;
    @Bind(R.id.phone)
    TextView phone;

    private User user = User.getInstance();

    private ArrayList<String> mResults;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        title.setText(getResources().getString(R.string.personal));
        back.setVisibility(View.GONE);
        myCenterName.setText(user.getUserName());
        Glide.with(this).load(User.getInstance().getUserPortrait())
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(avatar);
        phone.setText(getResources().getString(R.string.phone_no_) + user.getUserPhonenumber());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.myAddress, R.id.myRelease, R.id.myCar, R.id.myPoint, R.id.myVallet, R.id.about_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myAddress:
                toast(getString(R.string.under_development));
                break;
            case R.id.myRelease:
                startActivity(MyReleaseActivity.class, null);
                break;
            case R.id.myCar:
                toast(getString(R.string.under_development));
                break;
            case R.id.myPoint:
                toast(getString(R.string.under_development));
                break;
            case R.id.myVallet:
                toast(getString(R.string.under_development));
                break;
            case R.id.about_us:
                startActivity(AboutUsActivity.class, null);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        Glide.with(getContext())
                .load(User.getInstance().getUserPortrait())
                .into(avatar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event2(MessageEvent2 messageEvent2) {
        myCenterName.setText(messageEvent2.getMseeage());
    }

    @OnClick({R.id.avatar, R.id.myName, R.id.phone})
    public void onViewClicked2(View view) {
//        startActivity(MyInformationActivity.class,null);
        getActivity().startActivityForResult(new Intent(getActivity(), MyInformationActivity.class), 1);
    }


}
