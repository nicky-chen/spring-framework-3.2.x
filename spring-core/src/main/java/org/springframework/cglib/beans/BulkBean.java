//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.beans;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ReflectUtils;

public abstract class BulkBean {
    private static final BulkBean.BulkBeanKey KEY_FACTORY;
    protected Class target;
    protected String[] getters;
    protected String[] setters;
    protected Class[] types;

    protected BulkBean() {
    }

    public abstract void getPropertyValues(Object var1, Object[] var2);

    public abstract void setPropertyValues(Object var1, Object[] var2);

    public Object[] getPropertyValues(Object bean) {
        Object[] values = new Object[this.getters.length];
        this.getPropertyValues(bean, values);
        return values;
    }

    public Class[] getPropertyTypes() {
        return (Class[])this.types.clone();
    }

    public String[] getGetters() {
        return (String[])this.getters.clone();
    }

    public String[] getSetters() {
        return (String[])this.setters.clone();
    }

    public static BulkBean create(Class target, String[] getters, String[] setters, Class[] types) {
        BulkBean.Generator gen = new BulkBean.Generator();
        gen.setTarget(target);
        gen.setGetters(getters);
        gen.setSetters(setters);
        gen.setTypes(types);
        return gen.create();
    }

    static {
        KEY_FACTORY = (BulkBean.BulkBeanKey)KeyFactory.create(BulkBean.BulkBeanKey.class);
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private Class target;
        private String[] getters;
        private String[] setters;
        private Class[] types;

        public Generator() {
            super(SOURCE);
        }

        public void setTarget(Class target) {
            this.target = target;
        }

        public void setGetters(String[] getters) {
            this.getters = getters;
        }

        public void setSetters(String[] setters) {
            this.setters = setters;
        }

        public void setTypes(Class[] types) {
            this.types = types;
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }

        public BulkBean create() {
            this.setNamePrefix(this.target.getName());
            String targetClassName = this.target.getName();
            String[] typeClassNames = ReflectUtils.getNames(this.types);
            Object key = BulkBean.KEY_FACTORY.newInstance(targetClassName, this.getters, this.setters, typeClassNames);
            return (BulkBean)super.create(key);
        }

        public void generateClass(ClassVisitor v) throws Exception {
            new BulkBeanEmitter(v, this.getClassName(), this.target, this.getters, this.setters, this.types);
        }

        protected Object firstInstance(Class type) {
            BulkBean instance = (BulkBean)ReflectUtils.newInstance(type);
            instance.target = this.target;
            int length = this.getters.length;
            instance.getters = new String[length];
            System.arraycopy(this.getters, 0, instance.getters, 0, length);
            instance.setters = new String[length];
            System.arraycopy(this.setters, 0, instance.setters, 0, length);
            instance.types = new Class[this.types.length];
            System.arraycopy(this.types, 0, instance.types, 0, this.types.length);
            return instance;
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }

        static {
            SOURCE = new Source(BulkBean.class.getName());
        }
    }

    interface BulkBeanKey {
        Object newInstance(String var1, String[] var2, String[] var3, String[] var4);
    }
}
