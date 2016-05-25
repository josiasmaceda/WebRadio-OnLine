package br.com.maceda.android.radioonline.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.player.InterfacePlayer;
import br.com.maceda.android.radioonline.player.PlayerEvent;
import br.com.maceda.android.radioonline.player.PlayerRadio;
import br.com.maceda.android.radioonline.player.PlayerService;
import br.com.maceda.android.radioonline.util.Util;


public class RadioFragment extends Fragment implements ServiceConnection {
    private static final String TAG = "RadioFrag";

    private ProgressWheel progressBar;
    private FloatingActionButton fab;
    private View view;
    private InterfacePlayer interfacePlayer;

    public RadioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getActivity(), PlayerService.class);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unbindService(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);

        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(getContext())) {
                    if (interfacePlayer != null) {
                        if (interfacePlayer.isPlaying()) {
                            interfacePlayer.pause();
                            fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
                        } else {
                            interfacePlayer.play(PlayerRadio.URL_STREAM);
                            fab.setImageResource(R.drawable.ic_pause_white_24dp);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.sem_conexao_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.view = view;
        return view;
    }

    public void onServiceConnected(ComponentName className, IBinder service) {
        PlayerService.PlayerServiceBinder conexao = (PlayerService.PlayerServiceBinder) service;
        interfacePlayer = conexao.getInterface();
        if (interfacePlayer.isLoading()) {
            progressBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        } else if (interfacePlayer.isPlaying()) {
            progressBar.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_pause_white_24dp);
            fab.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            fab.setVisibility(View.VISIBLE);
        }
    }

    public void onServiceDisconnected(ComponentName className) {
        interfacePlayer = null;
    }

    @Subscribe
    public void onMessageEvent(PlayerEvent event) {
        if (event.action == PlayerEvent.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        } else if (event.action == PlayerEvent.DONE) {
            progressBar.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_pause_white_24dp);
            fab.setVisibility(View.VISIBLE);
        } else if (event.action == PlayerEvent.ERROR) {
            Snackbar.make(view, R.string.radio_offline, Snackbar.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            fab.setVisibility(View.VISIBLE);
        } else if (event.action == PlayerEvent.COMPLETION) {
            progressBar.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.ic_play_arrow_white_24dp);
            fab.setVisibility(View.VISIBLE);
        }
    }

}
