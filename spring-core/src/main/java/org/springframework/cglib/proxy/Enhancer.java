//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Label;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.DuplicatesPredicate;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.MethodInfoTransformer;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.cglib.core.ObjectSwitchCallback;
import org.springframework.cglib.core.ProcessSwitchCallback;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.RejectModifierPredicate;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.Transformer;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.core.VisibilityPredicate;
import org.springframework.cglib.proxy.CallbackGenerator.Context;

public class Enhancer extends AbstractClassGenerator {
    private static final CallbackFilter ALL_ZERO = new CallbackFilter() {
        public int accept(Method method) {
            return 0;
        }
    };
    private static final Source SOURCE;
    private static final Enhancer.EnhancerKey KEY_FACTORY;
    private static final String BOUND_FIELD = "CGLIB$BOUND";
    private static final String THREAD_CALLBACKS_FIELD = "CGLIB$THREAD_CALLBACKS";
    private static final String STATIC_CALLBACKS_FIELD = "CGLIB$STATIC_CALLBACKS";
    private static final String SET_THREAD_CALLBACKS_NAME = "CGLIB$SET_THREAD_CALLBACKS";
    private static final String SET_STATIC_CALLBACKS_NAME = "CGLIB$SET_STATIC_CALLBACKS";
    private static final String CONSTRUCTED_FIELD = "CGLIB$CONSTRUCTED";
    private static final Type FACTORY;
    private static final Type ILLEGAL_STATE_EXCEPTION;
    private static final Type ILLEGAL_ARGUMENT_EXCEPTION;
    private static final Type THREAD_LOCAL;
    private static final Type CALLBACK;
    private static final Type CALLBACK_ARRAY;
    private static final Signature CSTRUCT_NULL;
    private static final Signature SET_THREAD_CALLBACKS;
    private static final Signature SET_STATIC_CALLBACKS;
    private static final Signature NEW_INSTANCE;
    private static final Signature MULTIARG_NEW_INSTANCE;
    private static final Signature SINGLE_NEW_INSTANCE;
    private static final Signature SET_CALLBACK;
    private static final Signature GET_CALLBACK;
    private static final Signature SET_CALLBACKS;
    private static final Signature GET_CALLBACKS;
    private static final Signature THREAD_LOCAL_GET;
    private static final Signature THREAD_LOCAL_SET;
    private static final Signature BIND_CALLBACKS;
    private Class[] interfaces;
    private CallbackFilter filter;
    private Callback[] callbacks;
    private Type[] callbackTypes;
    private boolean classOnly;
    private Class superclass;
    private Class[] argumentTypes;
    private Object[] arguments;
    private boolean useFactory = true;
    private Long serialVersionUID;
    private boolean interceptDuringConstruction = true;

    public Enhancer() {
        super(SOURCE);
    }

    public void setSuperclass(Class superclass) {
        if(superclass != null && superclass.isInterface()) {
            this.setInterfaces(new Class[]{superclass});
        } else if(superclass != null && superclass.equals(Object.class)) {
            this.superclass = null;
        } else {
            this.superclass = superclass;
        }

    }

    public void setInterfaces(Class[] interfaces) {
        this.interfaces = interfaces;
    }

    public void setCallbackFilter(CallbackFilter filter) {
        this.filter = filter;
    }

    public void setCallback(Callback callback) {
        this.setCallbacks(new Callback[]{callback});
    }

    public void setCallbacks(Callback[] callbacks) {
        if(callbacks != null && callbacks.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        } else {
            this.callbacks = callbacks;
        }
    }

    public void setUseFactory(boolean useFactory) {
        this.useFactory = useFactory;
    }

    public void setInterceptDuringConstruction(boolean interceptDuringConstruction) {
        this.interceptDuringConstruction = interceptDuringConstruction;
    }

    public void setCallbackType(Class callbackType) {
        this.setCallbackTypes(new Class[]{callbackType});
    }

    public void setCallbackTypes(Class[] callbackTypes) {
        if(callbackTypes != null && callbackTypes.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty");
        } else {
            this.callbackTypes = CallbackInfo.determineTypes(callbackTypes);
        }
    }

