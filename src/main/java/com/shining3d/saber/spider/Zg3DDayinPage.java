package com.shining3d.saber.spider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.shining3d.saber.SaberConfig;
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
public class Zg3DDayinPage implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(Zg3DDayinPage.class);

    @Resource
    private Zg3DDayinPageDetail zg3DDayinPageDetail;
    @Resource
    private SaberConfig saberConfig;

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(500);

    @Override
    public void process(Page page) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        logger.info("当前抓取第 {} 页", saberConfig.getSpider3DDayinCurrentPage());
        Collection<String> hrefList = page.getHtml().xpath("//ul[@class='m4_box1']/li/div[@class='pics']/a/@href").all();

        hrefList = new HashSet<String>(hrefList);
        logger.info("当前页资讯详情url 列表 :" + hrefList);

        int i = 1;
        for (String link : hrefList) {
            logger.info("抓取第{}页， 第{}条记录", saberConfig.getSpider3DDayinCurrentPage(), i++);
            Spider.create(zg3DDayinPageDetail).addUrl(link).addPipeline(new SaberFilePipeline(getFilePath())).thread(1).run();

            try {
                TimeUnit.SECONDS.sleep(saberConfig.getSpiderSleepTime());
            } catch (Exception e) {
                logger.error("", e);
            }
        }

        logger.info("抓取第 {} 页完成，耗时:{} ms", saberConfig.getSpider3DDayinCurrentPage(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    private String getFilePath() {
        return saberConfig.getSpider3DDayinDownloadPath() + "/" + getSite().getDomain() + "/" + saberConfig.getSpider3DDayinCurrentPage();
    }

    @Override
    public Site getSite() {
        site.setCharset("gb2312");
        return site;

    }

    /*public static void main(String[] args) {
        int pageIndex = 1;
        int maxPage = 1;
        for (int i = pageIndex; i <= maxPage; i++) {
            Spider.create(new Zg3DDayinPage()).addUrl("http://www.3ddayin.net/zx/list_2_" + i + ".html").addPipeline(new FilePipeline("/Users/chengpanwang/Downloads/3d")).thread(1).run();
        }
    }*/
}
