package llc.newt.inputswapper

import android.accessibilityservice.AccessibilityService
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.tv.TvInputInfo
import android.media.tv.TvInputManager
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast


class AccessibilityService : AccessibilityService(), Runnable {

    val timeToHold: Long = 1000 // in ms
    var startedTimer = false
    var inputSwitchingHandler: Handler? = null

    var curInput = -1

    var tvInputs: List<TvInputInfo>? = null

    override fun onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show()
        Log.d(TAG, "onDestroy")
    }

    override fun onStart(intent: Intent, startId: Int) {
        Toast.makeText(this, "Started InputSwapper!", Toast.LENGTH_LONG).show();
        Log.v(TAG, "Started InputSwapper service")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        inputSwitchingHandler = Handler(Looper.getMainLooper())
        val tvInputManager = applicationContext.getSystemService(TV_INPUT_SERVICE) as TvInputManager

        Log.d(TAG, "InputSwapper AccessibilityService is connected!")

        val tvPassthroughInputs = tvInputManager.tvInputList.filter {
            // only show passthrough inputs of type HDMI
            // TODO: could cycle through all inputs on config
            it.isPassthroughInput && it.type == TvInputInfo.TYPE_HDMI
        }

        for (tvInput in tvPassthroughInputs) {
            Log.v(TAG, "Found passthrough input: ${tvInput.id}, ${tvInput.type}")
        }

        // save them for later!
        tvInputs = tvPassthroughInputs
    }

    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        Log.d(TAG, "onAccessibiltyEvent$accessibilityEvent")
    }

    override fun onInterrupt() {}

    // here you can intercept the keyevent
    override fun onKeyEvent(event: KeyEvent): Boolean {
        return handleKeyEvent(event)
    }

    private fun handleKeyEvent(event: KeyEvent): Boolean {
        val action: Int = event.action
        val keyCode: Int = event.keyCode
//        Log.v(TAG, "GOT KEYPRESS: [$action, $keyCode]")

        val inputSwitchingHandler = this.inputSwitchingHandler?.let { it } ?: return false

        if (keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            if (action == KeyEvent.ACTION_DOWN) {
                Log.v(TAG, "Got mute key down!")
                // mute down received, start a timer to change the input
                inputSwitchingHandler.postDelayed(this, timeToHold)
                startedTimer = true;
                return true
            } else if (action == KeyEvent.ACTION_UP) {
                Log.v(TAG, "Got mute key up!")
                // mute release received, stop our timer
                if (startedTimer) {
                    inputSwitchingHandler.removeCallbacksAndMessages(null)
                }
                startedTimer = false
                return true
            }
        }
        return false
    }

    // actually change the input, and reschedule another input changing call
    override fun run() {
        val inputSwitchingHandler = this.inputSwitchingHandler?.let { it } ?: return
        val tvInputs = this.tvInputs?.let { it } ?: return

        if (startedTimer && tvInputs.isNotEmpty()) {
            // make sure we've started the timer, and then change the input
            curInput = (curInput + 1) % tvInputs.size
            val nextInput = tvInputs[curInput]
            Log.v(TAG, "Changing the input to option #$curInput (${nextInput.id}, ${nextInput.type})")

            // launch the channel
            val channelSlug = "${nextInput.id}".replace("/", "%2F")
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("content://android.media.tv/passthrough/${channelSlug}"))
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(i)

            inputSwitchingHandler.postDelayed(this, timeToHold)
        }
    }
}