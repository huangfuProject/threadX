package com.threadx.metrics.server.service.impl;

import com.threadx.metrics.server.service.ServerItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServerItemServiceImplTest {
    @Autowired
    private ServerItemService serverItemService;

    @Test
    public void findByNameOrCreate() {
        System.out.println(serverItemService.findByNameOrCreate("nihao"));
    }
}