package llc.newt.inputswapper

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
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
//            applicationContext
//                .contentResolver
//                .registerContentObserver(
//                    android.provider.Settings.System.CONTENT_URI,
//                    true,
//                    VolumeObserver(this, errFrag)
//                );
        }
        errFrag.setVolume(-1, false)
    }
}