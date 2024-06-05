package com.threadx.metrics.server.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 请求头的数据解析<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 22:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestData implements Serializable {
    private static final long serialVersionUID = -1457079559423142057L;
    /**
     * 浏览器信息
     */
    private String browser = "未知";

    /**
     * os信息
     */
    private String os = "未知";;

    /**
     * ip信息
     */
    private String ipAddress = "未知";;
}
