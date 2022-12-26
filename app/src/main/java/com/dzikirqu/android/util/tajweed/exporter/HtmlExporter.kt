package com.dzikirqu.android.util.tajweed.exporter

import com.dzikirqu.android.util.tajweed.model.Result
import com.dzikirqu.android.util.tajweed.model.ResultType
import com.dzikirqu.android.util.tajweed.model.ResultUtil
import com.dzikirqu.android.util.tajweed.model.TwoPartResult
import java.lang.StringBuilder

/**
 * HtmlExporter
 * Exports the highlighted tajweed results for the given ayahs as html.
 * Note that this only works in Firefox due to a bug in Webkit - see:
 * https://bugs.webkit.org/show_bug.cgi?id=6148 and
 * http://stackoverflow.com/questions/11155849/partially-colored-arabic-word-in-html
 * for more details.
 */
class HtmlExporter : Exporter {
    override fun onOutputStarted() {
        val builder = "<html>" + "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<style>" +
                "body" +
                "</style>" +
                "<title>Tajweed Test</title>" +
                "</head>"
        write(builder)
    }

    override fun onOutputCompleted() {
        write("</html>")
    }

    override fun export(ayah: String, results: List<Result>, stringResult: (String) -> Unit) {
        // rules are sorted, but we need to remove or update overlapping rules
        // because this exporter currently doesn't support backtracking.
        ResultUtil.INSTANCE.fixOverlappingRules(results)
        val buffer = StringBuilder("<p>")
        var currentIndex = 0
        for (result in results) {
            var start = result.minimumStartingPosition
            if (start > currentIndex) {
                buffer.append(ayah.substring(currentIndex, start))
            }
            start = result.minimumStartingPosition
            if (result is TwoPartResult) {
                val twoPartResult = result
                if (start == twoPartResult.start) {
                    // first, then second
                    appendRule(
                        buffer, ayah, twoPartResult.resultType,
                        twoPartResult.start, twoPartResult.ending
                    )
                    if (twoPartResult.secondStart - twoPartResult.ending > 0) {
                        buffer.append(
                            ayah.substring(
                                twoPartResult.ending,
                                twoPartResult.secondStart
                            )
                        )
                    }
                    appendRule(
                        buffer, ayah, twoPartResult.secondResultType,
                        twoPartResult.secondStart, twoPartResult.secondEnding
                    )
                } else {
                    // second, then first
                    appendRule(
                        buffer, ayah, twoPartResult.secondResultType,
                        twoPartResult.secondStart, twoPartResult.secondEnding
                    )
                    if (twoPartResult.start - twoPartResult.secondEnding > 0) {
                        buffer.append(
                            ayah.substring(
                                twoPartResult.secondEnding,
                                twoPartResult.start
                            )
                        )
                    }
                    appendRule(
                        buffer, ayah,
                        twoPartResult.resultType, twoPartResult.start, twoPartResult.ending
                    )
                }
            } else {
                appendRule(buffer, ayah, result.resultType, result.start, result.ending)
            }
            currentIndex = result.maximumEndingPosition
        }
        buffer.append(ayah.substring(currentIndex))
        buffer.append("</p>")

        stringResult(buffer.toString())
    }

    private fun appendRule(
        buffer: StringBuilder,
        ayah: String,
        type: ResultType,
        start: Int,
        end: Int
    ) {
        buffer.append("<font color=\"#")
        buffer.append(getColorForRule(type))
        buffer.append("\">")
        buffer.append(ayah.substring(start, end))
        buffer.append("</font>")
    }

    private fun getColorForRule(type: ResultType): String {
        return type.color
    }

    private fun write(string: String) {
        println(string)
    }
}