package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class PartiallyTestedTest {
  
  @Test
  public void testSomeStuff() {
    Comparable<Integer> sideEffect = new     Comparable<Integer>() {
      @Override
      public int compareTo(Integer arg0) {
        return 0;
      }
      
    };
    
    PartiallyTested testee = new PartiallyTested(sideEffect);
    assertEquals(2,testee.doLotsOfThings(100));
    assertEquals(2,testee.getAddAORCheck());
  }

}
