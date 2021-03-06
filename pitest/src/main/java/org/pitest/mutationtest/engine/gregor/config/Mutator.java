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
package org.pitest.mutationtest.engine.gregor.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.help.Help;
import org.pitest.help.PitHelpError;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.mutators.*;
import org.pitest.mutationtest.engine.gregor.mutators.RemoveConditionalMutator.Choice;
import org.pitest.mutationtest.engine.gregor.mutators.experimental.NakedReceiverMutator;
import org.pitest.mutationtest.engine.gregor.mutators.experimental.RemoveIncrementsMutator;
import org.pitest.mutationtest.engine.gregor.mutators.experimental.RemoveSwitchMutator;
import org.pitest.mutationtest.engine.gregor.mutators.experimental.SwitchMutator;

import org.pitest.mutationtest.engine.gregor.mutators.ArithmeticOperatorDeletionLast;
import org.pitest.mutationtest.engine.gregor.mutators.ArithmeticOperatorDeletionFirst;

import org.pitest.mutationtest.engine.gregor.mutators.AbsoluteValueMutator;
import org.pitest.mutationtest.engine.gregor.mutators.IncrementMutator;
import org.pitest.mutationtest.engine.gregor.mutators.ConstantMutator;
import org.pitest.mutationtest.engine.gregor.mutators.m1;
import org.pitest.mutationtest.engine.gregor.mutators.m1field;
import org.pitest.mutationtest.engine.gregor.mutators.m4;

public final class Mutator {

  private static final Map<String, Iterable<MethodMutatorFactory>> MUTATORS = new LinkedHashMap<>();

