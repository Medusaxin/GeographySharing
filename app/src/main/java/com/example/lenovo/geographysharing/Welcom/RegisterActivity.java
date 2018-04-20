package com.example.lenovo.geographysharing.Welcom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText RegisterName;
    private EditText Password1;
    private EditText Password2;
    private EditText Phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //控件绑定
        RegisterName = (EditText) findViewById(R.id.register_name);
        Password1 = (EditText) findViewById(R.id.register_password1);
        Password2 = (EditText) findViewById(R.id.register_password2);
        Phone = (EditText) findViewById(R.id.phone_number);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_register_button && checkForm()) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private boolean checkForm(){
        final String name = RegisterName.getText().toString();
        final String pass1 = Password1.getText().toString();
        final String pass2 = Password2.getText().toString();
        final String phone = Phone.getText().toString();

        boolean isPass = true;

        if (name.isEmpty()){
            RegisterName.setError("请输入姓名!");
            isPass =false;
        }else{
            RegisterName.setError(null);
        }

        if (pass1.isEmpty() || pass1.length()<6){
            Password1.setError("输入密码大于6位!");
            isPass =false;
        }else{
            Password1.setError(null);
        }

        if (pass2.isEmpty()||pass2.length()<6||!(pass2.equals(pass1))){
            Password2.setError("两次密码输入不一致!");
            isPass =false;
        }else{
            Password2.setError(null);
        }

        if (phone.isEmpty()||phone.length()!=11){
            Phone.setError("手机号码输入错误!");
            isPass =false;
        }else{
            Phone.setError(null);
        }

        return isPass;

    }
}
