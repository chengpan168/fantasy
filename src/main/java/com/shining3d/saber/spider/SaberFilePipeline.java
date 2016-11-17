package com.shining3d.saber.spider;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by chengpanwang on 2016/11/17.
 */
@ThreadSafe
public class SaberFilePipeline extends FilePipeline {

    private static final Logger logger = LoggerFactory.getLogger(SaberFilePipeline.class);

    public SaberFilePipeline(String filePath) {
        setPath(filePath);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String path = this.path + PATH_SEPERATOR;
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".html")));
            printWriter.println("url:\t" + resultItems.getRequest().getUrl());
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}
