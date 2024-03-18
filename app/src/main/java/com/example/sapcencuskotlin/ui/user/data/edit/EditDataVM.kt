package com.example.sapcencuskotlin.ui.user.data.edit

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.api.ApiService
import com.example.sapcencuskotlin.helper.getKK
import com.example.sapcencuskotlin.helper.getKTP
import com.example.sapcencuskotlin.helper.showSnackbar
import com.example.sapcencuskotlin.model.DataModel
import com.example.sapcencuskotlin.model.DataPenduduk
import com.example.sapcencuskotlin.model.ResponseModel
import com.example.sapcencuskotlin.ui.user.UserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class EditDataVM(application: Application): AndroidViewModel(application) {
    private val _getData = MutableLiveData<MutableList<DataPenduduk>>()
    val getData : MutableLiveData<MutableList<DataPenduduk>> = _getData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading
    private val _idle = MutableLiveData<Boolean>()
    val idle : MutableLiveData<Boolean> = _idle
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : MutableLiveData<Boolean> = _isSuccess
    fun getData(context: Context,nik: String){
        _isLoading.postValue(true)
        CoroutineScope(Dispatchers.IO).run {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://desabulila.com") // Ganti BASE_URL_API_ANDA dengan URL base API Anda
//            .baseUrl("http://192.168.1.105")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(ApiService::class.java)
            val call = service.getPendudukDetail(nik)
            //timber url request
            Timber.d("requestnya: ${call.request().url}")
            Log.d("cek data", "requestnya: ${call.request().url}")
            call.enqueue(object : Callback<DataPenduduk> {
                override fun onResponse(
                    call: Call<DataPenduduk>,
                    response: Response<DataPenduduk>
                ) {
                    if (response.isSuccessful) {
                        Log.d("cek data "+nik, "onResponse: ${response.body()}")
                        Timber.d("onResponse: ${response.body()}")
                        _getData.postValue(mutableListOf(response.body()!!))
                        _isLoading.postValue(false)
                        //if nama null
                        if (response.body()!!.nama == null || response.body()!!.nama == ""){
                            _isSuccess.postValue(false)
                        }else{
                            _isSuccess.postValue(true)
                        }
                    } else {
                        // Handle jika respons tidak berhasil
                        Timber.d("onResponse: ${response.message()}")
                        Log.d("cek data", "onResponse: ${response.message()}")
                        _isLoading.postValue(false)
                        _isSuccess.postValue(false)
                    }
                }

                override fun onFailure(call: Call<DataPenduduk>, t: Throwable) {
                    // Handle jika ada kegagalan dalam permintaan jaringan
                    Timber.d("onFailure: ${t.message}")
                    Log.d("cek data", "onFailure: ${t.message}")
                    _isLoading.postValue(false)
                    _isSuccess.postValue(false)
                }
            })
        }
    }

}