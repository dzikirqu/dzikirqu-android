package com.dzikirqu.android.model.identifiers

import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.util.LocaleProvider


/**
 * Created by Mayburger on 10/12/18.
 */
class JuzObject(val juz:Int){
    val juzString = LocaleProvider.getString(LocaleConstants.JUZ) + " " + juz
}
