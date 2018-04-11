package com.fantasy.core.job;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.fantasy.core.helper.BearyChatHelper;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasy.common.HttpUtils;

/**
 * Created by panwang.chengpw on 2018/4/5.
 */
@Component
public class RoleSearchJob {

    private static final Logger logger       = LoggerFactory.getLogger(HttpUtils.class);

    @Resource
    private RoleDetailSpider    roleDetailSpider;

    private BigDecimal          defaultPrice = new BigDecimal(14000);

    @Scheduled(cron = "* */10 * * * *")
    public void run() {
        logger.info("角色搜索任务");

        String roleUrl  = "http://xyq.cbg.163.com/cgi-bin/xyq_overall_search.py?jfrspbps&server_type=2%2C3&zhuang_zhi=10%2C20%2C30%2C40%2C50%2C60%2C70%2C80%2C90&act=overall_search_role&page=1";
        String json = HttpUtils.get(roleUrl);



        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray list = (JSONArray) MapUtils.getObject(jsonObject, "equip_list");

        list.forEach(item -> {

            BigDecimal price = defaultPrice;
            JSONObject role = (JSONObject) item;
            //            法修
            int expt_fashu = MapUtils.getIntValue(role, "expt_fashu", 0);
            if (expt_fashu < 25) {
                price = price.subtract(new BigDecimal(500));
            }

            //            防修
            int expt_fangyu = MapUtils.getIntValue(role, "expt_fangyu", 0);
            if (expt_fangyu < 25) {
                price = price.subtract(new BigDecimal(500));
            }

            // 攻击控制
            int bb_expt_gongji = MapUtils.getIntValue(role, "bb_expt_gongji", 0);
            if (bb_expt_gongji == 25) {
                price = price.add(new BigDecimal(800));
            }

            //            防御控制
            int bb_expt_fangyu = MapUtils.getIntValue(role, "bb_expt_fangyu", 0);
            if (bb_expt_fangyu == 25) {
                price = price.add(new BigDecimal(800));
            }
            //            法术控制
            int bb_expt_fashu = MapUtils.getIntValue(role, "bb_expt_fashu", 0);
            if (bb_expt_fashu == 25) {
                price = price.add(new BigDecimal(800));
            }
            //            法抗控制
            int bb_expt_kangfa = MapUtils.getIntValue(role, "bb_expt_kangfa", 0);
            if (bb_expt_kangfa == 25) {
                price = price.add(new BigDecimal(800));
            }

            String salePrice = MapUtils.getString(role, "price");


            String eid = MapUtils.getString(role, "eid");
            String serverid = MapUtils.getString(role, "serverid");
            String equip_count = MapUtils.getString(role, "equip_count");

            String templateUrl = "http://xyq.cbg.163.com/equip?s=%s&eid=%s&o&equip_refer=%s";
            String url = String.format(templateUrl, serverid, eid, equip_count);
            price = price.add(roleDetailSpider.skillPrice(url));

            logger.info("搜索结果,售卖价格：{}, 计算价格:{}", salePrice, price);

            if (new BigDecimal(salePrice).compareTo(price) <= 0) {
                BearyChatHelper.sendMsg(String.format("售卖价格：%s, 计算价格:%d", salePrice, price), url);
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

    }
}
