//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cglib.transform;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Attribute;
import org.springframework.asm.FieldVisitor;

public class FieldVisitorTee extends FieldVisitor {
    private FieldVisitor fv1;
    private FieldVisitor fv2;

    public FieldVisitorTee(FieldVisitor fv1, FieldVisitor fv2) {
        super(262144);
        this.fv1 = fv1;
        this.fv2 = fv2;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return AnnotationVisitorTee.getInstance(this.fv1.visitAnnotation(desc, visible), this.fv2.visitAnnotation(desc, visible));
    }

    public void visitAttribute(Attribute attr) {
        this.fv1.visitAttribute(attr);
        this.fv2.visitAttribute(attr);
    }

    public void visitEnd() {
        this.fv1.visitEnd();
        this.fv2.visitEnd();
    }
}
