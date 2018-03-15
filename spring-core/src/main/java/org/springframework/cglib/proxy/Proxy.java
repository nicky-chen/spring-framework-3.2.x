//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.springframework.cglib.core.CodeGenerationException;

public class Proxy implements Serializable {
    protected InvocationHandler h;
    private static final CallbackFilter BAD_OBJECT_METHOD_FILTER = new CallbackFilter() {
        public int accept(Method method) {
            if(method.getDeclaringClass().getName().equals("java.lang.Object")) {
                String name = method.getName();
                if(!name.equals("hashCode") && !name.equals("equals") && !name.equals("toString")) {
                    return 1;
                }
            }

            return 0;
        }
    };

    protected Proxy(InvocationHandler h) {
        Enhancer.registerCallbacks(this.getClass(), new Callback[]{h, null});
        this.h = h;
    }

    public static InvocationHandler getInvocationHandler(Object proxy) {
        if(!(proxy instanceof Proxy.ProxyImpl)) {
            throw new IllegalArgumentException("Object is not a proxy");
        } else {
            return ((Proxy)proxy).h;
        }
    }

    public static Class getProxyClass(ClassLoader loader, Class[] interfaces) {
        Enhancer e = new Enhancer();
        e.setSuperclass(Proxy.ProxyImpl.class);
        e.setInterfaces(interfaces);
        e.setCallbackTypes(new Class[]{InvocationHandler.class, NoOp.class});
        e.setCallbackFilter(BAD_OBJECT_METHOD_FILTER);
        e.setUseFactory(false);
        return e.createClass();
    }

    public static boolean isProxyClass(Class cl) {
        return cl.getSuperclass().equals(Proxy.ProxyImpl.class);
    }

    public static Object newProxyInstance(ClassLoader loader, Class[] interfaces, InvocationHandler h) {
        try {
            Class clazz = getProxyClass(loader, interfaces);
            return clazz.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{h});
        } catch (RuntimeException var4) {
            throw var4;
        } catch (Exception var5) {
            throw new CodeGenerationException(var5);
        }
    }

    private static class ProxyImpl extends Proxy {
        protected ProxyImpl(InvocationHandler h) {
            super(h);
        }
    }
}
