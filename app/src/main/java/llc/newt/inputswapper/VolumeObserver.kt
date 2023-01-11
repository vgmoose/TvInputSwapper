package llc.newt.inputswapper

import android.content.ContentValues.TAG
import android.content.Context
import android.database.ContentObserver
import android.media.AudioManager
import android.util.Log


class VolumeObserver(context: Context, errFrag: ErrorFragment) :
    ContentObserver(null) {
    private val audioManager: AudioManager
    private val errFrag: ErrorFragment

    init {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        this.errFrag = errFrag
    }

    override fun deliverSelfNotifications(): Boolean {
        return false
    }

    override fun onChange(selfChange: Boolean) {
        val targ = AudioManager.STREAM_MUSIC
        val currentVolume = audioManager.getStreamVolume(targ)
        val isMuted = audioManager.isStreamMute(targ)
        Log.d(TAG, "Volume now $currentVolume, Mute is $isMuted")

        errFrag.setVolume(currentVolume, isMuted)
    }
}