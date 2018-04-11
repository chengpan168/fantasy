package com.fantasy.core.helper;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fantasy.common.HttpUtils;
import com.google.common.collect.Maps;

/**
 * Created by chengpanwang on 2017/2/8.
 */
@Component
public class BearyChatHelper {

    private static String bearyChatUrl = "https://hook.bearychat.com/=bwDg3/incoming/6fc847eee6e1e4a1c44fcdc744a16d5e";

    public static void sendMsg(String msg, String roleUrl) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("text", "意向角色");

        Map<String, Object> addressAttachment = Maps.newHashMap();
        addressAttachment.put("title", roleUrl);
        addressAttachment.put("url", roleUrl);

        Map<String, Object> msgAttachment = Maps.newHashMap();
        msgAttachment.put("title", "价格");
        msgAttachment.put("text", msg);

        param.put("attachments", Arrays.asList(addressAttachment, msgAttachment));

        HttpUtils.postJson(bearyChatUrl, param);
    }

}
