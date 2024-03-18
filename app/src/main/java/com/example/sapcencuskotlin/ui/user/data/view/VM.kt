package com.example.sapcencuskotlin.ui.user.data.view

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.api.ApiService
import com.example.sapcencuskotlin.helper.cekSimilarity
import com.example.sapcencuskotlin.helper.initSpinner
import com.example.sapcencuskotlin.helper.saveKTP
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.DataModel
import com.example.sapcencuskotlin.model.DataPenduduk
import com.example.sapcencuskotlin.model.GetModel
import com.example.sapcencuskotlin.model.KTPModel
import com.example.sapcencuskotlin.model.UserModel
import com.example.sapcencuskotlin.ui.user.kk.scan.KKScanActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class VM(application: Application): AndroidViewModel(application) {
    private val _getData = MutableLiveData<MutableList<DataPenduduk>>()
    val getData : MutableLiveData<MutableList<DataPenduduk>> = _getData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading
    val list = mutableListOf<DataPenduduk>()
    private val _idle = MutableLiveData<Boolean>()
    val idle : MutableLiveData<Boolean> = _idle
    fun getData(context: Context){
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).run {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://desabulila.com") // Ganti BASE_URL_API_ANDA dengan URL base API Anda
//            .baseUrl("http://192.168.1.105")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ApiService::class.java)
            val call = service.getPenduduk()
            //timber url request
            Timber.d("requestnya: ${call.request().url}")
            Log.d("cek data", "requestnya: ${call.request().url}")
            call.enqueue(object : Callback<DataModel> {
                override fun onResponse(
                    call: Call<DataModel>,
                    response: Response<DataModel>
                ) {
                    if (response.isSuccessful) {
                        Log.d("cek data "+response.body()?.dataPenduduk?.size, "onResponse: ${response.body()?.dataPenduduk?.get(0)?.nama}")
                        list.clear()
                        //masukkan data ke list
                        response.body()?.dataPenduduk?.forEach {
                            list.add(it)
                        }
                        _getData.postValue(list)
                        _isLoading.postValue(false)
                    } else {
                        // Handle jika respons tidak berhasil
                        Timber.d("onResponse: ${response.message()}")
                        Log.d("cek data", "onResponse: ${response.message()}")
                        _isLoading.postValue(false)
                    }
                }

                override fun onFailure(call: Call<DataModel>, t: Throwable) {
                    // Handle jika ada kegagalan dalam permintaan jaringan
                    Timber.d("onFailure: ${t.message}")
                    Log.d("cek data", "onFailure: ${t.message}")
                    _isLoading.postValue(false)
                }
            })
        }

    }
}