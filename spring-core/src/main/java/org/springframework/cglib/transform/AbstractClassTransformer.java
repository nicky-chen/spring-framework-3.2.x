//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;

public abstract class AbstractClassTransformer extends ClassTransformer {
    protected AbstractClassTransformer() {
        super(262144);
    }

    public void setTarget(ClassVisitor target) {
        this.cv = target;
    }
}

