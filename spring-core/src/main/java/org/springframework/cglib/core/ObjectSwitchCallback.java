//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import org.springframework.asm.Label;

public interface ObjectSwitchCallback {
    void processCase(Object var1, Label var2) throws Exception;

    void processDefault() throws Exception;
}
