package com.example.sapcencuskotlin.ocr

import java.util.Locale
import java.util.regex.Pattern

 class OCRValidator {
     fun isNumberExist(data: String): Boolean {
        val pattern = Pattern.compile(".*\\d.*")
        val matcher = pattern.matcher(data)
        return matcher.find()
    }
     fun isValidJenisKelamin(data:String):Boolean {
        //lowercase and replace data
        val dataLowerCase = data.toLowerCase()
        val dataReplace = dataLowerCase.replace(":", "")
        return (dataReplace.contains("laki-laki") || dataReplace.contains("perempuan")
                || dataReplace.startsWith("lak") || dataReplace.contains("aki-lak") || dataReplace.endsWith("laki")
                || dataReplace.startsWith("per") || dataReplace.endsWith("rempuan"))
    }
    fun isValidRtRw(data: String): Boolean {
        return data.contains("/") && data.replace(":", "").length == 7 && isNumberExist(data)
    }
     fun isValidAgama(data: String): Boolean {
         var data = data
         data = data.lowercase(Locale.getDefault()).replace(":", "")
         data = data.replace(" ", "")
         return data == "islam" || data == "kristen" || data == "katolik" || data == "budha" || data == "hindu" || data == "konghuchu"
     }
     fun isValidGolDarah(data: String): Boolean {
         var data = data
         data = data.lowercase(Locale.getDefault()).replace(":", "")
         return data == "a" || data == "b" || data == "ab" || data == "o" || data == "0" || data == "4" || data == "48" || data == "4b" || data == "a8"
     }
     fun isPossibleAlamat(data: String): Boolean {
         var data = data
         data = data.lowercase(Locale.getDefault())
         return data.contains("jl.") || data.contains("jalan ") || data.contains("no.") || data.contains(
             "blok."
         ) || data.contains("blok")
     }
     fun isPossibleDate(data: String): Boolean {
         return data.contains("-") && data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
             .toTypedArray().size == 3 && isNumberExist(data)
     }
     fun isPossiblePekerjaan(data: String): Boolean {
         var data = data
         data = data.lowercase(Locale.getDefault())
         return (data.contains("pelajar") || data.contains("mahasiswa") || data.contains("karyawan")
                 || data.contains("bekerja"))
     }
     fun isPossibleMasaBerlaku(data: String): Boolean {
         var data = data
         data = data.lowercase(Locale.getDefault())
         return data == "seumur hidup" || data.startsWith("seumur") || data.endsWith("hidup")
     }
     fun validateData(
         nik: String, tanggalLahir: String, rtRw: String, golonganDarah: String,
         jenisKelamin: String, setter: ValueSetter
     ) {
         // NIK Validation
         var nik = nik
         var tanggalLahir = tanggalLahir
         var rtRw = rtRw
         var golonganDarah = golonganDarah
         var jenisKelamin = jenisKelamin
         val cari = arrayOf("l", "I", "z", "Z", "A", "S", "s", "b", "J", "B", "q", "o", "O", ")")
         val ganti = arrayOf("1", "1", "2", "2", "4", "5", "5", "6", "7", "8", "9", "0", "0", "1")
         for (i in cari.indices) {
             nik = nik.replace(cari[i], ganti[i])
         }
         setter.setNikValue(nik)
         // Tanggal lahir
         for (i in cari.indices) {
             tanggalLahir = tanggalLahir.replace(cari[i], ganti[i])
         }
         setter.setTanggalLahirValue(tanggalLahir)
         // RT/RW
         for (i in cari.indices) {
             rtRw = rtRw.replace(cari[i], ganti[i])
         }
         setter.setRtRwValue(rtRw)
         // Golongan Darah
         val cariAngka = arrayOf("4", "0", "8")
         val gantiHuruf = arrayOf("A", "O", "B")
         for (i in cariAngka.indices) {
             golonganDarah = golonganDarah.replace(cariAngka[i], gantiHuruf[i])
         }
         setter.setGolonganDarahValue(golonganDarah)
         // Jenis Kelamin
         if (jenisKelamin.lowercase(Locale.getDefault()).contains("lak")) {
             jenisKelamin = "LAKI-LAKI"
         } else if (jenisKelamin.lowercase(Locale.getDefault())
                 .contains("per") || jenisKelamin.lowercase(
                 Locale.getDefault()
             ).contains("puan") || jenisKelamin.contains("rem")
         ) {
             jenisKelamin = "PEREMPUAN"
         }
         setter.setJenisKelaminValue(jenisKelamin)
         setter.finishAll()
     }
     interface ValueSetter {
         fun setNikValue(data: String?)
         fun setTanggalLahirValue(data: String?)
         fun setRtRwValue(data: String?)
         fun setGolonganDarahValue(data: String?)
         fun setJenisKelaminValue(data: String?)
         fun finishAll()
     }
}