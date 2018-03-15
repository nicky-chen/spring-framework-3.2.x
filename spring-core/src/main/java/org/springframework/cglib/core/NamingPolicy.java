package org.springframework.cglib.core;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/3/14 at 19:09
 */
public interface NamingPolicy {

    String getClassName(String var1, String var2, Object var3, Predicate var4);

    boolean equals(Object var1);

}
