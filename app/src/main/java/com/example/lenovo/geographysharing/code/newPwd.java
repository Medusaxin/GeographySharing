package com.example.lenovo.geographysharing.code;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.geographysharing.R;

/**
 * Created by Lisa on 2018/4/15.
 */

public class newPwd extends Activity {
    EditText pwd_new;
    EditText pwd_again;
    Button btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_new_pwd);
        pwd_new=(EditText)findViewById(R.id.pwd_new);
        pwd_again=(EditText)findViewById(R.id.pwd_again);

        btnsubmit=(Button)findViewById(R.id.submit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断两次输入密码是否一致
                if(pwd_again.getText().toString().equals(pwd_new.getText().toString())){
                    Toast.makeText(getApplicationContext(), "重置密码成功！",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "两次密码不一致！",
                            Toast.LENGTH_SHORT).show();
                    pwd_new.setText(null);
                    pwd_again.setText(null);
                }
            }
        });
    }
}
