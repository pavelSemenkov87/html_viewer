package pavelsemenkov.yota_html_view.Services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import pavelsemenkov.yota_html_view.MainActivity;

public class LoaderService extends IntentService {

    public static final String ACTION = "action";
    public static final String URL = "url";
    public final static String PINTENT = "pendingIntent";

    public LoaderService() {
        super("LoaderService");
    }

    private static final String EXTRA_SERVICE_CALLBACK = "EXTRA_SERVICE_CALLBACK";
    public static void startLoaderService(Context context, PendingIntent pi, String url) {
        Intent intent = new Intent(context, LoaderService.class).putExtra(PINTENT, pi)
                .putExtra(URL, url);
        intent.setAction(ACTION);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_SERVICE_CALLBACK);
        final String action = intent.getAction();
        PendingIntent pi = intent.getParcelableExtra(PINTENT);
        switch (action) {
            case ACTION:
                String htmlString = "";
                int codeRes = MainActivity.STATUS_FINISH;
                try {
                    java.net.URL url = new URL(intent.getStringExtra(URL));
                    HttpURLConnection httpURLConnection = null;
                    try {
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        int code = httpURLConnection.getResponseCode();
                    } catch (IOException e) {
                        e.printStackTrace();
                        httpURLConnection.disconnect();
                        codeRes = 1;
                    }

                    InputStream stream;
                    try {
                        stream = httpURLConnection.getInputStream();
                    } catch (IOException ignored) {
                        return;
                    }

                    Scanner scanner = new Scanner(stream);
                    while (scanner.hasNextLine())
                        htmlString += scanner.nextLine();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    codeRes = 1;
                }
                Intent intentR = new Intent().putExtra(MainActivity.RESULT, htmlString);
                try {
                    pi.send(LoaderService.this, codeRes, intentR);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
