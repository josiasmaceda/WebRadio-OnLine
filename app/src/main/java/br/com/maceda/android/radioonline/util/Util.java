package br.com.maceda.android.radioonline.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.vo.Missa;
import livroandroid.lib.utils.FileUtils;

/**
 * Created by josias on 24/04/2016.
 */
public class Util {

    private static final String TAG = "UTIL";

    public static void avaliar(Context context) {
        try {
            Uri marketUri = Uri.parse("play://details?id=" + context.getPackageName());
            context.startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
        } catch (Exception e) {
            Uri uri = android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        }
    }


    public static List<Missa> getJsonFromArquivo(Context context) {
        String json = readFileJSON(context, R.raw.missas);
        if (json == null) {
            return null;
        }

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Missa>>() {
        }.getType();
        List<Missa> missas = gson.fromJson(json, collectionType);
        return missas;
    }

    private static String readFileJSON(Context context, int arquivoRaw)  {
        try {
            return FileUtils.readRawFileString(context, arquivoRaw, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            Log.d(TAG, "isNetworkAvailable: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        return false;
    }
}
