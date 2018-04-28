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

public enum AbsoluteValueMutator implements MethodMutatorFactory {

    ABSOLUTE_VALUE_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new AbsoluteValueMethodVisitor(this, context, methodInfo, methodVisitor);
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

class AbsoluteValueMethodVisitor extends MethodVisitor {

    private static final String                     DESCRIPTION = "Changes a value to the absolute value";
    private final MethodMutatorFactory factory;
    private final MutationContext context;
    private final MethodInfo info;

    AbsoluteValueMethodVisitor(final MethodMutatorFactory factory, final MutationContext context, final MethodInfo info , final MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.factory = factory;
        this.context = context;
        this.info = info;

    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        mv.visitVarInsn(opcode, var);

        if(opcode == Opcodes.ILOAD) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if (this.context.shouldMutate(muID)) {
                mv.visitInsn(Opcodes.INEG);
            }
        }
        if(opcode == Opcodes.FLOAD) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if (this.context.shouldMutate(muID)) {
                mv.visitInsn(Opcodes.FNEG);
            }
        }
        if(opcode == Opcodes.LLOAD) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if (this.context.shouldMutate(muID)) {
                mv.visitInsn(Opcodes.LNEG);
            }
        }
        if(opcode == Opcodes.DLOAD) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION);
            if (this.context.shouldMutate(muID)) {
                mv.visitInsn(Opcodes.DNEG);
            }
        }



    }
}
