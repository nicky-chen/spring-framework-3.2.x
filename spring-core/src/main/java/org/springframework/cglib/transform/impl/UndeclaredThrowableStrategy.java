//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform.impl;

import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.TypeUtils;
import org.springframework.cglib.transform.ClassTransformer;
import org.springframework.cglib.transform.MethodFilter;
import org.springframework.cglib.transform.MethodFilterTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;

public class UndeclaredThrowableStrategy extends DefaultGeneratorStrategy {
    private ClassTransformer t;
    private static final MethodFilter TRANSFORM_FILTER = new MethodFilter() {
        public boolean accept(int access, String name, String desc, String signature, String[] exceptions) {
            return !TypeUtils.isPrivate(access) && name.indexOf(36) < 0;
        }
    };

    public UndeclaredThrowableStrategy(Class wrapper) {
        this.t = new UndeclaredThrowableTransformer(wrapper);
        this.t = new MethodFilterTransformer(TRANSFORM_FILTER, this.t);
    }

    protected ClassGenerator transform(ClassGenerator cg) throws Exception {
        return new TransformingClassGenerator(cg, this.t);
    }
}
