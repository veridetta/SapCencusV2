package com.example.sapcencuskotlin.api

import com.example.sapcencuskotlin.model.DataModel
import com.example.sapcencuskotlin.model.DataPenduduk
import com.example.sapcencuskotlin.model.GetModel
import com.example.sapcencuskotlin.model.ResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/api/get.php?api_key=87Y78GF78SHFDSHFU")
    fun getJenisKelamin(): Call<GetModel>

    @GET("/api/get_penduduk.php?api_key=87Y78GF78SHFDSHFU")
    fun getPenduduk(): Call<DataModel>
    @GET("/api/get_penduduk_detail.php?api_key=87Y78GF78SHFDSHFU")
    fun getPendudukDetail(@Query("nik") nik: String): Call<DataPenduduk>

    @FormUrlEncoded
    @POST("/api/create_penduduk.php?api_key=87Y78GF78SHFDSHFU")
    fun postData(
        @Field("nik") qKtp: String,
        @Field("nama") qNama: String,
        @Field("sex") qJk: String,
        @Field("agama_id") qAgama: String,
        @Field("tempatlahir") qTempat: String,
        @Field("tanggallahir") qTanggal: String,
        @Field("pekerjaan_id") qPekerjaan: String,
        @Field("warganegara_id") qWni: String,
        @Field("qRt") qRt: String,
        @Field("qRw") qRw: String,
        @Field("status_kawin") qStatus: String,
        @Field("golongan_darah_id") qGoldar: String,
        @Field("alamat") qAlamat: String,
        @Field("id_cluster") qKelurahan: String,
        @Field("qKecamatan") qKecamatan: String,
        @Field("hubungan_keluarga") qHubunganKeluarga: String,
        @Field("pendidikan_kk_id") qPendidikan: String,
        @Field("nama_ayah") qAayah: String,
        @Field("nama_ibu") qIbu: String,
        @Field("hubung_warga") qHubung: String,

    ): Call<ResponseModel>


    @FormUrlEncoded
    @POST("/api/update_penduduk.php?api_key=87Y78GF78SHFDSHFU")
    fun updateData(
        @Field("nik") nik: String,
        @Field("nama") nama: String,
        @Field("ktp_el") ktp_el: String,
        @Field("status_rekam") status_rekam: String,
        @Field("tag_id_card") tag_id_card: String,
        @Field("tempat_cetak_ktp") tempat_cetak_ktp: String,
        @Field("tanggal_cetak_ktp") tanggal_cetak_ktp: String,
        @Field("no_kk_sebelumnya") no_kk_sebelumnya: String,
        @Field("kk_level") kk_level: String,
        @Field("id_sex") id_sex: String,
        @Field("agama_id") agama_id: String,
        @Field("id_status") id_status: String,

        //data kelahiran
        @Field("akta_lahir") akta_lahir: String,
        @Field("tempatlahir") tempatlahir: String,
        @Field("tanggallahir") tanggallahir: String,
        @Field("waktu_lahir") waktu_lahir: String,
        @Field("tempat_dilahirkan") tempat_dilahirkan: String,
        @Field("jenis_kelahiran") jenis_kelahiran: String,
        @Field("kelahiran_anak_ke") kelahiran_anak_ke: String,
        @Field("penolong_kelahiran") penolong_kelahiran: String,
        @Field("berat_lahir") berat_lahir: String,
        @Field("panjang_lahir") panjang_lahir: String,

        //pendidikan dan pekerjaan
        @Field("pendidikan_kk_id") pendidikan_kk_id: String,
        @Field("pendidikan_sedang_id") pendidikan_sedang_id: String,
        @Field("pekerjaan_id") pekerjaan_id: String,

        //kewarganegaraan
        @Field("suku") suku: String,
        @Field("warganegara_id") warganegara_id: String,
        @Field("dokumen_pasport") dokumen_pasport: String,
        @Field("tanggal_akhir_paspor") tanggal_akhir_paspor: String,
        @Field("dokumen_kitas") dokumen_kitas: String,
        @Field("negara_asal") negara_asal: String,

        //Orang tua
        @Field("ayah_nik") ayah_nik: String,
        @Field("nama_ayah") nama_ayah: String,
        @Field("ibu_nik") ibu_nik: String,
        @Field("nama_ibu") nama_ibu: String,

        //alamat
        @Field("alamat") alamat: String,
        @Field("dusun") dusun: String,
        @Field("rw") rw: String,
        @Field("id_cluster") id_cluster: String,
        @Field("alamat_sebelumnya") alamat_sebelumnya: String,
        @Field("telepon") telepon: String,
        @Field("email") email: String,
        @Field("telegram") telegram: String,
        @Field("hubung_warga") hubung_warga: String,

        //Perkawinan
        @Field("status_kawin") status_kawin: String,
        @Field("akta_perkawinan") akta_perkawinan: String,
        @Field("tanggalperkawinan") tanggalperkawinan: String,
        @Field("akta_perceraian") akta_perceraian: String,
        @Field("tanggalperceraian") tanggalperceraian: String,

        //kesehatan
        @Field("golongan_darah_id") golongan_darah_id: String,
        @Field("cacat_id") cacat_id: String,
        @Field("sakit_menahun_id") sakit_menahun_id: String,
        @Field("cara_kb_id") cara_kb_id: String,
        @Field("id_asuransi") id_asuransi: String,
        @Field("no_asuransi") no_asuransi: String,
        @Field("bpjs_ketenagakerjaan") bpjs_ketenagakerjaan: String,
        @Field("hamil") hamil: String,

        //lainnya
        @Field("bahasa_id") bahasa_id: String,
        @Field("ket") ket: String,

        ): Call<ResponseModel>
}
