package llc.newt.inputswapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

public class VolumeChangeListener extends BroadcastReceiver {
    public final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    public final String MUTE_CHANGED_ACTION = "android.media.STREAM_MUTE_CHANGED_ACTION";

    long lastMutedTime = 0;
    long muteWindow = 250;

    Runnable onVolumeChanged;

    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int streamType = AudioManager.STREAM_MUSIC;

        boolean isMuted = audioManager.isStreamMute(streamType);
        Log.v("MUTE STATE", "It is: " + isMuted);

        if (intent.getAction().equals(MUTE_CHANGED_ACTION)) {
            if (lastMutedTime + muteWindow > System.currentTimeMillis()) {
                Log.v("MUTE STATE", "Got a second mute event, invoke callback");
                if (onVolumeChanged != null) {
                    onVolumeChanged.run();
                }
                return;
            }

            // always update
            lastMutedTime = System.currentTimeMillis();
        }

        // print all extras
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d("IntentData", String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }

//        if (intent.getAction().equals(VOLUME_CHANGED_ACTION)) {
//            int SYSTEM_currentVolume = audioManager.getStreamVolume(streamType);
//        }
    }
}