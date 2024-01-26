package com.example.sapcencuskotlin.helper

import android.R
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.sapcencuskotlin.model.UserModel
import com.google.android.material.snackbar.Snackbar

fun levenshteinDistance(s1: String, s2: String): Int {
    val dp = Array(s1.length + 1) { IntArray(s2.length + 1) { 0 } }

    for (i in 0..s1.length) {
        for (j in 0..s2.length) {
            when {
                i == 0 -> dp[i][j] = j
                j == 0 -> dp[i][j] = i
                else -> {
                    dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                        dp[i - 1][j - 1]
                    } else {
                        1 + minOf(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1])
                    }
                }
            }
        }
    }

    return dp[s1.length][s2.length]
}

fun initSpinner(sp: Spinner, genderOptions: Array<String>){
    val adapter = ArrayAdapter(sp.context, R.layout.simple_spinner_item, genderOptions)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    sp.adapter = adapter
}

fun cekSimilarity(sp: Spinner, genderOptions: Array<String>, userInput: String) {
    val threshold = 2 // Atur threshold sesuai kebutuhan
    var closestMatch: String? = null
    var minDistance = Int.MAX_VALUE

    for (option in genderOptions) {
        val distance = levenshteinDistance(userInput, option)
        if (distance < minDistance && distance <= threshold) {
            minDistance = distance
            closestMatch = option
        }
    }

    if (closestMatch != null) {
        val selectedIndex = genderOptions.indexOf(closestMatch)
        if (selectedIndex != -1) {
            sp.setSelection(selectedIndex)
            println("Kemungkinan yang dipilih adalah: $closestMatch")
        } else {
            println("Tidak dapat menemukan indeks opsi yang cocok.")
        }
    } else {
        println("Tidak ada yang cocok atau tidak ada kesamaan yang mencukupi.")
        sp.setSelection(0)
    }
}

fun showSnackbar(context: Context, message: String) {
    val activity = context as AppCompatActivity
    val view = activity.findViewById<View>(android.R.id.content)
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}

fun saveUser(context: Context, paketModel: UserModel) {
    val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("dataDocid", paketModel.docid)
    editor.putString("dataPin", paketModel.pin)
    editor.putString("dataName", paketModel.name)
    editor.putString("dataRole", paketModel.role)
    editor.putString("dataUid", paketModel.uid)
    editor.putBoolean("dataIsLogin", paketModel.isLogin)
    editor.apply()
}
fun getUser(context: Context): UserModel {
    val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
    val paketModel = UserModel()
    paketModel.docid = sharedPreferences.getString("dataDocid", "")
    paketModel.pin = sharedPreferences.getString("dataPin", "")
    paketModel.name = sharedPreferences.getString("dataName", "")
    paketModel.role = sharedPreferences.getString("dataRole", "")
    paketModel.uid = sharedPreferences.getString("dataUid", "")
    paketModel.isLogin = sharedPreferences.getBoolean("dataIsLogin", false)
    return paketModel
}
fun clearUser(context: Context) {
    val sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}

fun saveTempUser(context: Context, paketModel: UserModel) {
    val sharedPreferences = context.getSharedPreferences("TEMPUSER", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("dataDocid", paketModel.docid)
    editor.putString("dataPin", paketModel.pin)
    editor.putString("dataName", paketModel.name)
    editor.putString("dataRole", paketModel.role)
    editor.putString("dataUid", paketModel.uid)
    editor.putBoolean("dataIsLogin", paketModel.isLogin)
    editor.apply()
}
fun getTempUser(context: Context): UserModel {
    val sharedPreferences = context.getSharedPreferences("TEMPUSER", Context.MODE_PRIVATE)
    val paketModel = UserModel()
    paketModel.docid = sharedPreferences.getString("dataDocid", "")
    paketModel.pin = sharedPreferences.getString("dataPin", "")
    paketModel.name = sharedPreferences.getString("dataName", "")
    paketModel.role = sharedPreferences.getString("dataRole", "")
    paketModel.uid = sharedPreferences.getString("dataUid", "")
    paketModel.isLogin = sharedPreferences.getBoolean("dataIsLogin", false)
    return paketModel
}
fun clearTempUser(context: Context) {
    val sharedPreferences = context.getSharedPreferences("TEMPUSER", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}
