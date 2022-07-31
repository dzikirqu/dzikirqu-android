package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;
import com.wagyufari.dzikirqu.util.tajweed.model.ResultType;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Qalqalah Rule
 * The letters of qalqalah are ق ط ب ج د. Qalqalah is done on these letters when
 * there is either a sukoon on the letter or when one is stopping on these letters
 */

public class QalqalahRule implements Rule {
  private final boolean isNaskhMode;

  public QalqalahRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    int length = ayah.length();
    int startPos, endPos;
    ResultType mode = ResultType.QALQALAH;
    for (int i = 0; i < length; i++) {
      int[] next = CharacterUtil.getNextChars(ayah, i);
      int currentChar = next[0];

      if ((currentChar == CharacterUtil.DAAL ||
          currentChar == CharacterUtil.BA ||
          currentChar == CharacterUtil.JEEM ||
          currentChar == CharacterUtil.QAAF ||
          currentChar == CharacterUtil.TAA) &&
          ((next[1] == CharacterUtil.SUKUN || next[1] == CharacterUtil.JAZM) ||
              next[1] == ' ' ||
              CharacterUtil.isLetter(next[1]) ||
              weStopping(next))) {
        startPos = i;
        // In the madani pattern, only the qalqalh letter and sukun are highlighted (if present)
        endPos = i + 1;
        if ((next[1] == CharacterUtil.SUKUN || next[1] == CharacterUtil.JAZM) ) {
          endPos++;
        }
        if (isNaskhMode) {
          endPos = i + CharacterUtil.findRemainingMarks(next);
          mode = ResultType.QALQALAH_NASKH;
        }
        // A special case where no qalqalah is done see surah kafiroon ayah 4 for example
        if ((next[1] == CharacterUtil.SUKUN || next[1] == CharacterUtil.JAZM) || next[1] == ' ' || CharacterUtil.isLetter(next[1])) {
          for (int j = 1; j < next.length - 2 && next[j] != 0; j++) {
            if (!(CharacterUtil.isLetter(next[j]) &&
                (next[j + 1] == CharacterUtil.SHADDA || next[j + 2] == CharacterUtil.SHADDA))) {
              results.add(new Result(mode, startPos, endPos));
            } else {
              break;
            }
          }
        } else {
          results.add(new Result(mode, startPos, endPos));
        }
      }
    }
    return results;
  }

  private boolean weStopping(int[] next) {
    for (int i = 1; i < next.length; i++) {
      if ((CharacterUtil.isEndMark(
          next[i]) && next[i] != CharacterUtil.SMALL_LAAM_ALEF &&
          (isNaskhMode || (next[i] != CharacterUtil.SMALL_THREE_DOTS))) || next[i] == 0) {
        return true;
      }
      if (CharacterUtil.isLetter(next[i])) {
        break;
      }
    }
    return false;
  }
}
