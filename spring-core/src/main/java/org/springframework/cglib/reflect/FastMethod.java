//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FastMethod extends FastMember {
    FastMethod(FastClass fc, Method method) {
        super(fc, method, helper(fc, method));
    }

    private static int helper(FastClass fc, Method method) {
        int index = fc.getIndex(method.getName(), method.getParameterTypes());
        if(index >= 0) {
            return index;
        } else {
            Class[] types = method.getParameterTypes();
            System.err.println("hash=" + method.getName().hashCode() + " size=" + types.length);

            for(int i = 0; i < types.length; ++i) {
                System.err.println("  types[" + i + "]=" + types[i].getName());
            }

            throw new IllegalArgumentException("Cannot find method " + method);
        }
    }

    public Class getReturnType() {
        return ((Method)this.member).getReturnType();
    }

    public Class[] getParameterTypes() {
        return ((Method)this.member).getParameterTypes();
    }

    public Class[] getExceptionTypes() {
        return ((Method)this.member).getExceptionTypes();
    }

    public Object invoke(Object obj, Object[] args) throws InvocationTargetException {
        return this.fc.invoke(this.index, obj, args);
    }

    public Method getJavaMethod() {
        return (Method)this.member;
    }
}