  static {

    /**
     * Default mutator that inverts the negation of integer and floating point
     * numbers.
     */
    add("INVERT_NEGS", InvertNegsMutator.INVERT_NEGS_MUTATOR);

    /**
     * Default mutator that mutates the return values of methods.
     */
    add("RETURN_VALS", ReturnValsMutator.RETURN_VALS_MUTATOR);

    /**
     * Optional mutator that mutates integer and floating point inline
     * constants.
     */
    add("INLINE_CONSTS", new InlineConstantMutator());

    /**
     * Default mutator that mutates binary arithmetic operations.
     */
    add("MATH", MathMutator.MATH_MUTATOR);

    /**
     * Default mutator that removes method calls to void methods.
     *
     */
    add("VOID_METHOD_CALLS", VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR);

    /**
     * Default mutator that negates conditionals.
     */
    add("NEGATE_CONDITIONALS",
        NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR);

    /**
     * Default mutator that replaces the relational operators with their
     * boundary counterpart.
     */
    add("CONDITIONALS_BOUNDARY",
        ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR);

    /**
     * Default mutator that mutates increments, decrements and assignment
     * increments and decrements of local variables.
     */
    add("INCREMENTS", IncrementsMutator.INCREMENTS_MUTATOR);

    /**
     * Optional mutator that removes local variable increments.
     */

    add("REMOVE_INCREMENTS", RemoveIncrementsMutator.REMOVE_INCREMENTS_MUTATOR);

    /**
     * Optional mutator that removes method calls to non void methods.
     */
    add("NON_VOID_METHOD_CALLS",
        NonVoidMethodCallMutator.NON_VOID_METHOD_CALL_MUTATOR);

    /**
     * Optional mutator that replaces constructor calls with null values.
     */
    add("CONSTRUCTOR_CALLS", ConstructorCallMutator.CONSTRUCTOR_CALL_MUTATOR);



    /**
     * CUSTOM ROR
     */
    add("RELATION_REPLACE_EQ", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.EQ));
    add("RELATION_REPLACE_NE", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.NE));
    add("RELATION_REPLACE_GE", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.GE));
    add("RELATION_REPLACE_LE", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.LE));
    add("RELATION_REPLACE_GT", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.GT));
    add("RELATION_REPLACE_LT", new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.LT));

    addGroup("ROR", ror());

    /**
     * CUSTOM AOR
     */
    add("ARITHMETIC_REPLACE_IADD", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IADD));
    add("ARITHMETIC_REPLACE_ISUB", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.ISUB));
    add("ARITHMETIC_REPLACE_IMUL", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IMUL));
    add("ARITHMETIC_REPLACE_IDIV", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IDIV));
    add("ARITHMETIC_REPLACE_IREM", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IREM));

    add("ARITHMETIC_REPLACE_DADD", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DADD));
    add("ARITHMETIC_REPLACE_DSUB", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DSUB));
    add("ARITHMETIC_REPLACE_DMUL", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DMUL));
    add("ARITHMETIC_REPLACE_DDIV", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DDIV));
    add("ARITHMETIC_REPLACE_DREM", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DREM));

    add("ARITHMETIC_REPLACE_FADD", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FADD));
    add("ARITHMETIC_REPLACE_FSUB", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FSUB));
    add("ARITHMETIC_REPLACE_FMUL", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FMUL));
    add("ARITHMETIC_REPLACE_FDIV", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FDIV));
    add("ARITHMETIC_REPLACE_FREM", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FREM));

    add("ARITHMETIC_REPLACE_DADD", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DADD));
    add("ARITHMETIC_REPLACE_DSUB", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DSUB));
    add("ARITHMETIC_REPLACE_DMUL", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DMUL));
    add("ARITHMETIC_REPLACE_DDIV", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DDIV));
    add("ARITHMETIC_REPLACE_DREM", new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DREM));

    addGroup("AOR_I", aorInteger());
    addGroup("AOR_D", aorDouble());
    addGroup("AOR_F", aorFloat());
    addGroup("AOR_L", aorLong());

    add("ARITHMETIC_DELETE_LAST",
            ArithmeticOperatorDeletionLast.DELETE_ARITHMETIC_LAST_MUTATOR);

    add("ARITHMETIC_DELETE_FIRST",
            ArithmeticOperatorDeletionFirst.DELETE_ARITHMETIC_FIRST_MUTATOR);

    add("ABSOLUTE",
            AbsoluteValueMutator.ABSOLUTE_VALUE_MUTATOR);


    add("INC_ADD", new IncrementMutator(IncrementMutator.MutantType.INCREMENT));
    add("INC_SUB", new IncrementMutator(IncrementMutator.MutantType.DECREMENT));
    add("INC_RMV", new IncrementMutator(IncrementMutator.MutantType.REMOVE));
    add("INC_RVS", new IncrementMutator(IncrementMutator.MutantType.REVERSE));

    add("M1", m1.M1);
    add("M1FIELD", m1field.M1);

    add("M4", m4.M4);


    add("CRCR_NEGATE", new ConstantMutator(ConstantMutator.MutantType.NEGATE));
    add("CRCR_REPLACE_ZERO", new ConstantMutator(ConstantMutator.MutantType.REPLACE_ZERO));
    add("CRCR_ADD_ONE", new ConstantMutator(ConstantMutator.MutantType.ADD));
    add("CRCR_SUB_ONE", new ConstantMutator(ConstantMutator.MutantType.SUB));

    /**
     * Removes conditional statements so that guarded statements always execute
     * The EQUAL version ignores LT,LE,GT,GE, which is the default behaviour,
     * ORDER version mutates only those.
     */

    add("REMOVE_CONDITIONALS_EQ_IF", new RemoveConditionalMutator(Choice.EQUAL,
        true));
    add("REMOVE_CONDITIONALS_EQ_ELSE", new RemoveConditionalMutator(
        Choice.EQUAL, false));
    add("REMOVE_CONDITIONALS_ORD_IF", new RemoveConditionalMutator(
        Choice.ORDER, true));
    add("REMOVE_CONDITIONALS_ORD_ELSE", new RemoveConditionalMutator(
        Choice.ORDER, false));
    addGroup("REMOVE_CONDITIONALS", RemoveConditionalMutator.makeMutators());

    add("TRUE_RETURNS", BooleanTrueReturnValsMutator.BOOLEAN_TRUE_RETURN);
    add("FALSE_RETURNS", BooleanFalseReturnValsMutator.BOOLEAN_FALSE_RETURN);
    add("PRIMITIVE_RETURNS", PrimitiveReturnsMutator.PRIMITIVE_RETURN_VALS_MUTATOR);
    add("EMPTY_RETURNS", EmptyObjectReturnValsMutator.EMPTY_RETURN_VALUES);
    add("NULL_RETURNS", NullReturnValsMutator.NULL_RETURN_VALUES);
    addGroup("RETURNS", betterReturns());
    
    /**
     * Experimental mutator that removed assignments to member variables.
     */
    add("EXPERIMENTAL_MEMBER_VARIABLE",
        new org.pitest.mutationtest.engine.gregor.mutators.experimental.MemberVariableMutator());

    /**
     * Experimental mutator that swaps labels in switch statements
     */
    add("EXPERIMENTAL_SWITCH",
        new org.pitest.mutationtest.engine.gregor.mutators.experimental.SwitchMutator());

    /**
     * Experimental mutator that replaces method call with one of its parameters
     * of matching type
     */
    add("EXPERIMENTAL_ARGUMENT_PROPAGATION",
        ArgumentPropagationMutator.ARGUMENT_PROPAGATION_MUTATOR);

    /**
     * Experimental mutator that replaces method call with this
     */
    add("EXPERIMENTAL_NAKED_RECEIVER", NakedReceiverMutator.NAKED_RECEIVER);

    addGroup("REMOVE_SWITCH", RemoveSwitchMutator.makeMutators());
    addGroup("DEFAULTS", defaults());
    addGroup("STRONGER", stronger());
    addGroup("ALL", all());
    addGroup("NEW_DEFAULTS", newDefaults());

    addGroup("CRCR", crcr());
  }

  public static Collection<MethodMutatorFactory> all() {
    return fromStrings(MUTATORS.keySet());
  }

  private static Collection<MethodMutatorFactory> stronger() {
    return combine(
        defaults(),
        group(new RemoveConditionalMutator(Choice.EQUAL, false),
            new SwitchMutator()));
  }

  private static Collection<MethodMutatorFactory> combine(
      Collection<MethodMutatorFactory> a, Collection<MethodMutatorFactory> b) {
    List<MethodMutatorFactory> l = new ArrayList<>(a);
    l.addAll(b);
    return l;
  }

  /**
   * Default set of mutators - designed to provide balance between strength and
   * performance
   */
  public static Collection<MethodMutatorFactory> defaults() {
    return group(InvertNegsMutator.INVERT_NEGS_MUTATOR,
        ReturnValsMutator.RETURN_VALS_MUTATOR, MathMutator.MATH_MUTATOR,
        VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR,
        NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR,
        ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR,
        IncrementsMutator.INCREMENTS_MUTATOR);
  }
  
  /**
   * Proposed new defaults - replaced the RETURN_VALS mutator with the new more stable set
   */
  public static Collection<MethodMutatorFactory> newDefaults() {
    return combine(group(InvertNegsMutator.INVERT_NEGS_MUTATOR,
        MathMutator.MATH_MUTATOR,
        VoidMethodCallMutator.VOID_METHOD_CALL_MUTATOR,
        NegateConditionalsMutator.NEGATE_CONDITIONALS_MUTATOR,
        ConditionalsBoundaryMutator.CONDITIONALS_BOUNDARY_MUTATOR,
        IncrementsMutator.INCREMENTS_MUTATOR), betterReturns());
  }

  public static Collection<MethodMutatorFactory> ror() {
    return group(new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.EQ),
            new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.NE),
            new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.GE),
            new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.LE),
            new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.GT),
            new RelationalOperatorReplacementMutator(RelationalOperatorReplacementMutator.TYPE.LT));
  }

  public static Collection<MethodMutatorFactory> aorInteger() {
    return group(new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IADD),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.ISUB),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IMUL),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IDIV),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.IREM));
  }

  public static Collection<MethodMutatorFactory> aorDouble() {
    return group(new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DADD),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DSUB),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DMUL),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DSUB),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.DREM));
  }

  public static Collection<MethodMutatorFactory> aorFloat() {
    return group(new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FADD),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FSUB),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FMUL),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FDIV),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.FREM));
  }
  public static Collection<MethodMutatorFactory> aorLong() {
    return group(new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.LADD),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.LSUB),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.LMUL),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.LDIV),
            new ArithmeticOperatorReplacement(ArithmeticOperatorReplacement.TYPE.LREM));
  }

  public static Collection<MethodMutatorFactory> crcr() {
    return group(new ConstantMutator(ConstantMutator.MutantType.NEGATE),
            new ConstantMutator(ConstantMutator.MutantType.REPLACE_ZERO),
            new ConstantMutator(ConstantMutator.MutantType.ADD),
            new ConstantMutator(ConstantMutator.MutantType.SUB));
  }
  
  public static Collection<MethodMutatorFactory> betterReturns() {
    return group(BooleanTrueReturnValsMutator.BOOLEAN_TRUE_RETURN,
        BooleanFalseReturnValsMutator.BOOLEAN_FALSE_RETURN,
        PrimitiveReturnsMutator.PRIMITIVE_RETURN_VALS_MUTATOR,
        EmptyObjectReturnValsMutator.EMPTY_RETURN_VALUES,
        NullReturnValsMutator.NULL_RETURN_VALUES);
  }

  private static Collection<MethodMutatorFactory> group(
      final MethodMutatorFactory... ms) {
    return Arrays.asList(ms);
  }

  public static Collection<MethodMutatorFactory> byName(final String name) {
    return FCollection.map(MUTATORS.get(name),
        Prelude.id(MethodMutatorFactory.class));
  }

  private static void add(final String key, final MethodMutatorFactory value) {
    MUTATORS.put(key, Collections.singleton(value));
  }

  private static void addGroup(final String key,
      final Iterable<MethodMutatorFactory> value) {
    MUTATORS.put(key, value);
  }

  public static Collection<MethodMutatorFactory> fromStrings(
      final Collection<String> names) {
    final Set<MethodMutatorFactory> unique = new TreeSet<>(
        compareId());

    FCollection.flatMapTo(names, fromString(), unique);
    return unique;
  }

  private static Comparator<? super MethodMutatorFactory> compareId() {
    return new Comparator<MethodMutatorFactory>() {
      @Override
      public int compare(final MethodMutatorFactory o1,
          final MethodMutatorFactory o2) {
        return o1.getGloballyUniqueId().compareTo(o2.getGloballyUniqueId());
      }
    };
  }

  private static F<String, Iterable<MethodMutatorFactory>> fromString() {
    return new F<String, Iterable<MethodMutatorFactory>>() {
      @Override
      public Iterable<MethodMutatorFactory> apply(final String a) {
        Iterable<MethodMutatorFactory> i = MUTATORS.get(a);
        if (i == null) {
          throw new PitHelpError(Help.UNKNOWN_MUTATOR, a);
        }
        return i;
      }
    };
  }

}
