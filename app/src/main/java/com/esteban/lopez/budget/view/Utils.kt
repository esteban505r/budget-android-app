package com.esteban.lopez.budget.view

import java.text.DecimalFormat

class Utils {

    companion object{
        fun formatNumberToString(value: Double):String{
            val df = DecimalFormat("###,###.##")
            return when {
                value>1000000 -> {
                    "${df.format(value/1000000).toString()} MILLONES";
                }
                value>999 -> {
                    "${df.format(value/1000).toString()} MIL";
                }
                value==-1.0 -> {
                    ""
                }
                else -> {
                    df.format(value).toString()
                }
            }
        }

        fun formatNumberToStringWithoutLetters(value: Double):String{
           if(value!=-1.0){
               val df = DecimalFormat("###,###.#")
               return df.format(value)
           }
            else{
                return ""
           }
        }

        fun String.isADouble():Boolean{
            return when(toDoubleOrNull()) {
                null -> false
                else -> true
            }
        }

        fun String.removeCommas():String{
            return this.replace(",","")
        }

    }
}
