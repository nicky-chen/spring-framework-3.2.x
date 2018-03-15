//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

public class ClassesKey {
    private static final ClassesKey.Key FACTORY;

    private ClassesKey() {
    }

    public static Object create(Object[] array) {
        return FACTORY.newInstance(array);
    }

    //Class keyInterface, Customizer customizer
    static {
        FACTORY = (ClassesKey.Key)KeyFactory.create(ClassesKey.Key.class);
    }

    interface Key {
        Object newInstance(Object[] var1);
    }
}
