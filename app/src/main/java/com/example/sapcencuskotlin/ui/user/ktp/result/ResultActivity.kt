package com.example.sapcencuskotlin.ui.user.ktp.result

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.api.ApiService
import com.example.sapcencuskotlin.helper.DatePickerFragment
import com.example.sapcencuskotlin.helper.OnDateSelectedListener
import com.example.sapcencuskotlin.helper.cekSimilarity
import com.example.sapcencuskotlin.helper.getKK
import com.example.sapcencuskotlin.helper.getKTP
import com.example.sapcencuskotlin.helper.initSpinner
import com.example.sapcencuskotlin.helper.saveKTP
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.GetModel
import com.example.sapcencuskotlin.model.KTPModel
import com.example.sapcencuskotlin.model.ResponseModel
import com.example.sapcencuskotlin.ui.user.kk.scan.KKScanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.Calendar

class ResultActivity : AppCompatActivity(), OnDateSelectedListener {
    lateinit var konten : RelativeLayout
    lateinit var etNik: EditText
    lateinit var spJk: Spinner
    lateinit var etNama: EditText
    lateinit var etTempat: EditText
    lateinit var etTanggal: EditText
    lateinit var etAlamat: EditText
    lateinit var spRw: Spinner
    lateinit var spRt: Spinner
    lateinit var spKelurahan: Spinner
    lateinit var etKecamatan: EditText
    lateinit var spAgama: Spinner
    lateinit var spStatus: Spinner
    lateinit var spPekerjaan: Spinner
    lateinit var spStatusWNI : Spinner
    lateinit var spGoldar : Spinner
    lateinit var btnSimpan : Button
    lateinit var lyProses : CoordinatorLayout
    var stringMentah =""
    var sKtp = ""
    var sNama = ""
    //var sTtl = ""
    var sTempatLahir=""
    var sTanggalLahir=""
    var sAlamat = ""
    var sRtRw = ""
    var sKelurahan = ""
    var sKecamatan = ""
    var sAgama = ""
    var sJk = ""
    var sStatus = ""
    var sPekerjaan = ""
    var sWni = ""
    var golDar = ""

    var qKtp=""
    var qNama=""
    var qTempat=""
    var qTanggal=""
    var qAlamat=""
    var qRt=""
    var qRw=""
    var qKelurahan=""
    var qKecamatan=""
    var qAgama=""
    var qStatus=""
    var qPekerjaan=""
    var qJk=""
    var qWni = ""
    var qGolDar = ""
    var isCamera = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initView()
        initIntent()
        setView()
        getData()
        initListener()
    }

    private fun initView(){
        konten = findViewById(R.id.contentView)
        etNik = findViewById(R.id.etNik)
        etNama = findViewById(R.id.etNama)
        etTempat = findViewById(R.id.etTempat)
        etTanggal = findViewById(R.id.etTanggal)
        etAlamat = findViewById(R.id.etAlamat)
        spRt = findViewById(R.id.spRt)
        spRw = findViewById(R.id.spRw)
        spKelurahan = findViewById(R.id.spKelurahan)
        etKecamatan = findViewById(R.id.etKecamatan)
        spAgama = findViewById(R.id.spAgama)
        btnSimpan = findViewById(R.id.btnSimpan)
        spStatus = findViewById(R.id.spStatus)
        spJk = findViewById(R.id.spJk)
        spPekerjaan = findViewById(R.id.spPekerjaan)
        spStatusWNI = findViewById(R.id.spStatusWni)
        spGoldar = findViewById(R.id.spGoldar)
        lyProses = findViewById(R.id.lyProses)
        lyProses.visibility = CoordinatorLayout.VISIBLE
    }
    private fun initIntent(){
        stringMentah = intent.getStringExtra("text").toString()
        isCamera = intent.getBooleanExtra("isCamera", false)
        sKtp = intent.getStringExtra("nik").toString()
        sNama = intent.getStringExtra("nama").toString()
        sTempatLahir = intent.getStringExtra("tempatLahir").toString()
        sTanggalLahir = intent.getStringExtra("tanggalLahir").toString()
        sAlamat = intent.getStringExtra("alamat").toString()
        sRtRw = intent.getStringExtra("rtRw").toString()
        sKelurahan = intent.getStringExtra("kelurahan").toString()
        sKecamatan = intent.getStringExtra("kecamatan").toString()
        sAgama = intent.getStringExtra("agama").toString()
        sJk = intent.getStringExtra("jenisKelamin").toString()
        sStatus = intent.getStringExtra("statusPerkawinan").toString()
        val ktp = getKTP(this)
        sKtp = ktp.nik.toString()
        sNama = ktp.nama_lengkap.toString()
        sJk = ktp.jenis_kelamin.toString()
        sAgama = ktp.agama.toString()
        sTempatLahir = ktp.tempat_lahir.toString()
        sTanggalLahir = ktp.tanggal_lahir.toString()
        sPekerjaan = ktp.pekerjaan.toString()
        sWni = ktp.status_wni.toString()
        sRtRw = ktp.rt.toString()+"/"+ktp.rw.toString()
        sStatus = ktp.status_kawin.toString()
        golDar = ktp.goldar.toString()
    }

    private fun setView(){
        etNik.setText(sKtp)
        etNama.setText(sNama)
        etTempat.setText(sTempatLahir)
        etTanggal.setText(sTanggalLahir)
        etAlamat.setText(sAlamat)
        etKecamatan.setText(sKecamatan)
    }
    fun getData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://desabulila.com") // Ganti BASE_URL_API_ANDA dengan URL base API Anda
