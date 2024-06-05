package com.threadx.metrics.server.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangfukexing
 * @date 2023/4/21 14:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("huangfu","kexing");
        Assert.assertEquals("kexing", redisTemplate.opsForValue().get("huangfu"));
        redisTemplate.delete("huangfu");
    }
}
