package cn.itcast.solrj.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Test;

import cn.itcast.solrj.pojo.EasyUIResult;
import cn.itcast.solrj.pojo.Item;

public class ItemDataImport {

    @Test
    public void start() throws Exception {
        // 1、从数据库中查询商品数据
        String url = "http://manage.taotao.com/rest/item?page={page}&rows=100";
        
        // 定义httpsolrserver
        // http://solr.taotao.com/#/taotao  -- 网页地址
        String solrUrl = "http://solr.taotao.com/taotao";
        HttpSolrServer httpSolrServer = new HttpSolrServer(solrUrl); //定义solr的server
        httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
        httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间
        
        // 分页获取数据
        Integer page = 1;
        Integer pageSize = 0;
        do {
            String jsonData = doGet(StringUtils.replace(url, "{page}", String.valueOf(page)));
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Item.class);
            List<Item> rows = (List<Item>) easyUIResult.getRows();
            pageSize = rows.size();

            // 2、将商品数据写入到solr
            httpSolrServer.addBeans(rows);
            httpSolrServer.commit();

            page++;
        } while (pageSize == 100);

    }

    private String doGet(String url) {
        try {
            // 创建Httpclient对象
            CloseableHttpClient httpclient = HttpClients.createDefault();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = null;
            try {
                // 执行请求
                response = httpclient.execute(httpGet);
                // 判断返回状态是否为200
                if (response.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } finally {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
