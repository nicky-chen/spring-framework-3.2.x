package org.springframework.context.support.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * @author nicky_chin
 * @description:
 * @date: 2020/4/28 下午4:06
 * @since JDK 1.8
 */
public class BeanFactoryPostProcessorOne implements BeanFactoryPostProcessor, Ordered {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.out.println("调用 BeanFactoryPostProcessor_1 ...");

        System.out.println("容器中有 BeanDefinition 的个数：" + beanFactory.
            getBeanDefinitionCount());

        // 获取指定的 BeanDefinition
        BeanDefinition bd = beanFactory.getBeanDefinition("aa");

    }

    @Override

    public int getOrder() {
        return 1;

    }
}
