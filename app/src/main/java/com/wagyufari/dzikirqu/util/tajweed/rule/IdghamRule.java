package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;
import com.wagyufari.dzikirqu.util.tajweed.model.ResultType;
import com.wagyufari.dzikirqu.util.tajweed.model.TwoPartResult;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterInfo;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Idgham Rule
 * The letters of idgham are يرملون. Idgham occurs when a ن (noon sakina) or tanween are followed by
 * one of the aforementioned 6 letters. There are two of idgham.
 * Idgham with ghunna: when ن or tanween are followed by one of ينمو.
 * Igham without ghunna: when ن or tanween are followed by ل or ر
 */
public class IdghamRule implements Rule {
  private final boolean isNaskhMode;
  public IdghamRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }
  // with ghunna
  private static final Character YA = 0x064a;
  private static final Character MEEM = CharacterUtil.MEEM;
  private static final Character WAW = 0x0648;
  private static final Character NOON = CharacterUtil.NOON;

  // without ghunna
  private static final Character RA = 0x0631;
  private static final Character LAM = 0x0644;

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    int[] characters = ayah.codePoints().toArray();
    for (int i = 0; i < characters.length; i++) {
      int current = characters[i];
      boolean isYarmaloon = isYarmaloon(current);
      int[] previous = CharacterUtil.getPreviousChars(ayah, i);
      int[] next = CharacterUtil.getNextChars(ayah, i);
      if (isNaskhMode){
        int endPos = i + CharacterUtil.findNextLetterPronounced(next);
        int[] next2 = CharacterUtil.getNextChars(ayah, endPos);
        int nextPronounced = CharacterUtil.findNextLetterPronounced(next);
        //If noon sakin or tanween is followed by a letter of Yarmaloon
        if ((CharacterUtil.isTanween(next[0]) || CharacterUtil.isNoonSaakin(next)) &&
                isGhunnaYarmaloon(next[nextPronounced])){
          //Make sure it is not the important exception to this rule - when idgham criteria is met WITHIN a word
          boolean trueIdgham = false;
          for (int nextIndex = 1; nextIndex < nextPronounced; nextIndex++){
            if (next[nextIndex] == ' ') trueIdgham = true;
          }
          if (trueIdgham){
            results.add(new Result(ResultType.IDGHAM_WITH_GHUNNA,
                    i - CharacterUtil.findPreviousLetterPronounced(previous),
                    endPos + CharacterUtil.findRemainingMarks(next2)));
          }
        }
      }
      if (isYarmaloon && !isNaskhMode) {
        int previousMatchPosition = isValidIdgham(ayah, i);
        if (previousMatchPosition >= 0) {
          boolean withGhunna = (isGhunnaYarmaloon(current));
          if (withGhunna) {
            results.add(new TwoPartResult(ResultType.IDGHAM_WITH_GHUNNA, i, i + 1,
                ResultType.IDGHAM_NOT_PRONOUNCED, previousMatchPosition,
                previousMatchPosition + 1));
          } else {
            results.add(new Result(ResultType.IDGHAM_WITHOUT_GHUNNA,
                previousMatchPosition, previousMatchPosition + 1));
          }
        }
      }
    }
    return results;
  }

  private int isValidIdgham(String ayah, int index) {
    CharacterInfo previousCharacter = CharacterUtil.getPreviousCharacter(ayah, index);

    boolean result = false;
    int previousIndex = previousCharacter.index;
    int previous = previousCharacter.character;
    if (previous == CharacterUtil.FATHA_TANWEEN ||
        previous == CharacterUtil.DAMMA_TANWEEN ||
        previous == CharacterUtil.KASRA_TANWEEN ||
        previous == NOON) {
      result = true;
    } else if (previousCharacter.index > 0 &&
        (previous == CharacterUtil.ALEF_LAYINA || previous == CharacterUtil.ALEF)) {
      // if the previous character is either alif layina or alif, check to see if we have
      // tanween with fatha on the previous letter.
      previous = ayah.codePointBefore(previousCharacter.index);
      result = previous == CharacterUtil.FATHA_TANWEEN;
      previousIndex = previousCharacter.index - 1;
    }
    return result ? previousIndex : -1;
  }

  private boolean isYarmaloon(int thisChar){
    return (thisChar == RA || thisChar == LAM ||
            thisChar == YA || thisChar == NOON || thisChar == MEEM || thisChar == WAW);
  }

  private boolean isGhunnaYarmaloon(int thisChar){
    return (thisChar == YA || thisChar == NOON || thisChar == MEEM || thisChar == WAW);
  }
}
