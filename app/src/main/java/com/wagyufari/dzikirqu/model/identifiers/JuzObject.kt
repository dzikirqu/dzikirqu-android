package com.wagyufari.dzikirqu.model.identifiers

import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.util.LocaleProvider


/**
 * Created by Mayburger on 10/12/18.
 */
class JuzObject(val juz:Int){
    val juzString = LocaleProvider.getString(LocaleConstants.JUZ) + " " + juz
}
