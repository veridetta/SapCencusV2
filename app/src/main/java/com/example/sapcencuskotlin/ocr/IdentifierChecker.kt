package com.example.sapcencuskotlin.ocr

import java.util.Locale

class IdentifierChecker {
    fun isIdentifierTempatTanggalLahir(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return (data == "tempat/tgl lahir" || data.contains("tempa")
                || data.contains("tgl")
                || data.contains("lahir"))
    }

    fun isIdentifierJenisKelamin(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "jenis kelamin" || data.startsWith("jenis") || data.endsWith("kelamin") || data.startsWith(
            "j"
        ) && data.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[0].length == 5
    }

    fun isIdentifierAlamat(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data.contains("alamat") || data.contains("ala") && data.length == 7
    }

    fun isIdentifierRtRw(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return (data == "rt/rw" || data.startsWith("rt")
                || data.endsWith("rw"))
    }

    fun isIdentifierKelDesa(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return (data == "kel/desa" || data.startsWith("kel")
                || data.endsWith("desa"))
    }

    fun isIdentifierKecamatan(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "kecamatan" || data.startsWith("kec") || data.endsWith("matan")
    }

    fun isIdentifierAgama(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return (data == "agama" || data.startsWith("aga")
                || data.endsWith("ma"))
    }

    fun isIdentifierStatusPerkawinan(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return (data == "status perkawinan" || data.startsWith("status")
                || data.endsWith("perkawinan"))
    }

    fun isIdentifierKabKota(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data.contains("kabupaten") || data.contains("kota")
    }

    fun isIdentifierGolDarah(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "gol. darah" ||
                data.contains("gol.") || data.contains("darah")
    }

    fun isNotIdentifierPekerjaan(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "pekerjaan" || data.startsWith("peker") || data.endsWith("kerjaan")
    }

    fun isNotIdentifierBerlakuHingga(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "berlaku hingga" || data.startsWith("berlaku") || data.endsWith("hingga")
    }

    fun isNotIdentifierKewarganegaraan(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        return data == "kewarganegaraan" || data.startsWith("kewarga") || data.endsWith("negaraan")
    }
}