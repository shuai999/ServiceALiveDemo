package service.novate.cn.servicealivedemocn.novate.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/22 9:08
 * Version 1.0
 * Params:
 * Description:    QQ聊天通讯  Service代码中一定要轻量
*/

public class MessageService extends Service {


    private int MessageId = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){} ;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.e("TAG", "等待接收消息");

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    /**
     * MainActivity中一启动MessageService之后，就会调用 onStartCommand()方法
     */
    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        // 提高进程优先级 ，就会在通知栏中出现自己的应用，如果不想提高优先级，可以把这个注释
        // 参数1：id 参数2：通知
        startForeground(MessageId , new Notification());

        // 让MessageService绑定 GuardService并建立连接
        bindService(new Intent(this , GuardService.class) , mServiceConnection , Context.BIND_IMPORTANT) ;
        return START_STICKY;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接上
            Toast.makeText(MessageService.this , "建立连接" , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接，需要重新启动，然后重新绑定

            // 重新启动
            startService(new Intent(MessageService.this , GuardService.class)) ;
            // 重新绑定
            bindService(new Intent(MessageService.this , GuardService.class) , mServiceConnection , Context.BIND_IMPORTANT) ;


        }
    } ;

}
