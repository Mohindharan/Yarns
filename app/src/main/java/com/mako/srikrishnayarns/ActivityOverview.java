package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mako on 1/27/2017.
 */
public class ActivityOverview extends Fragment {
    public ActivityOverview(String key, String category) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.custom_card,container,false);
        return v;
    }
}
