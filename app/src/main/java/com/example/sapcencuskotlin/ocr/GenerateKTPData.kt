package com.example.sapcencuskotlin.ocr

import android.text.TextUtils
import android.util.Log
import com.google.mlkit.vision.text.Text
import java.util.Arrays
import java.util.Locale

class GenerateKTPData {
    interface Listener {
        fun finishScan()
    }

    private lateinit var resultScan: List<String>
    private lateinit var text: Text

    // Helper
    private val identifierChecker = IdentifierChecker()

    private val ocrValidator = OCRValidator()

    // Header
    private var provinsi = ""
    private var kabupatenKota = ""

    // Store data here
    private val nik = KTPData()
    private val nama = KTPData()
    private val tempatLahir = KTPData()
    private val tanggalLahir = KTPData()
    private val jenisKelamin = KTPData()
    private val alamat = KTPData()
    private val rtRw = KTPData()
    private val kelDesa = KTPData()
    private val kecamatan = KTPData()
    private val agama = KTPData()
    private val statusPerkawinan = KTPData()
    private val pekerjaan = KTPData()
    private val kewarganegaraan = KTPData()
    private val berlakuHingga = KTPData()
    private val golonganDarah = KTPData()
    private var isNikFound = false
    private var isNamaFound = false
    private var isTempatLahirFound = false
    private var isTanggalLahirFound = false
    private var isJenisKelaminFound = false
    private var isAlamatFound = false
    private var isRtRwFound = false
    private var isKelDesaFound = false
    private var isKecamatanFound = false
    private var isAgamaFound = false
    private var isStatusPerkawinanFound = false
    private var isGolonganDarahFound = false

    var identifiers: MutableList<String> = ArrayList()
    private var lastIndexIdentifier = 0
    private var lastIndexValue = 0

