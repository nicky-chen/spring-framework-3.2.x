//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.util.TraceClassVisitor;

public class DebuggingClassWriter extends ClassVisitor {
    public static final String DEBUG_LOCATION_PROPERTY = "cglib.debugLocation";
    private static String debugLocation = System.getProperty("cglib.debugLocation");
    private static boolean traceEnabled;
    private String className;
    private String superName;

    public DebuggingClassWriter(int flags) {
        super(flags, new ClassWriter(flags));
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.');
        this.superName = superName.replace('/', '.');
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public String getClassName() {
        return this.className;
    }

    public String getSuperName() {
        return this.superName;
    }

    public byte[] toByteArray() {
        return (byte[])AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                byte[] b = ((ClassWriter) DebuggingClassWriter.super.cv).toByteArray();
                if(DebuggingClassWriter.debugLocation != null) {
                    String dirs = DebuggingClassWriter.this.className.replace('.', File.separatorChar);

                    try {
                        (new File(DebuggingClassWriter.debugLocation + File.separatorChar + dirs)).getParentFile().mkdirs();
                        File file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".class");
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                        try {
                            out.write(b);
                        } finally {
                            out.close();
                        }

                        if(DebuggingClassWriter.traceEnabled) {
                            file = new File(new File(DebuggingClassWriter.debugLocation), dirs + ".asm");
                            out = new BufferedOutputStream(new FileOutputStream(file));

                            try {
                                ClassReader cr = new ClassReader(b);
                                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
                                TraceClassVisitor tcv = new TraceClassVisitor((ClassVisitor)null, pw);
                                cr.accept(cv, 0);
                                pw.flush();
                            } finally {
                                out.close();
                            }
                        }
                    } catch (IOException var17) {
                        throw new CodeGenerationException(var17);
                    }
                }

                return b;
            }
        });
    }

    static {
        if(debugLocation != null) {
            System.err.println("CGLIB debugging enabled, writing to '" + debugLocation + "'");

            try {
                Class.forName("org.springframework.asm.util.TraceClassVisitor");
                traceEnabled = true;
            } catch (Throwable var1) {
                ;
            }
        }

    }
}
