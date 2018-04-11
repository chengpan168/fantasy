package com.fantasy.core.spider;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by panwang.chengpw on 2018/3/26.
 */

@Component
public class RoleTask {

    @Scheduled(cron = "*/30 * * * * *")
    public void run() {

    }
}
