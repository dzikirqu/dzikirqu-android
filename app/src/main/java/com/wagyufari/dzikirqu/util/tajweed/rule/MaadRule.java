package com.wagyufari.dzikirqu.util.tajweed.rule;

import com.wagyufari.dzikirqu.util.tajweed.model.Result;
import com.wagyufari.dzikirqu.util.tajweed.model.ResultType;
import com.wagyufari.dzikirqu.util.tajweed.util.CharacterUtil;
import com.wagyufari.dzikirqu.util.tajweed.util.Characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maad Rule
 * There are 6 types of Maad:
 * 1. "Maad al tabee3i" - ordinary maad - when 7arf maad (alif, waw, or ya) have sukoon and the
 * preceding letter follows their sign (i.e. fat7a for alif, damma for waw, and kasra for ya').
 * This is a 2 count maad.
 *
 * 2. "Maad 3ared lil sukoon" - maad as a result of sukoon - this is when 7arf maad is followed by
 * a letter with sukoon (which could be the result of stopping as opposed to the letter naturally
 * having sukoon (i.e. at the end of an ayah)). This is 2, 4, or 6 count maad.
 *
 * 3. "Maad el munfasil" - separated maad - when the 7arf maad is followed by a hamza. This is a
 * 4 or 5 count maad.
 *
 * 4. "Maad el mutasil" - connected maad - when the 7arf maad is followed by a hamza (like the
 * above), but within the same word. This is a 4 count maad. The madani mushaf makes this the same
 * color as maad el munafasil.
 *
 * 5. "Maad sila al sughra" - a small alif, a small ya, or a small waw - this is 2 counts.
 *
 * 6. "Maad 6 7arakat" - when a maad letter is followed by a mushaddad harf - this is 6 counts.
 *
 * Additional notes:
 * - the madani tajweed mushaf doesn't highlight the standard maad (rule 1)
 * - it's difficult for us to differentiate between maad munfasil and muttasil without whitelisting
 *   cases, and since they're the same color in the mushaf, we treat them as the same.
 * - some letters have an explicit maad marker on top of them (ex laam and meem in alif laam meem).
 */
public class MaadRule implements Rule {
  private final boolean isNaskhMode;
  public MaadRule(boolean isNaskhMode) {
    this.isNaskhMode = isNaskhMode;
  }

  private static final List<Character> MAAD_LETTERS =
      Arrays.asList(Characters.ALEF, Characters.WAW, Characters.YA, Characters.ENDING_YA,
          Characters.ALEF_SUGHRA, Characters.WAW_SUGHRA, Characters.YA_SUGHRA);

  @Override
  public List<Result> checkAyah(String ayah) {
    List<Result> results = new ArrayList<>();
    if (isNaskhMode) return results;
    int[] characters = ayah.codePoints().toArray();
    for (int i = 0; i < characters.length; i++) {
      boolean isExplicitMaad = isExplicitMaad(characters, i);
      if (isHarfMaad(characters, i)) {
        if (isExplicitMaad) {
          // 4 characters
          results.add(new Result(ResultType.MAAD_MUNFASSIL_MUTASSIL, i, i + 2));
          continue;
        }
        // we have a maad - now let's see which type
        int nextCharacter = CharacterUtil.getNextLetter(characters, i);
        int nextTashkeel = nextCharacter > 0 ?
            CharacterUtil.getTashkeelForLetter(characters, nextCharacter) : -1;
        if (nextTashkeel > -1 && characters[nextTashkeel] == CharacterUtil.SHADDA) {
          results.add(new Result(ResultType.MAAD_LONG, i, i + 1));
        } else if (nextCharacter > -1 && characters[nextCharacter] == CharacterUtil.HAMZA) {
          results.add(new Result(ResultType.MAAD_MUNFASSIL_MUTASSIL, i, i + 1));
        } else if ((nextTashkeel > -1 && characters[nextTashkeel] == CharacterUtil.SUKUN) ||
            (nextCharacter > -1 && CharacterUtil.getNextLetter(characters, nextCharacter) == -1)) {
          results.add(new Result(ResultType.MAAD_SUKOON, i, i + 1));
        } else if (characters[i] == Characters.ALEF_SUGHRA ||
            characters[i] == Characters.WAW_SUGHRA ||
            characters[i] == Characters.YA_SUGHRA) {
          results.add(new Result(ResultType.MAAD_SILA_SUGHRA, i, i + 1));
        }
        // else, this is a normal maad, which isn't highlighted in the madani mus7af
      } else if (isExplicitMaad) {
        // a non-alef/waw/ya that has a maad - means we're in one of 7uroof al mutaqata3a
        results.add(new Result(ResultType.MAAD_LONG, i, i + 2));
      }
    }
    return results;
  }

  private boolean isHarfMaad(int[] characters, int index) {
    if (MAAD_LETTERS.contains((char) characters[index])) {
      int tashkeel = CharacterUtil.getTashkeelForLetter(characters, index);
      if (tashkeel == -1) {
        int previous = CharacterUtil.getPreviousLetter(characters, index);
        if (previous > -1) {
          tashkeel = CharacterUtil.getTashkeelForLetter(characters, previous);
          return tashkeel > -1 &&
              ((characters[tashkeel] == CharacterUtil.FATHA &&
                  (characters[index] == Characters.ALEF ||
                      characters[index] == Characters.ALEF_SUGHRA)) ||
                  (characters[tashkeel] == CharacterUtil.DAMMA &&
                      (characters[index] == Characters.WAW ||
                          characters[index] == Characters.WAW_SUGHRA)) ||
                  (characters[tashkeel] == CharacterUtil.KASRA &&
                      (characters[index] == Characters.YA ||
                          characters[index] == Characters.ENDING_YA ||
                          characters[index] == Characters.YA_SUGHRA)));
        }
      } else if (characters[tashkeel] == CharacterUtil.MAAD_MARKER) {
        // explicit maad
        return true;
      }
    }
    return false;
  }

  private boolean isExplicitMaad(int[] characters, int index) {
    int tashkeel = CharacterUtil.getTashkeelForLetter(characters, index);
    return tashkeel > - 1 && characters[tashkeel] == CharacterUtil.MAAD_MARKER;
  }
}
