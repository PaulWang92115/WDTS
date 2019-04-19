package com.paul.core.transactional.util;

import com.paul.core.transactional.transactional.WSTransactionManager;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {

    public static String get(String url){
        String result = "";
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-type","application/json");
            httpGet.addHeader("groupId", WSTransactionManager.getCurrentGroupId());
            httpGet.addHeader("tranactionCount",String.valueOf(WSTransactionManager.getTransactionCount()));

            CloseableHttpResponse response = httpClient.execute(httpGet);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(response.getEntity(),"utf-8");
            }
            response.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
