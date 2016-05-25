package br.com.maceda.android.radioonline.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PlayerService extends Service implements InterfacePlayer {

    private static final String TAG = "SERVICE_RADIO";
    private PlayerRadio player;
    private String uri;

    public class PlayerServiceBinder extends Binder {
        // Converte para InterfacePlayer
        public InterfacePlayer getInterface() {
            // retorna a interface para controlar o Service
            return PlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new PlayerRadio(getApplicationContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerServiceBinder();
    }

    public void play(String uri) {
        if (player == null)
            player = new PlayerRadio(getApplicationContext());

        this.uri = uri;
        try {
            player.start(uri);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void play() {
        if (player == null)
            player = new PlayerRadio(getApplicationContext());

        this.uri = PlayerRadio.URL_STREAM;
        try {
            player.start(uri);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void pause() {
        player.pause();
    }

    public void stop() {
        player.stop();
    }

    @Override
    public void onDestroy() {
        player.stop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public boolean isLoading() {
        return player.isLoading();
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Subscribe
    public void onMessageEvent(PlayerEvent event) {
        if (event.action == PlayerEvent.PLAY) {
            play(uri);
        } else if (event.action == PlayerEvent.PAUSE) {
            pause();
        } else if (event.action == PlayerEvent.ERROR) {
            player.close();
        }
    }
}
