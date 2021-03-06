package org.smartautomation.user.smartclassroom.Notif;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import org.smartautomation.user.smartclassroom.R;

import java.util.ArrayList;
import java.util.List;

public class Notification_Class extends Fragment {
    View myView;

//    private SectionsPageAdapter sectionsPageAdapter;
    private FragmentActivity myContext;
    //private ViewPager viewPager;
    String stud_id;
    String user;
    Bundle bundle = new Bundle();
    //TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_notification,container,false);
        stud_id = getArguments().getString("Stud_id").toString();
        user = getArguments().getString("User").toString();
        ViewPager viewPager = (ViewPager)myView.findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) myView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return myView;
    }

    private void setupViewPager(ViewPager viewPager){
        Adapter adapter=new Adapter(getChildFragmentManager());
        bundle.putString("id",stud_id);
        bundle.putString("user",user);
        notification_today notification_today= new notification_today();
        notification_all notification_all = new notification_all();
        notification_all.setArguments(bundle);
        notification_today.setArguments(bundle);
        adapter.addFragment(notification_today,"Today");
        adapter.addFragment(notification_all,"All");
        viewPager.setAdapter(adapter);
    }
    public void MessageBox(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
