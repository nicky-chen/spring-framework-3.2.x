//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;

public abstract class FastClass {
    private Class type;

    protected FastClass() {
        throw new Error("Using the FastClass empty constructor--please report to the cglib-devel mailing list");
    }

    protected FastClass(Class type) {
        this.type = type;
    }

    public static FastClass create(Class type) {
        return create(type.getClassLoader(), type);
    }

    public static FastClass create(ClassLoader loader, Class type) {
        FastClass.Generator gen = new FastClass.Generator();
        gen.setType(type);
        gen.setClassLoader(loader);
        return gen.create();
    }

    public Object invoke(String name, Class[] parameterTypes, Object obj, Object[] args) throws InvocationTargetException {
        return this.invoke(this.getIndex(name, parameterTypes), obj, args);
    }

    public Object newInstance() throws InvocationTargetException {
        return this.newInstance(this.getIndex(Constants.EMPTY_CLASS_ARRAY), (Object[])null);
    }

    public Object newInstance(Class[] parameterTypes, Object[] args) throws InvocationTargetException {
        return this.newInstance(this.getIndex(parameterTypes), args);
    }

    public FastMethod getMethod(Method method) {
        return new FastMethod(this, method);
    }

    public FastConstructor getConstructor(Constructor constructor) {
        return new FastConstructor(this, constructor);
    }

    public FastMethod getMethod(String name, Class[] parameterTypes) {
        try {
            return this.getMethod(this.type.getMethod(name, parameterTypes));
        } catch (NoSuchMethodException var4) {
            throw new NoSuchMethodError(var4.getMessage());
        }
    }

    public FastConstructor getConstructor(Class[] parameterTypes) {
        try {
            return this.getConstructor(this.type.getConstructor(parameterTypes));
        } catch (NoSuchMethodException var3) {
            throw new NoSuchMethodError(var3.getMessage());
        }
    }

    public String getName() {
        return this.type.getName();
    }

    public Class getJavaClass() {
        return this.type;
    }

    public String toString() {
        return this.type.toString();
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    public boolean equals(Object o) {
        return o != null && o instanceof FastClass?this.type.equals(((FastClass)o).type):false;
    }

    public abstract int getIndex(String var1, Class[] var2);

    public abstract int getIndex(Class[] var1);

    public abstract Object invoke(int var1, Object var2, Object[] var3) throws InvocationTargetException;

    public abstract Object newInstance(int var1, Object[] var2) throws InvocationTargetException;

    public abstract int getIndex(Signature var1);

    public abstract int getMaxIndex();

    protected static String getSignatureWithoutReturnType(String name, Class[] parameterTypes) {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append('(');

        for(int i = 0; i < parameterTypes.length; ++i) {
            sb.append(Type.getDescriptor(parameterTypes[i]));
        }

        sb.append(')');
        return sb.toString();
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private Class type;

        public Generator() {
            super(SOURCE);
        }

        public void setType(Class type) {
            this.type = type;
        }

        public FastClass create() {
            this.setNamePrefix(this.type.getName());
            return (FastClass)super.create(this.type.getName());
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.type.getClassLoader();
        }

        public void generateClass(ClassVisitor v) throws Exception {
            new FastClassEmitter(v, this.getClassName(), this.type);
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type, new Class[]{Class.class}, new Object[]{this.type});
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }

        static {
            SOURCE = new Source(FastClass.class.getName());
        }
    }
}
