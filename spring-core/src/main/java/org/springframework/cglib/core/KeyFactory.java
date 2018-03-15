//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.lang.reflect.Method;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator.Source;

public abstract class KeyFactory {
    private static final Signature GET_NAME = TypeUtils.parseSignature("String getName()");
    private static final Signature GET_CLASS = TypeUtils.parseSignature("Class getClass()");
    private static final Signature HASH_CODE = TypeUtils.parseSignature("int hashCode()");
    private static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(Object)");
    private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
    private static final Signature APPEND_STRING = TypeUtils.parseSignature("StringBuffer append(String)");
    private static final Type KEY_FACTORY = TypeUtils.parseType("org.springframework.cglib.core.KeyFactory");
    private static final int[] PRIMES = new int[]{11, 73, 179, 331, 521, 787, 1213, 1823, 2609, 3691, 5189, 7247, 10037, 13931, 19289, 26627, '轋', '씉', 69403, 95401, 131129, 180179, 247501, 340057, 467063, 641371, 880603, 1209107, 1660097, 2279161, 3129011, 4295723, 5897291, 8095873, 11114263, 15257791, 20946017, 28754629, 39474179, 54189869, 74391461, 102123817, 140194277, 192456917, 264202273, 362693231, 497900099, 683510293, 938313161, 1288102441, 1768288259};
    public static final Customizer CLASS_BY_NAME = new Customizer() {
        public void customize(CodeEmitter e, Type type) {
            if(type.equals(Constants.TYPE_CLASS)) {
                e.invoke_virtual(Constants.TYPE_CLASS, KeyFactory.GET_NAME);
            }

        }
    };
    public static final Customizer OBJECT_BY_CLASS = new Customizer() {
        public void customize(CodeEmitter e, Type type) {
            e.invoke_virtual(Constants.TYPE_OBJECT, KeyFactory.GET_CLASS);
        }
    };

    protected KeyFactory() {
    }

    public static KeyFactory create(Class keyInterface) {
        return create(keyInterface, (Customizer)null);
    }

    public static KeyFactory create(Class keyInterface, Customizer customizer) {
        return create(keyInterface.getClassLoader(), keyInterface, customizer);
    }

    public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
        KeyFactory.Generator gen = new KeyFactory.Generator();
        gen.setInterface(keyInterface);
        gen.setCustomizer(customizer);
        gen.setClassLoader(loader);
        return gen.create();
    }

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE;
        private Class keyInterface;
        private Customizer customizer;
        private int constant;
        private int multiplier;

        public Generator() {
            super(SOURCE);
        }

        protected ClassLoader getDefaultClassLoader() {
            return this.keyInterface.getClassLoader();
        }

        public void setCustomizer(Customizer customizer) {
            this.customizer = customizer;
        }

        public void setInterface(Class keyInterface) {
            this.keyInterface = keyInterface;
        }

        public KeyFactory create() {
            this.setNamePrefix(this.keyInterface.getName());
            return (KeyFactory)super.create(this.keyInterface.getName());
        }

        public void setHashConstant(int constant) {
            this.constant = constant;
        }

        public void setHashMultiplier(int multiplier) {
            this.multiplier = multiplier;
        }

        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }

        public void generateClass(ClassVisitor v) {
            ClassEmitter ce = new ClassEmitter(v);
            Method newInstance = ReflectUtils.findNewInstance(this.keyInterface);
            if(!newInstance.getReturnType().equals(Object.class)) {
                throw new IllegalArgumentException("newInstance method must return Object");
            } else {
                Type[] parameterTypes = TypeUtils.getTypes(newInstance.getParameterTypes());
                ce.begin_class(46, 1, this.getClassName(), KeyFactory.KEY_FACTORY, new Type[]{Type.getType(this.keyInterface)}, "<generated>");
                EmitUtils.null_constructor(ce);
                EmitUtils.factory_method(ce, ReflectUtils.getSignature(newInstance));
                int seed = 0;
                CodeEmitter e = ce.begin_method(1, TypeUtils.parseConstructor(parameterTypes), (Type[])null);
                e.load_this();
                e.super_invoke_constructor();
                e.load_this();

                int hc;
                for(hc = 0; hc < parameterTypes.length; ++hc) {
                    seed += parameterTypes[hc].hashCode();
                    ce.declare_field(18, this.getFieldName(hc), parameterTypes[hc], (Object)null);
                    e.dup();
                    e.load_arg(hc);
                    e.putfield(this.getFieldName(hc));
                }

                e.return_value();
                e.end_method();
                e = ce.begin_method(1, KeyFactory.HASH_CODE, (Type[])null);
                hc = this.constant != 0?this.constant:KeyFactory.PRIMES[Math.abs(seed) % KeyFactory.PRIMES.length];
                int hm = this.multiplier != 0?this.multiplier:KeyFactory.PRIMES[Math.abs(seed * 13) % KeyFactory.PRIMES.length];
                e.push(hc);

                for(int i = 0; i < parameterTypes.length; ++i) {
                    e.load_this();
                    e.getfield(this.getFieldName(i));
                    EmitUtils.hash_code(e, parameterTypes[i], hm, this.customizer);
                }

                e.return_value();
                e.end_method();
                e = ce.begin_method(1, KeyFactory.EQUALS, (Type[])null);
                Label fail = e.make_label();
                e.load_arg(0);
                e.instance_of_this();
                e.if_jump(153, fail);

                int i;
                for(i = 0; i < parameterTypes.length; ++i) {
                    e.load_this();
                    e.getfield(this.getFieldName(i));
                    e.load_arg(0);
                    e.checkcast_this();
                    e.getfield(this.getFieldName(i));
                    EmitUtils.not_equals(e, parameterTypes[i], fail, this.customizer);
                }

                e.push(1);
                e.return_value();
                e.mark(fail);
                e.push(0);
                e.return_value();
                e.end_method();
                e = ce.begin_method(1, KeyFactory.TO_STRING, (Type[])null);
                e.new_instance(Constants.TYPE_STRING_BUFFER);
                e.dup();
                e.invoke_constructor(Constants.TYPE_STRING_BUFFER);

                for(i = 0; i < parameterTypes.length; ++i) {
                    if(i > 0) {
                        e.push(", ");
                        e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.APPEND_STRING);
                    }

                    e.load_this();
                    e.getfield(this.getFieldName(i));
                    EmitUtils.append_string(e, parameterTypes[i], EmitUtils.DEFAULT_DELIMITERS, this.customizer);
                }

                e.invoke_virtual(Constants.TYPE_STRING_BUFFER, KeyFactory.TO_STRING);
                e.return_value();
                e.end_method();
                ce.end_class();
            }
        }

        private String getFieldName(int arg) {
            return "FIELD_" + arg;
        }

        static {
            SOURCE = new Source(KeyFactory.class.getName());
        }
    }
}
