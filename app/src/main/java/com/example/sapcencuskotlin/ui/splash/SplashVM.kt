package com.example.sapcencuskotlin.ui.splash

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sapcencuskotlin.helper.getUser
import com.example.sapcencuskotlin.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SplashVM(application: Application): AndroidViewModel(application) {
    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: MutableLiveData<Boolean> = _isLogin
    private val _getUser = MutableLiveData<UserModel>()
    val getUser: MutableLiveData<UserModel> = _getUser

    fun checkLogin(context: Context){
        CoroutineScope(Dispatchers.IO).run {
            val pref = getUser(context)
            if (pref.isLogin){
                _getUser.postValue(pref)
                _isLogin.postValue(true)
                return@run
            }
            _isLogin.postValue(false)
        }
    }
}