    constructor(
        resultScan: List<String>,
        listener: Listener
    ) {
        println(resultScan.toTypedArray().contentToString())
        this.resultScan = resultScan
        mappingData(listener)
    }
    private fun isNotIdentifier(data: String): Boolean {
        val result = (data.equals(
            "nik",
            ignoreCase = true
        ) || data == "Nama" || identifierChecker.isIdentifierTempatTanggalLahir(data)
                || identifierChecker.isIdentifierJenisKelamin(data) || identifierChecker.isIdentifierAlamat(
            data
        )
                || identifierChecker.isIdentifierRtRw(data) || identifierChecker.isIdentifierKelDesa(
            data
        )
                || identifierChecker.isIdentifierKecamatan(data) || identifierChecker.isIdentifierAgama(
            data
        )
                || identifierChecker.isIdentifierStatusPerkawinan(data) || identifierChecker.isIdentifierGolDarah(
            data
        )
                || data.lowercase(Locale.getDefault())
            .contains("provinsi") || identifierChecker.isIdentifierKabKota(data)
                || identifierChecker.isNotIdentifierPekerjaan(data) || identifierChecker.isNotIdentifierBerlakuHingga(
            data
        )
                || identifierChecker.isNotIdentifierKewarganegaraan(data))
        return !result
    }
    private fun mappingDataWithText(listener: Listener) {
        for (block in text!!.textBlocks) {
            for (line in block.lines) {
                val data = line.text
                if (getNIKData(data)) continue
                if (getNamaData(data)) continue
                if (getTempatLahirData(data)) continue
                if (getTanggalLahirData(data)) continue
                if (getJenisKelaminData(data)) continue
                if (getAlamatData(data)) continue
                if (getRtRwData(data)) continue
                if (getKelDesaData(data)) continue
                if (getKecamatanData(data)) continue
                if (getAgamaData(data)) continue
                if (getStatusPerkawinanData(data)) continue
                if (getProvinsiData(data)) continue
                if (getKabupatenKotaData(data)) continue
                getGolonganDarahData(data)
            }
        }
        validateData(listener)
    }
    private fun mappingData(listener: Listener) {
        // Can be converted into List<Text.Line> instead of List<String> to reduce looping
        for (data in resultScan!!) {
            if (getNIKData(data)) continue
            if (getNamaData(data)) continue
            if (getTempatLahirData(data)) continue
            if (getTanggalLahirData(data)) continue
            if (getJenisKelaminData(data)) continue
            if (getAlamatData(data)) continue
            if (getRtRwData(data)) continue
            if (getKelDesaData(data)) continue
            if (getKecamatanData(data)) continue
            if (getAgamaData(data)) continue
            if (getStatusPerkawinanData(data)) continue
            if (getProvinsiData(data)) continue
            if (getKabupatenKotaData(data)) continue
            getGolonganDarahData(data)
        }
        validateData(listener)
    }
    private fun validateData(listener: Listener) {
        ocrValidator.validateData(
            nik.value, tanggalLahir.value, rtRw.value, golonganDarah.value,
            jenisKelamin.value, object : OCRValidator.ValueSetter{
                override fun setNikValue(data: String?){
                    nik.value=data.toString()
                }

                override fun setTanggalLahirValue(data: String?) {
                    tanggalLahir.value=data.toString()
                }

                override fun setRtRwValue(data: String?) {
                    rtRw.value=data.toString()
                }

                override fun setGolonganDarahValue(data: String?) {
                    golonganDarah.value=data.toString()
                }

                override fun setJenisKelaminValue(data: String?) {
                    jenisKelamin.value=data.toString()
                }

                override fun finishAll() {
                    printResult()
                    listener.finishScan()
                }
            })
    }
    private fun printResult() {
        System.out.println("NIK " + nik.value)
        System.out.println("NAMA " + nama.value)
        System.out.println("TEMPAT LAHIR " + tempatLahir.value)
        System.out.println("TANGGAL LAHIR " + tanggalLahir.value)
        System.out.println("JENIS KELAMIN " + jenisKelamin.value)
        System.out.println("ALAMAT " + alamat.value)
        System.out.println("RT/RW " + rtRw.value)
        System.out.println("KEL/DESA " + kelDesa.value)
        System.out.println("KECAMATAN " + kecamatan.value)
        System.out.println("AGAMA " + agama.value)
        System.out.println("STATUS PERKAWINAN " + statusPerkawinan.value)
        println("PROVINSI $provinsi")
        println("KABUPATEN/KOTA $kabupatenKota")
        System.out.println("GOLONGAN DARAH " + golonganDarah.value)
        println(Arrays.toString(identifiers.toTypedArray()))
    }
    private fun checkIdentifier(identifier: String): Boolean {
        when (identifier) {
            "nik" -> return isNikFound
            "nama" -> return isNamaFound
            "ttl" -> return isTempatLahirFound
            "alamat" -> return isAlamatFound
            "rtrw" -> return isRtRwFound
            "keldesa" -> return isKelDesaFound
            "kec" -> return isKecamatanFound
            "agama" -> return isAgamaFound
            "sp" -> return isStatusPerkawinanFound
            "gd" -> return isGolonganDarahFound
        }
        return false
    }

    private fun isPreviousAndNextFound(identifier: String): Boolean {
        val index = identifiers.indexOf(identifier)
        var prev = false
        var next = false
        val lastIndex = identifiers.size - 1
        if (index < 0) return false
        if (index > 0) {
            val idtf = identifiers[index - 1]
            prev = checkIdentifier(idtf)
        }
        if (index < lastIndex) {
            val idtf = identifiers[index + 1]
            next = checkIdentifier(idtf)
        }
        return prev || next
    }

    // Scpecial field : Jenis Kelamin, Golongan Darah, Agama
    private fun exceptionForSpecialField(data: String): Boolean {
        var data = data
        data = data.lowercase(Locale.getDefault())
        val result = (ocrValidator.isValidAgama(data) || ocrValidator.isValidRtRw(data)
                || ocrValidator.isValidJenisKelamin(data) || ocrValidator.isValidGolDarah(data)
                || ocrValidator.isPossiblePekerjaan(data) || ocrValidator.isPossibleMasaBerlaku(data)
                || data.endsWith("kawin") || data == "wni")
        return !result
    }

