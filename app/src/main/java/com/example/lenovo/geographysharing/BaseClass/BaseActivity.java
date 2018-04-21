package com.example.lenovo.geographysharing.BaseClass;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.example.lenovo.geographysharing.R;


/**
 * Created by hejunlin on 17/3/19.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initData();
        if (ContextCompat.checkSelfPermission(BaseActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//参数为0,表示不跳向图库activity。
        }
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){
        return (T) findViewById(resId);
    }

    protected void setSupportActionBar(){
        mToolBar = bindViewId(R.id.toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
        }
    }

    protected void setSupportArrowActionBar(boolean isSupport){
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupport);
    }

    protected void setActionBarIcon(int resId){
        if (mToolBar != null){
            mToolBar.setNavigationIcon(resId);
        }
    }


}
