package org.pitest.mutationtest.engine.gregor.mutators;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public class IncrementMutator implements MethodMutatorFactory {

    public enum MutantType {

        INCREMENT, DECREMENT, REMOVE, REVERSE;
    }

    private final MutantType mutatorType;

    public IncrementMutator(MutantType mt) {
        this.mutatorType = mt;
    }

    @Override
    public MethodVisitor create(final MutationContext context,
                                final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
        if (this.mutatorType == IncrementMutator.MutantType.INCREMENT) {
            return new AddIncrementMutatorMethodVisitor(this, context, methodVisitor);
        } else if (this.mutatorType == IncrementMutator.MutantType.DECREMENT) {
            return new AddDecrementMutatorMethodVisitor(this, context, methodVisitor);
        } else if (this.mutatorType == IncrementMutator.MutantType.REVERSE) {
            return new ReverseMutatorMethodVisitor(this, context, methodVisitor);
        } else if (this.mutatorType == IncrementMutator.MutantType.REMOVE) {
            return new RemoveMutatorMethodVisitor(this, context, methodVisitor);
        } else {
            return null;
        }
    }

    @Override
    public String getGloballyUniqueId() {
        return this.getClass().getName() + "_" + this.mutatorType.name();
    }

    @Override
    public String getName() {
        return "UOI Mutator - " + this.mutatorType.name();
    }
}

// This operator works exclusively on function local variables, not object variables
class AddIncrementMutatorMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    AddIncrementMutatorMethodVisitor(final MethodMutatorFactory factory,
                                        final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(Opcodes.ASM6, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {

        final MutationIdentifier newId = this.context.registerMutation(this.factory, " Added unary increment "
                + increment + " -> " + (increment + 1) + " to local variable");

        if (this.context.shouldMutate(newId)) {
            this.mv.visitIincInsn(var, increment + 1);
        } else {
            this.mv.visitIincInsn(var, increment);
        }
    }

}

// This operator works exclusively on function local variables, not object variables
class AddDecrementMutatorMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    AddDecrementMutatorMethodVisitor(final MethodMutatorFactory factory,
                                        final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(Opcodes.ASM6, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {

        final MutationIdentifier newId = this.context.registerMutation(this.factory, " Added unary decrement "
                + increment + " -> " + (increment - 1) + " to local variable");

        if (this.context.shouldMutate(newId)) {
            this.mv.visitIincInsn(var, increment - 1);
        } else {
            this.mv.visitIincInsn(var, increment);
        }
    }

}

// This operator works exclusively on function local variables, not object variables
class RemoveMutatorMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    RemoveMutatorMethodVisitor(final MethodMutatorFactory factory,
                                  final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(Opcodes.ASM6, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        final MutationIdentifier newId = this.context.registerMutation(this.factory,
                " Removed unary increment of local variable");

        if (this.context.shouldMutate(newId)) {
            super.mv.visitIincInsn(var, 0);
        } else {
            super.mv.visitIincInsn(var, increment);
        }
    }

}

// This operator works exclusively on function local variables, not object variables
class ReverseMutatorMethodVisitor extends MethodVisitor {

    private final MethodMutatorFactory factory;
    private final MutationContext context;

    ReverseMutatorMethodVisitor(final MethodMutatorFactory factory,
                                   final MutationContext context, final MethodVisitor delegateMethodVisitor) {
        super(Opcodes.ASM6, delegateMethodVisitor);
        this.factory = factory;
        this.context = context;
    }

    @Override
    public void visitIincInsn(final int var, final int increment) {
        final MutationIdentifier newId = this.context.registerMutation(this.factory,
                " Reversed increment of local variable");

        if (this.context.shouldMutate(newId)) {
            this.mv.visitIincInsn(var, -increment);
        } else {
            this.mv.visitIincInsn(var, increment);
        }
    }

}