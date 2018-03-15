//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;

public abstract class BeanCopier {
    private static final BeanCopier.BeanCopierKey KEY_FACTORY;
    private static final Type CONVERTER;
    private static final Type BEAN_COPIER;
    private static final Signature COPY;
    private static final Signature CONVERT;

    public BeanCopier() {
    }

    public static BeanCopier create(Class source, Class target, boolean useConverter) {
        BeanCopier.Generator gen = new BeanCopier.Generator();
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    public abstract void copy(Object var1, Object var2, Converter var3);

    static {
        KEY_FACTORY = (BeanCopier.BeanCopierKey)KeyFactory.create(BeanCopier.BeanCopierKey.class);
        CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
        BEAN_COPIER = TypeUtils.parseType("org.springframework.cglib.beans.BeanCopier");
        COPY = new Signature("copy", Type.VOID_TYPE, new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
        CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private Class source;
        private Class target;
        private boolean useConverter;

        public Generator() {
            super(SOURCE);
        }

        public void setSource(Class source) {
            if(!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }

            this.source = source;
        }

        public void setTarget(Class target) {
            if(!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }

            this.target = target;
        }

        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.source.getClassLoader();
        }

        public BeanCopier create() {
            Object key = BeanCopier.KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BeanCopier)super.create(key);
        }

        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(this.source);
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, this.getClassName(), BeanCopier.BEAN_COPIER, (Type[])null, "<generated>");
            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(1, BeanCopier.COPY, (Type[])null);
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(this.source);
            PropertyDescriptor[] setters = ReflectUtils.getBeanGetters(this.target);
            Map names = new HashMap();

            for(int i = 0; i < getters.length; ++i) {
                names.put(getters[i].getName(), getters[i]);
            }

            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            if(this.useConverter) {
                e.load_arg(1);
                e.checkcast(targetType);
                e.store_local(targetLocal);
                e.load_arg(0);
                e.checkcast(sourceType);
                e.store_local(sourceLocal);
            } else {
                e.load_arg(1);
                e.checkcast(targetType);
                e.load_arg(0);
                e.checkcast(sourceType);
            }

            for(int i = 0; i < setters.length; ++i) {
                PropertyDescriptor setter = setters[i];
                PropertyDescriptor getter = (PropertyDescriptor)names.get(setter.getName());
                if(getter != null) {
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    if(this.useConverter) {
                        Type setterType = write.getSignature().getArgumentTypes()[0];
                        e.load_local(targetLocal);
                        e.load_arg(2);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(e, setterType);
                        e.push(write.getSignature().getName());
                        e.invoke_interface(BeanCopier.CONVERTER, BeanCopier.CONVERT);
                        e.unbox_or_zero(setterType);
                        e.invoke(write);
                    } else if(compatible(getter, setter)) {
                        e.dup2();
                        e.invoke(read);
                        e.invoke(write);
                    }
                }
            }

            e.return_value();
            e.end_method();
            ce.end_class();
        }

        private static boolean compatible(PropertyDescriptor getter, PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }

        static {
            SOURCE = new Source(BeanCopier.class.getName());
        }
    }

    interface BeanCopierKey {
        Object newInstance(String var1, String var2, boolean var3);
    }
}
