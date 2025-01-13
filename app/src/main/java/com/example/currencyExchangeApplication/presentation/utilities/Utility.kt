package com.example.currencyExchangeApplication.presentation.utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


object Utility {


    //Hide keyboard
    fun hideKeyboard(activity: Activity) {
        val input: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        input.hideSoftInputFromWindow(view.windowToken, 0)
    }


    //Network connection check
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    // Проверка доступности интернета с HTTP-запросом
    suspend fun isInternetAvailableWithHttpCheck(context: Context?): Boolean {
        if (!isNetworkAvailable(context)) return false
        return isInternetAvailable()
    }

    private suspend fun isInternetAvailable(url: String = "https://www.google.com", timeoutSeconds: Int = 2): Boolean {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder()
                .callTimeout(timeoutSeconds.toLong(), java.util.concurrent.TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            return@withContext try {
                client.newCall(request).execute().use { response ->
                    response.isSuccessful
                }
            } catch (e: IOException) {
                false
            }
        }
    }
}