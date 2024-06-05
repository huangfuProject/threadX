package com.threadx;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义的ClassLoad
 *
 * @author huangfukexing
 * @date 2023/3/10 17:44
 */
public class AgentClassLoader extends URLClassLoader {
    public AgentClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
