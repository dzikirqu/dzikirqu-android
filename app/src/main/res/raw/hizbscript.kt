//package com.wagyufari.dzikirqu
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.wagyufari.dzikirqu.SurahDao
//import com.wagyufari.dzikirqu.model.Ayah
//import com.wagyufari.dzikirqu.model.Hizb
//import com.wagyufari.dzikirqu.util.FileUtils
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class EditTextActivity : AppCompatActivity() {
//
//    @Inject
//    lateinit var surahDao: SurahDao
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_text)
//
//        val edit = edit
//
//        val hizbArray = ArrayList<Hizb>()
//        hizbArray.add(Hizb(1f, 1, 1, 7))
//        hizbArray.add(Hizb(1f, 2, 1, 25))
//        hizbArray.add(Hizb(1.25f, 2, 26, 43))
//        hizbArray.add(Hizb(1.50f, 2, 44, 59))
//        hizbArray.add(Hizb(1.75f, 2, 60, 75))
//
//        hizbArray.add(Hizb(2f, 2, 75, 91))
//        hizbArray.add(Hizb(2.25f, 2, 92, 105))
//        hizbArray.add(Hizb(2.50f, 2, 106, 123))
//        hizbArray.add(Hizb(2.75f, 2, 124, 141))
//
//        hizbArray.add(Hizb(3f, 2, 142, 157))
//        hizbArray.add(Hizb(3.25f, 2, 158, 176))
//        hizbArray.add(Hizb(3.50f, 2, 177, 188))
//        hizbArray.add(Hizb(3.75f, 2, 189, 202))
//
//        hizbArray.add(Hizb(4f, 2, 203, 218))
//        hizbArray.add(Hizb(4.25f, 2, 219, 232))
//        hizbArray.add(Hizb(4.50f, 2, 233, 242))
//        hizbArray.add(Hizb(4.75f, 2, 243, 252))
//
//        hizbArray.add(Hizb(5f, 2, 253, 262))
//        hizbArray.add(Hizb(5.25f, 2, 263, 271))
//        hizbArray.add(Hizb(5.50f, 2, 272, 282))
//        hizbArray.add(Hizb(5.75f, 2, 283, 286))
//        hizbArray.add(Hizb(5.75f, 3, 1, 14))
//
//        hizbArray.add(Hizb(6f, 3, 15, 32))
//        hizbArray.add(Hizb(6.25f, 3, 33, 51))
//        hizbArray.add(Hizb(6.50f, 3, 52, 75))
//        hizbArray.add(Hizb(6.75f, 3, 75, 92))
//
//        hizbArray.add(Hizb(7f, 3, 93, 112))
//        hizbArray.add(Hizb(7.25f, 3, 113, 132))
//        hizbArray.add(Hizb(7.50f, 3, 133, 152))
//        hizbArray.add(Hizb(7.75f, 3, 153, 170))
//
//        hizbArray.add(Hizb(8f, 3, 171, 185))
//        hizbArray.add(Hizb(8.25f, 3, 186, 200))
//        hizbArray.add(Hizb(8.50f, 4, 1, 11))
//        hizbArray.add(Hizb(8.75f, 4, 12, 23))
//
//        hizbArray.add(Hizb(9f, 4, 24, 35))
//        hizbArray.add(Hizb(9.25f, 4, 36, 57))
//        hizbArray.add(Hizb(9.50f, 4, 58, 73))
//        hizbArray.add(Hizb(9.75f, 4, 74, 87))
//
//        hizbArray.add(Hizb(10f, 4, 88, 99))
//        hizbArray.add(Hizb(10.25f, 4, 100, 113))
//        hizbArray.add(Hizb(10.50f, 4, 114, 134))
//        hizbArray.add(Hizb(10.75f, 4, 135, 147))
//
//        hizbArray.add(Hizb(11f, 4, 148, 162))
//        hizbArray.add(Hizb(11.25f, 4, 163, 176))
//        hizbArray.add(Hizb(11.50f, 5, 1, 11))
//        hizbArray.add(Hizb(11.75f, 5, 12, 26))
//
//        hizbArray.add(Hizb(12f, 5, 27, 40))
//        hizbArray.add(Hizb(12.25f, 5, 41, 50))
//        hizbArray.add(Hizb(12.50f, 5, 51, 66))
//        hizbArray.add(Hizb(12.75f, 5, 67, 81))
//
//        hizbArray.add(Hizb(13f, 5, 82, 96))
//        hizbArray.add(Hizb(13.25f, 5, 97, 108))
//        hizbArray.add(Hizb(13.50f, 5, 109, 120))
//        hizbArray.add(Hizb(13.50f, 6, 1, 12))
//        hizbArray.add(Hizb(13.75f, 6, 13, 35))
//
//        hizbArray.add(Hizb(14f, 6, 36, 58))
//        hizbArray.add(Hizb(14.25f, 6, 59, 73))
//        hizbArray.add(Hizb(14.50f, 6, 74, 94))
//        hizbArray.add(Hizb(14.75f, 6, 95, 110))
//
//        hizbArray.add(Hizb(15f, 6, 111, 126))
//        hizbArray.add(Hizb(15.25f, 6, 127, 140))
//        hizbArray.add(Hizb(15.50f, 6, 141, 150))
//        hizbArray.add(Hizb(15.75f, 6, 151, 165))
//
//        hizbArray.add(Hizb(16f, 7, 1, 30))
//        hizbArray.add(Hizb(16.25f, 7, 31, 46))
//        hizbArray.add(Hizb(16.50f, 7, 47, 64))
//        hizbArray.add(Hizb(16.75f, 7, 65, 87))
//
//        hizbArray.add(Hizb(17f, 7, 88, 116))
//        hizbArray.add(Hizb(17.25f, 7, 117, 141))
//        hizbArray.add(Hizb(17.50f, 7, 142, 155))
//        hizbArray.add(Hizb(17.75f, 7, 156, 170))
//
//        hizbArray.add(Hizb(18f, 7, 171, 188))
//        hizbArray.add(Hizb(18.25f, 7, 189, 206))
//        hizbArray.add(Hizb(18.50f, 8, 1, 21))
//        hizbArray.add(Hizb(18.75f, 8, 22, 40))
//
//        hizbArray.add(Hizb(19f, 8, 41, 60))
//        hizbArray.add(Hizb(19.25f, 8, 61, 75))
//        hizbArray.add(Hizb(19.50f, 9, 1, 18))
//        hizbArray.add(Hizb(19.75f, 9, 19, 33))
//
//        hizbArray.add(Hizb(20f, 9, 34, 45))
//        hizbArray.add(Hizb(20.25f, 9, 46, 59))
//        hizbArray.add(Hizb(20.50f, 9, 60, 74))
//        hizbArray.add(Hizb(20.75f, 9, 75, 92))
//
//        hizbArray.add(Hizb(21f,  9, 93, 110))
//        hizbArray.add(Hizb(21.25f, 9, 111, 121))
//        hizbArray.add(Hizb(21.50f, 9, 122, 129))
//        hizbArray.add(Hizb(21.50f, 10, 1, 10))
//        hizbArray.add(Hizb(21.75f, 10, 11, 25))
//
//        hizbArray.add(Hizb(22f,  10, 26, 52))
//        hizbArray.add(Hizb(22.25f, 10, 53, 79))
//        hizbArray.add(Hizb(22.50f, 10, 71, 89))
//        hizbArray.add(Hizb(22.75f, 10, 90, 109))
//        hizbArray.add(Hizb(22.75f, 11, 1, 5))
//
//        hizbArray.add(Hizb(23f,  11, 6, 23))
//        hizbArray.add(Hizb(23.25f, 11, 24, 40))
//        hizbArray.add(Hizb(23.50f, 11, 41, 60))
//        hizbArray.add(Hizb(23.75f, 11, 61, 83))
//
//        hizbArray.add(Hizb(24f,  11, 84, 107))
//        hizbArray.add(Hizb(24.25f, 11, 108, 123))
//        hizbArray.add(Hizb(24.25f, 12, 1, 6))
//        hizbArray.add(Hizb(24.50f, 12, 7, 29))
//        hizbArray.add(Hizb(24.75f, 12, 30, 52))
//
//        hizbArray.add(Hizb(25f, 12, 53, 76))
//        hizbArray.add(Hizb(25.25f, 12, 77, 100))
//        hizbArray.add(Hizb(25.50f, 12, 101, 111))
//        hizbArray.add(Hizb(25.50f, 13, 1, 4))
//        hizbArray.add(Hizb(25.75f, 13, 5, 18))
//
//        hizbArray.add(Hizb(26f, 13, 19, 34))
//        hizbArray.add(Hizb(26.25f, 13, 35, 43))
//        hizbArray.add(Hizb(26.25f, 14, 1, 9))
//        hizbArray.add(Hizb(26.50f, 14, 10, 27))
//        hizbArray.add(Hizb(26.75f, 14, 28, 52))
//
//        hizbArray.add(Hizb(27f, 15, 1, 48))
//        hizbArray.add(Hizb(27.25f, 15, 49, 99))
//        hizbArray.add(Hizb(27.50f, 16, 1, 155))
//        hizbArray.add(Hizb(27.75f, 16, 30, 50))
//
//        hizbArray.add(Hizb(28f, 16, 51, 74))
//        hizbArray.add(Hizb(28.25f, 16, 75, 89))
//        hizbArray.add(Hizb(28.50f, 16, 90, 110))
//        hizbArray.add(Hizb(28.75f, 16, 111, 128))
//
//        hizbArray.add(Hizb(29f, 17, 1, 22))
//        hizbArray.add(Hizb(29.25f, 17, 23, 49))
//        hizbArray.add(Hizb(29.50f, 17, 50, 69))
//        hizbArray.add(Hizb(29.75f, 17, 70, 98))
//
//        hizbArray.add(Hizb(30f, 17, 99, 111))
//        hizbArray.add(Hizb(30f, 18, 1, 16))
//        hizbArray.add(Hizb(30.25f, 18, 17, 31))
//        hizbArray.add(Hizb(30.50f, 18, 32, 50))
//        hizbArray.add(Hizb(30.75f, 18, 51, 74))
//
//        hizbArray.add(Hizb(31f,    18, 75 ,  98))
//        hizbArray.add(Hizb(31.25f, 18, 99 ,  110))
//        hizbArray.add(Hizb(31.25f, 19, 1  ,  21))
//        hizbArray.add(Hizb(31.50f, 19, 22 ,  58))
//        hizbArray.add(Hizb(31.75f, 19, 59 ,  98))
//
//        hizbArray.add(Hizb(32f,    20, 1  ,  54))
//        hizbArray.add(Hizb(32.25f, 20, 55 ,  82))
//        hizbArray.add(Hizb(32.50f, 20, 83 ,  110))
//        hizbArray.add(Hizb(32.75f, 20, 111,  135))
//
//        hizbArray.add(Hizb(33f,    21, 1  ,  28))
//        hizbArray.add(Hizb(33.25f, 21, 29 ,  50))
//        hizbArray.add(Hizb(33.50f, 21, 51 ,  82))
//        hizbArray.add(Hizb(33.75f, 21, 83 ,  112))
//
//        hizbArray.add(Hizb(34f,    22, 1  ,  18))
//        hizbArray.add(Hizb(34.25f, 22, 19 ,  37))
//        hizbArray.add(Hizb(34.50f, 22, 38 ,  59))
//        hizbArray.add(Hizb(34.75f, 22, 60 ,  78))
//
//        hizbArray.add(Hizb(35f,    23, 1  ,  35))
//        hizbArray.add(Hizb(35.25f, 23, 36 ,  74))
//        hizbArray.add(Hizb(35.50f, 23, 75 ,  118))
//        hizbArray.add(Hizb(35.75f, 24, 1  ,  20))
//
//        hizbArray.add(Hizb(36f,    24, 21 ,  34))
//        hizbArray.add(Hizb(36.25f, 24, 35 ,  52))
//        hizbArray.add(Hizb(36.50f, 24, 53 ,  64))
//        hizbArray.add(Hizb(36.75f, 25, 1  ,  20))
//
//        hizbArray.add(Hizb(37f,    25, 21 ,  52))
//        hizbArray.add(Hizb(37.25f, 25, 53 ,  77))
//        hizbArray.add(Hizb(37.50f, 26, 1  ,  51))
//        hizbArray.add(Hizb(37.75f, 26, 52 ,  110))
//
//        hizbArray.add(Hizb(38f,    26, 111,  180))
//        hizbArray.add(Hizb(38.25f, 26, 181,  227))
//        hizbArray.add(Hizb(38.50f, 27, 1  ,  26))
//        hizbArray.add(Hizb(38.75f, 27, 27 ,  55))
//
//        hizbArray.add(Hizb(39f,    27, 56 ,  81))
//        hizbArray.add(Hizb(39.25f, 27, 82 ,  93))
//        hizbArray.add(Hizb(39.25f, 28, 1  ,  11))
//        hizbArray.add(Hizb(39.50f, 28, 12 ,  28))
//        hizbArray.add(Hizb(39.75f, 28, 29 ,  50))
//
//        hizbArray.add(Hizb(40f,    28, 51 ,  75))
//        hizbArray.add(Hizb(40.25f, 28, 76 ,  88))
//        hizbArray.add(Hizb(40.50f, 29, 1  ,  25))
//        hizbArray.add(Hizb(40.75f, 29, 26 ,  45))
//
//        hizbArray.add(Hizb(41f,    29, 46  , 69   ))
//        hizbArray.add(Hizb(41.25f, 30, 1   , 30   ))
//        hizbArray.add(Hizb(41.50f, 30, 31  , 53   ))
//        hizbArray.add(Hizb(41.75f, 30, 54  , 60   ))
//        hizbArray.add(Hizb(41.75f, 31, 1   ,21    ))
//
//        hizbArray.add(Hizb(42f,    31, 22  ,34   ))
//        hizbArray.add(Hizb(42f,    32, 1   ,10    ))
//        hizbArray.add(Hizb(42.25f, 32, 11  ,30   ))
//        hizbArray.add(Hizb(42.50f, 33, 1   ,17    ))
//        hizbArray.add(Hizb(42.75f, 33, 18  ,30   ))
//
//        hizbArray.add(Hizb(43f,    33, 31  , 50   ))
//        hizbArray.add(Hizb(43.25f, 33, 51  , 59   ))
//        hizbArray.add(Hizb(43.50f, 33, 60  , 73   ))
//        hizbArray.add(Hizb(43.50f, 34, 1   , 9    ))
//        hizbArray.add(Hizb(43.75f, 34, 10  , 23   ))
//
//        hizbArray.add(Hizb(44f,    34, 24  ,  45  ))
//        hizbArray.add(Hizb(44.25f, 34, 46  ,  54  ))
//        hizbArray.add(Hizb(44.25f, 35, 1   , 14   ))
//        hizbArray.add(Hizb(44.50f, 35, 15  ,  40  ))
//        hizbArray.add(Hizb(44.75f, 35, 41  ,  45  ))
//        hizbArray.add(Hizb(44.75f, 36, 1   , 27   ))
//
//        hizbArray.add(Hizb(45f,    36, 28  , 59   ))
//        hizbArray.add(Hizb(45.25f, 36, 60  , 83   ))
//        hizbArray.add(Hizb(45.25f, 37, 1   , 21    ))
//        hizbArray.add(Hizb(45.50f, 37, 22  , 82   ))
//        hizbArray.add(Hizb(45.75f, 37, 83  , 144  ))
//
//        hizbArray.add(Hizb(46f,    37, 145 , 183 ))
//        hizbArray.add(Hizb(46f,    38, 1   ,20    ))
//        hizbArray.add(Hizb(46.25f, 38, 21  ,51   ))
//        hizbArray.add(Hizb(46.50f, 38, 52  , 88   ))
//        hizbArray.add(Hizb(46.50f, 39, 1   ,7    ))
//        hizbArray.add(Hizb(46.75f, 39, 8   ,31    ))
//
//        hizbArray.add(Hizb(47f,    39, 32  , 52   ))
//        hizbArray.add(Hizb(47.25f, 39, 53  , 75   ))
//        hizbArray.add(Hizb(47.50f, 40, 1   , 20    ))
//        hizbArray.add(Hizb(47.75f, 40, 21  , 40   ))
//
//        hizbArray.add(Hizb(48f,    40, 41  , 65   ))
//        hizbArray.add(Hizb(48.25f, 40, 66  , 85   ))
//        hizbArray.add(Hizb(48.25f, 41, 1   , 8     ))
//        hizbArray.add(Hizb(48.50f, 41, 9   , 24    ))
//        hizbArray.add(Hizb(48.75f, 41, 25  , 46   ))
//
//        hizbArray.add(Hizb(49f,    41, 47  ,  54   ))
//        hizbArray.add(Hizb(49f,    42, 1   , 12    ))
//        hizbArray.add(Hizb(49.25f, 42, 13  ,  26   ))
//        hizbArray.add(Hizb(49.50f, 42, 27  ,  50   ))
//        hizbArray.add(Hizb(49.75f, 42, 51  ,  53   ))
//        hizbArray.add(Hizb(49.75f, 43, 1   , 23    ))
//
//        hizbArray.add(Hizb(50f,    43, 24  , 56   ))
//        hizbArray.add(Hizb(50.25f, 43, 57  , 89   ))
//        hizbArray.add(Hizb(50.25f, 44, 1   , 16    ))
//        hizbArray.add(Hizb(50.50f, 44, 17  , 59   ))
//        hizbArray.add(Hizb(50.50f, 45, 1   , 11    ))
//        hizbArray.add(Hizb(50.75f, 45, 12  , 37   ))
//
//        hizbArray.add(Hizb(51f,     46,1 , 20))
//        hizbArray.add(Hizb(51.25f,  46,21 ,35   ))
//        hizbArray.add(Hizb(51.25f,  47,1 , 9    ))
//        hizbArray.add(Hizb(51.50f,  47,10 ,32   ))
//        hizbArray.add(Hizb(51.75f,  47,33 ,38   ))
//        hizbArray.add(Hizb(51.75f,  48,1 , 17   ))
//
//        hizbArray.add(Hizb(52f,     48,18 ,29   ))
//        hizbArray.add(Hizb(52.25f,  49,1 , 13   ))
//        hizbArray.add(Hizb(52.50f,  49,14 ,18   ))
//        hizbArray.add(Hizb(52.50f,  50,1 , 26   ))
//        hizbArray.add(Hizb(52.75f,  50,27 ,45   ))
//        hizbArray.add(Hizb(52.75f,  51,1 , 30   ))
//
//        hizbArray.add(Hizb(53f,     51,31 ,60   ))
//        hizbArray.add(Hizb(53f,     52,1 , 23   ))
//        hizbArray.add(Hizb(53.25f,  52,24 ,49   ))
//        hizbArray.add(Hizb(53.25f,  53,1 , 25   ))
//        hizbArray.add(Hizb(53.50f,  53,26 ,62   ))
//        hizbArray.add(Hizb(53.50f,  54,1 , 8    ))
//        hizbArray.add(Hizb(53.75f,  54,9 , 55   ))
//
//        hizbArray.add(Hizb(54f,     55,1 , 78   ))
//        hizbArray.add(Hizb(54.25f,  56,1 , 74   ))
//        hizbArray.add(Hizb(54.50f,  56,75 ,96   ))
//        hizbArray.add(Hizb(54.50f,  57,1 , 15   ))
//        hizbArray.add(Hizb(54.75f,  57,16 ,29   ))
//
//        hizbArray.add(Hizb(55f,     58,1 , 13   ))
//        hizbArray.add(Hizb(55.25f,  58,14 ,22   ))
//        hizbArray.add(Hizb(55.25f,  59,1 , 10   ))
//        hizbArray.add(Hizb(55.25f,  59,11 ,24   ))
//        hizbArray.add(Hizb(55.50f,  60,1 , 6    ))
//        hizbArray.add(Hizb(55.75f,  60,7 , 13   ))
//        hizbArray.add(Hizb(55.75f,  61,1 , 14   ))
//
//        hizbArray.add(Hizb(56f,     62,1 , 11   ))
//        hizbArray.add(Hizb(56f,     63,1 , 3    ))
//        hizbArray.add(Hizb(56.25f,  63,4 , 11   ))
//        hizbArray.add(Hizb(56.50f,  64,1 , 18   ))
//        hizbArray.add(Hizb(56.50f,  65,1 , 12   ))
//        hizbArray.add(Hizb(56.75f,  66,1 , 12   ))
//
//        hizbArray.add(Hizb(57f,     67,1 , 30   ))
//        hizbArray.add(Hizb(57.25f,  68,1 , 52   ))
//        hizbArray.add(Hizb(57.50f,  69,1 , 52   ))
//        hizbArray.add(Hizb(57.50f,  70,1 , 18   ))
//        hizbArray.add(Hizb(57.75f,  70,19 ,44   ))
//        hizbArray.add(Hizb(57.75f,  71,1 , 28   ))
//
//        hizbArray.add(Hizb(58f,     72,1 , 28   ))
//        hizbArray.add(Hizb(58f,     73,1 , 19   ))
//        hizbArray.add(Hizb(58.25f,  73,20 ,20   ))
//        hizbArray.add(Hizb(58.25f,  74,1 , 56   ))
//        hizbArray.add(Hizb(58.50f,  75,1 , 40   ))
//        hizbArray.add(Hizb(58.50f,  76,1 , 18   ))
//        hizbArray.add(Hizb(58.75f,  76,19 ,31   ))
//        hizbArray.add(Hizb(58.75f,  77,1 , 50   ))
//
//        hizbArray.add(Hizb(59f,     78,1 , 40   ))
//        hizbArray.add(Hizb(59f,     79,1 , 46   ))
//        hizbArray.add(Hizb(59.25f,  80,1 , 42   ))
//        hizbArray.add(Hizb(59.25f,  81,1 , 29   ))
//        hizbArray.add(Hizb(59.50f,  82,1 , 19   ))
//        hizbArray.add(Hizb(59.50f,  83,1 , 36   ))
//        hizbArray.add(Hizb(59.75f,  84,1 , 25   ))
//        hizbArray.add(Hizb(59.75f,  85,1 , 22   ))
//
//        hizbArray.add(Hizb(60f,    87,1 , 19))
//        hizbArray.add(Hizb(60f,    88,1 , 26))
//        hizbArray.add(Hizb(60f,    89,1 , 30))
//        hizbArray.add(Hizb(60.25f, 90,1 , 20    ))
//        hizbArray.add(Hizb(60.25f, 91,1 , 15    ))
//        hizbArray.add(Hizb(60.25f, 92,1 , 21    ))
//        hizbArray.add(Hizb(60.25f, 93,1 , 11    ))
//        hizbArray.add(Hizb(60.50f, 94,1 , 8    ))
//        hizbArray.add(Hizb(60.50f, 95,1 , 8    ))
//        hizbArray.add(Hizb(60.50f, 96,1 , 19    ))
//        hizbArray.add(Hizb(60.50f, 97,1 , 5    ))
//        hizbArray.add(Hizb(60.50f, 98,1 , 8    ))
//        hizbArray.add(Hizb(60.50f, 99,1 , 8    ))
//        hizbArray.add(Hizb(60.75f, 100,1 ,11    ))
//        hizbArray.add(Hizb(60.75f, 100,9 ,11    ))
//        hizbArray.add(Hizb(60.75f, 101,1 ,11    ))
//        hizbArray.add(Hizb(60.75f, 102,1 ,8    ))
//        hizbArray.add(Hizb(60.75f, 103,1 ,3    ))
//        hizbArray.add(Hizb(60.75f, 104,1 ,9    ))
//        hizbArray.add(Hizb(60.75f, 105,1 ,5    ))
//        hizbArray.add(Hizb(60.75f, 106,1 ,4    ))
//        hizbArray.add(Hizb(60.75f, 107,1 ,7    ))
//        hizbArray.add(Hizb(60.75f, 108,1 ,3    ))
//        hizbArray.add(Hizb(60.75f, 109,1 ,6    ))
//        hizbArray.add(Hizb(60.75f, 110,1 ,3    ))
//        hizbArray.add(Hizb(60.75f, 111,1 ,5    ))
//        hizbArray.add(Hizb(60.75f, 112,1 ,4    ))
//        hizbArray.add(Hizb(60.75f, 113,1 ,5    ))
//        hizbArray.add(Hizb(60.75f, 114,1 ,6    ))
//
//        val juz1_5 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran1.json"
//            ) {},
//            object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
//        val juz6_10 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran2.json"
//            ) {}, object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
//        val juz11_15 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran3.json"
//            ) {}, object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
//        val juz16_20 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran4.json"
//            ) {}, object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
//        val juz21_25 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran5.json"
//            ) {}, object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
//        val juz26_30 = Gson().fromJson<ArrayList<Ayah>>(
//            FileUtils.getJsonStringFromAssets(
//                this,
//                "json/quran/quran6.json"
//            ) {}, object : TypeToken<ArrayList<Ayah>>() {}.type,
//        )
////        juz1_5.addAll(juz6_10)
////        juz1_5.addAll(juz11_15)
////        juz1_5.addAll(juz16_20)
////        juz1_5.addAll(juz21_25)
////        juz1_5.addAll(juz26_30)
//
////        hizbArray.forEach { hizb ->
////            juz26_30.filter { it.chapterId == hizb.surah && it.verse_number in hizb.fromAyat..hizb.toAyat }.forEach {
////                it.hizb = hizb.hizb
////            }
////        }
//
//        edit.setText(Gson().toJson(juz26_30))
//
//    }
//
//    val newJuz = ArrayList<Ayah>()
//
//    suspend fun printHizb(hizb: Float): String {
//        val ayat = newJuz.filter { it.hizb == hizb }.firstOrNull()
//        val surah = surahDao.getSurahById(ayat?.chapterId ?: 0).firstOrNull()
//        return "Juz ${ayat?.juz} Hizb ${hizb} from ${surah?.name} Ayah ${ayat?.verse_number}"
//    }
//
//    class Hizb(val hizb: Float, val surah: Int, val fromAyat: Int, val toAyat: Int){
//
//    }
//
//    class AyahJsonModel(
//        var text_madani: String,
//        var juz_number: Int,
//        var chapter_id: Int,
//        var verse_number: Int,
//        var use_bismillah: Boolean? = false,
//        var translations: ArrayList<Translations>
//    ) {
//        class Translations(
//            var language_name: String,
//            var text: String
//        )
//    }
//}