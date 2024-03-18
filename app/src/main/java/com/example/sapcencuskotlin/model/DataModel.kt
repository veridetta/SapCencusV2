package com.example.sapcencuskotlin.model

data class DataModel (
    val dataPenduduk: List<DataPenduduk>,
    val jenisKelamin: List<DataJenisKelamin>,
    val agama: List<DataAgama>,
    val pekerjaan: List<DataPekerjaan>,
    val statusKawin: List<DataStatusKawin>,
    val golonganDarah: List<DataGolonganDarah>,
    val dusun: List<DataDusun>,
)

data class DataPenduduk(
    
    //data diri
    val id : String,
    val nik: String, 
    val nama: String, 
    val ktp_el: String, 
    val status_rekam: String, 
    val tag_id_card: String, 
    val tempat_cetak_ktp: String, 
    val tanggal_cetak_ktp: String,
    val no_kk_sebelumnya: String, 
    val kk_level: String, 
    val id_sex: String, 
    val agama_id: String, 
    val id_status: String, 

    //data kelahiran
    val akta_lahir: String, 
    val tempatlahir: String, 
    val tanggallahir: String, 
    val waktu_lahir: String, 
    val tempat_dilahirkan: String, 
    val jenis_kelahiran: String, 
    val kelahiran_anak_ke: String, 
    val penolong_kelahiran: String, 
    val berat_lahir: String, 
    val panjang_lahir: String, 

    //pendidikan dan pekerjaan
    val pendidikan_kk_id: String, 
    val pendidikan_sedang_id: String, 
    val pekerjaan_id: String, 

    //KEWARGANEGARAAN
    val suku: String, 
    val warganegara_id: String, 
    val dokumen_pasport: String, 
    //if wna
    val tanggal_akhir_paspor: String, 
    val dokumen_kitas: String, 
    val negara_asal: String, 

    //ORANG TUA
    val ayah_nik: String, 
    val nama_ayah: String, 
    val ibu_nik: String, 
    val nama_ibu: String, 

    //alamat
    val alamat: String, 
    val dusun: String, 
    val rw: String, 
    val id_cluster: String, 
    val alamat_sebelumnya: String, 
    val telepon: String, 
    val email: String, 
    val telegram: String, 
    val hubung_warga: String, 

    //PERKAWINAN
    val status_kawin: String, 
    val akta_perkawinan: String,  //jika selain belum kawin
    val tanggalperkawinan: String,  //mm-dd-yyyy jika selain belum kawin
    val akta_perceraian: String,  //jika cerai hidup
    val tanggalperceraian: String,  //mm-dd-yyyy jika cerai hidup


    //kesehatan
    val golongan_darah_id: String, 
    val cacat_id: String, 
    val sakit_menahun_id: String, 
    val cara_kb_id: String, 
    val id_asuransi: String, 
    val no_asuransi: String,  //jika ada
    val bpjs_ketenagakerjaan: String, 
    val hamil: String,  //jika perempuan

    //lainnya
    val bahasa_id: String, 
    val ket: String,

)
data class DataJenisKelamin(
    val id: String,
    val nama: String
)
data class DataAgama(
    val id: String,
    val nama: String
)

data class DataPekerjaan(
    val id: String,
    val nama: String
)

data class DataStatusKawin(
    val id: String,
    val nama: String
)

data class DataGolonganDarah(
    val id: String,
    val nama: String
)

data class DataDusun(
    val id: String,
    val rt: String,
    val rw: String,
    val dusun: String,
    val idKepala: String // Jika perlu tambahan field, sesuaikan di sini
    // Tambahkan properti lain sesuai kebutuhan
)