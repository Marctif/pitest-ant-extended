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

import org.objectweb.asm.Label;
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

public enum m1field implements MethodMutatorFactory {

    M1;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new m1fieldVisitor(this, context, methodVisitor);
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

class m1fieldVisitor extends MethodVisitor {

    private static final String                     DESCRIPTION = "Null check for field derefernce (m1 method)";
    private static final String                     DESCRIPTION2 = "Null check for field derefernce (m1 field)";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();
    private final MethodMutatorFactory factory;
    private final MutationContext context;
    private Label lab;

    m1fieldVisitor(final MethodMutatorFactory factory, final MutationContext context, final MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor);
        this.factory = factory;
        this.context = context;

    }

    public void visitFieldInsn(int opcode, String owner, String name,
                               String desc) {
        if((opcode == Opcodes.GETFIELD)) {
            final MutationIdentifier muID = this.context.registerMutation(factory, DESCRIPTION2 + ": " + desc + " : ");
            if (this.context.shouldMutate(muID)) {
                final Label label = new Label();
                super.visitInsn(Opcodes.ACONST_NULL);
                //super.visitFieldInsn(opcode, owner, name, desc);
                //super.visitInsn(Opcodes.DUP);
                //super.visitJumpInsn(Opcodes.IFNONNULL, lab);

            } else {
                super.visitFieldInsn(opcode, owner, name, desc);
            }
        }

    }

}