    public Object create() {
        this.classOnly = false;
        this.argumentTypes = null;
        return this.createHelper();
    }

    public Object create(Class[] argumentTypes, Object[] arguments) {
        this.classOnly = false;
        if(argumentTypes != null && arguments != null && argumentTypes.length == arguments.length) {
            this.argumentTypes = argumentTypes;
            this.arguments = arguments;
            return this.createHelper();
        } else {
            throw new IllegalArgumentException("Arguments must be non-null and of equal length");
        }
    }

    public Class createClass() {
        this.classOnly = true;
        return (Class)this.createHelper();
    }

    public void setSerialVersionUID(Long sUID) {
        this.serialVersionUID = sUID;
    }

    private void validate() {
        if(this.classOnly ^ this.callbacks == null) {
            if(this.classOnly) {
                throw new IllegalStateException("createClass does not accept callbacks");
            } else {
                throw new IllegalStateException("Callbacks are required");
            }
        } else if(this.classOnly && this.callbackTypes == null) {
            throw new IllegalStateException("Callback types are required");
        } else {
            if(this.callbacks != null && this.callbackTypes != null) {
                if(this.callbacks.length != this.callbackTypes.length) {
                    throw new IllegalStateException("Lengths of callback and callback types array must be the same");
                }

                Type[] check = CallbackInfo.determineTypes(this.callbacks);

                for(int i = 0; i < check.length; ++i) {
                    if(!check[i].equals(this.callbackTypes[i])) {
                        throw new IllegalStateException("Callback " + check[i] + " is not assignable to " + this.callbackTypes[i]);
                    }
                }
            } else if(this.callbacks != null) {
                this.callbackTypes = CallbackInfo.determineTypes(this.callbacks);
            }

            if(this.filter == null) {
                if(this.callbackTypes.length > 1) {
                    throw new IllegalStateException("Multiple callback types possible but no filter specified");
                }

                this.filter = ALL_ZERO;
            }

            if(this.interfaces != null) {
                for(int i = 0; i < this.interfaces.length; ++i) {
                    if(this.interfaces[i] == null) {
                        throw new IllegalStateException("Interfaces cannot be null");
                    }

                    if(!this.interfaces[i].isInterface()) {
                        throw new IllegalStateException(this.interfaces[i] + " is not an interface");
                    }
                }
            }

        }
    }

    private Object createHelper() {
        this.validate();
        if(this.superclass != null) {
            this.setNamePrefix(this.superclass.getName());
        } else if(this.interfaces != null) {
            this.setNamePrefix(this.interfaces[ReflectUtils.findPackageProtected(this.interfaces)].getName());
        }

        return super.create(KEY_FACTORY.newInstance(this.superclass != null?this.superclass.getName():null, ReflectUtils.getNames(this.interfaces), this.filter, this.callbackTypes, this.useFactory, this.interceptDuringConstruction, this.serialVersionUID));
    }

    protected ClassLoader getDefaultClassLoader() {
        return this.superclass != null?this.superclass.getClassLoader():(this.interfaces != null?this.interfaces[0].getClassLoader():null);
    }

    private Signature rename(Signature sig, int index) {
        return new Signature("CGLIB$" + sig.getName() + "$" + index, sig.getDescriptor());
    }

    public static void getMethods(Class superclass, Class[] interfaces, List methods) {
        getMethods(superclass, interfaces, methods, (List)null, (Set)null);
    }

    private static void getMethods(Class superclass, Class[] interfaces, List methods, List interfaceMethods, Set forcePublic) {
        ReflectUtils.addAllMethods(superclass, methods);
        List target = interfaceMethods != null?interfaceMethods:methods;
        if(interfaces != null) {
            for(int i = 0; i < interfaces.length; ++i) {
                if(interfaces[i] != Factory.class) {
                    ReflectUtils.addAllMethods(interfaces[i], target);
                }
            }
        }

        if(interfaceMethods != null) {
            if(forcePublic != null) {
                forcePublic.addAll(MethodWrapper.createSet(interfaceMethods));
            }

            methods.addAll(interfaceMethods);
        }

        CollectionUtils.filter(methods, new RejectModifierPredicate(8));
        CollectionUtils.filter(methods, new VisibilityPredicate(superclass, true));
        CollectionUtils.filter(methods, new DuplicatesPredicate());
        CollectionUtils.filter(methods, new RejectModifierPredicate(16));
    }

