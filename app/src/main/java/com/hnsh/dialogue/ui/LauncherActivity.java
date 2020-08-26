package com.hnsh.dialogue.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.jet.modules.home.view.HomeActivity;
import com.jaeger.library.StatusBarUtil;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
        setContentView(R.layout.activity_launcher);
        autoIntent();
    }

    private void autoIntent() {
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1000);//使程序休眠一秒
                    Intent it = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
