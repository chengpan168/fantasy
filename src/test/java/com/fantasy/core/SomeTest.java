package com.fantasy.core;

import com.fantasy.core.helper.BearyChatHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Created by chengpanwang on 2016/11/18.
 */
public class SomeTest {

    @Test
    public void testReplace() {
        String templateUrl = "http://xyq.cbg.163.com/equip?s=%s&eid=%s&o&equip_refer=%s";
        String url = String.format(templateUrl, "1", "2", "3");
        System.out.println(url);
    }

    @Test
    public void testBearyChat() {
        BearyChatHelper.sendMsg("售卖价格: 1000, 计算价格: 2000", "http://wwww.baidu.com");
    }
}
