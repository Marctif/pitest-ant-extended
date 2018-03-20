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

public enum ArithmeticOperatorReplacement implements MethodMutatorFactory {

    REPLACE_ARITHMETIC_MUTATOR;

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        return new ArithmeticReplaceMethodVisitor(this, methodInfo, context, methodVisitor);
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

class ArithmeticReplaceMethodVisitor extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = "replaced arithmetic operator";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    /**
     * +
     * -
     * *
     * /
     * %
     */
    static {
        //Integers
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.ISUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.ISUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.ISUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.ISUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IADD, DESCRIPTION));

        //Double
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DADD, DESCRIPTION));

        //Float
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FADD, DESCRIPTION));

        //Long
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LADD, DESCRIPTION));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LREM, DESCRIPTION));

        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LSUB, DESCRIPTION));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LMUL, DESCRIPTION));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LDIV, DESCRIPTION));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LADD, DESCRIPTION));

    }

    ArithmeticReplaceMethodVisitor(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                   final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}
