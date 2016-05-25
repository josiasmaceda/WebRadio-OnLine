package br.com.maceda.android.radioonline.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.List;

import br.com.maceda.android.radioonline.R;

/**
 * Classe utilitária para criar notificações.
 * <p/>
 * http://developer.android.com/guide/topics/ui/notifiers/notifications.html
 */
public class NotificationUtil {

    public static final String ACTION_VISUALIZAR = "br.com.maceda.android.radioonline.ACTION_VISUALIZAR";
    public static final String ACTION_PAUSE = "br.com.maceda.android.radioonline.ACTION_PAUSE";
    public static final String ACTION_PLAY = "br.com.maceda.android.radioonline.ACTION_PLAY";

    private static PendingIntent getPendingIntent(Context context, Intent intent, int id) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);
        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }

    public static void create(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configurações padrão
        b.setSmallIcon(R.mipmap.ic_launcher); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela

        b.setColor(Color.GRAY);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void createBig(Context context, Intent intent, String contentTitle, String contentText, List<String> lines, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Configura o estilo Inbox
        int size = lines.size();
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(contentTitle);
        for (String s : lines) {
            inboxStyle.addLine(s);
        }
        inboxStyle.setSummaryText(contentText);

        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configurações padrão
        b.setSmallIcon(R.mipmap.ic_launcher); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela
        b.setNumber(size); // Número para aparecer na notificação
        b.setStyle(inboxStyle); // Estilo customizado

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void createWithAction(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.PRIORITY_DEFAULT); // DEFAULT_ALL  Ativa configurações padrão
        b.setSmallIcon(R.drawable.ic_radio_white_24dp); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela
        b.setOngoing(true); //Não permite fechar a notificacao
        b.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(contentText)
                .setBigContentTitle(contentTitle));
        //.setSummaryText("Dummy summary text"));

        // Ação customizada (deixei a mesma intent para os dois)
        PendingIntent actionPause = PendingIntent.getBroadcast(
                context, 0, new Intent(ACTION_PAUSE), 0);
        PendingIntent actionPlay = PendingIntent.getBroadcast(
                context, 0, new Intent(ACTION_PLAY), 0);
        b.addAction(R.drawable.ic_pause_black_24dp, "Pause", actionPause);
        b.addAction(R.drawable.ic_play_arrow_black_24dp, "Play", actionPlay);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    // Notification no Android 5.0 Lollipop (Cor vermelha e heads-up e tela de bloqueio)
    public static void createHeadsUpNotification(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configurações padrão
        b.setSmallIcon(R.mipmap.ic_launcher); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela

        // No Android 5.0
        b.setColor(Color.RED);
        // Heads-up notification
        b.setFullScreenIntent(p, false);
        // Privada se estiver na tela de bloqueio
        b.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    // Notification no Android 5.0 Lollipop (tela de bloqueio)
    public static void createPrivateNotification(Context context, Intent intent, String contentTitle, String contentText, int id) {
        PendingIntent p = getPendingIntent(context, intent, id);

        // Cria a notificação
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setDefaults(Notification.DEFAULT_ALL); // Ativa configurações padrão
        b.setSmallIcon(R.mipmap.ic_launcher); // Ícone
        b.setContentTitle(contentTitle); // Título
        b.setContentText(contentText); // Mensagem
        b.setContentIntent(p); // Intent que será chamada ao clicar na notificação.
        b.setAutoCancel(true); // Auto cancela a notificação ao clicar nela

        // PUBLIC, PRIVATE, SECRET
        b.setVisibility(NotificationCompat.VISIBILITY_SECRET);

        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.notify(id, b.build());
    }

    public static void cancell(Context context, int id) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancel(id);
    }

    public static void cancellAll(Context context) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
        nm.cancelAll();
    }
}
