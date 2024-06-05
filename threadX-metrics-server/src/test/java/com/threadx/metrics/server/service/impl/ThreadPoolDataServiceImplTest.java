package com.threadx.metrics.server.service.impl;

import com.threadx.metrics.server.conditions.ThreadPoolPageDataConditions;
import com.threadx.metrics.server.service.ThreadPoolDataService;
import com.threadx.metrics.server.vo.ThreadPoolDataVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ThreadPoolDataServiceImplTest {

    @Autowired
    private ThreadPoolDataService threadPoolDataService;

    @Test
    public void findPageByThreadPoolPageDataConditions() {
        ThreadPoolPageDataConditions threadPoolPageDataConditions = new ThreadPoolPageDataConditions();
        threadPoolPageDataConditions.setPageNumber(3);
        threadPoolPageDataConditions.setPageSize(3);
        ThreadxPage<ThreadPoolDataVo> pageByThreadPoolPageDataConditions = threadPoolDataService.findPageByThreadPoolPageDataConditions(threadPoolPageDataConditions);

        System.out.println();
    }
}