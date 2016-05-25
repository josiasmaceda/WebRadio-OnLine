package br.com.maceda.android.radioonline.player;

public interface InterfacePlayer {
    void play(String uri);

    void play();

    void pause();

    void stop();

    boolean isPlaying();

    boolean isLoading();

    String getUri();
}
