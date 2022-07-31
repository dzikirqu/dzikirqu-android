package com.wagyufari.dzikirqu

import java.io.*

fun createNewFile(name: String): String {
    val filename = "$name.json"
    File(filename).createNewFile()
    return filename
}

fun writeToFile(data: String, fileName: String) {
    val filePath = createNewFile(fileName)
    try {
        PrintWriter(FileWriter(filePath)).use {
            it.write(data)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getJsonString(path: String): String {
    val `is`: InputStream = File(path).inputStream()
    val writer: Writer = StringWriter()
    val buffer = CharArray(1024)
    `is`.use { `is` ->
        val reader: Reader = BufferedReader(InputStreamReader(`is`, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }
    return writer.toString()
}