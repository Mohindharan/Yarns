package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Mako on 2/8/2017.
 */
public class Review_form extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_forms,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setdarktoolbarcolor();
        getActivity().setTitle("Review forms");
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new Review_form.sliderAdapter(getChildFragmentManager()));
        tabLayout=(TabLayout)view.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }
    public class sliderAdapter extends FragmentPagerAdapter {
        private String tabs[]={"C Forms","E1 Forms"};

        public sliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new C_forms();
                case 1:
                    return new E1_forms();
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
