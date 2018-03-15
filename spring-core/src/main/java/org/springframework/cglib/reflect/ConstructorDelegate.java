//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.TypeUtils;

public abstract class ConstructorDelegate {
    private static final ConstructorDelegate.ConstructorKey KEY_FACTORY;

    protected ConstructorDelegate() {
    }

    public static ConstructorDelegate create(Class targetClass, Class iface) {
        ConstructorDelegate.Generator gen = new ConstructorDelegate.Generator();
        gen.setTargetClass(targetClass);
        gen.setInterface(iface);
        return gen.create();
    }

    static {
        KEY_FACTORY = (ConstructorDelegate.ConstructorKey)KeyFactory.create(ConstructorDelegate.ConstructorKey.class, KeyFactory.CLASS_BY_NAME);
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private static final Type CONSTRUCTOR_DELEGATE;
        private Class iface;
        private Class targetClass;

        public Generator() {
            super(SOURCE);
        }

        public void setInterface(Class iface) {
            this.iface = iface;
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }

        public ConstructorDelegate create() {
            this.setNamePrefix(this.targetClass.getName());
            Object key = ConstructorDelegate.KEY_FACTORY.newInstance(this.iface.getName(), this.targetClass.getName());
            return (ConstructorDelegate)super.create(key);
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.targetClass.getClassLoader();
        }

        public void generateClass(ClassVisitor v) {
            this.setNamePrefix(this.targetClass.getName());
            Method newInstance = ReflectUtils.findNewInstance(this.iface);
            if(!newInstance.getReturnType().isAssignableFrom(this.targetClass)) {
                throw new IllegalArgumentException("incompatible return type");
            } else {
                Constructor constructor;
                try {
                    constructor = this.targetClass.getDeclaredConstructor(newInstance.getParameterTypes());
                } catch (NoSuchMethodException var7) {
                    throw new IllegalArgumentException("interface does not match any known constructor");
                }

                ClassEmitter ce = new ClassEmitter(v);
                ce.begin_class(46, 1, this.getClassName(), CONSTRUCTOR_DELEGATE, new Type[]{Type.getType(this.iface)}, "<generated>");
                Type declaring = Type.getType(constructor.getDeclaringClass());
                EmitUtils.null_constructor(ce);
                CodeEmitter e = ce.begin_method(1, ReflectUtils.getSignature(newInstance), ReflectUtils.getExceptionTypes(newInstance));
                e.new_instance(declaring);
                e.dup();
                e.load_args();
                e.invoke_constructor(declaring, ReflectUtils.getSignature(constructor));
                e.return_value();
                e.end_method();
                ce.end_class();
            }
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }

        static {
            SOURCE = new Source(ConstructorDelegate.class.getName());
            CONSTRUCTOR_DELEGATE = TypeUtils.parseType("org.springframework.cglib.reflect.ConstructorDelegate");
        }
    }

    interface ConstructorKey {
        Object newInstance(String var1, String var2);
    }
}
