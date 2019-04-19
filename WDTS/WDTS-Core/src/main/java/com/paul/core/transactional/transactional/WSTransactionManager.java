package com.paul.core.transactional.transactional;

import com.alibaba.fastjson.JSONObject;
import com.paul.core.transactional.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WSTransactionManager {

    private static NettyClient nettyClient;
    private static ThreadLocal<WSTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    @Autowired
    public void setNettyClient(NettyClient nettyClient){WSTransactionManager.nettyClient = nettyClient;}

    public static Map<String,WSTransaction> WS_TRANSACTIN_MAP = new HashMap<>();

    /**
     * 创建事务组，返回groupId
     */
    public static String createWSTransactionGroup(){
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",groupId);
        jsonObject.put("command","create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");
        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建分布式事务
     */
    public static WSTransaction createWSTransaction(String groupId){
        String transactionId = UUID.randomUUID().toString();
        WSTransaction wsTransaction = new WSTransaction(groupId,transactionId);
        WS_TRANSACTIN_MAP.put(groupId,wsTransaction);
        current.set(wsTransaction);
        addTransactionCount();

        System.out.println("创建事务");
        return wsTransaction;
    }

    public static WSTransaction addWSTransaction(WSTransaction wsTransaction,Boolean isEnd,TransactonType transactonType){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId",wsTransaction.getGroupId());
        jsonObject.put("transactionId",wsTransaction.getTransactionId());
        jsonObject.put("transactionType",transactonType);
        jsonObject.put("command","add");
        jsonObject.put("isEnd",isEnd);
        jsonObject.put("transactionCount",WSTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("添加事务");
        return wsTransaction;
    }

    public static WSTransaction getWSTransaction(String groupId){return WS_TRANSACTIN_MAP.get(groupId);}

    public static WSTransaction getCurrent(){return current.get();}
    public static String getCurrentGroupId(){return currentGroupId.get();}

    public static void setCurrentGroupId(String groupId){currentGroupId.set(groupId);}

    public static Integer getTransactionCount(){return transactionCount.get();}

    public static void setTransactionCount(Integer value){transactionCount.set(value);}

    public static void addTransactionCount(){
        Integer count = transactionCount.get();
        count++;
        transactionCount.set(count);
    }
}
