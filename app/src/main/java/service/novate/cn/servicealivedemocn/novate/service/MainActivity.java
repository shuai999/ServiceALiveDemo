package service.novate.cn.servicealivedemocn.novate.service;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/22 9:25
 * Version 1.0
 * Params:
 * Description:
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService(new Intent(this , MessageService.class)) ;
        startService(new Intent(this , GuardService.class)) ;

        // 这里必须判断，否则5.0以下手机肯定崩溃
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            startService(new Intent(this , JobWakeUpService.class)) ;
        }
    }
}
