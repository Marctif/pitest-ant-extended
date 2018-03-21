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

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;

public enum ArithmeticOperatorDeletionFirst implements MethodMutatorFactory {

    DELETE_ARITHMETIC_FIRST_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new ArithmeticDeleteFirstMethodVisitor(this, context, methodVisitor);
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

class ArithmeticDeleteFirstMethodVisitor extends MethodVisitor {

    private static final String                     DESCRIPTION = "delete arithmetic operator (first operand)";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();
    private final MethodMutatorFactory factory;
    private final MutationContext context;

    ArithmeticDeleteFirstMethodVisitor(final MethodMutatorFactory factory, final MutationContext context, final MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.factory = factory;
        this.context = context;

    }

    @Override
    public void visitInsn(int opcode) {

        if ((opcode == Opcodes.IADD) || (opcode == Opcodes.ISUB) || (opcode == Opcodes.IMUL) || (opcode == Opcodes.IDIV) || (opcode == Opcodes.IREM) ||
                (opcode == Opcodes.FADD) || (opcode == Opcodes.FSUB) || (opcode == Opcodes.FMUL) || (opcode == Opcodes.FDIV) || (opcode == Opcodes.FREM)) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if(this.context.shouldMutate(muID)) {
                super.visitInsn(Opcodes.SWAP);
                super.visitInsn(Opcodes.POP);
            } else {
                super.visitInsn(opcode);
            }
        } else if ((opcode == Opcodes.LADD) || (opcode == Opcodes.LSUB) || (opcode == Opcodes.LMUL) || (opcode == Opcodes.LDIV) || (opcode == Opcodes.LREM) ||
                (opcode == Opcodes.DADD) || (opcode == Opcodes.DSUB) || (opcode == Opcodes.DMUL) || (opcode == Opcodes.DDIV) || (opcode == Opcodes.DREM)){
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if(this.context.shouldMutate(muID)) {
                super.visitInsn(Opcodes.DUP2_X2);
                super.visitInsn(Opcodes.POP2);
                super.visitInsn(Opcodes.POP2);
            } else {
                super.visitInsn(opcode);
            }
        } else {
            super.visitInsn(opcode);
        }

    }
}
