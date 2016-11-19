package pavelsemenkov.yota_html_view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import pavelsemenkov.yota_html_view.Fragments.EmptyFragment;
import pavelsemenkov.yota_html_view.Fragments.ViewerHtmlFragment;
import pavelsemenkov.yota_html_view.Services.LoaderService;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FragmentTransaction fTrans;
    private EditText Input;
    private MyProgressDialog myProgressDialog;
    private final String LOG_TAG = "myLogs";
    public final static int STATUS_FINISH = 200;
    public final static String RESULT = "result";
    public final static int CODE_SERVICE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Input = (EditText) findViewById(R.id.url);
        myProgressDialog = new MyProgressDialog(MainActivity.this);
        initToolbar();
        initView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.activity_frame, new EmptyFragment());
        fTrans.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_head, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.see_html:
                if (isConnectingToInternet()) {
                    String url = "f";
                    url = Input.getText().toString();
                    if (URLUtil.isValidUrl(url)) {
                        hideKeyBoard();
                        PendingIntent pi;
                        Intent intent, intent1 = new Intent(this, LoaderService.class);
                        pi = createPendingResult(CODE_SERVICE, intent1, 0);
                        LoaderService.startLoaderService(this, pi, url);
                        myProgressDialog.showProgressDialog(R.string.progress_dialog_text);
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.not_valid_url),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.no_internet),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);
        if (resultCode == STATUS_FINISH) {
            switch (requestCode) {
                case CODE_SERVICE:
                    fTrans = getSupportFragmentManager().beginTransaction();
                    fTrans.replace(R.id.activity_frame, ViewerHtmlFragment.getInstance(data.getStringExtra(RESULT)));
                    fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    fTrans.commit();
                    myProgressDialog.dismissProgressDialog();
                    break;
            }
        }else {
            myProgressDialog.dismissProgressDialog();
            Toast.makeText(MainActivity.this, R.string.not_load_url, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
