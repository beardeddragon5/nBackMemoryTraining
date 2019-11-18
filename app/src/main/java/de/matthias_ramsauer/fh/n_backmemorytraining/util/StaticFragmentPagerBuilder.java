package de.matthias_ramsauer.fh.n_backmemorytraining.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaticFragmentPagerBuilder {

    private final List<CharSequence> pageTitles;
    private final List<Fragment> fragments;

    public StaticFragmentPagerBuilder(int count) {
        this.fragments = new ArrayList<>(count);
        this.pageTitles = new ArrayList<>(count);
    }

    public void addPage(@Nullable CharSequence title, @NonNull Fragment fragment) {
        this.fragments.add(fragment);
        this.pageTitles.add(title);
    }

    public FragmentPagerAdapter build(FragmentManager manager) {
        return new FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            private CharSequence[] titles = pageTitles.toArray(new CharSequence[pageTitles.size()]);
            private Fragment[] pages = fragments.toArray(new Fragment[fragments.size()]);

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return pages[position];
            }

            @Override
            public int getCount() {
                return pages.length;
            }
        };
    }
}
