package com.shining3d.saber.spider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.shining3d.saber.SaberConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;

@Service
public class Zg3DDayinPageDetail implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Zg3DDayinPageDetail.class);

    @Resource
    private SaberConfig saberConfig;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(5).setSleepTime(5000);

    @Override
    public void process(Page page) {
        page.putField("url", page.getUrl());
        page.putField("title", page.getHtml().xpath("//h2[@class='m4_box4']"));
        page.putField("info", page.getHtml().xpath("//div[@class='m4_box5']"));

        String content = page.getHtml().xpath("//div[@class='m4_box6']").toString();

        List<String> imgList = page.getHtml().xpath("//div[@class='m4_box6']//img/@src").all();
        page.putField("img", imgList);
        for (String imgUrl : imgList) {
            String url = upload(imgUrl);
            content = StringUtils.replace(content, imgUrl, url);
        }

        page.putField("content", content);

    }

    @Override
    public Site getSite() {
        site.setCharset("gb2312");
        return site;

    }

    HttpClient client = HttpClients.createDefault();

    public String upload(String imgUrl) {

        if (!StringUtils.startsWith(imgUrl, "http")) {
            imgUrl = "http://www.3ddayin.net" + imgUrl;
        }

        System.out.println("upload img:" + imgUrl);

        HttpGet get = new HttpGet(imgUrl);
        InputStream in = null;
        try {
            HttpResponse response = client.execute(get);

            logger.info("http get status:" + response.getStatusLine());

            logger.info("http response length :" + response.getEntity().getContentLength());

            in = response.getEntity().getContent();

            String fileName = StringUtils.substring(imgUrl, StringUtils.lastIndexOf(imgUrl, "/") + 1);
            logger.info("file name : " + fileName);

            HttpPost post = new HttpPost(saberConfig.getImgUploadUrl());
            //            post.setHeader("Content-Type", "multipart/form-data");
            post.setHeader("accessId", saberConfig.getAuthPanguAccessId());
            post.setHeader("accessSecret", saberConfig.getAuthPanguAccessSecret());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", IOUtils.toByteArray(in), ContentType.MULTIPART_FORM_DATA, fileName);
            post.setEntity(builder.build());

            HttpResponse postResponse = client.execute(post);
            logger.info("upload response status:" + postResponse.getStatusLine());
            String res = IOUtils.toString(postResponse.getEntity().getContent());
            if (postResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("上传图片失败");
            }


            if (StringUtils.isBlank(res)) {
                return null;
            }
            Map map = JSON.parseObject(res, Map.class);
            return String.valueOf(map.get("finalImgUrl"));

        } catch (IOException e) {
            logger.error("", e);
            throw new RuntimeException("上传图片失败");
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /*public static void main(String[] args) {
        Spider.create(new Zg3DDayinPageDetail()).addUrl("http://www.3ddayin.net/zx/26094.html").addPipeline(new FilePipeline(
                                                                                                                                   "/Users/chengpanwang/Downloads/3d")).thread(1).run();
    }*/
}
