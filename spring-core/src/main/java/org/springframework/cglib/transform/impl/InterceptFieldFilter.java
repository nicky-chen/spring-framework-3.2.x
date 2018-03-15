//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

public interface InterceptFieldFilter {
    boolean acceptRead(Type var1, String var2);

    boolean acceptWrite(Type var1, String var2);
}
