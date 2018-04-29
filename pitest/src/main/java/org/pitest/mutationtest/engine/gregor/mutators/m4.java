package org.pitest.mutationtest.engine.gregor.mutators;

//imports
import java.util.Random;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.*;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public enum m4 implements MethodMutatorFactory{
    M4;

    @Override
    public MethodVisitor create(final MutationContext context, final MethodInfo methodInfo,
                                final MethodVisitor methodVisitor){
        return new m4MethodVisitor(this, context, methodVisitor);
    }

    @Override
    public String getName(){
        return name();
    }

    @Override
    public String getGloballyUniqueId(){
        return this.getClass().getName();
    }

}
class m4MethodVisitor extends MethodVisitor{
    private final MethodMutatorFactory methodMutatorFactory;
    private final MutationContext context;
    ArrayList<Integer> variableIndex = new ArrayList<>();
    ArrayList<String> variableType = new ArrayList<>();
    int i;

    m4MethodVisitor(final MethodMutatorFactory methodMutatorFactory, final MutationContext context, final MethodVisitor delegateMethodVisitor){
        super(Opcodes.ASM6, delegateMethodVisitor);
        this.methodMutatorFactory = methodMutatorFactory;
        this.context = context;

    }

    @Override
    public void visitVarInsn(final int opcode, final int variable){
        if (opcode == Opcodes.ISTORE || opcode == Opcodes.FSTORE ||
                opcode == Opcodes.DSTORE || opcode == Opcodes.LSTORE || opcode == Opcodes.ASTORE){
            variableIndex.add(variable);
           // if (!(variableIndex.contains(variable))){
                if (opcode == Opcodes.ASTORE)
                    variableType.add("Object");
                else if(opcode == Opcodes.ISTORE)
                    variableType.add("Integer");
                else if(opcode == Opcodes.LSTORE)
                    variableType.add("Long");
                else if(opcode == Opcodes.DSTORE)
                    variableType.add("Double");
                else if(opcode == Opcodes.FSTORE)
                    variableType.add("Float");
           // }
           // else {
//                if(variableType.isEmpty()){
//                    variableType.add("Object");
//                }

               // variableType.(variableIndex.indexOf(variable), variable);
                if (opcode == Opcodes.ASTORE)
                    variableType.add(variableIndex.indexOf(variable), "Object");
                else if(opcode == Opcodes.ISTORE)
                    variableType.add(variableIndex.indexOf(variable), "Integer");
                else if(opcode == Opcodes.LSTORE)
                    variableType.add(variableIndex.indexOf(variable), "Long");
                else if(opcode == Opcodes.DSTORE)
                    variableType.add(variableIndex.indexOf(variable), "Double");
                else if(opcode == Opcodes.FSTORE)
                    variableType.add(variableIndex.indexOf(variable), "Float");
         //   }
            super.visitVarInsn(opcode, variable);
        }

        //Check if variable is loaded and a replacement is ready for use
        else if(opcode == Opcodes.ILOAD || opcode == Opcodes.FLOAD ||
                opcode == Opcodes.DLOAD || opcode == Opcodes.LLOAD || opcode == Opcodes.ALOAD){
            if(variableIndex.contains(variable)){
                int index = variableIndex.indexOf(variable);
                Random rand = new Random();
                i = rand.nextInt(variableIndex.size());

                int counter = 0;
                for(int n = 0; n < variableType.size(); n++) {
                    if(variableType.get(i).equals(variableType.get(index))){
                        counter ++;
                    }
                }

                if(counter > 1){
                    do {
                        i = rand.nextInt(variableIndex.size());
                    } while (i == index && !(variableType.get(i).equals(variableType.get(index))));
                }
                final MutationIdentifier newIdentifier = this.context.registerMutation(this.methodMutatorFactory,
                        "Replaced " + variableType.get(index) + " with " + variableType.get(i) + " M4");
                if (this.context.shouldMutate(newIdentifier)) {
                    super.visitVarInsn(opcode, variableIndex.get(i));
                }
                else super.visitVarInsn(opcode, variable);
            }
            else super.visitVarInsn(opcode, variable);
        }

    }
}
