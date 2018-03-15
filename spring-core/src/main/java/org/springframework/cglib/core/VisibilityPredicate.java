//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.springframework.asm.Type;

public class VisibilityPredicate implements Predicate {
    private boolean protectedOk;
    private String pkg;

    public VisibilityPredicate(Class source, boolean protectedOk) {
        this.protectedOk = protectedOk;
        this.pkg = TypeUtils.getPackageName(Type.getType(source));
    }

    public boolean evaluate(Object arg) {
        int mod = arg instanceof Member?((Member)arg).getModifiers():((Integer)arg).intValue();
        return Modifier.isPrivate(mod)?false:(Modifier.isPublic(mod)?true:(Modifier.isProtected(mod)?this.protectedOk:this.pkg.equals(TypeUtils.getPackageName(Type.getType(((Member)arg).getDeclaringClass())))));
    }
}