    public void generateClass(ClassVisitor v) throws Exception {
        Class sc = this.superclass == null?Object.class:this.superclass;
        if(TypeUtils.isFinal(sc.getModifiers())) {
            throw new IllegalArgumentException("Cannot subclass final class " + sc);
        } else {
            List constructors = new ArrayList(Arrays.asList(sc.getDeclaredConstructors()));
            this.filterConstructors(sc, constructors);
            List actualMethods = new ArrayList();
            List interfaceMethods = new ArrayList();
            final Set forcePublic = new HashSet();
            getMethods(sc, this.interfaces, actualMethods, interfaceMethods, forcePublic);
            List methods = CollectionUtils.transform(actualMethods, new Transformer() {
                public Object transform(Object value) {
                    Method method = (Method)value;
                    int modifiers = 16 | method.getModifiers() & -1025 & -257 & -33;
                    if(forcePublic.contains(MethodWrapper.create(method))) {
                        modifiers = modifiers & -5 | 1;
                    }

                    return ReflectUtils.getMethodInfo(method, modifiers);
                }
            });
            ClassEmitter e = new ClassEmitter(v);
            e.begin_class(46, 1, this.getClassName(), Type.getType(sc), this.useFactory?TypeUtils.add(TypeUtils.getTypes(this.interfaces), FACTORY):TypeUtils.getTypes(this.interfaces), "<generated>");
            List constructorInfo = CollectionUtils.transform(constructors, MethodInfoTransformer.getInstance());
            e.declare_field(2, "CGLIB$BOUND", Type.BOOLEAN_TYPE, (Object)null);
            if(!this.interceptDuringConstruction) {
                e.declare_field(2, "CGLIB$CONSTRUCTED", Type.BOOLEAN_TYPE, (Object)null);
            }

            e.declare_field(26, "CGLIB$THREAD_CALLBACKS", THREAD_LOCAL, (Object)null);
            e.declare_field(26, "CGLIB$STATIC_CALLBACKS", CALLBACK_ARRAY, (Object)null);
            if(this.serialVersionUID != null) {
                e.declare_field(26, "serialVersionUID", Type.LONG_TYPE, this.serialVersionUID);
            }

            for(int i = 0; i < this.callbackTypes.length; ++i) {
                e.declare_field(2, getCallbackField(i), this.callbackTypes[i], (Object)null);
            }

            this.emitMethods(e, methods, actualMethods);
            this.emitConstructors(e, constructorInfo);
            this.emitSetThreadCallbacks(e);
            this.emitSetStaticCallbacks(e);
            this.emitBindCallbacks(e);
            if(this.useFactory) {
                int[] keys = this.getCallbackKeys();
                this.emitNewInstanceCallbacks(e);
                this.emitNewInstanceCallback(e);
                this.emitNewInstanceMultiarg(e, constructorInfo);
                this.emitGetCallback(e, keys);
                this.emitSetCallback(e, keys);
                this.emitGetCallbacks(e);
                this.emitSetCallbacks(e);
            }

            e.end_class();
        }
    }

    protected void filterConstructors(Class sc, List constructors) {
        CollectionUtils.filter(constructors, new VisibilityPredicate(sc, true));
        if(constructors.size() == 0) {
            throw new IllegalArgumentException("No visible constructors in " + sc);
        }
    }

    protected Object firstInstance(Class type) throws Exception {
        return this.classOnly?type:this.createUsingReflection(type);
    }

    protected Object nextInstance(Object instance) {
        Class protoclass = instance instanceof Class?(Class)instance:instance.getClass();
        return this.classOnly?protoclass:(instance instanceof Factory?(this.argumentTypes != null?((Factory)instance).newInstance(this.argumentTypes, this.arguments, this.callbacks):((Factory)instance).newInstance(this.callbacks)):this.createUsingReflection(protoclass));
    }

    public static void registerCallbacks(Class generatedClass, Callback[] callbacks) {
        setThreadCallbacks(generatedClass, callbacks);
    }

