package com.wagyufari.dzikirqu.util.tajweed.model;

public class Result {
  public int start;
  public int ending;
  public int oldEnding;
  public final ResultType resultType;

  public Result(ResultType resultType, int start, int ending) {
    this.start = start;
    this.ending = ending;
    this.oldEnding = ending;
    this.resultType = resultType;
  }

  void setMaximumEndingPosition(int newEnd) {
    this.oldEnding = ending;
    this.ending = newEnd;
  }

  public int getMinimumStartingPosition() {
    return start;
  }

  public int getMaximumEndingPosition() {
    return ending;
  }

}
