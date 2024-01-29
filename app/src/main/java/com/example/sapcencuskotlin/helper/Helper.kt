package com.example.sapcencuskotlin.helper

import android.R
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.sapcencuskotlin.model.KKModel
import com.example.sapcencuskotlin.model.KTPModel
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

fun saveKTP(context: Context, ktpModel: KTPModel) {
    val sharedPreferences = context.getSharedPreferences("KTP", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("dataNik", ktpModel.nik)
    editor.putString("dataNamaLengkap", ktpModel.nama_lengkap)
    editor.putString("dataJenisKelamin", ktpModel.jenis_kelamin)
    editor.putString("dataAgama", ktpModel.agama)
    editor.putString("dataTempatLahir", ktpModel.tempat_lahir)
    editor.putString("dataTanggalLahir", ktpModel.tanggal_lahir)
    editor.putString("dataPekerjaan", ktpModel.pekerjaan)
    editor.putString("dataStatusWni", ktpModel.status_wni)
    editor.putString("dataRw", ktpModel.rw)
    editor.putString("dataRt", ktpModel.rt)
    editor.putString("dataStatusKawin", ktpModel.status_kawin)
    editor.putString("dataGoldar", ktpModel.goldar)
    editor.putString("dataAlamat", ktpModel.alamat)
    //kelurahan dan kecamatan
    editor.putString("dataKelurahan", ktpModel.kelurahan)
    editor.putString("dataKecamatan", ktpModel.kecamatan)
    editor.apply()
}
fun getKTP(context: Context): KTPModel {
    val sharedPreferences = context.getSharedPreferences("KTP", Context.MODE_PRIVATE)
    val ktpModel = KTPModel()
    ktpModel.nik = sharedPreferences.getString("dataNik", "")
    ktpModel.nama_lengkap = sharedPreferences.getString("dataNamaLengkap", "")
    ktpModel.jenis_kelamin = sharedPreferences.getString("dataJenisKelamin", "")
    ktpModel.agama = sharedPreferences.getString("dataAgama", "")
    ktpModel.tempat_lahir = sharedPreferences.getString("dataTempatLahir", "")
    ktpModel.tanggal_lahir = sharedPreferences.getString("dataTanggalLahir", "")
    ktpModel.pekerjaan = sharedPreferences.getString("dataPekerjaan", "")
    ktpModel.status_wni = sharedPreferences.getString("dataStatusWni", "")
    ktpModel.rw = sharedPreferences.getString("dataRw", "")
    ktpModel.rt = sharedPreferences.getString("dataRt", "")
    ktpModel.status_kawin = sharedPreferences.getString("dataStatusKawin", "")
    ktpModel.goldar = sharedPreferences.getString("dataGoldar", "")
    ktpModel.alamat = sharedPreferences.getString("dataAlamat", "")
    //kelurahan dan kecamatan
    ktpModel.kelurahan = sharedPreferences.getString("dataKelurahan", "")
    ktpModel.kecamatan = sharedPreferences.getString("dataKecamatan", "")
    return ktpModel
}
fun clearKTP(context: Context) {
    val sharedPreferences = context.getSharedPreferences("KTP", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}
fun saveKK(context: Context, kkModel: KKModel){
    val sharedPreferences = context.getSharedPreferences("KK", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("dataHubunganKeluarga", kkModel.hubungan_keluarga)
    editor.putString("dataPendidikan", kkModel.pendidikan)
    editor.putString("dataNamaAyah", kkModel.nama_ayah)
    editor.putString("dataNamaIbu", kkModel.nama_ibu)
    editor.apply()
}
fun getKK(context: Context): KKModel {
    val sharedPreferences = context.getSharedPreferences("KK", Context.MODE_PRIVATE)
    val kkModel = KKModel()
    kkModel.hubungan_keluarga = sharedPreferences.getString("dataHubunganKeluarga", "")
    kkModel.pendidikan = sharedPreferences.getString("dataPendidikan", "")
    kkModel.nama_ayah = sharedPreferences.getString("dataNamaAyah", "")
    kkModel.nama_ibu = sharedPreferences.getString("dataNamaIbu", "")
    return kkModel
}
fun clearKK(context: Context) {
    val sharedPreferences = context.getSharedPreferences("KK", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.clear()
    editor.apply()
}
