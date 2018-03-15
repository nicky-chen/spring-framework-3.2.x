//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.util.ArrayList;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;

public class ClassNameReader {
    private static final ClassNameReader.EarlyExitException EARLY_EXIT = new ClassNameReader.EarlyExitException();

    private ClassNameReader() {
    }

    public static String getClassName(ClassReader r) {
        return getClassInfo(r)[0];
    }

    public static String[] getClassInfo(ClassReader r) {
        final ArrayList array = new ArrayList();

        try {
            r.accept(new ClassVisitor(262144, (ClassVisitor)null) {
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    array.add(name.replace('/', '.'));
                    if(superName != null) {
                        array.add(superName.replace('/', '.'));
                    }

                    for(int i = 0; i < interfaces.length; ++i) {
                        array.add(interfaces[i].replace('/', '.'));
                    }

                    throw ClassNameReader.EARLY_EXIT;
                }
            }, 6);
        } catch (ClassNameReader.EarlyExitException var3) {
            ;
        }

        return (String[])array.toArray(new String[0]);
    }

    private static class EarlyExitException extends RuntimeException {
        private EarlyExitException() {
        }
    }
}
