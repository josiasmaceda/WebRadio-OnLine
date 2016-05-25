package br.com.maceda.android.radioonline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.player.PlayerEvent;

/**
 * Created by josias on 17/05/2016.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
        tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            // state = 1 means when phone is ringing
            if (state == 1) {
                String msg = context.getString(R.string.pausando_web_radio);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(PlayerEvent.PAUSE);
            }
        }
    }
}
