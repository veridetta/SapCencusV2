package com.example.sapcencuskotlin.ui.user.kk.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.sapcencuskotlin.R
import com.example.sapcencuskotlin.api.ApiService
import com.example.sapcencuskotlin.helper.cekSimilarity
import com.example.sapcencuskotlin.helper.getKK
import com.example.sapcencuskotlin.helper.getKTP
import com.example.sapcencuskotlin.helper.initSpinner
import com.example.sapcencuskotlin.helper.saveKK
import com.example.sapcencuskotlin.helper.saveKTP
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.GetModel
import com.example.sapcencuskotlin.model.KKModel
import com.example.sapcencuskotlin.model.KTPModel
import com.example.sapcencuskotlin.model.ResponseModel
import com.example.sapcencuskotlin.ui.user.UserActivity
import com.example.sapcencuskotlin.ui.user.kk.scan.KKScanActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import timber.log.Timber

class ResultKKActivity : AppCompatActivity() {
    lateinit var konten : RelativeLayout
    lateinit var etAyah: EditText
    lateinit var etIbu: EditText
    lateinit var spPendidikan : Spinner
    lateinit var spHubungan : Spinner
    lateinit var spHubung : Spinner
    lateinit var btnSimpan : Button
    lateinit var lyProses : CoordinatorLayout
    var stringMentah =""
    var sAyah = ""
    var sIbu = ""
    //var sTtl = ""
    var sHubungan=""
    var sHubung=""
    var sPendidikan=""

