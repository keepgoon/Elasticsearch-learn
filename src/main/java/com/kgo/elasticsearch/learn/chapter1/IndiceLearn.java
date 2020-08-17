package com.kgo.elasticsearch.learn.chapter1;

import cn.hutool.core.util.StrUtil;
import com.kgo.elasticsearch.learn.basic.ESClient;
import com.kgo.elasticsearch.learn.basic.RequestMethod;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IndiceLearn {
    private final Logger log = LoggerFactory.getLogger(IndiceLearn.class);
    private ESClient esClient =ESClient.getInstance("192.168.1.233:9200","","");

    public static void main(String[] args) {
        IndiceLearn indiceLearn = new IndiceLearn();
        indiceLearn.exsitIndice("kgo_indice_learn");

        indiceLearn.cloneClient();
    }

    /**
     *  索引是否存在
     * @param indice
     * @return
     */
    public boolean exsitIndice(String indice){
        try {
            String endPoint = StrUtil.format("/{}",indice);
            Request request = new Request(RequestMethod.HEAD,endPoint);
            Response res =  esClient.getClient().performRequest(request);
            log.info("请求状态码 ： {} ",res.getStatusLine().getStatusCode());
            if (res.getStatusLine().getStatusCode() == 200) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("索引是否存在请求出错 ：  {} ",e.getMessage());
        }
        return false;
    }

    /**
     * 关闭客户端
     */
    public void cloneClient(){
        try {
            esClient.getClient().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
