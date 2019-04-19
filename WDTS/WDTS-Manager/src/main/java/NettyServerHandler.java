import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static Map<String,List<String>> transactionTypeMap = new HashMap<String, List<String>>();

    private static Map<String,Boolean> isEndMap = new HashMap<String, Boolean>();

    private static Map<String,Integer> transactionCountMap = new HashMap<String, Integer>();


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        Channel channel = ctx.channel();
        channelGroup.add(ctx.channel());
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

        System.out.println("接受数据:"+msg.toString());
        JSONObject jsonObject = JSON.parseObject((String)msg);
        String command = jsonObject.getString("command");
        String groupId = jsonObject.getString("groupId");
        String transactionType = jsonObject.getString("transactionType");
        Integer transactionCount = jsonObject.getInteger("transactionCount");
        Boolean isEnd = jsonObject.getBoolean("isEnd");

        if("create".equals(command)){
            transactionTypeMap.put(groupId,new ArrayList<String>());
        }else if("add".equals(command)){
            transactionTypeMap.get(transactionType);
            if(isEnd){
                isEndMap.put(groupId,true);
                transactionCountMap.put(groupId,transactionCount);
            }

            JSONObject result = new JSONObject();
            result.put("groupId",groupId);

            if(isEndMap.get(groupId) && transactionCountMap.get(groupId).equals(transactionTypeMap.get(groupId).size())){
                if(transactionTypeMap.get(groupId).contains("rollback")){
                    result.put("command","rollback");
                    sendResult(result);
                }else{
                    result.put("command","commit");
                    sendResult(result);
                }
            }
        }


    }

    private void sendResult(JSONObject result){
        for(Channel channel:channelGroup){
            System.out.println("发送数据："+result.toJSONString());
            channel.writeAndFlush(result.toJSONString());
        }
    }


}
