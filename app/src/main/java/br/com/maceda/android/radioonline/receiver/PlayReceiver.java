package br.com.maceda.android.radioonline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.maceda.android.radioonline.player.PlayerEvent;

public class PlayReceiver extends BroadcastReceiver {
    private static final String TAG = "PLAY_RECEIVER";

    public PlayReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Play");
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY));
    }
}
