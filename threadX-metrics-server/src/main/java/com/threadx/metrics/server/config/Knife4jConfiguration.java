package com.threadx.metrics.server.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置信息
 * Knife4jConfiguration
 *
 * @author huangfukexing
 * @date 2023/5/6 14:16
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class Knife4jConfiguration {

    @Bean
    public Docket docket() {

        //指定使用Swagger2规范
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("#线程池监控日志收集服务")
                        .termsOfServiceUrl("https://threadx.huangfu.cn/")
                        .contact("huangfu_project@163.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("线程池监控日志收集服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.threadx.metrics.server.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}