package com.dzikirqu.android.util.tajweed.rule;

import com.dzikirqu.android.util.tajweed.model.Result;
import com.dzikirqu.android.util.tajweed.model.ResultType;
import com.dzikirqu.android.util.tajweed.util.CharacterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Ghunna Rule
 * Occurs when a noon shadda or meem shadda appears
 */
public class GhunnaRule implements Rule {
  private final boolean isNaskhMode;

  public GhunnaRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    int length = ayah.length();
    int startPos, endPos;
    ResultType mode = ResultType.GHUNNA;
    for (int i = 0; i < length; i++) {
      int[] previous = CharacterUtil.getPreviousChars(ayah, i);
      int[] next = CharacterUtil.getNextChars(ayah, i);
      int currentChar = next[0];
      if ((currentChar == CharacterUtil.NOON ||
          currentChar == CharacterUtil.MEEM) &&
          (next[1] == CharacterUtil.SHADDA ||
              next[2] == CharacterUtil.SHADDA)) {
        startPos = i;
        endPos = i + CharacterUtil.findRemainingMarks(next);
        int indexOfPreviousPronounced = CharacterUtil.findPreviousLetterPronounced(previous);
        if (isNaskhMode) {
          startPos = i - indexOfPreviousPronounced;
          mode = ResultType.GHUNNA_NASKH;
        }
        // In Naskh, meem idgham is differentiated with a different color than ghunna
        results.add(new Result(mode, startPos, endPos));
      }
    }
    return results;
  }
}
