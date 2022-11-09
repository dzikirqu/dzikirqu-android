package com.dzikirqu.android.util.tajweed.model;

import java.util.ArrayList;
import java.util.List;

public class ResultUtil {

  public static final ResultUtil INSTANCE = new ResultUtil();

  private ResultUtil() {
  }

  /**
   * Sort the list according to the position in the ayah each result occurs.
   *
   * @param results the sorted results
   */
//  public void sort(List<Result> results) {
//    // note that this Comparator imposes an order inconsistent with equals
//    results.sort(Comparator.comparingInt(Result::getMinimumStartingPosition));
//  }

  public void fixOverlappingRules(List<Result> results) {
    // the following will delete portions of overlapping rules
    List<Result> removeMe = new ArrayList<>();
    for (int index = 1; index < results.size(); index++) {
      Result result = results.get(index);
      Result previousResult = results.get(index - 1);
      int min = result.getMinimumStartingPosition();
      int previousMax = previousResult.getMaximumEndingPosition();
      if (previousMax > min) {
        if (previousResult.resultType.equals(ResultType.GHUNNA) || previousMax - min <= 0) {
          removeMe.add(previousResult);
        } else {
          previousResult.setMaximumEndingPosition(min);
        }
      }
    }
    results.removeAll(removeMe);
  }
}
