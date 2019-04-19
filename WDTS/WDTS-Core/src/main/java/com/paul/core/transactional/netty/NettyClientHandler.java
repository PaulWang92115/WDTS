package com.paul.core.transactional.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paul.core.transactional.transactional.TransactonType;
import com.paul.core.transactional.transactional.WSTransaction;
import com.paul.core.transactional.transactional.WSTransactionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext context;

    @Override
    public void channelActive(ChannelHandlerContext ctx)throws Exception{
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        System.out.println("接受数据："+msg.toString());
        JSONObject jsonObject = JSON.parseObject((String) msg);

        String groupId = jsonObject.getString("groupId");
        String command = jsonObject.getString("command");

        System.out.println("接受command:"+command);

        WSTransaction wsTransaction = WSTransactionManager.getWSTransaction(groupId);
        if("commit".equals(command)){
            //这里是个map，假设每个系统只有一个事务
            wsTransaction.setTransactonType(TransactonType.commit);
        }else{
            wsTransaction.setTransactonType(TransactonType.rollback);
        }
        wsTransaction.getTask().singalTask();

    }

    public synchronized Object call(JSONObject data) throws Exception{
        context.writeAndFlush(data.toJSONString());
        return null;
    }
}
