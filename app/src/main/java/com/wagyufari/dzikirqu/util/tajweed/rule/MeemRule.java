package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;
import com.wagyufari.dzikirqu.util.tajweed.model.ResultType;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * There are two rules with meem:
 * After a meem sakin, if there is a ba then we have Ikhfa meem rule
 * After a meem sakin, if there is another meem (with a shadda, in full scripts)
 * then we have Idgaam meem rule
 */
public class MeemRule implements Rule {

  private final boolean isNaskhMode;

  public MeemRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    int length = ayah.length();
    int startPos, endPos;
    ResultType idghamMode = ResultType.MEEM_IDGHAM;
    ResultType ikhfaMode = ResultType.MEEM_IKHFA;
    for (int i = 0; i < length; i++) {
      int[] next = CharacterUtil.getNextChars(ayah, i);
      if (CharacterUtil.isMeemSaakin(next)) {
        for (int j = 1; j < next.length && next[j] != 0; j++) {
          if (CharacterUtil.isLetter(next[j])) {
            if (next[j] == CharacterUtil.MEEM || next[j] == CharacterUtil.BA) {
              startPos = i;
              endPos = i + CharacterUtil.findRemainingMarks(next);
              if (isNaskhMode) {
                endPos = i + j + CharacterUtil
                    .findRemainingMarks(Arrays.copyOfRange(next, j, next.length));
                idghamMode = ResultType.MEEM_IDGHAM_NASKH;
                ikhfaMode = ResultType.MEEM_IKHFA_NASKH;
              }
              if (next[j] == CharacterUtil.MEEM) {
                results.add(new Result(idghamMode, startPos, endPos));
              } else {
                results.add(new Result(ikhfaMode, startPos, endPos));
              }
            } else {
              break;
            }
          }
        }
      }
    }
    return results;
  }
}
