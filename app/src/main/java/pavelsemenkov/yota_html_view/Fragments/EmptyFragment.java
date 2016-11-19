package pavelsemenkov.yota_html_view.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pavelsemenkov.yota_html_view.R;

public class EmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.empty_fragment, container, false);
        return view;
    }
}