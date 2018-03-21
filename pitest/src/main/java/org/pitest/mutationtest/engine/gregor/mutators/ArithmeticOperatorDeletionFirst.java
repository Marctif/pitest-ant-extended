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
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.AbstractInsnMutator;
import org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;
import org.pitest.mutationtest.engine.gregor.InsnSubstitution;

public enum ArithmeticOperatorDeletionFirst implements MethodMutatorFactory {

    DELETE_ARITHMETIC_FIRST_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new ArithmeticDeleteFirstMethodVisitor(this, methodInfo, context, methodVisitor);
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

class ArithmeticDeleteFirstMethodVisitor extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = "delete arithmetic operator";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    /**
     * +
     * -
     * *
     * /
     * %
     *
     * Note pop removes operand when int or float but pop2 is for double/long
     */
    static {
        //Integers
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.POP, DESCRIPTION));

        //Float
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.POP, DESCRIPTION));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.POP, DESCRIPTION));

        //Double
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));

        //Long
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.POP2, DESCRIPTION));

    }

    ArithmeticDeleteFirstMethodVisitor(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                   final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}
