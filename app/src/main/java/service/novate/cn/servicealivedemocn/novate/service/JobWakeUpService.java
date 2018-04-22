package service.novate.cn.servicealivedemocn.novate.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntDef;

import java.util.List;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/22 10:33
 * Version 1.0
 * Params:
 * Description:   JobService -> 5.0以上才有的
*/


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {


    private final int jobWakeUpId = 1 ;
    /**
     * 开启服务之后，指定一个轮询时间
     */
    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        // 开启一个轮询
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobWakeUpId , new ComponentName(this , JobWakeUpService.class)) ;
        jobBuilder.setPeriodic(2000) ;  // 2s
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder.build()) ;


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // 开启一个定时任务，定时轮询 ，  判断MessageService有没有被杀死
        // 如果杀死，就启动，因为这个方法是每隔一段时间进行轮询调用 onStartJob()方法

        // 判断服务有没有被杀死（判断服务有没有正在运行）
        boolean messageServiceALive = serviceAlive(JobWakeUpService.class.getName());
        // 如果服务没有活，就启动 ，这里只需要启动一个服务就ok，不需要启动另一个 GuardService
        if (!messageServiceALive){
            startService(new Intent(JobWakeUpService.this , MessageService.class)) ;
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    /**
     * 判断某个服务是否正在运行的方法
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