//            .baseUrl("http://192.168.1.105")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        val call = service.getJenisKelamin()
        //timber url request
        Timber.d("requestnya: ${call.request().url}")
        Log.d("cek data", "requestnya: ${call.request().url}")
        call.enqueue(object : Callback<GetModel> {
            override fun onResponse(
                call: Call<GetModel>,
                response: Response<GetModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("cek data "+response.body()?.jenisKelamin?.size, "onResponse: ${response.body()?.jenisKelamin?.get(0)?.nama}")
                    val jenisKelaminList = response.body()?.jenisKelamin
                    val namaJenisKelaminList = jenisKelaminList?.map { it.nama } ?: listOf()
                    val idJenisKelaminList = jenisKelaminList?.map { it.id } ?: listOf()
                    val agamaList = response.body()?.agama
                    val namaAgamaList = agamaList?.map { it.nama } ?: listOf()
                    val idAgamaList = agamaList?.map { it.id } ?: listOf()
                    val statusList = response.body()?.statusKawin
                    val namaStatusList = statusList?.map { it.nama } ?: listOf()
                    val idStatusList = statusList?.map { it.id } ?: listOf()
                    val pekerjaanList = response.body()?.pekerjaan
                    val namaPekerjaanList = pekerjaanList?.map { it.nama } ?: listOf()
                    val idPekerjaanList = pekerjaanList?.map { it.id } ?: listOf()
                    val dusunList = response.body()?.dusun
                    val namaDusunList = dusunList?.map { it.rt+" "+it.rw+" "+it.dusun } ?: listOf()
                    val idDusunList = dusunList?.map { it.id } ?: listOf()

                    val rtList = mutableListOf<String>()
                    val rwList = mutableListOf<String>()
                    for (i in 1..20){
                        if (i<10){
                            rtList.add("00$i")
                            rwList.add("00$i")
                        }else{
                            rtList.add("0$i")
                            rwList.add("0$i")
                        }
                    }
                    val statusWniList = mutableListOf<String>()
                    statusWniList.add("WNI")
                    statusWniList.add("WNA")
                    statusWniList.add("DUA KEWARGANEGARAAN")
                    val statusWniIdList = mutableListOf<String>()
                    statusWniIdList.add("1")
                    statusWniIdList.add("2")
                    statusWniIdList.add("3")
                    val goldarList = mutableListOf<String>()
                    goldarList.add("A")
                    goldarList.add("B")
                    goldarList.add("AB")
                    goldarList.add("O")
                    goldarList.add("A+")
                    goldarList.add("A-")
                    goldarList.add("B+")
                    goldarList.add("B-")
                    goldarList.add("AB+")
                    goldarList.add("AB-")
                    goldarList.add("O+")
                    goldarList.add("O-")
                    goldarList.add("TIDAK TAHU")
                    val goldarIdList = mutableListOf<String>()
                    goldarIdList.add("1")
                    goldarIdList.add("2")
                    goldarIdList.add("3")
                    goldarIdList.add("4")
                    goldarIdList.add("5")
                    goldarIdList.add("6")
                    goldarIdList.add("7")
                    goldarIdList.add("8")
                    goldarIdList.add("9")
                    goldarIdList.add("10")
                    goldarIdList.add("11")
                    goldarIdList.add("12")
                    goldarIdList.add("13")

                    initSpinner(spGoldar, goldarList.toTypedArray())
                    initSpinner(spStatusWNI, statusWniList.toTypedArray())
                    initSpinner(spRt, rtList.toTypedArray())
                    initSpinner(spJk, namaJenisKelaminList.toTypedArray())
                    initSpinner(spAgama, namaAgamaList.toTypedArray())
                    initSpinner(spStatus, namaStatusList.toTypedArray())
                    initSpinner(spPekerjaan, namaPekerjaanList.toTypedArray())
                    initSpinner(spRw, rwList.toTypedArray())
                    initSpinner(spKelurahan, namaDusunList.toTypedArray())
                    if (sRtRw.contains("/")){
                        cekSimilarity(spRt,rtList.toTypedArray(), sRtRw.split("/")[0])
                        cekSimilarity(spRw,rwList.toTypedArray(), sRtRw.split("/")[1])
                        cekSimilarity(spKelurahan,namaDusunList.toTypedArray(), sRtRw.split("/")[0]+" "+
                                sRtRw.split("/")[1]+" "+sKelurahan)
                    }
                    cekSimilarity(spJk,namaJenisKelaminList.toTypedArray(), sJk)
                    cekSimilarity(spAgama,namaAgamaList.toTypedArray(), sAgama)
                    cekSimilarity(spStatus,namaStatusList.toTypedArray(), sStatus)
                    cekSimilarity(spPekerjaan,namaPekerjaanList.toTypedArray(), sPekerjaan)
                    cekSimilarity(spStatusWNI,statusWniList.toTypedArray(), sWni)
                    cekSimilarity(spGoldar,goldarList.toTypedArray(), golDar)
                    //agar etTanggal hanya bisa di klik, tidak bisa di edit
                    etTanggal.keyListener = null
                    lyProses.visibility = CoordinatorLayout.GONE
                    btnSimpan.setOnClickListener {

                        qKtp = etNik.text.toString()
                        qNama = etNama.text.toString()
                        qJk = idJenisKelaminList[namaJenisKelaminList.indexOf(spJk.selectedItem.toString())]
                        qAgama = idAgamaList[namaAgamaList.indexOf(spAgama.selectedItem.toString())]
                        qTempat = etTempat.text.toString()
                        //etTanggal = 25-10-1972 diubah ke 1972-10-25
                        val tgl = etTanggal.text.toString().split("-")
                        if(tgl.size==3){
                            etTanggal.setText(tgl[2]+"-"+tgl[1]+"-"+tgl[0])
                        }
                        qTanggal = etTanggal.text.toString()
                        qPekerjaan = idPekerjaanList[namaPekerjaanList.indexOf(spPekerjaan.selectedItem.toString())]
                        qWni = statusWniIdList[statusWniList.indexOf(spStatusWNI.selectedItem.toString())]
                        qRt = spRt.selectedItem.toString()
                        qRw = spRw.selectedItem.toString()
                        qStatus = idStatusList[namaStatusList.indexOf(spStatus.selectedItem.toString())]
                        qGolDar = goldarIdList[goldarList.indexOf(spGoldar.selectedItem.toString())]
                        qAlamat = etAlamat.text.toString()
                        qKelurahan = idDusunList[namaDusunList.indexOf(spKelurahan.selectedItem.toString())]
                        qKecamatan = etKecamatan.text.toString()
                        //masukkan log
                        Log.d("ResultActivity", "datanya: $qKtp $qNama $qTempat $qTanggal $qAlamat $qRt $qRw $qKelurahan $qKecamatan $qAgama $qStatus $qPekerjaan $qJk")
                        //postData()
                        //cek kosong
                        if(qKtp.isNullOrEmpty() || qNama.isNullOrEmpty() || qTempat.isNullOrEmpty() || qTanggal.isNullOrEmpty() || qAlamat.isNullOrEmpty() || qRt.isNullOrEmpty() || qRw.isNullOrEmpty() || qKelurahan.isNullOrEmpty() || qKecamatan.isNullOrEmpty() || qAgama.isNullOrEmpty() || qStatus.isNullOrEmpty() || qPekerjaan.isNullOrEmpty() || qJk.isNullOrEmpty() || qWni.isNullOrEmpty() || qGolDar.isNullOrEmpty()){
                            showSnackbar(this@ResultActivity,"Pastikan semua sudah terisi")
                        }else{
                            lyProses.visibility = CoordinatorLayout.VISIBLE
                            val ktpData = KTPModel()
                            ktpData.nik = qKtp
                            ktpData.nama_lengkap = qNama
                            ktpData.jenis_kelamin = qJk
                            ktpData.agama = qAgama
                            ktpData.tempat_lahir = qTempat
                            ktpData.tanggal_lahir = qTanggal
                            ktpData.pekerjaan = qPekerjaan
                            ktpData.status_wni = qWni
                            ktpData.rt = qRt
                            ktpData.rw = qRw
                            ktpData.status_kawin = qStatus
                            ktpData.goldar = qGolDar
                            ktpData.alamat = qAlamat
                            ktpData.kelurahan = qKelurahan
                            ktpData.kecamatan = qKecamatan
                            saveKTP(this@ResultActivity, ktpData)
                            //intent
                            val intent = Intent(this@ResultActivity, KKScanActivity::class.java)
                            startActivity(intent)
                        }

                    }
                } else {
                    // Handle jika respons tidak berhasil
                    Timber.d("onResponse: ${response.message()}")
                    Log.d("cek data", "onResponse: ${response.message()}")
                    lyProses.visibility = CoordinatorLayout.GONE
                }
            }

            override fun onFailure(call: Call<GetModel>, t: Throwable) {
                // Handle jika ada kegagalan dalam permintaan jaringan
                Timber.d("onFailure: ${t.message}")
                Log.d("cek data", "onFailure: ${t.message}")
                lyProses.visibility = CoordinatorLayout.GONE

            }
        })
    }
    fun initListener(){
        //ketika tanggal diklik akan muncul datepicker
        etTanggal.setOnClickListener {
            showDatePicker()
        }
    }
    override fun onDateSelected(selectedDate: String) {
        etTanggal.setText(selectedDate)
    }

    // ...

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment(this)
        datePickerFragment.show(supportFragmentManager, "datePicker")
    }
}