package llc.newt.inputswapper

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

//    var observer: VolumeObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val errFrag = ErrorFragment()
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_browse_fragment, errFrag)
                .commitNow()

//            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            this.startActivity(intent)

            // create volume listener
            val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
            val MUTE_CHANGED_ACTION = "android.media.STREAM_MUTE_CHANGED_ACTION"

            var volumeChangeListener = VolumeChangeListener()
            var intentFilter = IntentFilter();
            intentFilter.addAction(VOLUME_CHANGED_ACTION)
            intentFilter.addAction(MUTE_CHANGED_ACTION)

            Log.v("ANYTHING", "probably not")
            applicationContext.registerReceiver(volumeChangeListener, intentFilter)

        }
        errFrag.setVolume(-1, false)
    }
}