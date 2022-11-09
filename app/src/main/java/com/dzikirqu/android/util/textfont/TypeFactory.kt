package com.dzikirqu.android.util.textfont

import android.content.Context
import android.graphics.Typeface

/**
 * Created by wagyufari
 */
class TypeFactory internal constructor(context: Context) {

    private val BOLD = "fonts/Lato-Bold.ttf"
    private val MEDIUM = "fonts/Lato-Medium.ttf"
    private val REGULAR = "fonts/Lato-Regular.ttf"
    private val BLACK = "fonts/Lato-Black.ttf"
    private val UTHMAN = "fonts/uthman.otf"
    private val CASLON = "fonts/BigCaslon.ttf"


    val bold: Typeface
    val uthman: Typeface
    val medium: Typeface
    val black:Typeface
    val regular: Typeface
    val caslon: Typeface

    init {
        bold = Typeface.createFromAsset(context.assets, BOLD)
        black = Typeface.createFromAsset(context.assets, BLACK)
        medium = Typeface.createFromAsset(context.assets, MEDIUM)
        regular = Typeface.createFromAsset(context.assets, REGULAR)
        uthman = Typeface.createFromAsset(context.assets, UTHMAN)
        caslon = Typeface.createFromAsset(context.assets, CASLON)
    }
}
