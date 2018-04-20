package com.example.lenovo.geographysharing.code;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.R;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Lisa on 2018/4/13.
 */

public class inputCode extends Activity implements View.OnClickListener{

    TextView display_num;   //显示一下手机号
    EditText code;     //输入验证码
    Button nextbtn;    //下一步
    Button time;    //倒计时按钮
    String num;
    int i=60;           //短信验证提示时间为60秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //启动短信验证sdk
        MobSDK.init(this,"252b1b295dd62","f9f0d0f01ba2dae86076a8ec32a8a7ae");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        display_num=(TextView)findViewById(R.id.user_phone);
        code=(EditText)findViewById(R.id.code_input);
        nextbtn=(Button)findViewById(R.id.next);
        time=(Button)findViewById(R.id.code_time);
        nextbtn.setOnClickListener(this);
        time.setOnClickListener(this);


        //接收数据
        Bundle bundle=this.getIntent().getExtras();
        num=bundle.getString("phone");
        //在相应的文本框中显示用户手机号(加密)
        display_num.setText(num.replace(num.substring(3,7),"****"));

        final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler); // 注册回调监听接口

        sendCode();

    }


    //由于EventHandler开启了线程，不能直接在afterEvent中更新UI，
// 所以还需要在MainActivity当中定义一个Handler
// 来接受EventHandler发送过来的消息。


      Handler handler=new Handler(){
    public void handleMessage(Message msg) {
        if (msg.what == -1) {
            time.setText(i+"秒");  //为啥非要加个文字
        } else if (msg.what == -2) {
            time.setText("重新发送");
            time.setClickable(true); // 设置可点击
            i = 60;
        } else {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "提交验证码成功",
                            Toast.LENGTH_SHORT).show();
                    // 验证成功后输入新密码
                    Intent intent = new Intent(inputCode.this, newPwd.class);
                    startActivity(intent);
                    finish();// 成功跳转之后销毁当前页面

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }else if(result== SMSSDK.RESULT_ERROR){//验证码错误
                Toast.makeText(getApplicationContext(), "验证码有误",
                        Toast.LENGTH_SHORT).show();
   //可能存在bug
            }
        }

    }

};



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.code_time:
                sendCode();
                break;
            case R.id.next:
                if(code.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"验证码不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
                //判断验证码是否正确(可以添加一个进度条)
                SMSSDK.submitVerificationCode("86",num,code.getText().toString());
                break;
            default:
                break;
        }
    }


    //发送验证码封装成方法
    public void sendCode(){
        SMSSDK.getVerificationCode("86",num); // 调用sdk发送短信验证

        time.setText(i + "秒");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = 60; i > 0; i--) {
                    handler.sendEmptyMessage(-1);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);// 线程休眠实现读秒功能
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-2);// 在60秒后重新显示为重新发送
            }
        }).start();
    }



    //在完成短信验证之后，需要销毁回调监听接口
    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

}
