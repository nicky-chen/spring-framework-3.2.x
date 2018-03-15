//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform.impl;

public interface FieldProvider {
    String[] getFieldNames();

    Class[] getFieldTypes();

    void setField(int var1, Object var2);

    Object getField(int var1);

    void setField(String var1, Object var2);

    Object getField(String var1);
}
