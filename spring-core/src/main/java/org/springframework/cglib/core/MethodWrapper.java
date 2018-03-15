//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MethodWrapper {
    private static final MethodWrapper.MethodWrapperKey KEY_FACTORY;

    private MethodWrapper() {
    }

    public static Object create(Method method) {
        return KEY_FACTORY.newInstance(method.getName(), ReflectUtils.getNames(method.getParameterTypes()), method.getReturnType().getName());
    }

    public static Set createSet(Collection methods) {
        Set set = new HashSet();
        Iterator it = methods.iterator();

        while(it.hasNext()) {
            set.add(create((Method)it.next()));
        }

        return set;
    }

    static {
        KEY_FACTORY = (MethodWrapper.MethodWrapperKey)KeyFactory.create(MethodWrapperKey.class);
    }

    public interface MethodWrapperKey {
        Object newInstance(String var1, String[] var2, String var3);
    }
}
