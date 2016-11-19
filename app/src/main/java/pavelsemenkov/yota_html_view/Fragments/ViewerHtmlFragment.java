package pavelsemenkov.yota_html_view.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pavelsemenkov.yota_html_view.R;

public class ViewerHtmlFragment extends Fragment {

    private TextView html_view;
    private String html;

    public static ViewerHtmlFragment getInstance(String html) {
        ViewerHtmlFragment requestFragment = new ViewerHtmlFragment();
        requestFragment.setHtml(html);
        return requestFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.viewer_html_fragment, container, false);
        html_view = (TextView)view.findViewById(R.id.html_view);
        html_view.setText(html);
        return view;
    }
    private void setHtml(String html){
        this.html = html;
    }
}