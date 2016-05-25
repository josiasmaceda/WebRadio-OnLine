package br.com.maceda.android.radioonline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import br.com.maceda.android.radioonline.player.PlayerEvent;

public class PauseReceiver extends BroadcastReceiver {
    private static final String TAG = "PAUSE_RECEIVER";

    public PauseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PAUSE));
    }
}
