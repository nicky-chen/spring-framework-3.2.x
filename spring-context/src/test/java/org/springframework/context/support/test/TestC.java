package org.springframework.context.support.test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2020/4/24 下午5:15
 *
 * @since JDK 1.8
 */
public class TestC {

    @Autowired
    private TestB testB;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
