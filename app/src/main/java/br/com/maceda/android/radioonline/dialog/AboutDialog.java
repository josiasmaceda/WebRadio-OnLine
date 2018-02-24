package br.com.maceda.android.radioonline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.maceda.android.radioonline.R;

/**
 * Created by josias on 25/01/2016.
 */
public class AboutDialog extends DialogFragment {

    // Methodo utilitario para mostrar o dialog
    public static void show(FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        new AboutDialog().show(ft, "dialog_about");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Cria o html com o texto de about
        SpannableStringBuilder aboutBody = new SpannableStringBuilder();
        aboutBody.append(Html.fromHtml(getString(R.string.about_dialog_text)));

        // Infla o layout
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.dialog_about, null);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(aboutBody);
        textView.setMovementMethod(new LinkMovementMethod());

        // Cria o dialog customizado
        return new AlertDialog.Builder(getActivity())
                .setTitle(getContext().getString(R.string.sobre_aplicativo))
                .setView(view)
                .setNegativeButton(getContext().getString(R.string.ver_aplicativos), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appsMaceda();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    public void appsMaceda(){
        try {
            Uri marketUri = Uri.parse("market://search?q=pub:Maceda");
            startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
        } catch (Exception e) {
            Uri uri = android.net.Uri.parse(getContext().getString(R.string.uri_page_play_developer));
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    }

    private void aplicativosMaceda() {
        try {
            Uri marketUri = Uri.parse(getContext().getString(R.string.uri_market_developer));
            startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
        } catch (Exception e) {
            Uri uri = android.net.Uri.parse(getContext().getString(R.string.uri_page_play_developer));
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);

        }
    }
}
