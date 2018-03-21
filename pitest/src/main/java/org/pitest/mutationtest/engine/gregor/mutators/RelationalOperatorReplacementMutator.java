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
import org.pitest.mutationtest.engine.gregor.AbstractJumpMutator;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public class RelationalOperatorReplacementMutator implements MethodMutatorFactory {

    public enum TYPE {
        EQ,NE,LE,GE,GT,LT
    }

    TYPE type;

    public RelationalOperatorReplacementMutator(TYPE type) {
        this.type = type;
    }

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        if(type == TYPE.EQ)        {
            return new RelationalMethodVisitorEQ(this, context, methodVisitor);
        }
        else if(type == TYPE.NE)        {
            return new RelationalMethodVisitorNE(this, context, methodVisitor);
        }
        else if(type == TYPE.LE) {
            return new RelationalMethodVisitorLE(this, context, methodVisitor);
        }
        else if(type == TYPE.GE) {
            return new RelationalMethodVisitorGE(this, context, methodVisitor);
        }
        else if(type == TYPE.GT) {
            return new RelationalMethodVisitorGT(this, context, methodVisitor);
        }
        else {
            return new RelationalMethodVisitorLT(this, context, methodVisitor);
        }
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName() + '-' + this.type.name();
    }

    @Override
    public String getName() {
        return "ROR" + this.type.name();
    }

}

