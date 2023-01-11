package llc.newt.inputswapper

import android.os.Bundle
import android.view.View

import androidx.core.content.ContextCompat
import androidx.leanback.app.ErrorSupportFragment

/**
 * This class demonstrates how to extend [ErrorSupportFragment].
 */
class ErrorFragment : ErrorSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)
    }

    internal fun setVolume(currentVolume: Int, mode: Boolean) {
        imageDrawable =
            ContextCompat.getDrawable(activity!!, androidx.leanback.R.drawable.lb_ic_loop)
        message = "Volume changed to: [$currentVolume], mode: [$mode], !"
        setDefaultBackground(TRANSLUCENT)

        buttonText = resources.getString(R.string.dismiss_error)
        buttonClickListener = View.OnClickListener {
            fragmentManager!!.beginTransaction().remove(this@ErrorFragment).commit()
        }
    }

    companion object {
        private val TRANSLUCENT = true
    }
}