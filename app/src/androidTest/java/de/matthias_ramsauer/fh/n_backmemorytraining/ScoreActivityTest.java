package de.matthias_ramsauer.fh.n_backmemorytraining;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnitRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ScoreActivityTest extends AndroidJUnitRunner {

    /**
     * This action simulates a situation where your activity is stopped
     * or paused, respectively, because it's interrupted by another app
     * or a system action.
     */
    @Test
    public void moveToCreated() {
        final ActivityScenario<ScoreActivity> scenario = ActivityScenario.launch(ScoreActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    /**
     * If a device is low on resources, the system might destroy an
     * activity, requiring your app to recreate that activity when
     * the user returns to your app
     */
    @Test
    public void recreate() {
        final ActivityScenario<ScoreActivity> scenario = ActivityScenario.launch(ScoreActivity.class);
        scenario.recreate();
    }
}
