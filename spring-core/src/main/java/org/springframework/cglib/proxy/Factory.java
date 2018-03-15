//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.proxy;

public interface Factory {
    Object newInstance(Callback var1);

    Object newInstance(Callback[] var1);

    Object newInstance(Class[] var1, Object[] var2, Callback[] var3);

    Callback getCallback(int var1);

    void setCallback(int var1, Callback var2);

    void setCallbacks(Callback[] var1);

    Callback[] getCallbacks();
}

