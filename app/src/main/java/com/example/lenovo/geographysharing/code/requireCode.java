package com.example.lenovo.geographysharing.code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.R;


/**
 * Created by Lisa on 2018/4/12.
 */

public class requireCode extends Activity {
    private EditText phone_num;  //手机号输入框
    private Button code_gain;  //获取验证码按钮


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        phone_num=(EditText)findViewById(R.id.phone_number);
        code_gain=(Button)findViewById(R.id.gain);


        code_gain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPhoneValid(phone_num.getText().toString())){
                    //带手机号跳转
                    Intent intent=new Intent(requireCode.this,inputCode.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("phone",phone_num.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

    }

    //判断手机号是否正确(未包含具体格式）
    private boolean isPhoneValid(String s){
        if (s.isEmpty()) {
            Toast.makeText(this, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (s.length()!=11){
            Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
