package com.ijglb.muzei.lbwallpaper

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.string
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.ProviderContract
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.Request

class LBWallpaperWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG = "LBWallpaperWorker"

        internal fun enqueueLoad(context: Context) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(OneTimeWorkRequestBuilder<LBWallpaperWorker>()
                .setConstraints(Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build())
                .build())
        }
    }

    override fun doWork(): Result {
        val providerClient = ProviderContract.getProviderClient(
            applicationContext, LBWallpaperProvider::class.java)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://img.ijglb.com/api.php?action=muzei")
            .build()
        try {
            val result = client.newCall(request).execute().body?.string()
            result?.let {
                Log.d(TAG,result)
                val json = Gson().fromJson<JsonObject>(it)
                val artwork = Artwork(
                    title = json["title"].string,
                    token = json["imgurl"].string,
                    persistentUri =  Uri.parse(json["imgurl"].string),
                    webUri = Uri.parse(json["weburl"].string))
                providerClient.addArtwork(artwork)
            }
        }catch(e: Exception){
            e.printStackTrace()
            return Result.failure()
        }
        return Result.success()
    }
}