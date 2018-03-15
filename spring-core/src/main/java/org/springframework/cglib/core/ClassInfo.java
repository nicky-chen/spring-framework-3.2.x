//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import org.springframework.asm.Type;

public abstract class ClassInfo {
    protected ClassInfo() {
    }

    public abstract Type getType();

    public abstract Type getSuperType();

    public abstract Type[] getInterfaces();

    public abstract int getModifiers();

    public boolean equals(Object o) {
        return o == null?false:(!(o instanceof ClassInfo)?false:this.getType().equals(((ClassInfo)o).getType()));
    }

    public int hashCode() {
        return this.getType().hashCode();
    }

    public String toString() {
        return this.getType().getClassName();
    }
}
