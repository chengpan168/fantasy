package com.shining3d.saber.spider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.update.IDisconfUpdatePipeline;
import com.google.common.base.Stopwatch;
import com.shining3d.common.StringUtils;
import com.shining3d.common.VelocityUtils;
import com.shining3d.saber.SaberConfig;

import us.codecraft.webmagic.Spider;

/**
 * Created by chengpanwang on 2016/11/16.
 */
@Service
public class Zg3DDayinFileProcessor implements IDisconfUpdatePipeline {

    private static final Logger logger = LoggerFactory.getLogger(Zg3DDayinFileProcessor.class);

    @Resource
    private Zg3DDayinPage       zg3DDayinPage;
    @Resource
    private SaberConfig         saberConfig;
    @Resource
    private TaskExecutor taskExecutor;

    private boolean running = false;

    @Override
    public void reloadDisconfFile(String name, String filePath) throws Exception {
        logger.info("重新加载配置项，key:{}, value:{}", name, filePath);

        if (!StringUtils.equals(name, "dynamic_config.properties")) {
            return;
        }
        InputStream in = null;
        try {
            Properties properties = new Properties();
            if (StringUtils.isNotBlank(filePath)) {
                in = new FileInputStream(filePath);
                properties.load(in);
                Object value = properties.get("spider.3ddayin.fileProcessor");
                logger.info("中国3d打印抓取开关：{}", value);
//                saberConfig.setSpider3DDayinFileProcessor(String.valueOf(value));
                process();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Override
    public void reloadDisconfItem(String s, Object o) throws Exception {
        logger.info("重新加载配置项，key:{}, value:{}", s, o);
        if (StringUtils.equals(s, "spider.3ddayin.switch")) {
//            saberConfig.setSpider3DDayinFileProcessor(String.valueOf(o));
            process();
        }
    }

    private void process() {
        taskExecutor.execute(new Runnable() {

            @Override
            public void run() {
                runTask();
            }
        });
    }

    private void runTask() {
       /* if (!saberConfig.getSpider3DDayinFileProcessor()) {
            return;
        } else if (running) {
            logger.info("正在运行中， 可以先停掉，再重新开始。。。");
            return;
        }*/
        running = true;

        int pageStart = saberConfig.getSpider3DDayinPageStart();
//        int pageEnd = saberConfig.getSpider3DDayinPageEnd();

        Stopwatch stopwatch = Stopwatch.createStarted();

        running = false;

//        logger.info("抓取第页面，start:{}, end:{} 全部完成，耗时:{} ms", pageStart, pageEnd, stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

}