    // Check if data contains any number

    // Check if data contains any number
    private fun getNIKData(data: String): Boolean {
        if (isNikFound) return false // skip process if nik is already found
        val isNumberExist = ocrValidator.isNumberExist(data)
        // check if data is equal to nik with case ignored
        if (data.equals("nik", ignoreCase = true) && nik.index < 0) {
            nik.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("nik")
            return true
        }
        // check if first character is ':' and if length of character is 17 with deleted space if exist
        if (data.toCharArray()[0] == ':' && data.replace(" ", "").length == 17) {
            // check if data contains any number
            if (isNumberExist) {
                // set a value to nik, change nik found to true and update last index for value
                nik.value = data.replace(":", "").replace(" ", "")
                isNikFound = true
                lastIndexValue++
                return true
            }
        }
        // check if data with space removed and no ':' found, length is 16 and contains any number
        if (data.replace(" ", "").length == 16 && isNumberExist) {
            nik.value = data.replace(" ", "")
            isNikFound = true
            lastIndexValue++
            return true
        }
        return false
    }

    private fun getNamaData(data: String): Boolean {
        if (isNamaFound) return false // skip process if nama is already found
        if (data == "Nama" && nama.index < 0) {
            nama.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("nama")
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == nama.index && exceptionForSpecialField(
                data
            )
        ) {
            nama.value=data.replace(":", "")
            lastIndexValue++
            isNamaFound = true
            return true
        }
        if (isNotIdentifier(data) && exceptionForSpecialField(data) && !ocrValidator.isPossibleAlamat(
                data
            )
            && !ocrValidator.isPossibleDate(data)
        ) {
            if (identifiers.indexOf("nama") == lastIndexValue && !data.contains(":")) {
                nama.value=data
                lastIndexValue++
                isNamaFound = true
                return true
            } else if (isPreviousAndNextFound("nama")) {
                nama.value=data
                lastIndexValue++
                isNamaFound = true
                return true
            }
        }
        return false
    }

