package com.example.lenovo.geographysharing.Issue;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.R;

/**
 * Created by shdeng on 2018/4/20.
 */

public class IssueDetailRequestActivity extends BaseActivity {
    private Button bt_Issue ;


    @Override
    protected void initData() {

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_requirements;
    }
    @Override
        protected void initView() {     setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("发布需求");
    /*
    * 以下被注释的方法适合一个button时用
    * */
//        btn_Upload_Pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Chose_Pic_or_Cam();
//            }
//        });
    }

    public  static  void launchIIssueDetailRequestActivity(Activity activity){
        Intent intent = new Intent(activity, IssueDetailRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);

    }




}
