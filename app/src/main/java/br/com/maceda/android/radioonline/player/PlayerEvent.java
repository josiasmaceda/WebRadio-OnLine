package br.com.maceda.android.radioonline.player;

/**
 * Created by josias on 22/04/2016.
 */
public class PlayerEvent {

    public final String action;
    public static final String ERROR = "ERROR";
    public static final String LOADING = "LOADING";
    public static final String DONE = "DONE";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String COMPLETION = "COMPLETION";

    public PlayerEvent(String action) {
        this.action = action;
    }
}