    public static void registerStaticCallbacks(Class generatedClass, Callback[] callbacks) {
        setCallbacksHelper(generatedClass, callbacks, "CGLIB$SET_STATIC_CALLBACKS");
    }

    public static boolean isEnhanced(Class type) {
        try {
            getCallbacksSetter(type, "CGLIB$SET_THREAD_CALLBACKS");
            return true;
        } catch (NoSuchMethodException var2) {
            return false;
        }
    }

    private static void setThreadCallbacks(Class type, Callback[] callbacks) {
        setCallbacksHelper(type, callbacks, "CGLIB$SET_THREAD_CALLBACKS");
    }

    private static void setCallbacksHelper(Class type, Callback[] callbacks, String methodName) {
        try {
            Method setter = getCallbacksSetter(type, methodName);
            setter.invoke((Object)null, new Object[]{callbacks});
        } catch (NoSuchMethodException var4) {
            throw new IllegalArgumentException(type + " is not an enhanced class");
        } catch (IllegalAccessException var5) {
            throw new CodeGenerationException(var5);
        } catch (InvocationTargetException var6) {
            throw new CodeGenerationException(var6);
        }
    }

    private static Method getCallbacksSetter(Class type, String methodName) throws NoSuchMethodException {
        return type.getDeclaredMethod(methodName, new Class[]{Callback[].class});
    }

    private Object createUsingReflection(Class type) {
        setThreadCallbacks(type, this.callbacks);

        Object var2;
        try {
            if(this.argumentTypes == null) {
                var2 = ReflectUtils.newInstance(type);
                return var2;
            }

            var2 = ReflectUtils.newInstance(type, this.argumentTypes, this.arguments);
        } finally {
            setThreadCallbacks(type, (Callback[])null);
        }

        return var2;
    }

    public static Object create(Class type, Callback callback) {
        Enhancer e = new Enhancer();
        e.setSuperclass(type);
        e.setCallback(callback);
        return e.create();
    }

    public static Object create(Class superclass, Class[] interfaces, Callback callback) {
        Enhancer e = new Enhancer();
        e.setSuperclass(superclass);
        e.setInterfaces(interfaces);
        e.setCallback(callback);
        return e.create();
    }

    public static Object create(Class superclass, Class[] interfaces, CallbackFilter filter, Callback[] callbacks) {
        Enhancer e = new Enhancer();
        e.setSuperclass(superclass);
        e.setInterfaces(interfaces);
        e.setCallbackFilter(filter);
        e.setCallbacks(callbacks);
        return e.create();
    }

    private void emitConstructors(ClassEmitter ce, List constructors) {
        boolean seenNull = false;
        Iterator it = constructors.iterator();

        while(it.hasNext()) {
            MethodInfo constructor = (MethodInfo)it.next();
            CodeEmitter e = EmitUtils.begin_method(ce, constructor, 1);
            e.load_this();
            e.dup();
            e.load_args();
            Signature sig = constructor.getSignature();
            seenNull = seenNull || sig.getDescriptor().equals("()V");
            e.super_invoke_constructor(sig);
            e.invoke_static_this(BIND_CALLBACKS);
            if(!this.interceptDuringConstruction) {
                e.load_this();
                e.push(1);
                e.putfield("CGLIB$CONSTRUCTED");
            }

            e.return_value();
            e.end_method();
        }

        if(!this.classOnly && !seenNull && this.arguments == null) {
            throw new IllegalArgumentException("Superclass has no null constructors but no arguments were given");
        }
    }

    private int[] getCallbackKeys() {
        int[] keys = new int[this.callbackTypes.length];

        for(int i = 0; i < this.callbackTypes.length; keys[i] = i++) {
            ;
        }

        return keys;
    }

    private void emitGetCallback(ClassEmitter ce, int[] keys) {
        final CodeEmitter e = ce.begin_method(1, GET_CALLBACK, (Type[])null);
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.load_arg(0);
        e.process_switch(keys, new ProcessSwitchCallback() {
            public void processCase(int key, Label end) {
                e.getfield(Enhancer.getCallbackField(key));
                e.goTo(end);
            }

            public void processDefault() {
                e.pop();
                e.aconst_null();
            }
        });
        e.return_value();
        e.end_method();
    }