    var qAyah=""
    var qIbu=""
    var qHubungan = ""
    var qHubung = ""
    var qPendidikan = ""
    var isCamera = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_kkactivity)
        initView()
    }

    private fun initView(){
        konten = findViewById(R.id.contentView)
        etAyah = findViewById(R.id.etAyah)
        etIbu = findViewById(R.id.etIbu)
        spHubungan = findViewById(R.id.spHubungan)
        spHubung = findViewById(R.id.spHubung)
        //KEPALA KELUARGA, ANAK, ISTRI, MENANTU, CUCU, ORANG TUA, MERTUA, FAMILI LAIN, PEMBANTU, LAINNYA
        val hubunganList = mutableListOf<String>()
        hubunganList.add("KEPALA KELUARGA")
        hubunganList.add("SUAMI")
        hubunganList.add("ANAK")
        hubunganList.add("ISTRI")
        hubunganList.add("MENANTU")
        hubunganList.add("CUCU")
        hubunganList.add("ORANG TUA")
        hubunganList.add("MERTUA")
        hubunganList.add("FAMILI LAIN")
        hubunganList.add("PEMBANTU")
        hubunganList.add("LAINNYA")
        val hubunganIdList = mutableListOf<String>()
        hubunganIdList.add("1")
        hubunganIdList.add("2")
        hubunganIdList.add("3")
        hubunganIdList.add("4")
        hubunganIdList.add("5")
        hubunganIdList.add("6")
        hubunganIdList.add("7")
        hubunganIdList.add("8")
        hubunganIdList.add("9")
        hubunganIdList.add("10")
        hubunganIdList.add("11")
        initSpinner(spHubungan, hubunganList.toTypedArray())

        spPendidikan = findViewById(R.id.spPendidikan)
        //TIDAK / BELUM SEKOLAH, BELUM TAMAT SD/SEDERAJAT, TAMAT SD / SEDERAJAT, SLTP/SEDERAJAT, SLTA / SEDERAJAT, DIPLOMA I / II, AKADEMI / DIPLOMA III / S. MUDA, DIPLOMA IV / STRATA I, STRATA II, STRATA III
        val pendidikanList = mutableListOf<String>()
        pendidikanList.add("TIDAK / BELUM SEKOLAH")
        pendidikanList.add("BELUM TAMAT SD/SEDERAJAT")
        pendidikanList.add("TAMAT SD / SEDERAJAT")
        pendidikanList.add("SLTP/SEDERAJAT")
        pendidikanList.add("SLTA / SEDERAJAT")
        pendidikanList.add("DIPLOMA I / II")
        pendidikanList.add("AKADEMI / DIPLOMA III / S. MUDA")
        pendidikanList.add("DIPLOMA IV / STRATA I")
        pendidikanList.add("STRATA II")
        pendidikanList.add("STRATA III")

        val pendidikanIdList = mutableListOf<String>()
        pendidikanIdList.add("1")
        pendidikanIdList.add("2")
        pendidikanIdList.add("3")
        pendidikanIdList.add("4")
        pendidikanIdList.add("5")
        pendidikanIdList.add("6")
        pendidikanIdList.add("7")
        pendidikanIdList.add("8")
        pendidikanIdList.add("9")
        pendidikanIdList.add("10")
        initSpinner(spPendidikan, pendidikanList.toTypedArray())
        val hubunglist = mutableListOf<String>()
        hubunglist.add("EMAIL")
        hubunglist.add("TELEGRAM")
        initSpinner(spHubung, hubunglist.toTypedArray())
        btnSimpan = findViewById(R.id.btnSimpan)
        lyProses = findViewById(R.id.lyProses)
        lyProses.visibility = CoordinatorLayout.VISIBLE
        stringMentah = intent.getStringExtra("text").toString()
        isCamera = intent.getBooleanExtra("isCamera", false)
        val kk = getKK(this)
        if(kk != null){
            sAyah = kk.nama_ayah.toString()
            sIbu = kk.nama_ibu.toString()
            sHubungan = kk.hubungan_keluarga.toString()
            sPendidikan = kk.pendidikan.toString()
        }
        etAyah.setText(sAyah)
        etIbu.setText(sIbu)
        btnSimpan.setOnClickListener {
            qAyah = etAyah.text.toString()
            qIbu = etIbu.text.toString()
            //qTtl = etTtl.text.toString()
            qHubungan = hubunganIdList[spHubungan.selectedItemPosition]
            qPendidikan = pendidikanIdList[spPendidikan.selectedItemPosition]
            qHubung = hubunglist[spHubung.selectedItemPosition]
            if(qAyah.isNotEmpty() && qIbu.isNotEmpty() && qHubungan.isNotEmpty() && qPendidikan.isNotEmpty()){
                lyProses.visibility = CoordinatorLayout.VISIBLE
                val savekk = KKModel()
                savekk.nama_ayah = qAyah
                savekk.nama_ibu = qIbu
                savekk.hubungan_keluarga = qHubungan
                savekk.pendidikan = qPendidikan
                savekk.hubung_warga = qHubung
                saveKK(this, savekk)
                postData()
            } else {
                showSnackbar(this, "Data tidak boleh kosong")
            }
        }
    }

    fun postData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://desabulila.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        // Mengganti dengan nilai-nilai yang diperoleh dari input pengguna
        val getKTP = getKTP(this)
        val getKK = getKK(this)
        val call = service.postData(
            getKTP?.nik.toString(),
            getKTP?.nama_lengkap.toString(),
            getKTP?.jenis_kelamin.toString(),
            getKTP?.agama.toString(),
            getKTP?.tempat_lahir.toString(),
            getKTP?.tanggal_lahir.toString(),
            getKTP?.pekerjaan.toString(),
            getKTP?.status_wni.toString(),
            getKTP?.rt.toString(),
            getKTP?.rw.toString(),
            getKTP?.status_kawin.toString(),
            getKTP?.goldar.toString(),
            getKTP?.alamat.toString(),
            getKTP?.kelurahan.toString(),
            getKTP?.kecamatan.toString(),
            getKK?.hubungan_keluarga.toString(),
            getKK?.pendidikan.toString(),
            getKK?.nama_ayah.toString(),
            getKK?.nama_ibu.toString(),
            getKK?.hubung_warga.toString(),
        )
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    // Berhasil melakukan permintaan
                    if(response.body()?.kode == 1){
                        // Berhasil mengirim data
                        showSnackbar(this@ResultKKActivity, response.body()?.pesan.toString())
                        lyProses.visibility = CoordinatorLayout.GONE
                        //dialhikan dalam 3 detik
                        //timer
                        val timer = object: Thread() {
                            override fun run() {
                                try {
                                    sleep(3000)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                } finally {
                                    val intent = Intent(this@ResultKKActivity, UserActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        timer.start()

                    } else {
                        // Gagal mengirim data
                        showSnackbar(this@ResultKKActivity, response.body()?.pesan.toString())
                        Log.d("cek data", "onResponse: ${response.body()?.pesan.toString()}")
                        lyProses.visibility = CoordinatorLayout.GONE
                    }
                } else {
                    // Handle respons gagal dari server
                    showSnackbar(this@ResultKKActivity, response.message())
                    lyProses.visibility = CoordinatorLayout.GONE
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                // Handle jika ada kegagalan dalam permintaan jaringan
                showSnackbar(this@ResultKKActivity, t.message.toString())
                lyProses.visibility = CoordinatorLayout.GONE
            }
        })
    }
}