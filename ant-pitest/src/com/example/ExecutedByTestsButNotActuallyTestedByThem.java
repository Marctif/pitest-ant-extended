package com.example;

public class ExecutedByTestsButNotActuallyTestedByThem {

  public int returnOne() {
    return 1;
  }

  public int returnAdd(int a, int b, int c) {
    if( a == 2)
      return 2;

    return a + b + c;
  }
  
}
