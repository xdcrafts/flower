package org.xdcrafts.flower.spring.impl;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Abstract bean factory that aware of bean name.
 * @param <T> bean type
 */
public abstract class AbstractNameAwareFactoryBean<T>
    extends AbstractFactoryBean<T>
    implements BeanNameAware {

    private String beanName;

    @Override
    public void setBeanName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can not be null or empty string!");
        }
        this.beanName = name;
    }

    public String getBeanName() {
        return this.beanName;
    }
}
