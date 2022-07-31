package com.wagyufari.dzikirqu.util.tajweed.util;

public class CharacterUtil {

  // diacritic marks
  public static final Character FATHA_TANWEEN = 0x064b;
  public static final Character DAMMA_TANWEEN = 0x064c;
  public static final Character KASRA_TANWEEN = 0x064d;
  public static final Character FATHA = 0x064e;
  public static final Character DAMMA = 0x064f;
  public static final Character KASRA = 0x0650;
  public static final Character SUKUN = 0x0610;
  public static final Character SHADDA = 0x0651;
  public static final Character SMALL_ALEF = 0x0670; //the superscript alef
  public static final Character JAZM = 0x06E1; //AKA Naskh Sukun

  public static final Character ALEF = 0x0627; // normal alef
  public static final Character ALEF_LAYINA = 0x0649; // looks like ya but pronounced like alef

  public static final Character NOON = 0x0646;
  public static final Character BA = 0x0628;
  public static final Character MEEM = 0x0645;
  public static final Character JEEM = 0x062c;
  public static final Character DAAL = 0x062f;
  public static final Character QAAF = 0x0642;
  public static final Character TAA = 0x0637;

  public static final Character MAAD_MARKER = 0x0653;
  // this is the meem on top of a character (like a noon) when it should be pronounced like a meem
  public static final Character SMALL_HIGH_MEEM_ISOLATED = 0x06E2;

  public static final Character TATWEEL = 0x0640;
  public static final Character HAMZA = 0x0621;

  // pause marks
  public static final Character SMALL_SAAD_LAAM_ALEF = 0x06D6;
  public static final Character SMALL_QAAF_LAAM_ALEF = 0x06D7;
  public static final Character SMALL_HIGH_MEEM = 0x06D8;
  public static final Character SMALL_LAAM_ALEF = 0x06D9;
  public static final Character SMALL_HIGH_JEEM = 0x06DA;
  public static final Character SMALL_THREE_DOTS = 0x06DB;

  public static boolean isDiaMark (int c){
      return c == FATHA_TANWEEN ||
              c == DAMMA_TANWEEN ||
              c == KASRA_TANWEEN ||
              c == FATHA ||
              c == DAMMA ||
              c == KASRA ||
              c == SUKUN ||
              c == SHADDA ||
              c == SMALL_ALEF ||
              c == MAAD_MARKER ||
              c == JAZM;
  }

  public static boolean isEndMark (int c){
      return c == SMALL_SAAD_LAAM_ALEF ||
              c == SMALL_QAAF_LAAM_ALEF ||
              c == SMALL_HIGH_MEEM ||
              c == SMALL_LAAM_ALEF ||
              c == SMALL_HIGH_JEEM ||
              c == SMALL_THREE_DOTS;
  }
  
  public static boolean isLetter (int c){
    return !isEndMark(c) && !isDiaMark(c) && c != ' ' && c != TATWEEL;
  }
  
  /**
   * Given an array of characters this checks for the cases there is a noon saakin.
   * Namely, noon saakin can either exist if there is a noon either explicitly followed
   * by a sukun or in certain unmarked texts it will have no marks. Note: Only array
   * size 2 is necessary to determine this.
   */
  public static boolean isNoonSaakin(int[] nextChars){
    return nextChars[0] == NOON &&
            ((nextChars[1] == SUKUN || nextChars[1] == JAZM) ||
             nextChars[1] == ' ' ||
             isLetter(nextChars[1]));
  }

  public static boolean isMeemSaakin(int[] nextChars){
    return (nextChars[0] == MEEM &&
            ((nextChars[1] == SUKUN || nextChars[1] == JAZM)||
            nextChars[1] == ' ' ||
            isLetter(nextChars[1])));
  }

  public static boolean isTanween(int thisChar){
    return thisChar == FATHA_TANWEEN ||
            thisChar == DAMMA_TANWEEN ||
            thisChar == KASRA_TANWEEN;
  }
  
  /**
   * Given a string and an index, return an arrays with next characters from the index.
   * The first position is always the character at current position.
   * Zeros are used to represent non-characters and thus end of the string.
   */
  public static int[] getNextChars(String ayah, int index){
    int[] next = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    for (int j = 0; j < next.length; j++){
      int nIndex = index + j;
      if(nIndex < ayah.length()){
        next[j] = ayah.codePointAt(nIndex);
      }
    }
    return next;
  }

  public static int[] getPreviousChars(String ayah, int index){
    int[] previous = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    for (int j = 0; j < previous.length; j++){
      int pIndex = index - j;
      if(pIndex >= 0){
        previous[j] = ayah.codePointAt(pIndex);
      }
    }
    return previous;
  }
  
  /**
   * Given a string and an index, return the previous character and its position, ignoring spaces.
   * @param string the string to look at
   * @param index the index of the character to look before
   * @return a {{@link CharacterInfo}} instance containing the index and character. In case of
   * errors or inability to move backwards, a character of -1 is returned.
   */
  public static CharacterInfo getPreviousCharacter(String string, int index) {
    int previous = -1;
    int lastIndex = index - 1;
    if (index > 0) {
      previous = string.codePointBefore(index);
      while (Character.isSpaceChar(previous) && lastIndex > 0) {
        previous = string.codePointBefore(lastIndex--);
      }
    }

    if (Character.isSpaceChar(previous)) {
      previous = -1;
      lastIndex = -1;
    }
    return new CharacterInfo(lastIndex, previous);
  }

  /**
   * Finds the previous letter (ignoring tashkeel, spaces, etc)
   * @param letters the set of letters
   * @param index the current index
   * @return the index of the previous letter, or -1
   */
  public static int getPreviousLetter(int[] letters, int index) {
    for (int i = index - 1; i >= 0; i--) {
      if (isLetter(letters[i])) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Finds the next letter (ignoring tashkeel, spaces, etc)
   * @param letters the set of letters
   * @param index the current index
   * @return the index of the next letter, or -1
   */
  public static int getNextLetter(int[] letters, int index) {
    for (int i = index + 1; i < letters.length; i++) {
      if (isLetter(letters[i])) {
        return i;
      }
    }
    return -1;
  }

  public static int getTashkeelForLetter(int[] letters, int index) {
    if (index + 1 < letters.length && isDiaMark(letters[index + 1])) {
      return index + 1;
    }
    return -1;
  }

  //Given an array using getPreviousChars(), returns the index of the previous letter that has a diacritic mark
  //Assumes all pronounced letters have a diacritic mark; this assumption is valid only for Naskh
  public static int findPreviousLetterPronounced (int[] previous){
    for (int i = 1; i < previous.length; i++){
      if (isLetter(previous[i]) && isDiaMark(previous[i-1])){
        return i;
      }
    }
    return 0;
  }
  //Given an array using getNextChars(), returns the index of the next letter that has a diacritic mark
  //Assumes all pronounced letters have a diacritic mark; this assumption is valid only for Naskh
  public static int findNextLetterPronounced (int[] next){
    for (int i = 1; i < next.length - 1; i++){
      if (isLetter(next[i]) && isDiaMark(next[i + 1])) {
        return i;
      }
    }
    return 0;
  }

  //Given an array using getNextChars(), returns index of the array at which all the diacritic marks are included
  public static int findRemainingMarks(int[] next) {
    for (int i = 1; i < next.length; i++){
      if (!isDiaMark(next[i])){
        return i;
      }
    }
    return 0;
  }
}
