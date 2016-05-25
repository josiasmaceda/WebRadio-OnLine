package br.com.maceda.android.radioonline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.maceda.android.radioonline.dialog.AboutDialog;
import br.com.maceda.android.radioonline.fragment.TabFragment;
import br.com.maceda.android.radioonline.player.InterfacePlayer;
import br.com.maceda.android.radioonline.player.PlayerService;
import br.com.maceda.android.radioonline.util.NotificationUtil;
import br.com.maceda.android.radioonline.util.Util;


public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "RADIO_MAIN";
    private static final int ID_NOTIFICATION_RADIO = 815;

    private InterfacePlayer interfacePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(getString(R.string.subtitle_app));
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, new TabFragment()).commit();
        Util.getJsonFromArquivo(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        NotificationUtil.cancell(this, ID_NOTIFICATION_RADIO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, PlayerService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);

        if (interfacePlayer != null) {
            if (interfacePlayer.isPlaying()) {
                String mensagem = "\"Posso te dar uma dica? Ame mais! Mas ame verdadeiramente, assim como " +
                        "Jesus te amou na cruz e como Ele espalhou esperança em uma sociedade que perdeu a " +
                        "capacidade de amar. Ame como Jesus amou a sua igreja!\"";

                Intent intentNotification = new Intent(this, MainActivity.class);
                NotificationUtil.createWithAction(this, intentNotification, getResources().getString(R.string.app_name), mensagem, ID_NOTIFICATION_RADIO);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        String textoParaCompartilhar = getString(R.string.texto_para_compartilhar);

        MenuItem menuShare = menu.findItem(R.id.action_compartilhar);
        ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(menuShare);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        share.setShareIntent(shareIntent);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textoParaCompartilhar);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_avaliar) {
            Util.avaliar(this);
        } else if (id == R.id.action_sobre) {
            AboutDialog.show(getSupportFragmentManager());
        } else if (id == R.id.action_fale_conosco) {
            faleConosco();
        } else if (id == R.id.action_peca_oracao) {
            pedirOracao();
        } else if (id == R.id.action_facebook) {
            facebook();
        } else if (id == R.id.action_ligar) {
            ligarParaParoquia();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ligarParaParoquia() {
        Uri uri = Uri.parse(getString(R.string.uri_telefone_nsmh));
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    private void facebook() {
        Uri uri = Uri.parse(getString(R.string.uri_facebook_nsmh));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void pedirOracao() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.pedido_de_oracao));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.gostaria_de_pedir));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_contato)});
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
    }

    private void faleConosco() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contato));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_contato)});
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
    }

    public void onServiceConnected(ComponentName className, IBinder service) {
        PlayerService.PlayerServiceBinder conexao = (PlayerService.PlayerServiceBinder) service;
        interfacePlayer = conexao.getInterface();
    }

    public void onServiceDisconnected(ComponentName className) {
        interfacePlayer = null;
    }


}