    private fun getTempatLahirData(data: String): Boolean {
        if (data.length < 11) return false
        val isTempatTglLahir = identifierChecker.isIdentifierTempatTanggalLahir(data)
        if (isTempatLahirFound) return false
        if (isTempatTglLahir && !data.contains(":") && tempatLahir.index < 0 && data.length <= 16) {
            tempatLahir.index=lastIndexIdentifier
            tanggalLahir.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("ttl")
            return true
        }
        if (isTempatTglLahir && data.contains(":")) {
            val ttlLengkap =
                data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            if (data.contains("-") && data.contains(",")) {
                val ttl = ttlLengkap.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                tempatLahir.value=ttl[0]
                tanggalLahir.value=ttl[1]
                isTanggalLahirFound = true
            } else {
                tempatLahir.value=ttlLengkap
            }
            isTempatLahirFound = true
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == tempatLahir.index && exceptionForSpecialField(
                data
            )
        ) {
            if (data.contains(",")) {
                val ttl = data.replace(":", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                tempatLahir.value=ttl[0]
                tanggalLahir.value=ttl[1]
                isTanggalLahirFound = true
            } else {
                tempatLahir.value=data.replace(":", "")
            }
            lastIndexValue++
            isTempatLahirFound = true
            return true
        }
        if (!data.contains(":") && data.lowercase(Locale.getDefault())
                .contains("lahir") && data.lowercase(
                Locale.getDefault()
            ).split("lahir".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().size > 1
        ) {
            val ttl = data.lowercase(Locale.getDefault()).split("lahir".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (ttl[1].contains("-") && ttl[1].contains(",")) {
                val ttlNow = ttl[1].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                tempatLahir.value=ttlNow[0].uppercase(Locale.getDefault())
                tanggalLahir.value=ttlNow[1].replace(" ", "")
                isTanggalLahirFound = true
            } else {
                tempatLahir.value=ttl[1].uppercase(Locale.getDefault())
            }
            isTempatLahirFound = true
            return true
        }
        if (data.contains(",") && data.contains("-")) {
            val ttl = data.replace(":", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            tempatLahir.value=ttl[0]
            tanggalLahir.value=ttl[1].replace(" ", "")
            isTempatLahirFound = true
            isTanggalLahirFound = true
            lastIndexValue++
            return true
        }
        if (data.toCharArray()[0] != ':' && data.contains(":") && data.contains(",") && isTempatTglLahir) {
            val ttl = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            tempatLahir.value=ttl[0]
            tanggalLahir.value=ttl[1]
            isTempatLahirFound = true
            isTanggalLahirFound = true
            return true
        }
        return false
    }

    private fun getTanggalLahirData(data: String): Boolean {
        if (isTanggalLahirFound) return false
        if (lastIndexValue - 1 == tanggalLahir.index && data.split("-".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray().size == 3 && isTempatLahirFound
        ) {
            tanggalLahir.value=data
            isTanggalLahirFound = true
            return true
        }
        if (!data.contains(":") && data.contains("-") && ocrValidator.isNumberExist(data) && data.split(
                "-".toRegex()
            ).dropLastWhile { it.isEmpty() }
                .toTypedArray().size == 3
        ) {
            tanggalLahir.value=data
            isTanggalLahirFound = true
            return true
        }
        return false
    }

    private fun getJenisKelaminData(data: String): Boolean {
        if (isJenisKelaminFound) return false
        if (data.length < 3) return false
        val isIdentifier = identifierChecker.isIdentifierJenisKelamin(data)
        val isValue = ocrValidator.isValidJenisKelamin(data)
        Log.d("Jenis Kelamin ", "getJenisKelaminData:  $data")
        if (data.toCharArray()[0] != ':' && isIdentifier && jenisKelamin.index < 0) {
            jenisKelamin.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("jk")
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == jenisKelamin.index && isValue) {
            jenisKelamin.value=data.replace(":", "")
            lastIndexValue++
            isJenisKelaminFound = true
            return true
        }
        if (isValue) {
            jenisKelamin.value=data
            Log.d("jenis kelamin ada ","$data")
            if (jenisKelamin.index > -1) lastIndexValue++
            isJenisKelaminFound = true
            return true
        }

        return false
    }

    private fun getAlamatData(data: String): Boolean {
        if (isAlamatFound) return false
        val isIdentifier = identifierChecker.isIdentifierAlamat(data)
        if (data.toCharArray()[0] != ':' && isIdentifier && alamat.index < 0) {
            alamat.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("alamat")
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == alamat.index && exceptionForSpecialField(
                data
            )
        ) {
            alamat.value=data.replace(":", "")
            lastIndexValue++
            isAlamatFound = true
            return true
        }
        if (isNotIdentifier(data) && exceptionForSpecialField(data)) {
            val dt = if (data.contains(":")) data.replace(":", "") else data
            if (ocrValidator.isPossibleAlamat(data)) {
                alamat.value=dt
                lastIndexValue++
                isAlamatFound = true
                return true
            } else if (lastIndexValue == alamat.index) {
                alamat.value=dt
                lastIndexValue++
                isAlamatFound = true
                return true
            } else if (isPreviousAndNextFound("alamat")) {
                alamat.value=dt
                lastIndexValue++
                isAlamatFound = true
                return true
            }
        }
        return false
    }

    private fun getRtRwData(data: String): Boolean {
        if (isRtRwFound) return false
        val isIdentifier = identifierChecker.isIdentifierRtRw(data)
        if (isIdentifier && !data.contains(":") && rtRw.index < 0) {
            rtRw.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("rtrw")
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == rtRw.index) {
            rtRw.value=data.replace(":", "")
            lastIndexValue++
            isRtRwFound = true
            return true
        }
        if (ocrValidator.isValidRtRw(data)) {
            rtRw.value=data
            if (rtRw.index > -1) lastIndexValue++
            isRtRwFound = true
            return true
        }
        return false
    }

    private fun getKelDesaData(data: String): Boolean {
        if (isKelDesaFound) return false
        if (data.length < 4) return false
        val isIdentifier = identifierChecker.isIdentifierKelDesa(data)
        if (isIdentifier && kelDesa.index < 0) {
            kelDesa.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("kd")
            return true
        }
        if (isNotIdentifier(data) && exceptionForSpecialField(data)
            && !ocrValidator.isPossibleDate(data) && !ocrValidator.isPossibleAlamat(data)
        ) {
            val dt = if (data.contains(":")) data.replace(":", "") else data
            if (lastIndexValue == kelDesa.index) {
                kelDesa.value=dt
                lastIndexValue++
                isKelDesaFound = true
                return true
            } else if (isPreviousAndNextFound("kd")) {
                kelDesa.value=data.replace(":", "")
                lastIndexValue++
                isKelDesaFound = true
                return true
            }
        }
        //        if (data.toCharArray()[0] == ':' && lastIndexValue == kelDesa.index && exceptionForSpecialField(data)){
//            kelDesa.value=data.replace(":", ""));
//            lastIndexValue++;
//            isKelDesaFound = true;
//            return true;
//        }
        return false
    }

    private fun getKecamatanData(data: String): Boolean {
        if (isKecamatanFound) return false
        val isIdentifier = identifierChecker.isIdentifierKecamatan(data)
        if (isIdentifier && kecamatan.index < 0 && !data.contains(":")) {
            kecamatan.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("kec")
            return true
        }
        if (data.toCharArray()[0] == ':' && lastIndexValue == kecamatan.index && exceptionForSpecialField(
                data
            )
        ) {
            kecamatan.value=data.replace(":", "")
            lastIndexValue++
            isKecamatanFound = true
            return true
        }
        if (isNotIdentifier(data) && exceptionForSpecialField(data) && !ocrValidator.isPossibleDate(
                data
            )
            && !ocrValidator.isPossibleAlamat(data)
        ) {
            val dt = if (data.contains(":")) data.replace(":", "") else data
            if (lastIndexValue == kecamatan.index || lastIndexValue == identifiers.indexOf("kec")) {
                kecamatan.value=dt
                lastIndexValue++
                isKecamatanFound = true
                return true
            } else if (isPreviousAndNextFound("kec")) {
                kecamatan.value=data.replace(":", "")
                lastIndexValue++
                isKecamatanFound = true
                return true
            }
        }
        return false
    }

    private fun getAgamaData(data: String): Boolean {
        var data = data
        if (isAgamaFound) return false
        if (data.length < 2) return false
        val isIdentifier = identifierChecker.isIdentifierAgama(data)
        if (isIdentifier && agama.index < 0) {
            agama.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("agama")
            return true
        }
        val isNormalReligion = ocrValidator.isValidAgama(data)
        if (isNormalReligion) {
            data = data.replace(" ", "")
            data = data.replace(":", "")
            agama.value=data
            if (agama.index > -1) lastIndexValue++
            isAgamaFound = true
            return true
        }
        return false
    }

    private fun getStatusPerkawinanData(data: String): Boolean {
        if (isStatusPerkawinanFound) return false
        val isIdentifier = identifierChecker.isIdentifierStatusPerkawinan(data)
        if (isIdentifier && !data.contains(":") && statusPerkawinan.index < 0 && !data.lowercase(
                Locale.getDefault()
            ).endsWith("kawin")
        ) {
            statusPerkawinan.index=lastIndexIdentifier
            lastIndexIdentifier++
            identifiers.add("sp")
            return true
        }
        if (data.lowercase(Locale.getDefault()).endsWith("kawin")) {
            var dt = ""
            if (data.contains(":")) {
                dt = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            } else {
                if (data.lowercase(Locale.getDefault())
                        .startsWith("belum") || data.lowercase(Locale.getDefault())
                        .startsWith("kawin")
                ) {
                    dt = data
                } else {
                    val spkw = data.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (spkw.size > 2) dt = if (spkw.size > 3) spkw[2] + spkw[3] else spkw[2]
                }
            }
            if (!dt.isEmpty()) {
                statusPerkawinan.value=dt
                if (statusPerkawinan.index > -1) lastIndexValue++
                isStatusPerkawinanFound = true
            }
            return true
        }
        //        if (isIdentifier && data.contains(":")){
//            statusPerkawinan.value=data.split(":")[1]);
//            isStatusPerkawinanFound = true;
//            return true;
//        }
//        if (isIdentifier && data.split(" ").length > 2){
//            String[] stPks = data.split(" ");
//            String stPk = stPks.length == 3 ? stPks[2] : stPks[2]+stPks[3];
//            statusPerkawinan.value=stPk);
//            isStatusPerkawinanFound = true;
//            return true;
//        }
        return false
    }

    private fun getProvinsiData(data: String): Boolean {
        if (!provinsi.isEmpty()) return false
        val isProvinsi = data.lowercase(Locale.getDefault()).contains("provinsi")
        if (isProvinsi) {
            val provinsiDt = data.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val removeFirst = Arrays.copyOfRange(provinsiDt, 1, provinsiDt.size)
            provinsi = TextUtils.join(" ", removeFirst)
            return true
        }
        return false
    }

    private fun getKabupatenKotaData(data: String): Boolean {
        if (!kabupatenKota.isEmpty()) return false
        val isKabupatenKota = identifierChecker.isIdentifierKabKota(data)
        if (isKabupatenKota) {
//            String[] kabKotaDt = data.split(" ");
//            String[] removeFirst = Arrays.copyOfRange(kabKotaDt, 1, kabKotaDt.length);
//            kabupatenKota = TextUtils.join(" ", removeFirst);
            kabupatenKota = data
            return true
        }
        if (data.lowercase(Locale.getDefault()).contains("jakarta")) {
            kabupatenKota = data
            return true
        }
        return false
    }

    private fun getGolonganDarahData(data: String) {
        if (isGolonganDarahFound) return
        val isIdentifier = identifierChecker.isIdentifierGolDarah(data)
        if (isIdentifier && !data.contains(":") && golonganDarah.index < 0 && data.endsWith("darah")) {
            if (golonganDarah.index < 0) {
                golonganDarah.index=lastIndexIdentifier
                lastIndexIdentifier++
                identifiers.add("gd")
                return
            }
        }
        val splitData = data.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        if (isIdentifier && splitData.size > 2) {
            if (data.contains(":")) {
                golonganDarah.value=data.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].replace(" ", "")
            } else {
                golonganDarah.value=splitData[2]
            }
            isGolonganDarahFound = true
        }
    }

    fun getProvinsi(): String? {
        return provinsi
    }

    fun getKabupatenKota(): String? {
        return kabupatenKota
    }

    fun getNik(): KTPData? {
        return nik
    }

    fun getNama(): KTPData? {
        return nama
    }

    fun getTempatLahir(): KTPData? {
        return tempatLahir
    }

    fun getTanggalLahir(): KTPData? {
        return tanggalLahir
    }

    fun getJenisKelamin(): KTPData? {
        return jenisKelamin
    }

    fun getAlamat(): KTPData? {
        return alamat
    }

    fun getRtRw(): KTPData? {
        return rtRw
    }

    fun getKelDesa(): KTPData? {
        return kelDesa
    }

    fun getKecamatan(): KTPData? {
        return kecamatan
    }

    fun getAgama(): KTPData? {
        return agama
    }

    fun getStatusPerkawinan(): KTPData? {
        return statusPerkawinan
    }

    fun getPekerjaan(): KTPData? {
        return pekerjaan
    }

    fun getKewarganegaraan(): KTPData? {
        return kewarganegaraan
    }

    fun getBerlakuHingga(): KTPData? {
        return berlakuHingga
    }

    fun getGolonganDarah(): KTPData? {
        return golonganDarah
    }
}