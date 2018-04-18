package com.example.administrator.kenya.ui.city.friends;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.kenya.R;
import com.example.administrator.kenya.adapter.FriendAdapter;
import com.example.administrator.kenya.base.BaseActivity;
import com.example.administrator.kenya.classes.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsActivity extends BaseActivity {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Friend> friendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        friendList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Friend friend = new Friend();
            friend.setName("你好！树先生");
            friendList.add(friend);
        }
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutmanager);
        FriendAdapter adapter = new FriendAdapter(this, friendList);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
