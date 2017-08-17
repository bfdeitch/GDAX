package com.treehouse.gdax

import org.junit.Test
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class NumberFormatTest {
    @Test
    fun test() {
        println(formatNumString2(0.00000035, 8))
        println(formatNumString3(0.00000035, 8))
        println(formatNumString3(.000001, 8))
        println(formatNumString3(35.0, 8))
        println(formatNumString3(3.55, 8))
        println(formatNumString3(3550.0, 8))
        assert(true)
    }

    fun formatNumString3(number: Double, decimalSpots: Int): String {
        println("AAAAA")
        println(number.toString())
        val newNumber = String.format("%.${decimalSpots}f", number)
        println("newNumber: $newNumber")
        val beforeDec = number.toString().substringBefore(".")
        val afterDec = number.toString().substringAfter(".").padEnd(decimalSpots, '0')
        return "$beforeDec.$afterDec"
    }

    fun formatNumString2(number: Double, decimalSpots: Int): String {
        val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
        df.maximumFractionDigits = 340 //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        return df.format(number)
    }
}