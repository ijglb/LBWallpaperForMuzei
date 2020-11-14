package com.ijglb.muzei.lbwallpaper

import android.net.Uri
import com.google.android.apps.muzei.api.provider.Artwork
import com.google.android.apps.muzei.api.provider.MuzeiArtProvider

class LBWallpaperProvider: MuzeiArtProvider() {
    companion object {
        private const val TAG = "LBWallpaperProvider"
    }
    /**
     * Callback method when the user has viewed all of the available artwork. This should be used
     * as a cue to load more artwork so that the user has a constant stream of new artwork.
     *
     * Muzei will always prefer to show unseen artwork, but will automatically cycle through all
     * of the available artwork if no new artwork is found (i.e., if you don't load new artwork
     * after receiving this callback).
     *
     * @param initial true when there is no artwork available, such as is the case when this is
     * the initial load of this MuzeiArtProvider.
     */
    override fun onLoadRequested(initial: Boolean) {
        if(initial){
            setArtwork(Artwork(
                    title = "ฅ╹ᗜ╹ฅ",
                    token = "https://img.ijglb.com/content/uploadfile/202011/5a231604734407.png",
                    persistentUri = Uri.parse("file:///android_asset/initial.png"),
                    webUri = Uri.parse("https://img.ijglb.com/post/888")
            ))
        }else {
            context?.let { LBWallpaperWorker.enqueueLoad(it) }
        }
    }
}