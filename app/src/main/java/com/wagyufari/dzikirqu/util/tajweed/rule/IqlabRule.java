package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;
import com.wagyufari.dzikirqu.util.tajweed.model.ResultType;
import com.wagyufari.dzikirqu.util.tajweed.model.TwoPartResult;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterInfo;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Iqlab Rule
 * Iqlab occurs when a ن (noon sakina) or tanween is followed by a ب.
 */
public class IqlabRule implements Rule {
  private final boolean isNaskhMode;
  public IqlabRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    int index = -1;
    while ((index = (ayah.indexOf(CharacterUtil.BA, index + 1))) > -1) {
      CharacterInfo previousCharacter = CharacterUtil.getPreviousCharacter(ayah, index);
      int[] prev = CharacterUtil.getPreviousChars(ayah, index);
      int[] next = CharacterUtil.getNextChars(ayah, index);
      int previous = previousCharacter.character;
      int previousPronounced = CharacterUtil.findPreviousLetterPronounced(prev);
      if (isNaskhMode && ((prev[previousPronounced] == CharacterUtil.NOON &&
              (prev[previousPronounced - 1] == CharacterUtil.SUKUN ||
                      prev[previousPronounced - 1] == CharacterUtil.JAZM)) ||
              CharacterUtil.isTanween(prev[previousPronounced - 1]))) {
        int startPos = index - previousPronounced;
        int[] prev2 = CharacterUtil.getPreviousChars(ayah, startPos);
        results.add(new Result(ResultType.IQLAB_NASKH, startPos - CharacterUtil.findPreviousLetterPronounced(prev2),
                index + CharacterUtil.findRemainingMarks(next)));
      } else if (!isNaskhMode && (CharacterUtil.isTanween(previous) ||
          previous == CharacterUtil.NOON)) {
        results.add(new TwoPartResult(ResultType.IQLAB, index, index + 1,
            ResultType.IQLAB_NOT_PRONOUNCED, previousCharacter.index,
            previousCharacter.index + 1));
      } else if (!isNaskhMode && (previous == CharacterUtil.SMALL_HIGH_MEEM_ISOLATED)) {
        // the letter should be pronounced as a meem - let's double check it's above a noon
        CharacterInfo actual = CharacterUtil.getPreviousCharacter(ayah, previousCharacter.index);
        if (actual.character == CharacterUtil.NOON) {
          results.add(new TwoPartResult(ResultType.IQLAB, previousCharacter.index, index + 1,
              ResultType.IQLAB_NOT_PRONOUNCED, actual.index, previousCharacter.index));
        }
      }
    }
    return results;
  }
}
