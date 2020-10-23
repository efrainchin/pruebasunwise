package com.example.numberroman

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*

class MainActivity : AppCompatActivity() {

    private val map: TreeMap<Int, String> = TreeMap<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (!TextUtils.isEmpty(editTextTextPersonName.text.toString())
                    && !TextUtils.isEmpty(editTextTextPersonName2.text.toString())){
                textView2.text = getFinalPosition(Integer.parseInt(editTextTextPersonName.text.toString())
                        ,Integer.parseInt(editTextTextPersonName2.text.toString()))
                editTextTextPersonName.text = null
                editTextTextPersonName2.text = null
            }
        }

        button2.setOnClickListener {
            validateFormatDate()
        }
    }

    /**
     * Método para obtener la posición final
     */

    private fun getFinalPosition(n: Int, m: Int): String{
        return if (n%2 == 0 && m%2 == 0)
            if (n>m) "U" else "L"
        else if (n%2 !=0 && m%2 !=0 )
            if (n>m) "D" else "R"
        else if (n%2 == 0 && m%2 != 0)
            if (n>m) "D" else "L"
        else if (n%2 !=0 && m%2 == 0)
            if (n<m) "R" else "U"
        else
            "R"
    }


    /**
     * Tomando como base lo siguientes puntos de problema
     * 753 BC = 1 AUC
     * 1 BC = 753 AUC
     * 1 AD = 754 AUC
     * 2012 AD = 2765 AUC
     *(753BC) <= A <= B <= (2012AD)
     *
     * Se aplico la siguente formula para calcular el rango de fechas
     * BC = x = 753 - ( x - 1)
     * AD = x = x + 753
     */
    private fun validateFormatDate(){
        maps()
        if (!TextUtils.isEmpty(editTextTextPersonName3.text.toString())){
            val date = editTextTextPersonName3.text.toString().split("-")
            if (date.size>1){
                val date1 = date[0]
                val date2 = date[1]
                if ((date1.contains("bc",true) || date1.contains("ad",true))
                        && (date2.contains("bc",true) || date2.contains("ad",true))){
                    if (date1.contains("bc",true) && date2.contains("ad",true)){
                        val bc = Integer.parseInt(date1.filter { it.isDigit() })
                        val ad = Integer.parseInt(date2.filter { it.isDigit() })
                        var auc_bc =0
                        var auc_ad = 0
                        if ((bc < 754) && (ad < 3246 )){
                            auc_bc = 753 -(bc-1)
                            auc_ad = ad + 753
                            textView3.text = "${getIsDigitNumberRoman(auc_bc,auc_ad)}"
                        }else
                            showMessage("Formato incorrecto")
                    }else if (date1.contains("bc",true) && date2.contains("bc",true)){
                        val bc_1 = Integer.parseInt(date1.filter { it.isDigit() })
                        val bc_2 = Integer.parseInt(date2.filter { it.isDigit() })
                        var auc_bc_1 =0
                        var auc_bc_2 = 0
                        if ((bc_1 in (bc_2 + 1)..753) && bc_2 < 754){
                            auc_bc_1 = 753 -(bc_1-1)
                            auc_bc_2 = 753 -(bc_2-1)
                            if (auc_bc_1 < auc_bc_2)
                                textView3.text = "${getIsDigitNumberRoman(auc_bc_1,auc_bc_2)}"
                            else
                                textView3.text = "${getIsDigitNumberRoman(auc_bc_2,auc_bc_1)}"
                        }else
                            showMessage("Formato incorrecto")
                    }else if (date1.contains("ad",true) && date2.contains("ad",true)){
                        val ad_1 = Integer.parseInt(date1.filter { it.isDigit() })
                        val ad_2 = Integer.parseInt(date2.filter { it.isDigit() })
                        var auc_bc_1 =0
                        var auc_bc_2 = 0
                        if (ad_1 < 3246 && ad_2 < 3246){
                            auc_bc_1 = ad_1 + 753
                            auc_bc_2 = ad_2 + 753
                            if (auc_bc_1 < auc_bc_2)
                                textView3.text = "${getIsDigitNumberRoman(auc_bc_1,auc_bc_2)}"
                            else
                                textView3.text = "${getIsDigitNumberRoman(auc_bc_2,auc_bc_1)}"
                        }else
                            showMessage("Formato incorrecto")
                    }else{
                        val bc = Integer.parseInt(date2.filter { it.isDigit() })
                        val ad = Integer.parseInt(date1.filter { it.isDigit() })
                        var auc_bc =0
                        var auc_ad = 0
                        if ((bc < 754) && (ad < 3246 )){
                            auc_bc = 753 -(bc-1)
                            auc_ad = ad + 753
                            textView3.text = "${getIsDigitNumberRoman(auc_bc,auc_ad)}"
                        }else
                            showMessage("Formato incorrecto")
                    }
                }
            }
        }else
            showMessage("Formato incorrecto")

        editTextTextPersonName3.text = null
    }

    private fun getIsDigitNumberRoman(i: Int,j: Int): Int{
        var number_digit = 0
        for (x in i..j){
            val number_roman = toRoman(x)
            if (number_roman.length > number_digit)
                number_digit = number_roman.length
        }
        return number_digit
    }

    private fun showMessage(message: String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    /**
     * Método para convertir numeros enteros a numero romanos
     */
    private fun toRoman(number: Int): String{
        val l = map.floorKey(number)
        return if (number == l)
            map[number].toString()
        else
            map[l] + toRoman(number-l)
    }

    private fun maps(){
        map[1000] = "M";
        map[900] = "CM";
        map[500] = "D";
        map[400] = "CD";
        map[100] = "C";
        map[90] = "XC";
        map[50] = "L";
        map[40] = "XL";
        map[10] = "X";
        map[9] = "IX";
        map[5] = "V";
        map[4] = "IV";
        map[1] = "I";
    }
}