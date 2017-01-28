package com.mako.srikrishnayarns;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Mako on 1/23/2017.
 */
public class MyUtils {

    public void SlideUP(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_down));
        view.setVisibility(View.GONE);
    }

    public void SlideDown(View view,Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_up));
        view.setVisibility(View.VISIBLE);
    }


}