package pavelsemenkov.yota_html_view;

import android.app.ProgressDialog;

public class MyProgressDialog {

    private MainActivity activity;
    private ProgressDialog ProgressDialog;
    private String dialogText;

    public MyProgressDialog(MainActivity activity) {
        ProgressDialog = new ProgressDialog(activity);
        this.activity = activity;
    }

    public void showProgressDialog(int textResourceId) {
        ProgressDialog.setIndeterminate(true);
        dialogText = activity.getString(textResourceId);
        ProgressDialog.setMessage(dialogText);
        ProgressDialog.setCancelable(true);
        ProgressDialog.setCanceledOnTouchOutside(false);
        ProgressDialog.show();
    }

    public void dismissProgressDialog() {
        ProgressDialog.dismiss();
    }
}
