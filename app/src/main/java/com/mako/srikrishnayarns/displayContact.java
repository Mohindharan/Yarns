package com.mako.srikrishnayarns;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class displayContact extends Fragment
{
    String key;
    String category;
    View view;
    String name;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View v =inflater.inflate(R.layout.display_contact,container,false);

        return v;
    }
    public displayContact(String s, String name, String category) {
        this.key = s;
        this.category = category;
        this.name=name;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        initUI();
    }

    private void initUI() {
        getActivity().setTitle("Contact Information");
        TextView tx=(TextView)view.findViewById(R.id.contact_Main_name_tv);
        tx.setText(name+"("+category+")");
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new sliderAdapter(getChildFragmentManager()));
        tabLayout=(TabLayout)view.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    public class sliderAdapter extends FragmentPagerAdapter {
        private String tabs[]={"Overview","Activities"};

        public sliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new ContactOverview(key,category);
                case 1:
                    return new ActivityOverview(key,category);
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