    private void emitSetCallback(ClassEmitter ce, int[] keys) {
        final CodeEmitter e = ce.begin_method(1, SET_CALLBACK, (Type[])null);
        e.load_arg(0);
        e.process_switch(keys, new ProcessSwitchCallback() {
            public void processCase(int key, Label end) {
                e.load_this();
                e.load_arg(1);
                e.checkcast(Enhancer.this.callbackTypes[key]);
                e.putfield(Enhancer.getCallbackField(key));
                e.goTo(end);
            }

            public void processDefault() {
            }
        });
        e.return_value();
        e.end_method();
    }

    private void emitSetCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, SET_CALLBACKS, (Type[])null);
        e.load_this();
        e.load_arg(0);

        for(int i = 0; i < this.callbackTypes.length; ++i) {
            e.dup2();
            e.aaload(i);
            e.checkcast(this.callbackTypes[i]);
            e.putfield(getCallbackField(i));
        }

        e.return_value();
        e.end_method();
    }

    private void emitGetCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, GET_CALLBACKS, (Type[])null);
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.push(this.callbackTypes.length);
        e.newarray(CALLBACK);

        for(int i = 0; i < this.callbackTypes.length; ++i) {
            e.dup();
            e.push(i);
            e.load_this();
            e.getfield(getCallbackField(i));
            e.aastore();
        }

        e.return_value();
        e.end_method();
    }

    private void emitNewInstanceCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, NEW_INSTANCE, (Type[])null);
        e.load_arg(0);
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        this.emitCommonNewInstance(e);
    }

    private void emitCommonNewInstance(CodeEmitter e) {
        e.new_instance_this();
        e.dup();
        e.invoke_constructor_this();
        e.aconst_null();
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.return_value();
        e.end_method();
    }

    private void emitNewInstanceCallback(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(1, SINGLE_NEW_INSTANCE, (Type[])null);
        switch(this.callbackTypes.length) {
            case 0:
                break;
            case 1:
                e.push(1);
                e.newarray(CALLBACK);
                e.dup();
                e.push(0);
                e.load_arg(0);
                e.aastore();
                e.invoke_static_this(SET_THREAD_CALLBACKS);
                break;
            default:
                e.throw_exception(ILLEGAL_STATE_EXCEPTION, "More than one callback object required");
        }

        this.emitCommonNewInstance(e);
    }

    private void emitNewInstanceMultiarg(ClassEmitter ce, List constructors) {
        final CodeEmitter e = ce.begin_method(1, MULTIARG_NEW_INSTANCE, (Type[])null);
        e.load_arg(2);
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.new_instance_this();
        e.dup();
        e.load_arg(0);
        EmitUtils.constructor_switch(e, constructors, new ObjectSwitchCallback() {
            public void processCase(Object key, Label end) {
                MethodInfo constructor = (MethodInfo)key;
                Type[] types = constructor.getSignature().getArgumentTypes();

                for(int i = 0; i < types.length; ++i) {
                    e.load_arg(1);
                    e.push(i);
                    e.aaload();
                    e.unbox(types[i]);
                }

                e.invoke_constructor_this(constructor.getSignature());
                e.goTo(end);
            }

            public void processDefault() {
                e.throw_exception(Enhancer.ILLEGAL_ARGUMENT_EXCEPTION, "Constructor not found");
            }
        });
        e.aconst_null();
        e.invoke_static_this(SET_THREAD_CALLBACKS);
        e.return_value();
        e.end_method();
    }

    private void emitMethods(ClassEmitter ce, List methods, List actualMethods) {
        CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
        Map groups = new HashMap();
        final Map indexes = new HashMap();
        final Map originalModifiers = new HashMap();
        final Map positions = CollectionUtils.getIndexMap(methods);
        Map declToBridge = new HashMap();
        Iterator it1 = methods.iterator();
        Iterator it2 = actualMethods != null?actualMethods.iterator():null;

        while(it1.hasNext()) {
            MethodInfo method = (MethodInfo)it1.next();
            Method actualMethod = it2 != null?(Method)it2.next():null;
            int index = this.filter.accept(actualMethod);
            if(index >= this.callbackTypes.length) {
                throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
            }

            originalModifiers.put(method, new Integer(actualMethod != null?actualMethod.getModifiers():method.getModifiers()));
            indexes.put(method, new Integer(index));
            List group = (List)groups.get(generators[index]);
            if(group == null) {
                groups.put(generators[index], group = new ArrayList(methods.size()));
            }

            ((List)group).add(method);
            if(TypeUtils.isBridge(actualMethod.getModifiers())) {
                Set bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
                if(bridges == null) {
                    bridges = new HashSet();
                    declToBridge.put(actualMethod.getDeclaringClass(), bridges);
                }

                ((Set)bridges).add(method.getSignature());
            }
        }

        final Map bridgeToTarget = (new BridgeMethodResolver(declToBridge)).resolveAll();
        Set seenGen = new HashSet();
        CodeEmitter se = ce.getStaticHook();
        se.new_instance(THREAD_LOCAL);
        se.dup();
        se.invoke_constructor(THREAD_LOCAL, CSTRUCT_NULL);
        se.putfield("CGLIB$THREAD_CALLBACKS");
        Object[] state = new Object[1];
        Context context = new Context() {
            public ClassLoader getClassLoader() {
                return Enhancer.this.getClassLoader();
            }

            public int getOriginalModifiers(MethodInfo method) {
                return ((Integer)originalModifiers.get(method)).intValue();
            }

            public int getIndex(MethodInfo method) {
                return ((Integer)indexes.get(method)).intValue();
            }

            public void emitCallback(CodeEmitter e, int index) {
                Enhancer.this.emitCurrentCallback(e, index);
            }

            public Signature getImplSignature(MethodInfo method) {
                return Enhancer.this.rename(method.getSignature(), ((Integer)positions.get(method)).intValue());
            }

            public void emitInvoke(CodeEmitter e, MethodInfo method) {
                Signature bridgeTarget = (Signature)bridgeToTarget.get(method.getSignature());
                if(bridgeTarget != null) {
                    e.invoke_virtual_this(bridgeTarget);
                    Type retType = method.getSignature().getReturnType();
                    if(!retType.equals(bridgeTarget.getReturnType())) {
                        e.checkcast(retType);
                    }
                } else {
                    e.super_invoke(method.getSignature());
                }

            }

            public CodeEmitter beginMethod(ClassEmitter ce, MethodInfo method) {
                CodeEmitter e = EmitUtils.begin_method(ce, method);
                if(!Enhancer.this.interceptDuringConstruction && !TypeUtils.isAbstract(method.getModifiers())) {
                    Label constructed = e.make_label();
                    e.load_this();
                    e.getfield("CGLIB$CONSTRUCTED");
                    e.if_jump(154, constructed);
                    e.load_this();
                    e.load_args();
                    e.super_invoke();
                    e.return_value();
                    e.mark(constructed);
                }

                return e;
            }
        };

        for(int i = 0; i < this.callbackTypes.length; ++i) {
            CallbackGenerator gen = generators[i];
            if(!seenGen.contains(gen)) {
                seenGen.add(gen);
                List fmethods = (List)groups.get(gen);
                if(fmethods != null) {
                    try {
                        gen.generate(ce, context, fmethods);
                        gen.generateStatic(se, context, fmethods);
                    } catch (RuntimeException var21) {
                        throw var21;
                    } catch (Exception var22) {
                        throw new CodeGenerationException(var22);
                    }
                }
            }
        }

        se.return_value();
        se.end_method();
    }

    private void emitSetThreadCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(9, SET_THREAD_CALLBACKS, (Type[])null);
        e.getfield("CGLIB$THREAD_CALLBACKS");
        e.load_arg(0);
        e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_SET);
        e.return_value();
        e.end_method();
    }

    private void emitSetStaticCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(9, SET_STATIC_CALLBACKS, (Type[])null);
        e.load_arg(0);
        e.putfield("CGLIB$STATIC_CALLBACKS");
        e.return_value();
        e.end_method();
    }

    private void emitCurrentCallback(CodeEmitter e, int index) {
        e.load_this();
        e.getfield(getCallbackField(index));
        e.dup();
        Label end = e.make_label();
        e.ifnonnull(end);
        e.pop();
        e.load_this();
        e.invoke_static_this(BIND_CALLBACKS);
        e.load_this();
        e.getfield(getCallbackField(index));
        e.mark(end);
    }

    private void emitBindCallbacks(ClassEmitter ce) {
        CodeEmitter e = ce.begin_method(26, BIND_CALLBACKS, (Type[])null);
        Local me = e.make_local();
        e.load_arg(0);
        e.checkcast_this();
        e.store_local(me);
        Label end = e.make_label();
        e.load_local(me);
        e.getfield("CGLIB$BOUND");
        e.if_jump(154, end);
        e.load_local(me);
        e.push(1);
        e.putfield("CGLIB$BOUND");
        e.getfield("CGLIB$THREAD_CALLBACKS");
        e.invoke_virtual(THREAD_LOCAL, THREAD_LOCAL_GET);
        e.dup();
        Label found_callback = e.make_label();
        e.ifnonnull(found_callback);
        e.pop();
        e.getfield("CGLIB$STATIC_CALLBACKS");
        e.dup();
        e.ifnonnull(found_callback);
        e.pop();
        e.goTo(end);
        e.mark(found_callback);
        e.checkcast(CALLBACK_ARRAY);
        e.load_local(me);
        e.swap();

        for(int i = this.callbackTypes.length - 1; i >= 0; --i) {
            if(i != 0) {
                e.dup2();
            }

            e.aaload(i);
            e.checkcast(this.callbackTypes[i]);
            e.putfield(getCallbackField(i));
        }

        e.mark(end);
        e.return_value();
        e.end_method();
    }

    private static String getCallbackField(int index) {
        return "CGLIB$CALLBACK_" + index;
    }

    static {
        SOURCE = new Source(Enhancer.class.getName());
        KEY_FACTORY = (Enhancer.EnhancerKey)KeyFactory.create(Enhancer.EnhancerKey.class);
        FACTORY = TypeUtils.parseType("org.springframework.cglib.proxy.Factory");
        ILLEGAL_STATE_EXCEPTION = TypeUtils.parseType("IllegalStateException");
        ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
        THREAD_LOCAL = TypeUtils.parseType("ThreadLocal");
        CALLBACK = TypeUtils.parseType("org.springframework.cglib.proxy.Callback");
        CALLBACK_ARRAY = Type.getType(Callback[].class);
        CSTRUCT_NULL = TypeUtils.parseConstructor("");
        SET_THREAD_CALLBACKS = new Signature("CGLIB$SET_THREAD_CALLBACKS", Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        SET_STATIC_CALLBACKS = new Signature("CGLIB$SET_STATIC_CALLBACKS", Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{CALLBACK_ARRAY});
        MULTIARG_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{Constants.TYPE_CLASS_ARRAY, Constants.TYPE_OBJECT_ARRAY, CALLBACK_ARRAY});
        SINGLE_NEW_INSTANCE = new Signature("newInstance", Constants.TYPE_OBJECT, new Type[]{CALLBACK});
        SET_CALLBACK = new Signature("setCallback", Type.VOID_TYPE, new Type[]{Type.INT_TYPE, CALLBACK});
        GET_CALLBACK = new Signature("getCallback", CALLBACK, new Type[]{Type.INT_TYPE});
        SET_CALLBACKS = new Signature("setCallbacks", Type.VOID_TYPE, new Type[]{CALLBACK_ARRAY});
        GET_CALLBACKS = new Signature("getCallbacks", CALLBACK_ARRAY, new Type[0]);
        THREAD_LOCAL_GET = TypeUtils.parseSignature("Object get()");
        THREAD_LOCAL_SET = TypeUtils.parseSignature("void set(Object)");
        BIND_CALLBACKS = TypeUtils.parseSignature("void CGLIB$BIND_CALLBACKS(Object)");
    }

    public interface EnhancerKey {
        Object newInstance(String var1, String[] var2, CallbackFilter var3, Type[] var4, boolean var5, boolean var6, Long var7);
    }
}
