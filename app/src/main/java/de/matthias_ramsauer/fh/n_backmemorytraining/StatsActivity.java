package de.matthias_ramsauer.fh.n_backmemorytraining;

import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats.StatsGraphFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.ui.stats.StatsListFragment;
import de.matthias_ramsauer.fh.n_backmemorytraining.util.StaticFragmentPagerBuilder;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        setSupportActionBar(findViewById(R.id.toolbar));

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final StaticFragmentPagerBuilder builder = new StaticFragmentPagerBuilder(2);
        builder.addPage(getResources().getText(R.string.stats_list), new StatsListFragment());
        builder.addPage(getResources().getText(R.string.stats_graph), new StatsGraphFragment());

        final FragmentPagerAdapter adapter = builder.build(getSupportFragmentManager());
        final ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(adapter);
    }
}