class RelationalMethodVisitorEQ extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = " replaced by ==";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFNE, new Substitution(Opcodes.IFEQ,  "!=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLE, new Substitution(Opcodes.IFEQ,  "<=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGE, new Substitution(Opcodes.IFEQ,  ">=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGT, new Substitution(Opcodes.IFEQ,  ">"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLT, new Substitution(Opcodes.IFEQ,  "<"  + DESCRIPTION ));

        MUTATIONS.put(Opcodes.IF_ICMPNE, new Substitution(Opcodes.IF_ICMPEQ, "!= (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLE, new Substitution(Opcodes.IF_ICMPEQ, "<= (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGE, new Substitution(Opcodes.IF_ICMPEQ, ">= (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGT, new Substitution(Opcodes.IF_ICMPEQ, "> (branch)"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLT, new Substitution(Opcodes.IF_ICMPEQ, "< (branch)"  + DESCRIPTION ));
    }

    RelationalMethodVisitorEQ(final MethodMutatorFactory factory,
                            final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}

class RelationalMethodVisitorNE extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = " replaced by !=";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFEQ, new Substitution(Opcodes.IFNE,  "==" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLE, new Substitution(Opcodes.IFNE,  "<=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGE, new Substitution(Opcodes.IFNE,  ">=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGT, new Substitution(Opcodes.IFNE,  ">"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLT, new Substitution(Opcodes.IFNE,  "<"  + DESCRIPTION ));

        MUTATIONS.put(Opcodes.IF_ICMPEQ, new Substitution(Opcodes.IF_ICMPNE,  "== (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPLE, new Substitution(Opcodes.IF_ICMPNE,  "<= (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPGE, new Substitution(Opcodes.IF_ICMPNE,  ">= (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPGT, new Substitution(Opcodes.IF_ICMPNE,  "> (branch)"  + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPLT, new Substitution(Opcodes.IF_ICMPNE,  "< (branch)"  + DESCRIPTION));
    }

    RelationalMethodVisitorNE(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}

class RelationalMethodVisitorGE extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = " replaced by >=";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFEQ, new Substitution(Opcodes.IFGE,  "==" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLE, new Substitution(Opcodes.IFGE,  "<=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFNE, new Substitution(Opcodes.IFGE,  "!=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGT, new Substitution(Opcodes.IFGE,  ">"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLT, new Substitution(Opcodes.IFGE,  "<"  + DESCRIPTION ));

        MUTATIONS.put(Opcodes.IF_ICMPEQ, new Substitution(Opcodes.IF_ICMPGE, "== (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPLE, new Substitution(Opcodes.IF_ICMPGE, "<= (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPNE, new Substitution(Opcodes.IF_ICMPGE, "!= (branch)" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPGT, new Substitution(Opcodes.IF_ICMPGE, "> (branch)"  + DESCRIPTION));
        MUTATIONS.put(Opcodes.IF_ICMPLT, new Substitution(Opcodes.IF_ICMPGE, "< (branch)"  + DESCRIPTION));
    }

    RelationalMethodVisitorGE(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}

class RelationalMethodVisitorLE extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = " replaced by <=";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFEQ, new Substitution(Opcodes.IFLE,  "==" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGE, new Substitution(Opcodes.IFLE,  ">=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFNE, new Substitution(Opcodes.IFLE,  "!=" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGT, new Substitution(Opcodes.IFLE,  ">"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFLT, new Substitution(Opcodes.IFLE,  "<"  + DESCRIPTION ));

        MUTATIONS.put(Opcodes.IF_ICMPEQ, new Substitution(Opcodes.IF_ICMPLE, "== (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGE, new Substitution(Opcodes.IF_ICMPLE, ">= (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPNE, new Substitution(Opcodes.IF_ICMPLE, "!= (branch)" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGT, new Substitution(Opcodes.IF_ICMPLE, "> (branch)"  + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLT, new Substitution(Opcodes.IF_ICMPLE, "< (branch)"  + DESCRIPTION ));
    }

    RelationalMethodVisitorLE(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}

class RelationalMethodVisitorGT extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = " replaced by > ";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFEQ, new Substitution(Opcodes.IFGT,  "==" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFGE, new Substitution(Opcodes.IFGT,  ">=" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFNE, new Substitution(Opcodes.IFGT,  "!=" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFLE, new Substitution(Opcodes.IFGT,  "<=" + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFLT, new Substitution(Opcodes.IFGT,  "<"  + DESCRIPTION));

        MUTATIONS.put(Opcodes.IF_ICMPEQ, new Substitution(Opcodes.IF_ICMPGT,  "== (branch)"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGE, new Substitution(Opcodes.IF_ICMPGT,  ">= (branch)"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPNE, new Substitution(Opcodes.IF_ICMPGT,  "!= (branch)"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLE, new Substitution(Opcodes.IF_ICMPGT,  "<= (branch)"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLT, new Substitution(Opcodes.IF_ICMPGT,  "< (branch)" + DESCRIPTION ));
    }

    RelationalMethodVisitorGT(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}

class RelationalMethodVisitorLT extends AbstractJumpMutator {

    private static final String                     DESCRIPTION = "replaced by <";
    private static final Map<Integer, Substitution> MUTATIONS   = new HashMap<>();

    static {

        MUTATIONS.put(Opcodes.IFEQ, new Substitution(Opcodes.IFLT,  "== " + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFGE, new Substitution(Opcodes.IFLT,  ">= " + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFNE, new Substitution(Opcodes.IFLT,  "!="  + DESCRIPTION));
        MUTATIONS.put(Opcodes.IFLE, new Substitution(Opcodes.IFLT,  "<= " + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IFGT, new Substitution(Opcodes.IFLT,  "> "  + DESCRIPTION));

        MUTATIONS.put(Opcodes.IF_ICMPEQ, new Substitution(Opcodes.IF_ICMPLT,  "== (branch) " + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGE, new Substitution(Opcodes.IF_ICMPLT,  ">= (branch) " + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPNE, new Substitution(Opcodes.IF_ICMPLT,  "!= (branch) " + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPLE, new Substitution(Opcodes.IF_ICMPLT,  "<= (branch) " + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IF_ICMPGT, new Substitution(Opcodes.IF_ICMPLT,  "> (branch) " + DESCRIPTION ));
    }

    RelationalMethodVisitorLT(final MethodMutatorFactory factory,
                              final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(factory, context, delegateMethodVisitor);
    }

    @Override
    protected Map<Integer, Substitution> getMutations() {
        return MUTATIONS;
    }

}