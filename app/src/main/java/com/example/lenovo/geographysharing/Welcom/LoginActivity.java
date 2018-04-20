package com.example.lenovo.geographysharing.Welcom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.Home.HomeActivity;
import com.example.lenovo.geographysharing.R;
import com.example.lenovo.geographysharing.code.inputCode;
import com.example.lenovo.geographysharing.code.requireCode;

public class LoginActivity extends Activity implements View.OnClickListener{
    private EditText LoginName;
    private EditText Password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponent();

    }
    private void initializeComponent() {

//        //标题栏
//        ActionBar actionBar = getSupportActionBar();
//        //隐藏标题栏
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        //隐藏状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //控件实例化
        Button login = (Button) findViewById(R.id.login_button);
        Button forget = (Button) findViewById(R.id.login_forget);
        Button create = (Button) findViewById(R.id.login_create);
        //控件的监听绑定
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        create.setOnClickListener(this);

        LoginName = (EditText) findViewById(R.id.login_username);
        Password = (EditText) findViewById(R.id.login_password);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button && checkForm()) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.login_forget) {
            //Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(LoginActivity.this,requireCode.class);
            startActivity(intent);

        } else if (v.getId() == R.id.login_create) {
            Toast.makeText(this, "注册新用户", Toast.LENGTH_SHORT);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkForm(){
        String name = LoginName.getText().toString();
        String pass = Password.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()){
            LoginName.setError("用户名不能为空");
            isPass = false;
        }else {
            LoginName.setError(null);
        }

        if (pass.isEmpty()||pass.length()<6)
        {
            Password.setError("密码输入错误");
            isPass = false;
        }else {
            Password.setError(null);
        }

        return isPass;

    }

    public static void launchLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        //防止多次实例调用
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
