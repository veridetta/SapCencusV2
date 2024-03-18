package com.example.sapcencuskotlin.ui.user.data.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.api.ApiService
import com.example.sapcencuskotlin.databinding.ActivityEditDataBinding
import com.example.sapcencuskotlin.databinding.ActivityEditUserBinding
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.ResponseModel
import com.example.sapcencuskotlin.ui.admin.edituser.EditUserVM
import com.example.sapcencuskotlin.ui.user.UserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditDataActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditDataBinding
    lateinit var vm: EditDataVM
    var isComplete = true
    var nik =""
    lateinit var list_ktp_el : Array<String>
    lateinit var list_status_rekam : Array<String>
    lateinit var list_kk_level : Array<String>
    lateinit var list_id_sex : Array<String>
    lateinit var list_agama_id : Array<String>
    lateinit var list_id_status : Array<String>
    lateinit var list_tempat_dilahirkan : Array<String>
    lateinit var list_jenis_kelahiran : Array<String>
    lateinit var list_penolong_kelahiran : Array<String>
    lateinit var list_pendidikan_kk_id : Array<String>
    lateinit var list_pendidikan_sedang_id : Array<String>
    lateinit var list_pekerjaan_id : Array<String>
    lateinit var list_suku : Array<String>
    lateinit var list_warganegara_id : Array<String>
    lateinit var list_dusun : Array<String>
    lateinit var list_hubung_warga : Array<String>
    lateinit var list_status_kawin : Array<String>
    lateinit var list_golongan_darah_id : Array<String>
    lateinit var list_cacat_id : Array<String>
    lateinit var list_sakit_menahun_id : Array<String>
    lateinit var list_cara_kb_id : Array<String>
    lateinit var list_id_asuransi : Array<String>
    lateinit var list_hamil : Array<String>
    lateinit var list_bahasa_id : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initSpinner()
        initIntent()
        initListener()
    }
    fun initView(){
        vm = ViewModelProvider(this)[EditDataVM::class.java]
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun initIntent(){
        nik = intent.getStringExtra("nik").toString()
        vm.getData(this, nik)
        vm.getData.observe(this) {
            binding.lyDataDiri.etNik.setText(it[0].nik)
            binding.lyDataDiri.etNama.setText(it[0].nama)
            val ktp_el = cekNull(it[0].ktp_el)
            binding.lyDataDiri.spKtpEl.setSelection(ktp_el.toInt())
            val status_rekam = cekNull(it[0].status_rekam)
            binding.lyDataDiri.spStatusRekam.setSelection(status_rekam.toInt())
            binding.lyDataDiri.etTagIdCard.setText(it[0].tag_id_card)
            binding.lyDataDiri.lyKtpEl.etTempatCetakKtp.setText(it[0].tempat_cetak_ktp)
            binding.lyDataDiri.lyKtpEl.etTanggalCetakKtp.setText(it[0].tanggal_cetak_ktp)
            binding.lyDataDiri.etNoKkSebelumnya.setText(it[0].no_kk_sebelumnya)
            val kk_level = cekNull(it[0].kk_level)
            binding.lyDataDiri.spKkLevel.setSelection(kk_level.toInt())
            val idSex = cekNull(it[0].id_sex)
            binding.lyDataDiri.spIdSex.setSelection(idSex.toInt())
            val agamaId = cekNull(it[0].agama_id)
            binding.lyDataDiri.spAgamaId.setSelection(agamaId.toInt())
            val idStatus = cekNull(it[0].id_status)
            binding.lyDataDiri.spIdStatus.setSelection(idStatus.toInt())
            binding.lyDataKelahiran.etAktaLahir.setText(it[0].akta_lahir)
            binding.lyDataKelahiran.etTempatlahir.setText(it[0].tempatlahir)
            binding.lyDataKelahiran.etTanggallahir.setText(it[0].tanggallahir)
            binding.lyDataKelahiran.etWaktuLahir.setText(it[0].waktu_lahir)
            val tempatDilahirkan = cekNull(it[0].tempat_dilahirkan)
            binding.lyDataKelahiran.spTempatDilahirkan.setSelection(tempatDilahirkan.toInt())
            val jenisKelahiran = cekNull(it[0].jenis_kelahiran)
            binding.lyDataKelahiran.spJenisKelahiran.setSelection(jenisKelahiran.toInt())
            binding.lyDataKelahiran.etKelahiranAnakKe.setText(it[0].kelahiran_anak_ke)
            val penolongKelahiran = cekNull(it[0].penolong_kelahiran)
            binding.lyDataKelahiran.spPenolongKelahiran.setSelection(penolongKelahiran.toInt())
            binding.lyDataKelahiran.etBeratLahir.setText(it[0].berat_lahir)
            binding.lyDataKelahiran.etPanjangLahir.setText(it[0].panjang_lahir)
            val pendidikanKkId = cekNull(it[0].pendidikan_kk_id)
            binding.lyDataPendidikan.spPendidikanKkId.setSelection(pendidikanKkId.toInt())
            val pendidikanSedangId = cekNull(it[0].pendidikan_sedang_id)
            binding.lyDataPendidikan.spPendidikanSedangId.setSelection(pendidikanSedangId.toInt())
            val pekerjaanId = cekNull(it[0].pekerjaan_id)
            binding.lyDataPendidikan.spPekerjaanId.setSelection(pekerjaanId.toInt())
            val suku = cekNull(it[0].suku)
            binding.lyDataKewarganegaraan.spSuku.setSelection(suku.toInt())
            val warganegaraId = cekNull(it[0].warganegara_id)
            binding.lyDataKewarganegaraan.spWarganegaraId.setSelection(warganegaraId.toInt())
            binding.lyDataKewarganegaraan.etDokumenPasport.setText(it[0].dokumen_pasport)
            binding.lyDataKewarganegaraan.lyNonWni.etTanggalAkhirPaspor.setText(it[0].tanggal_akhir_paspor)
            binding.lyDataKewarganegaraan.lyNonWni.etDokumenKitas.setText(it[0].dokumen_kitas)
            binding.lyDataKewarganegaraan.etNegaraAsal.setText(it[0].negara_asal)
            binding.lyDataOrangTua.etAyahNik.setText(it[0].ayah_nik)
            binding.lyDataOrangTua.etNamaAyah.setText(it[0].nama_ayah)
            binding.lyDataOrangTua.etIbuNik.setText(it[0].ibu_nik)
            binding.lyDataOrangTua.etNamaIbu.setText(it[0].nama_ibu)
            binding.lyDataAlamat.etAlamat.setText(it[0].alamat)
            val dusun = cekNull(it[0].dusun)
            binding.lyDataAlamat.spDusun.setSelection(dusun.toInt())
//            binding.lyDataAlamat.spRw.setSelection(it[0].rw.toInt())
//            binding.lyDataAlamat.spIdCluster.setSelection(it[0].id_cluster.toInt())
            binding.lyDataAlamat.etAlamatSebelumnya.setText(it[0].alamat_sebelumnya)
            binding.lyDataAlamat.etTelepon.setText(it[0].telepon)
            binding.lyDataAlamat.etEmail.setText(it[0].email)
            binding.lyDataAlamat.etTelegram.setText(it[0].telegram)
            var hubungWarga =""
            if (it[0].hubung_warga == null || it[0].hubung_warga == "null" || it[0].hubung_warga.isEmpty()) {
                binding.lyDataAlamat.spHubungWarga.setSelection(0)
            } else {
                val selectedHubung = list_hubung_warga.indexOf(hubungWarga)
                if (selectedHubung == -1) {
                    binding.lyDataAlamat.spHubungWarga.setSelection(0)
                } else {
                    binding.lyDataAlamat.spHubungWarga.setSelection(selectedHubung)
                }
            }
            val statusKawin = cekNull(it[0].status_kawin)
            binding.lyDataPerkawinan.spStatusKawin.setSelection(statusKawin.toInt())
            binding.lyDataPerkawinan.etAktaPerkawinan.setText(it[0].akta_perkawinan)
            binding.lyDataPerkawinan.etTanggalperkawinan.setText(it[0].tanggalperkawinan)
            binding.lyDataPerkawinan.etAktaPerceraian.setText(it[0].akta_perceraian)
            binding.lyDataPerkawinan.etTanggalperceraian.setText(it[0].tanggalperceraian)
            val golonganDarahId = cekNull(it[0].golongan_darah_id)
            binding.lyDataKesehatan.spGolonganDarahId.setSelection(golonganDarahId.toInt())
            val cacatId = cekNull(it[0].cacat_id)
            binding.lyDataKesehatan.spCacatId.setSelection(cacatId.toInt())
            val sakitMenahun = cekNull(it[0].sakit_menahun_id)
            binding.lyDataKesehatan.spSakitMenahunId.setSelection(sakitMenahun.toInt())
            val caraKbId = cekNull(it[0].cara_kb_id)
            binding.lyDataKesehatan.spCaraKbId.setSelection(caraKbId.toInt())
            val idAsuransi = cekNull(it[0].id_asuransi)
            binding.lyDataKesehatan.spIdAsuransi.setSelection(idAsuransi.toInt())
            binding.lyDataKesehatan.etNoAsuransi.setText(it[0].no_asuransi)
            binding.lyDataKesehatan.etBpjsKetenagakerjaan.setText(it[0].bpjs_ketenagakerjaan)
            val hamil = cekNull(it[0].hamil)
            binding.lyDataKesehatan.spHamil.setSelection(hamil.toInt())
            val bahasaId = cekNull(it[0].bahasa_id)
            binding.lyDataLainnya.spBahasaId.setSelection(bahasaId.toInt())
            binding.lyDataLainnya.etKet.setText(it[0].ket)

        }
        vm.isSuccess.observe(this){
            if (it){
                Toast.makeText(this, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data gagal diambil", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        isLoading()
    }
    fun initSpinner(){
        //list spinner ktp_el, status_rekam, kk_level, id_sex, agama_id, id_status, tempat_dilahirkan, jenis_kelahiran, penolong_kelahiran, pendidikan_kk_id, pendidikan_sedang_id, pekerjaan_id, suku, warganegara_id, dusun, rw, id_cluster, hubung_warga, status_kawin, golongan_darah_id, cacat_id, sakit_menahun_id, cara_kb_id, id_asuransi, hamil, bahasa_id
        //array ktp_el
        list_ktp_el = arrayOf("Pilih identitas-EL","BELUM", "KTP-EL","KIA")
        // valuenya nanti di ubah ke 1,2,3
        val adapterKtpEl = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_ktp_el)
        binding.lyDataDiri.spKtpEl.adapter = adapterKtpEl
        //array status_rekam
         list_status_rekam = arrayOf("Pilih status rekam","BELUM WAJIB","BELUM REKAM", "SUDAH REKAM",
            "CARD PRINTED","PRINT READY RECORD","CARD SHIPPED","SEND FOR PRINTING","CARD ISSUED")
        // valuenya nanti di ubah ke 1,2,3
        val adapterStatusRekam = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_status_rekam)
        binding.lyDataDiri.spStatusRekam.adapter = adapterStatusRekam
        //array kk_level
         list_kk_level = arrayOf("Pilih Hubungan Keluarga","KEPALA KELUARGA","SUAMI","ISTRI",
            "ANAK","MENANTU","CUCU","ORANG TUA","MERTUA","FAMILI LAIN","PEMBANTU","LAINNYA")
        // valuenya nanti di ubah ke 1,2,3
        val adapterKkLevel = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_kk_level)
        binding.lyDataDiri.spKkLevel.adapter = adapterKkLevel
       //array id_sex
         list_id_sex = arrayOf("Pilih jenis kelamin","Laki-laki","Perempuan")
        // valuenya nanti di ubah ke 1,2,3
        val adapterIdSex = ArrayAdapter(this, android.R.layout.simple_spinner_item,list_id_sex)
        binding.lyDataDiri.spIdSex.adapter = adapterIdSex
        //array agama_id
         list_agama_id = arrayOf("Pilih Agama","ISLAM","KRISTEN","KATOLIK","HINDU","BUDHA",
            "KONGHUCU","KEPERCAYAAN TERHADAP TUHAN YME / LAINNYA")
        // valuenya nanti di ubah ke 1,2,3
        val adapterAgamaId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_agama_id)
        binding.lyDataDiri.spAgamaId.adapter = adapterAgamaId
        //array id_status
         list_id_status = arrayOf("Pilih Status","Tetap","Tidak Tetap")
        // valuenya nanti di ubah ke 1,2,3
        val adapterIdStatus = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_id_status)
        binding.lyDataDiri.spIdStatus.adapter = adapterIdStatus
        //array tempat_dilahirkan
         list_tempat_dilahirkan = arrayOf("Pilih Tempat Lahir","RS/RB","PUSKESMAS","POLINDES"
            ,"RUMAH", "LAINNYA")
        // valuenya nanti di ubah ke 1,2,3
        val adapterTempatDilahirkan = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_tempat_dilahirkan)
        binding.lyDataKelahiran.spTempatDilahirkan.adapter = adapterTempatDilahirkan
        //array jenis_kelahiran
         list_jenis_kelahiran = arrayOf("Pilih Jenis Kelahiran","TUNGGAL","KEMBAR 2","KEMBAR 3",
            "KEMBAR 4")
        // valuenya nanti di ubah ke 1,2,3
        val adapterJenisKelahiran = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_jenis_kelahiran)
        binding.lyDataKelahiran.spJenisKelahiran.adapter = adapterJenisKelahiran
        //array penolong_kelahiran
         list_penolong_kelahiran = arrayOf("Pilih Penoong Kelahiran","DOKTER","BIDAN/PERAWAT",
            "DUKUN","LAINNYA")
        // valuenya nanti di ubah ke 1,2,3
        val adapterPenolongKelahiran = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_penolong_kelahiran)
        binding.lyDataKelahiran.spPenolongKelahiran.adapter = adapterPenolongKelahiran
        //array pendidikan_kk_id
         list_pendidikan_kk_id = arrayOf("Pilih Pendidikan","TIDAK / BELUM SEKOLAH","BELUM TAMAT SD/SEDERAJAT",
            "TAMAT SD / SEDERAJAT","SLTP/SEDERAJAT","SLTA / SEDERAJAT","DIPLOMA I / II","AKADEMI / DIPLOMA III / S. MUDA",
            "DIPLOMA IV / STRATA I","STRATA II","STRATA III")
        // valuenya nanti di ubah ke 1,2,3
        val adapterPendidikanKkId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_pendidikan_kk_id)
        binding.lyDataPendidikan.spPendidikanKkId.adapter = adapterPendidikanKkId

        //array pendidikan_sedang_id
         list_pendidikan_sedang_id = arrayOf("Pilih Pendidikan","BELUM MASUK TK/KELOMPOK BERMAIN",
            "SEDANG TK/KELOMPOK BERMAIN","TIDAK PERNAH SEKOLAH", "SEDANG SD/SEDERAJAT","TIDAK TAMAT SD/SEDERAJAT",
            "SEDANG SLTP/SEDERAJAT","TIDAK TAMAT SLTP/SEDERAJAT","SEDANG SLTA/SEDERAJAT","TIDAK TAMAT SLTA/SEDERAJAT",
            "SEDANG D-1/SEDERAJAT","SEDANG D-2/SEDERAJAT","SEDANG D-3/SEDERAJAT","SEDANG SLB A/SEDERAJAT",
            "SEDANG SLB B/SEDERAJAT","SEDANG SLB C/SEDERAJAT","TIDAK DAPAT MEMBACA DAN MENULIS HURUF LATIN/ARAB",
            "TIDAK SEDANG SEKOLAH")
        val adapterPendidikanSedangId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_pendidikan_sedang_id)
        binding.lyDataPendidikan.spPendidikanSedangId.adapter = adapterPendidikanSedangId
        //array pekerjaan_id
         list_pekerjaan_id = arrayOf("Pilih Pekerjaan","BELUM/TIDAK BEKERJA","MENGURUS RUMAH TANGGA",
            "PELAJAR/MAHASISWA","PENSIUNAN","PEGAWAI NEGERI SIPIL (PNS)","TENTARA NASIONAL INDONESIA (TNI)",
            "KEPOLISIAN RI (POLRI)","PERDAGANGAN","PETANI/PEKEBUN","PETERNAK","NELAYAN/PERIKANAN",
            "INDUSTRI","KONSTRUKSI","TRANSPORTASI","KARYAWAN SWASTA","KARYAWAN BUMN","KARYAWAN BUMD",
            "KARYAWAN HONORER","BURUH HARIAN LEPAS","BURUH TANI/PERKEBUNAN","BURUH NELAYAN/PERIKANAN",
            "BURUH PETERNAKAN","PEMBANTU RUMAH TANGGA","TUKANG CUKUR","TUKANG LISTRIK","TUKANG BATU",
            "TUKANG KAYU","TUKANG SOL SEPATU","TUKANG LAS/PANDAI BESI","TUKANG JAHIT","TUKANG GIGI",
            "PENATA RIAS","PENATA BUSANA","PENATA RAMBUT","MEKANIK","SENIMAN","TABIB","PARAJI",
            "PERANCANG BUSANA","PENTERJEMAH","IMAM MASJID","PENDETA","PASTOR","WARTAWAN","USTADZ/MUBALIGH",
            "JURU MASAK","PROMOTOR ACARA","ANGGOTA DPR-RI","ANGGOTA DPD","ANGGOTA BPK","PRESIDEN",
            "WAKIL PRESIDEN","ANGGOTA MAHKAMAH KONSTITUSI","ANGGOTA KABINET KEMENTERIAN","DUTA BESAR",
            "GUBERNUR","WAKIL GUBERNUR","BUPATI","WAKIL BUPATI","WALIKOTA","WAKIL WALIKOTA","ANGGOTA DPRD PROVINSI",
            "ANGGOTA DPRD KABUPATEN/KOTA","DOSEN","GURU","PILOT","PENGACARA","NOTARIS","ARSITEK","AKUNTAN",
            "KONSULTAN","DOKTER","BIDAN","PERAWAT","APOTEKER","PSIKIATER/PSIKOLOG","PENYIAR TELEVISI",
            "PENYIAR RADIO","PELAUT","PENELITI","SOPIR","PIALANG","PARANORMAL","PEDAGANG","PERANGKAT DESA",
            "KEPALA DESA","BIARAWATI","WIRASWASTA","LAINNYA")
        val adapterPekerjaanId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_pekerjaan_id)
        binding.lyDataPendidikan.spPekerjaanId.adapter = adapterPekerjaanId
        //array suku
         list_suku = arrayOf("Pilih Suku/Etnis","Aceh","Alas","Alor","Ambon","Ampana","Anak Dalam","Aneuk Jamee","Arab: Orang Hadhrami","Aru","Asmat","Bare’e","Banten","Besemah","Bali","Balantak","Banggai","Baduy","Bajau","Banjar","Batak","Batak Karo","Mandailing","Angkola","Toba","Pakpak","Simalungun","Batin","Bawean","Bentong","Berau","Betawi","Bima","Boti","Bolang Mongondow","Bugis","Bungku","Buru","Buol","Bulungan","Buton","Bonai","Cham","Cirebon","Damal","Dampeles","Dani","Dairi","Daya","Dayak","Dompu","Donggo","Dongga","Dondo","Duri","Eropa","Flores","Lamaholot","Gayo","Gorontalo","Gumai","India","Jawa","Tengger","Osing","Samin","Bawean","Jambi","Jepang","Kei","Kaili","Kampar","Kaur","Kayu Agung","Kerinci","Komering","Konjo Pegunungan","Konjo Pesisir","Koto","Kubu","Kulawi","Kutai","Kluet","Korea","Krui","Laut","Lampung","Lematang","Lembak","Lintang","Lom","Lore","Lubu","Moronene","Madura","Makassar","Mamasa","Manda","Melayu","Mentawai","Minahasa","Ponosakan","Minangkabau","Mongondow","Mori","Muko-Muko","Muna","Muyu","Mekongga","Moro","Nias","Ngada","Osing","Ogan","Ocu","Padoe","Papua","Palembang","Pamona","Pesisi","Pasir","Pubian","Pattae","Pakistani","Peranakan","Rawa","Rejang","Rote","Rongga","Rohingya","Sabu","Saluan","Sambas","Samin","Sangi","Sasak","Sekak Bangka","Sekayu","Semendo","Serawai","Simeulue","Sigulai","Suluk","Sumbawa","Sumba","Sunda","Sungkai","Talau","Talang Mamak","Tamiang","Tengger","Ternate","Tidore","Tidung","Timor","Tionghoa","Tojo","Toraja","Tolaki","Toli Toli","Tomini","Una-una","Ulu","Wolio"
        )
        val adapterSuku = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_suku)
        binding.lyDataKewarganegaraan.spSuku.adapter = adapterSuku
        //array warganegara_id
         list_warganegara_id = arrayOf("Pilih Kewarganegaraan","WNI","WNA","DUA KEWARGANEGARAAN")
        val adapterWarganegaraId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_warganegara_id)
        binding.lyDataKewarganegaraan.spWarganegaraId.adapter = adapterWarganegaraId
        //array dusun (Real value)
         list_dusun = arrayOf("Pilih Dusun","I","II","III","IV","V")
        val adapterDusun = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_dusun)
        binding.lyDataAlamat.spDusun.adapter = adapterDusun
        //array rw kosong
        //array id_cluster kosong
        //array hubung_warga (Real Value)
         list_hubung_warga = arrayOf("Pilih Cara Hubung Warga",
            "Email","Telegram")
        val adapterHubungWarga = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_hubung_warga)
        binding.lyDataAlamat.spHubungWarga.adapter = adapterHubungWarga
        //array status_kawin
         list_status_kawin = arrayOf("Pilih Status Kawin","BELUM KAWIN","KAWIN","CERAI HIDUP",
            "CERAI MATI")
        val adapterStatusKawin = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_status_kawin)
        binding.lyDataPerkawinan.spStatusKawin.adapter = adapterStatusKawin
        //array golongan_darah_id
         list_golongan_darah_id = arrayOf("Pilih Golongan Darah","A","B","AB","O","A+",
            "A-","B+","B-","AB+","AB-","O+","O-","TIDAK TAHU")
        val adapterGolonganDarahId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_golongan_darah_id)
        binding.lyDataKesehatan.spGolonganDarahId.adapter = adapterGolonganDarahId
        //array cacat_id
         list_cacat_id = arrayOf("Pilih Jenis Cacat","CACAT FISIK","CACAT NETRA/BUTA",
            "CACAT RUNGU/WICARA","CACAT MENTAL/JIWA","CACAT FISIK DAN MENTAL","CACAT LAINNYA","TIDAK CACAT")
        val adapterCacatId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_cacat_id)
        binding.lyDataKesehatan.spCacatId.adapter = adapterCacatId
        //array sakit_menahun_id
         list_sakit_menahun_id = arrayOf("Pilih Sakit Menahun","JANTUNG","LEVER","PARU-PARU",
            "KANKER","STROKE","DIABETES MELITUS", "GINJAL","MALARIA","LEPRA/KUSTA","HIV/AIDS","GILA/STRES",
            "TBC","ASTHMA","TIDAK ADA/TIDAK SAKIT")
        val adapterSakitMenahunId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_sakit_menahun_id)
        binding.lyDataKesehatan.spSakitMenahunId.adapter = adapterSakitMenahunId
        //array cara_kb_id
         list_cara_kb_id = arrayOf("Pilih Cara KB","PILO","IUD","SUNTIK","KONDOM",
            "SUSUK KB","STERILISASI WANITA","STERILISASI PRIA","LAINNYA", "TIDAK MENGGUNAKAN")
        val adapterCaraKbId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_cara_kb_id)
        binding.lyDataKesehatan.spCaraKbId.adapter = adapterCaraKbId
        //array id_asuransi (ASURANSI LAINNYA = 99)
         list_id_asuransi = arrayOf("Pilih Asuransi","TIDAK/BELUM PUNYA", "BPJS PENERIMA BANTUAN IURAN (PBI)",
            "BPJS NON PBI", "ASURANSI LAINNYA")
        val adapterIdAsuransi = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_id_asuransi)
        binding.lyDataKesehatan.spIdAsuransi.adapter = adapterIdAsuransi
        //array hamil
         list_hamil = arrayOf("Pilih Status Kehamilan","HAMIL","TIDAK HAMIL")
        val adapterHamil = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_hamil)
        binding.lyDataKesehatan.spHamil.adapter = adapterHamil
        //array bahasa_id
         list_bahasa_id = arrayOf("Pilih Isian","LATIN","DAERAH","ARAB","ARAB DAN LATIN",
            "ARAB DAN DAERAH","ARAB, LATIN DAN DAERAH")
        val adapterBahasaId = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_bahasa_id)
        binding.lyDataLainnya.spBahasaId.adapter = adapterBahasaId

    }
    fun initListener() {
        binding.btnSimpan.setOnClickListener {
            Log.d("Klik", "Simpan")
            isComplete = true
            cekRequired()
            if (isComplete){
                Toast.makeText(this, "Data Lengkap", Toast.LENGTH_SHORT).show()
                postData()
            }
        }
    }
    fun cekRequired(){
        if (binding.lyDataDiri.etNik.text.toString().isEmpty()){
            binding.lyDataDiri.etNik.error = "NIK tidak boleh kosong"
            binding.lyDataDiri.etNik.requestFocus()
            Toast.makeText(this, "NIK tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "NIK tidak boleh kosong")
            return
        }
        if (binding.lyDataDiri.etNama.text.toString().isEmpty()){
            binding.lyDataDiri.etNama.error = "Nama tidak boleh kosong"
            binding.lyDataDiri.etNama.requestFocus()
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Nama tidak boleh kosong")
            return
        }
        if (binding.lyDataDiri.spKkLevel.selectedItemPosition == 0){
            Toast.makeText(this, "Hubungan keluarga tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Hubungan keluarga tidak boleh kosong")
            return
        }
        if (binding.lyDataDiri.spIdSex.selectedItemPosition == 0){
            Toast.makeText(this, "Jenis kelamin tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Jenis kelamin tidak boleh kosong")
            return
        }
        if (binding.lyDataDiri.spAgamaId.selectedItemPosition == 0){
            Toast.makeText(this, "Agama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Agama tidak boleh kosong")
            return
        }
        if (binding.lyDataDiri.spIdStatus.selectedItemPosition == 0){
            Toast.makeText(this, "Status tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Status tidak boleh kosong")
            return
        }
        if (binding.lyDataKelahiran.spTempatDilahirkan.selectedItemPosition == 0){
            Toast.makeText(this, "Tempat dilahirkan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Tempat lahir tidak boleh kosong")
            return
        }
        if (binding.lyDataKelahiran.spJenisKelahiran.selectedItemPosition == 0){
            Toast.makeText(this, "Jenis kelahiran tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Jenis kelahiran tidak boleh kosong")
            return
        }
        if (binding.lyDataPendidikan.spPendidikanKkId.selectedItemPosition == 0) {
            Toast.makeText(this, "Pendidikan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Pendidikan tidak boleh kosong")
            return
        }
        if (binding.lyDataPendidikan.spPekerjaanId.selectedItemPosition == 0) {
            Toast.makeText(this, "Pekerjaan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Pekerjaan tidak boleh kosong")
            return
        }
        if (binding.lyDataKewarganegaraan.spWarganegaraId.selectedItemPosition == 0) {
            Toast.makeText(this, "Kewarganegaraan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Kewarganegaraan tidak boleh kosong")
            return
        }
        if(binding.lyDataOrangTua.etNamaAyah.text.toString().isEmpty()){
            binding.lyDataOrangTua.etNamaAyah.error = "Nama ayah tidak boleh kosong"
            binding.lyDataOrangTua.etNamaAyah.requestFocus()
            isComplete = false
            Log.d("isComplete", "Nama ayah tidak boleh kosong")
            return
        }
        if(binding.lyDataOrangTua.etNamaIbu.text.toString().isEmpty()){
            binding.lyDataOrangTua.etNamaIbu.error = "Nama ibu tidak boleh kosong"
            binding.lyDataOrangTua.etNamaIbu.requestFocus()
            isComplete = false
            Log.d("isComplete", "Nama ibu tidak boleh kosong")
            return
        }
        if(binding.lyDataAlamat.spHubungWarga.selectedItemPosition == 0){
            Toast.makeText(this, "Cara Hubung warga tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Hubung Warga tidak boleh kosong")
            return
        }
        if(binding.lyDataPerkawinan.spStatusKawin.selectedItemPosition == 0){
            Toast.makeText(this, "Status kawin tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Status kawin tidak boleh kosong")
            return
        }
        if(binding.lyDataKesehatan.spGolonganDarahId.selectedItemPosition == 0){
            Toast.makeText(this, "Golongan darah tidak boleh kosong", Toast.LENGTH_SHORT).show()
            isComplete = false
            Log.d("isComplete", "Golongan darah tidak boleh kosong")
            return
        }
    }
    fun postData(){
        //cek semua spinner jika posisi 0, maka value =""
        val idAsuransi = if (binding.lyDataKesehatan.spIdAsuransi.selectedItemPosition == 3){
            99
        } else {
            binding.lyDataKesehatan.spIdAsuransi.selectedItemPosition
        }
        //ambil value dari dusun
        val dusun = when(binding.lyDataAlamat.spDusun.selectedItemPosition){
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            else -> ""
        }
        val hubungWarga = when(binding.lyDataAlamat.spHubungWarga.selectedItemPosition){
            1 -> "Email"
            2 -> "Telegram"
            else -> ""
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://desabulila.com")
//            .baseUrl("http://192.168.1.105")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        // Mengganti dengan nilai-nilai yang diperoleh dari input pengguna
        val call = service.updateData(
            setToNull(binding.lyDataDiri.etNik.text.toString()),
            setToNull(binding.lyDataDiri.etNama.text.toString()),
            select0toEmpty(binding.lyDataDiri.spKtpEl.selectedItemPosition),
            select0toEmpty(binding.lyDataDiri.spStatusRekam.selectedItemPosition),
            setToNull(binding.lyDataDiri.etTagIdCard.text.toString()),
            setToNull(binding.lyDataDiri.lyKtpEl.etTempatCetakKtp.text.toString()),
            setToNull(binding.lyDataDiri.lyKtpEl.etTanggalCetakKtp.text.toString()),
            setToNull(binding.lyDataDiri.etNoKkSebelumnya.text.toString()),
            select0toEmpty(binding.lyDataDiri.spKkLevel.selectedItemPosition),
            select0toEmpty(binding.lyDataDiri.spIdSex.selectedItemPosition),
            select0toEmpty(binding.lyDataDiri.spAgamaId.selectedItemPosition),
            select0toEmpty(binding.lyDataDiri.spIdStatus.selectedItemPosition),

            setToNull(binding.lyDataKelahiran.etAktaLahir.text.toString()),
            setToNull(binding.lyDataKelahiran.etTempatlahir.text.toString()),
            setToNull(binding.lyDataKelahiran.etTanggallahir.text.toString()),
            setToNull(binding.lyDataKelahiran.etWaktuLahir.text.toString()),
            select0toEmpty(binding.lyDataKelahiran.spTempatDilahirkan.selectedItemPosition),
            select0toEmpty(binding.lyDataKelahiran.spJenisKelahiran.selectedItemPosition),
            setToNull(binding.lyDataKelahiran.etKelahiranAnakKe.text.toString()),
            select0toEmpty(binding.lyDataKelahiran.spPenolongKelahiran.selectedItemPosition),
            setToNull(binding.lyDataKelahiran.etBeratLahir.text.toString()),
            setToNull(binding.lyDataKelahiran.etPanjangLahir.text.toString()),

            select0toEmpty(binding.lyDataPendidikan.spPendidikanKkId.selectedItemPosition),
            select0toEmpty(binding.lyDataPendidikan.spPendidikanSedangId.selectedItemPosition),
            select0toEmpty(binding.lyDataPendidikan.spPekerjaanId.selectedItemPosition),

            select0toEmpty(binding.lyDataKewarganegaraan.spSuku.selectedItemPosition),
            select0toEmpty(binding.lyDataKewarganegaraan.spWarganegaraId.selectedItemPosition),
            setToNull(binding.lyDataKewarganegaraan.etDokumenPasport.text.toString()),
            setToNull(binding.lyDataKewarganegaraan.lyNonWni.etTanggalAkhirPaspor.text.toString()),
            setToNull(binding.lyDataKewarganegaraan.lyNonWni.etDokumenKitas.text.toString()),
            setToNull(binding.lyDataKewarganegaraan.etNegaraAsal.text.toString()),

            setToNull(binding.lyDataOrangTua.etAyahNik.text.toString()),
            setToNull(binding.lyDataOrangTua.etNamaAyah.text.toString()),
            setToNull(binding.lyDataOrangTua.etIbuNik.text.toString()),
            setToNull(binding.lyDataOrangTua.etNamaIbu.text.toString()),

            setToNull(binding.lyDataAlamat.etAlamat.text.toString()),
            dusun,
            select0toEmpty(binding.lyDataAlamat.spRw.selectedItemPosition),
            select0toEmpty(binding.lyDataAlamat.spIdCluster.selectedItemPosition),
            setToNull(binding.lyDataAlamat.etAlamatSebelumnya.text.toString()),
            setToNull(binding.lyDataAlamat.etTelepon.text.toString()),
            setToNull(binding.lyDataAlamat.etEmail.text.toString()),
            setToNull(binding.lyDataAlamat.etTelegram.text.toString()),
            hubungWarga,

            select0toEmpty(binding.lyDataPerkawinan.spStatusKawin.selectedItemPosition),
            setToNull(binding.lyDataPerkawinan.etAktaPerkawinan.text.toString()),
            setToNull(binding.lyDataPerkawinan.etTanggalperkawinan.text.toString()),
            setToNull(binding.lyDataPerkawinan.etAktaPerceraian.text.toString()),
            setToNull(binding.lyDataPerkawinan.etTanggalperceraian.text.toString()),

            select0toEmpty(binding.lyDataKesehatan.spGolonganDarahId.selectedItemPosition),
            select0toEmpty(binding.lyDataKesehatan.spCacatId.selectedItemPosition),
            select0toEmpty(binding.lyDataKesehatan.spSakitMenahunId.selectedItemPosition),
            select0toEmpty(binding.lyDataKesehatan.spCaraKbId.selectedItemPosition),
            idAsuransi.toString(),
            setToNull(binding.lyDataKesehatan.etNoAsuransi.text.toString()),
            setToNull(binding.lyDataKesehatan.etBpjsKetenagakerjaan.text.toString()),
            select0toEmpty(binding.lyDataKesehatan.spHamil.selectedItemPosition),

            select0toEmpty(binding.lyDataLainnya.spBahasaId.selectedItemPosition),
            setToNull(binding.lyDataLainnya.etKet.text.toString()))
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    // Berhasil melakukan permintaan
                    if(response.body()?.kode == 1){
                        // Berhasil mengirim data
                        showSnackbar(this@EditDataActivity, response.body()?.pesan.toString())
                        binding.lyProses.lyProses.visibility = CoordinatorLayout.GONE
                        //dialhikan dalam 3 detik
                        //timer
                        val timer = object: Thread() {
                            override fun run() {
                                try {
                                    sleep(3000)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                } finally {
                                    val intent = Intent(this@EditDataActivity, UserActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        timer.start()

                    } else {
                        // Gagal mengirim data
                        showSnackbar(this@EditDataActivity, response.body()?.pesan.toString())
                        Log.d("cek data", "onResponse: ${response.body()?.pesan.toString()}")
                        binding.lyProses.lyProses.visibility = CoordinatorLayout.GONE
                    }
                } else {
                    // Handle respons gagal dari server
                    showSnackbar(this@EditDataActivity, response.message())
                    binding.lyProses.lyProses.visibility = CoordinatorLayout.GONE
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                // Handle jika ada kegagalan dalam permintaan jaringan
                showSnackbar(this@EditDataActivity, t.message.toString())
                binding.lyProses.lyProses.visibility = CoordinatorLayout.GONE
            }
        })
    }
    fun select0toEmpty(value: Int): String {
        return if (value == 0) {
            null.toString()
        } else {
            value.toString()
        }
    }
    fun cekNull(value: String?): String {
        return if (value == null || value == "null" || value.isEmpty()) {
            "0"
        } else {
            value
        }
    }
    fun isLoading(){
        vm.isLoading.observe(this){
            if (it){
                binding.lyProses.lyProses.visibility = CoordinatorLayout.VISIBLE
            } else {
                binding.lyProses.lyProses.visibility = CoordinatorLayout.GONE
            }
        }
    }
    fun setToNull(value: String?): String {
        return if (value == "0" || value == "null" || value == "") {
            null.toString()
        } else {
            value.toString()
        }
    }
}