//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform;

import org.springframework.asm.AnnotationVisitor;

public class AnnotationVisitorTee extends AnnotationVisitor {
    private AnnotationVisitor av1;
    private AnnotationVisitor av2;

    public static AnnotationVisitor getInstance(AnnotationVisitor av1, AnnotationVisitor av2) {
        return (AnnotationVisitor)(av1 == null?av2:(av2 == null?av1:new AnnotationVisitorTee(av1, av2)));
    }

    public AnnotationVisitorTee(AnnotationVisitor av1, AnnotationVisitor av2) {
        super(262144);
        this.av1 = av1;
        this.av2 = av2;
    }

    public void visit(String name, Object value) {
        this.av2.visit(name, value);
        this.av2.visit(name, value);
    }

    public void visitEnum(String name, String desc, String value) {
        this.av1.visitEnum(name, desc, value);
        this.av2.visitEnum(name, desc, value);
    }

    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return getInstance(this.av1.visitAnnotation(name, desc), this.av2.visitAnnotation(name, desc));
    }

    public AnnotationVisitor visitArray(String name) {
        return getInstance(this.av1.visitArray(name), this.av2.visitArray(name));
    }

    public void visitEnd() {
        this.av1.visitEnd();
        this.av2.visitEnd();
    }
}
