package com.example.lenovo.geographysharing.Issue;

import android.app.Activity;
import android.content.Intent;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.R;

/**
 * Created by lenovo on 2018/1/2.
 */

public class IssueDetailActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_details;
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("填写发布设备信息");


    }

    @Override
    protected void initData() {

    }

    public static void launchIssueDetailActivity(Activity activity){
        Intent intent = new Intent(activity,IssueDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }


}
