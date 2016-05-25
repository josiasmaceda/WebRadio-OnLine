package br.com.maceda.android.radioonline.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class PlayerRadio {

    private static final String CATEGORIA = "RADIO_ONLINE";

    public static final String URL_STREAM = "http://painel.localmidia.com.br:8090/aovivo.mp3";
    private static final int WHAT_TIME = 999;
    //public static final String URL_STREAM_TESTE = "http://vprbbc.streamguys.net/vprbbc24.mp3";

    private final Context context;
    private static final int NOVO = 0;
    private static final int TOCANDO = 1;
    private static final int PAUSADO = 2;
    private static final int PARADO = 3;

    private int status = NOVO;
    private boolean estadoInicial = true;
    private boolean loading = false;
    private MediaPlayer player;
    private String uri;

    private Handler handlerTempo = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            estadoInicial = true;
            return false;
        }
    });

    public PlayerRadio(Context context) {
        this.context = context;
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void start(String uri) {
        this.uri = uri;
        //tempo para recarregar o buffer novamente e sincronizar
        //1o minuto, se pausar e voltar a dar play volta para estadoInicial
        handlerTempo.removeMessages(WHAT_TIME);
        handlerTempo.sendEmptyMessageDelayed(WHAT_TIME, 1000 * 60 * 10);

        if (player == null) {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        if (!player.isPlaying()) {
            if (estadoInicial) {
                new Player().execute(uri);
            } else {
                player.start();
                status = TOCANDO;
            }
        }

    }

    public void pause() {
        if (player.isPlaying())
            player.pause();
        status = PAUSADO;
    }

    public void stop() {
        if (player != null)
            player.stop();
        status = PARADO;
        estadoInicial = true;
    }

    public void close() {
        stop();
        if (player != null)
            player.release();
        player = null;
        status = NOVO;
        estadoInicial = true;
    }


    public boolean isPlaying() {
        return status == TOCANDO;
    }

    public boolean isLoading() {
        return loading;
    }

    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean prepared = false;

            try {
                String urlStream = URL_STREAM;
                if ((params != null) && (params[0] != null))
                    urlStream = params[0];

                player = new MediaPlayer();
                player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(urlStream);
                player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        Log.d("PLAYER", "onInfo: what " + what + " - extra " + extra);
                        return false;
                    }
                });
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("PLAYER", "onCompletion: ");
                        estadoInicial = true;
                        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.COMPLETION));
                        close();
                        start(PlayerRadio.this.uri);
                    }
                });
                player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.d("PLAYER", "onError: " + what);
                        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.ERROR));
                        PlayerRadio.this.close();
                        estadoInicial = true;
                        return false;
                    }
                });
                player.prepare();
                WifiManager.WifiLock wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                        .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
                wifiLock.acquire();
                prepared = true;
            } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
                Log.d("Player", "doInBackground " + e.getMessage());
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean pronto) {
            super.onPostExecute(pronto);
            if (pronto) {
                EventBus.getDefault().post(new PlayerEvent(PlayerEvent.DONE));
                player.start();
                status = TOCANDO;
                estadoInicial = false;
            } else {
                EventBus.getDefault().post(new PlayerEvent(PlayerEvent.ERROR));
                PlayerRadio.this.close();
                estadoInicial = true;
            }
            loading = false;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            EventBus.getDefault().post(new PlayerEvent(PlayerEvent.LOADING));
        }
    }
}
