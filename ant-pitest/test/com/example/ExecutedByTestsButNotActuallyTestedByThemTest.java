package com.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ExecutedByTestsButNotActuallyTestedByThemTest {

  @Test
  public void doesntReallyTestAnything() {
    ExecutedByTestsButNotActuallyTestedByThem testee = new ExecutedByTestsButNotActuallyTestedByThem();
    testee.returnOne();
    assertEquals(6,testee.returnAdd(1,2,3));
  }
  
}
