package com.goodfun.positionminitorjava.websocket;

import com.goodfun.positionminitorjava.service.DemoService;
import com.goodfun.positionminitorjava.service.api.RealQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket")
@Component
public class MyWebSocket {

    //用来存放每个客户端对应的MsgWebsocketController对象。
    private static ConcurrentHashMap<String, MyWebSocket> webSocketSet = new ConcurrentHashMap<>();
    //TaskScheduler实现轮询
    private static TaskScheduler task;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收appointmentId
    private String appointmentId = "";


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("session open. ID:" + session.getId());
        this.session = session;
        this.appointmentId = session.getId();//加入set中
        webSocketSet.put(appointmentId, this);
        System.out.println("有新连接加入！当前在线人数为" + webSocketSet.size());
        if (task == null) {

            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize(); // 初始化
            task = new TaskScheduler(scheduler);
            task.startCron(new Runnable() {

                @Override
                public void run() {

                    synchronized (session) {

                        broadcast("t1-load",session.getId());
                        System.out.println("10s查询");
                    }
                }


            }, new CronTrigger("0/10 * * * * *"));
            //这里设置的每10s查询一次数据库

        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("session close. ID:" + session.getId());

        webSocketSet.remove(appointmentId);  //从map中删除
        if (webSocketSet == null || webSocketSet.size() == 0) {

            task.stopCron();
            //没有连接则将task致为null
            task = null;
        }
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("get client msg. ID:" + session.getId() + ". msg:" + message);

        System.out.println("来自客户端的消息:" + message);

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public synchronized void onError(Session session, Throwable error) {

        //关闭定时器
        webSocketSet.remove(appointmentId);  //从map中删除
        if (webSocketSet == null || webSocketSet.size() == 0) {

            task = null;
        }
        System.out.println("发生错误");

        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public void broadcast(String message, String appointId) {

        //这里可以设定只推送给这个appointId的
        for (Map.Entry<String, MyWebSocket> item : webSocketSet.entrySet()) {

            item.getValue().session.getAsyncRemote().sendText(message);//异步发送消息.
        }

    }

}
