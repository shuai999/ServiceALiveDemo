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
import android.widget.Toast;

/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/4/22 9:32
 * Version 1.0
 * Params:
 * Description:    守护进程，双进程通讯，需要使用aidl（进程间通讯）
 *                 两个进程之间相互监听，相互唤醒
*/
public class GuardService extends Service {
    private int GuardId = 1;


    /**
     * 返回 IBinder驱动
     * Stub: 存根
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub(){} ;
    }


//    private ProcessConnection mBinder = new ProcessConnection.Stub(){} ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提高进程优先级 ，就会在通知栏中出现自己的应用，如果不想提高优先级，可以把这个注释
        startForeground(GuardId , new Notification());

        // 让GuardService绑定MessageService 并建立连接
        bindService(new Intent(this , MessageService.class) , mServiceConnection , Context.BIND_IMPORTANT) ;
        return START_STICKY;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接上
            Toast.makeText(GuardService.this , "建立连接" , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接，就需要重新启动，然后重新绑定

            // 重新启动
            startService(new Intent(GuardService.this , MessageService.class)) ;
            // 重新绑定
            bindService(new Intent(GuardService.this , MessageService.class) , mServiceConnection , Context.BIND_IMPORTANT) ;
        }
    } ;
}
