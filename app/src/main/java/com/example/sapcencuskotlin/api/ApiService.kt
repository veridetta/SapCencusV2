package com.example.sapcencuskotlin.api

import com.example.sapcencuskotlin.model.GetModel
import com.example.sapcencuskotlin.model.ResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/get.php?api_key=87Y78GF78SHFDSHFU")
    fun getJenisKelamin(): Call<GetModel>

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
}
