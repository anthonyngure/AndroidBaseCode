/*
 * Copyright (c) 2018.
 *
 * Anthony Ngure
 *
 * Email : anthonyngure25@gmail.com
 */

package ke.co.toshngure.showcase;

import android.graphics.Point;
import android.view.View;


public interface IAnimationFactory {

    void fadeInView(View target, long duration, AnimationStartListener listener);

    void fadeOutView(View target, long duration, AnimationEndListener listener);

    void animateTargetToPoint(MaterialShowcaseView showcaseView, Point point);

    interface AnimationStartListener {
        void onAnimationStart();
    }

    interface AnimationEndListener {
        void onAnimationEnd();
    }
}

