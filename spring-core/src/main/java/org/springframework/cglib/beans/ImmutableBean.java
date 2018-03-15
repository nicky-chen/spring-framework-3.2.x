//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;

public class ImmutableBean {
    private static final Type ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
    private static final Signature CSTRUCT_OBJECT = TypeUtils.parseConstructor("Object");
    private static final Class[] OBJECT_CLASSES;
    private static final String FIELD_NAME = "CGLIB$RWBean";

    private ImmutableBean() {
    }

    public static Object create(Object bean) {
        ImmutableBean.Generator gen = new ImmutableBean.Generator();
        gen.setBean(bean);
        return gen.create();
    }

    static {
        OBJECT_CLASSES = new Class[]{Object.class};
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private Object bean;
        private Class target;

        public Generator() {
            super(SOURCE);
        }

        public void setBean(Object bean) {
            this.bean = bean;
            this.target = bean.getClass();
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }

        public Object create() {
            String name = this.target.getName();
            this.setNamePrefix(name);
            return super.create(name);
        }

        public void generateClass(ClassVisitor v) {
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(46, 1, this.getClassName(), targetType, (Type[])null, "<generated>");
            ce.declare_field(18, "CGLIB$RWBean", targetType, (Object)null);
            CodeEmitter e = ce.begin_method(1, ImmutableBean.CSTRUCT_OBJECT, (Type[])null);
            e.load_this();
            e.super_invoke_constructor();
            e.load_this();
            e.load_arg(0);
            e.checkcast(targetType);
            e.putfield("CGLIB$RWBean");
            e.return_value();
            e.end_method();
            PropertyDescriptor[] descriptors = ReflectUtils.getBeanProperties(this.target);
            Method[] getters = ReflectUtils.getPropertyMethods(descriptors, true, false);
            Method[] setters = ReflectUtils.getPropertyMethods(descriptors, false, true);

            int i;
            MethodInfo setter;
            for(i = 0; i < getters.length; ++i) {
                setter = ReflectUtils.getMethodInfo(getters[i]);
                e = EmitUtils.begin_method(ce, setter, 1);
                e.load_this();
                e.getfield("CGLIB$RWBean");
                e.invoke(setter);
                e.return_value();
                e.end_method();
            }

            for(i = 0; i < setters.length; ++i) {
                setter = ReflectUtils.getMethodInfo(setters[i]);
                e = EmitUtils.begin_method(ce, setter, 1);
                e.throw_exception(ImmutableBean.ILLEGAL_STATE_EXCEPTION, "Bean is immutable");
                e.end_method();
            }

            ce.end_class();
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type, ImmutableBean.OBJECT_CLASSES, new Object[]{this.bean});
        }

        protected Object nextInstance(Object instance) {
            return this.firstInstance(instance.getClass());
        }

        static {
            SOURCE = new Source(ImmutableBean.class.getName());
        }
    }
}
