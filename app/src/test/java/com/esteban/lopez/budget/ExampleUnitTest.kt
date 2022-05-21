package com.esteban.lopez.budget

import org.junit.Test

import org.junit.Assert.*
import java.text.DecimalFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val df = DecimalFormat("###,###")
        print(df.format(33333))
        assert(true)
    }
}