//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.proxy;

import java.util.List;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.Signature;

interface CallbackGenerator {
    void generate(ClassEmitter var1, CallbackGenerator.Context var2, List var3) throws Exception;

    void generateStatic(CodeEmitter var1, CallbackGenerator.Context var2, List var3) throws Exception;

    public interface Context {
        ClassLoader getClassLoader();

        CodeEmitter beginMethod(ClassEmitter var1, MethodInfo var2);

        int getOriginalModifiers(MethodInfo var1);

        int getIndex(MethodInfo var1);

        void emitCallback(CodeEmitter var1, int var2);

        Signature getImplSignature(MethodInfo var1);

        void emitInvoke(CodeEmitter var1, MethodInfo var2);
    }
}
