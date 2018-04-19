/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.engine.gregor.mutators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;

import static java.util.Arrays.asList;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.DUP2_X1;
import static org.objectweb.asm.Opcodes.DUP2_X2;

public enum m1 implements MethodMutatorFactory {

    M1;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new m1Visitor(this, context, methodVisitor);
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName();
    }

    @Override
    public String getName() {
        return name();
    }

}

class m1Visitor extends MethodVisitor {

    private static final String                     DESCRIPTION = "Null check for field derefernce (m1)";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();
    private final MethodMutatorFactory factory;
    private final MutationContext context;

    m1Visitor(final MethodMutatorFactory factory, final MutationContext context, final MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.factory = factory;
        this.context = context;

    }

   // visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf)

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

        if ((opcode == Opcodes.INVOKEVIRTUAL)) {
            Type returnType = Type.getReturnType(desc);
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION + ": " + desc + " : " + returnType );
            if(this.context.shouldMutate(muID)) {

                int args = Type.getArgumentTypes(desc).length;
                for(int x = 0; x< args; x++)
                    super.visitInsn(Opcodes.POP);

               // if(returnType.equals(Type.INT_TYPE)){
                   // super.visitInsn(Opcodes.ICONST_0);
               // }

            } else {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }

        } else{
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

//        if ((opcode == Opcodes.INVOKEVIRTUAL)) {
//            super.visitMethodInsn(opcode, owner, name, desc, itf);
//            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
//            if(this.context.shouldMutate(muID)) {
////                super.visitInsn(Opcodes.POP);
////                super.visitInsn(Opcodes.POP);
//            } else {
//                super.visitMethodInsn(opcode, owner, name, desc, itf);
//            }
//
//        }
    }

    private void replaceMethodCallWithArgumentHavingSameTypeAsReturnValue(
            final Type[] argTypes, final Type returnType, final int opcode) {
        final int indexOfPropagatedArgument = findLastIndexOfArgumentWithSameTypeAsReturnValue(
                argTypes, returnType);
        popArgumentsBeforePropagatedArgument(argTypes, indexOfPropagatedArgument);
        popArgumentsFollowingThePropagated(argTypes, returnType,
                indexOfPropagatedArgument);
        removeThisFromStackIfNotStatic(returnType, opcode);
    }
    private int findLastIndexOfArgumentWithSameTypeAsReturnValue(
            final Type[] argTypes, final Type returnType) {
        return asList(argTypes).lastIndexOf(returnType);
    }

    private void removeThisFromStackIfNotStatic(final Type returnType,
                                                final int opcode) {
        if (isNotStatic(opcode)) {
            swap(this.mv, returnType, Type.getType(Object.class));
            this.mv.visitInsn(POP);
        }
    }

    private static boolean isNotStatic(final int opcode) {
        return INVOKESTATIC != opcode;
    }

    private void popArgumentsBeforePropagatedArgument(final Type[] argTypes,
                                                      final int indexOfPropagatedArgument) {
        final Type[] argumentTypesBeforeNewReturnValue = Arrays.copyOfRange(
                argTypes, indexOfPropagatedArgument + 1, argTypes.length);
        popArguments(argumentTypesBeforeNewReturnValue);
    }

    private void popArguments(final Type[] argumentTypes) {
        for (int i = argumentTypes.length - 1; i >= 0; i--) {
            popArgument(argumentTypes[i]);
        }
    }

    private void popArgumentsFollowingThePropagated(final Type[] argTypes,
                                                    final Type returnType, final int indexOfPropagatedArgument) {
        final Type[] argsFollowing = Arrays.copyOfRange(argTypes, 0,
                indexOfPropagatedArgument);
        for (int j = argsFollowing.length - 1; j >= 0; j--) {
            swap(this.mv, returnType, argsFollowing[j]);
            popArgument(argsFollowing[j]);
        }
    }


    private void popArgument(final Type argumentType) {
        if (argumentType.getSize() != 1) {
            this.mv.visitInsn(POP2);
        } else {
            this.mv.visitInsn(POP);
        }
    }

    // based on: http://stackoverflow.com/a/11359551
    private static void swap(final MethodVisitor mv, final Type stackTop,
                             final Type belowTop) {
        if (stackTop.getSize() == 1) {
            if (belowTop.getSize() == 1) {
                // Top = 1, below = 1
                mv.visitInsn(SWAP);
            } else {
                // Top = 1, below = 2
                mv.visitInsn(DUP_X2);
                mv.visitInsn(POP);
            }
        } else {
            if (belowTop.getSize() == 1) {
                // Top = 2, below = 1
                mv.visitInsn(DUP2_X1);
            } else {
                // Top = 2, below = 2
                mv.visitInsn(DUP2_X2);
            }
            mv.visitInsn(POP2);
        }
    }
}
