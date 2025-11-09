package ru.otus.aop.asm;

import static java.lang.invoke.MethodType.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent {

    private static final Logger log = LoggerFactory.getLogger(Agent.class);
    private static final String STRING_FIELD_DESCRIPTOR = "(Ljava/lang/String;)V";

    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(
                    ClassLoader loader,
                    String className,
                    Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain,
                    byte[] classfileBuffer) {
                if (className.equals("ru/otus/aop/asm/TestLogging")) {
                    return addProxyMethod(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] addProxyMethod(byte[] originalClass) {
        var originalMethodName = "calculation";
        var proxiedMethodName = "calculationProxied";

        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {

            private List<MethodInfo> calculationMethods = new ArrayList<>();

            private static class MethodInfo {
                MethodNode node;
                int access;
                String desc;
                String signature;
                String[] exceptions;
                boolean hasLog;

                MethodInfo(
                        MethodNode node,
                        int access,
                        String desc,
                        String signature,
                        String[] exceptions,
                        boolean hasLog) {
                    this.node = node;
                    this.access = access;
                    this.desc = desc;
                    this.signature = signature;
                    this.exceptions = exceptions;
                    this.hasLog = hasLog;
                }
            }

            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String descriptor, String signature, String[] exceptions) {

                if (!name.equals(originalMethodName)) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }

                MethodNode methodNode = new MethodNode(Opcodes.ASM9);
                boolean[] hasAnnotation = new boolean[1];

                return new MethodVisitor(Opcodes.ASM9, methodNode) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if (desc.equals("Lru/otus/annotation/Log;")) {
                            hasAnnotation[0] = true;
                        }
                        return super.visitAnnotation(desc, visible);
                    }

                    @Override
                    public void visitEnd() {
                        super.visitEnd();
                        calculationMethods.add(new MethodInfo(
                                methodNode, access, descriptor, signature, exceptions, hasAnnotation[0]));
                    }
                };
            }

            @Override
            public void visitEnd() {
                for (MethodInfo info : calculationMethods) {
                    String finalName = info.hasLog ? proxiedMethodName : originalMethodName;
                    MethodVisitor mv =
                            super.visitMethod(info.access, finalName, info.desc, info.signature, info.exceptions);
                    info.node.accept(mv);
                    if (info.hasLog) {
                        handleAnnotationLog(originalMethodName, proxiedMethodName, info.desc);
                    }
                }
                super.visitEnd();
            }

            private void handleAnnotationLog(String originalName, String proxiedName, String descriptor) {
                MethodVisitor mv = super.visitMethod(Opcodes.ACC_PUBLIC, originalName, descriptor, null, null);
                mv.visitCode();

                // System.out.println("executed method: calculation, param: " + param);
                mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
                mv.visitInsn(Opcodes.DUP);
                mv.visitLdcInsn("executed method: " + originalName + ", param: ");
                mv.visitMethodInsn(
                        Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", STRING_FIELD_DESCRIPTOR, false);
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(I)Ljava/lang/StringBuilder;",
                        false);
                mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", STRING_FIELD_DESCRIPTOR, false);

                // вызов proxied метода
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL, "ru/otus/aop/asm/TestLogging", proxiedName, descriptor, false);

                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(0, 0);
                mv.visitEnd();
            }
        };

        cr.accept(cv, 0);
        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            log.error("error:", e);
        }
        return finalClass;
    }
}
