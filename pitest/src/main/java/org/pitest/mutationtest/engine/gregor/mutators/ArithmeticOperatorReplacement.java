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

public class ArithmeticOperatorReplacement implements MethodMutatorFactory {

    public enum TYPE {
        IADD, ISUB, IMUL, IDIV, IREM, //int
        DADD, DSUB, DMUL, DDIV, DREM, //double
        FADD, FSUB, FMUL, FDIV, FREM, //float
        LADD, LSUB, LMUL, LDIV, LREM, //long
    }

    TYPE type;

    public ArithmeticOperatorReplacement(TYPE type) {
        this.type = type;
    }

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        if(type == TYPE.IADD) {
            return new ArithmeticReplaceMethodVisitorIADD(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.ISUB) {
            return new ArithmeticReplaceMethodVisitorISUB(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.IMUL) {
            return new ArithmeticReplaceMethodVisitorIMUL(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.IDIV) {
            return new ArithmeticReplaceMethodVisitorIDIV(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.IREM) {
            return new ArithmeticReplaceMethodVisitorIREM(this, methodInfo, context, methodVisitor);
        }

        else if(type == TYPE.DADD) {
            return new ArithmeticReplaceMethodVisitorDADD(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.DSUB) {
            return new ArithmeticReplaceMethodVisitorDSUB(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.DMUL) {
            return new ArithmeticReplaceMethodVisitorDMUL(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.DDIV) {
            return new ArithmeticReplaceMethodVisitorDDIV(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.DREM) {
            return new ArithmeticReplaceMethodVisitorDREM(this, methodInfo, context, methodVisitor);
        }

        else if(type == TYPE.FADD) {
            return new ArithmeticReplaceMethodVisitorFADD(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.FSUB) {
            return new ArithmeticReplaceMethodVisitorFSUB(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.FMUL) {
            return new ArithmeticReplaceMethodVisitorFMUL(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.FDIV) {
            return new ArithmeticReplaceMethodVisitorFDIV(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.FREM) {
            return new ArithmeticReplaceMethodVisitorFREM(this, methodInfo, context, methodVisitor);
        }

        else if(type == TYPE.LADD) {
            return new ArithmeticReplaceMethodVisitorLADD(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.LSUB) {
            return new ArithmeticReplaceMethodVisitorLSUB(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.LMUL) {
            return new ArithmeticReplaceMethodVisitorLMUL(this, methodInfo, context, methodVisitor);
        }
        else if(type == TYPE.LDIV) {
            return new ArithmeticReplaceMethodVisitorLDIV(this, methodInfo, context, methodVisitor);
        }
        else {
            return new ArithmeticReplaceMethodVisitorLREM(this, methodInfo, context, methodVisitor);
        }

    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName() + '-' + this.type.name();
    }

    @Override
    public String getName() {
        return "AOR" + this.type.name();
    }

}

class ArithmeticReplaceMethodVisitorIADD extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " int replaced by +";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONSIA   = new HashMap<>();

    static {
        MUTATIONSIA.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IADD, "-" + DESCRIPTION ));
        MUTATIONSIA.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IADD, "*" + DESCRIPTION ));
        MUTATIONSIA.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IADD, "/" + DESCRIPTION ));
        MUTATIONSIA.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IADD, "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorIADD(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                   final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONSIA;
    }

}

class ArithmeticReplaceMethodVisitorISUB extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " int replaced by -";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.ISUB,  "+"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.ISUB,  "*"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.ISUB,  "/"+ DESCRIPTION ));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.ISUB,  "%"+ DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorISUB(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorIMUL extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " int replaced by *";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IMUL, "+" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IMUL, "-" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IMUL, "/" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IMUL, "%" +  DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorIMUL(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorIDIV extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " int replaced by /";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IDIV,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IDIV,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IDIV,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.IDIV,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorIDIV(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorIREM extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " int replaced by %";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.IREM,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.IREM,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.IREM,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.IREM,  "/" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorIREM (final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorDADD extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " double replaced by +";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DADD,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DADD,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DADD,  "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DADD,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorDADD(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorDSUB extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " double replaced by -";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DSUB,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DSUB,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DSUB,  "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DSUB,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorDSUB(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorDMUL extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " double replaced by *";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DMUL,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DMUL,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DMUL,  "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DMUL,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorDMUL(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorDDIV extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " double replaced by /";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DDIV,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DDIV,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DDIV,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.DDIV,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorDDIV(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorDREM extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " double replaced by % ";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.DREM,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.DREM,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.DREM,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.DREM,  "/" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorDREM (final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                        final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorFADD extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " float replaced by +";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FADD, "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FADD, "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FADD, "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FADD, "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorFADD(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorFSUB extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " float replaced by -";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FSUB,"+" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FSUB,"*" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FSUB,"/" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FSUB,"%" +  DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorFSUB(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorFMUL extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " float replaced by *";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FMUL, "+" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FMUL, "-" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FMUL, "/" +  DESCRIPTION ));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FMUL, "%" +  DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorFMUL(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorFDIV extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " float replaced by /";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FDIV, "+" +  DESCRIPTION));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FDIV, "-" +  DESCRIPTION));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FDIV, "*" +  DESCRIPTION));
        MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.FDIV, "%" +  DESCRIPTION));
    }

    ArithmeticReplaceMethodVisitorFDIV(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorFREM extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " float replaced by %";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.FREM, "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.FREM, "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.FREM, "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.FREM, "/" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorFREM (final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                        final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorLADD extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " long replaced by +";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LADD, "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LADD, "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LADD, "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LADD, "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorLADD(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorLSUB extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " long replaced by - ";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LSUB,  "+" + DESCRIPTION));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LSUB,  "*" + DESCRIPTION));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LSUB,  "/" + DESCRIPTION));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LSUB,  "%" + DESCRIPTION));
    }

    ArithmeticReplaceMethodVisitorLSUB(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorLMUL extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " long replaced by *";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LMUL,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LMUL,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LMUL,  "/" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LMUL,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorLMUL(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorLDIV extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " long replaced by / ";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LDIV,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LDIV,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LDIV,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.LDIV,  "%" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorLDIV(final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                       final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}

class ArithmeticReplaceMethodVisitorLREM extends AbstractInsnMutator {

    private static final String                     DESCRIPTION = " long replaced by %";
    private static final Map<Integer, ZeroOperandMutation> MUTATIONS   = new HashMap<>();

    static {
        MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.LREM,  "+" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.LREM,  "-" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.LREM,  "*" + DESCRIPTION ));
        MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.LREM,  "/" + DESCRIPTION ));
    }

    ArithmeticReplaceMethodVisitorLREM (final MethodMutatorFactory factory,  final MethodInfo methodInfo,
                                        final MutationContext context, final MethodVisitor writer) {
        super(factory, methodInfo, context, writer);
    }

    @Override
    protected Map<Integer, ZeroOperandMutation> getMutations() {
        return MUTATIONS;
    }

}




