package com.goodfun.positionminitorjava.websocket;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class TaskScheduler {

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 在ScheduledFuture中有一个cancel可以停止定时任务。
     */
    private ScheduledFuture<?> future;

    public TaskScheduler(ThreadPoolTaskScheduler threadPoolTaskScheduler) {

        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }


    /**
     * 启动任务
     **/

    public void startCron(Runnable runnable, CronTrigger cronTrigger) {

        System.out.println("TaskScheduler#startCron()");
        future = threadPoolTaskScheduler.schedule(runnable, cronTrigger);
    }

    /**
     * 停止任务
     **/
    public void stopCron() {

        System.out.println("TaskScheduler#stopCron()");
        if (future != null) {

            future.cancel(true);
        }
    }

    /**
     * 变更任务间隔，再次启动
     **/

    public void changeCron(Runnable runnable, CronTrigger cronTrigger) {

        System.out.println("TaskScheduler#changeCron()");
        stopCron();// 先停止，在开启.
        future = threadPoolTaskScheduler.schedule(runnable, cronTrigger);
    }
}
