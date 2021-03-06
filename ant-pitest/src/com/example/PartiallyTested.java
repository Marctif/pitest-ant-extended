package com.example;


public class PartiallyTested {
  
  private int state = 0;
  private Comparable<Integer> sideEffect;
  
  public PartiallyTested(Comparable<Integer> sideEffect) {
    this.sideEffect = sideEffect;
  }
  
  public int doLotsOfThings(int end) {
    int j = 0;
    // mutating the i++ to i-- will cause long, but not actually infinite loop here
    // most of the mutation analysis time is spent running this before deciding to timeout
    ///for ( int i = 0; i < end; i++ ) {
      j = j + 2;
      updateState(j);
      NoTestCoverage c = new NoTestCoverage();
    j++;
    j++;
    if(c != null  )
      c.returnOne();
    j--;
    j--;
    performASideEffect(c.returnOne());

    NoTestCoverage cn = new NoTestCoverage();
    cn.num = c.num;
    cn.num = null;
       if(cn.num == null)
      return 2;
    //p.performASideEffect(j);
     j = j ;
      j--;
    //}
    return j;
  }
  
  private void performASideEffect(int j) {
    sideEffect.compareTo(j);
  }

  private void updateState(int value) {
    state = state + value;
  }
  
  public int getState() {
    return state;
  }

  public int getAddAORCheck () {
    return 1+1;
  }
}
