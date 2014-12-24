package klam.snow;

import android.view.animation.Interpolator;

/**
 * Created by K.Lam on 2014/12/22.
 */
public class InterpolatorCollection {
    public static class SinInterpolator implements Interpolator {
        int periodCount;
        public SinInterpolator(int periodCount){
            this.periodCount = periodCount;
        }

        @Override
        public float getInterpolation(float input) {
            return (float)Math.sin(Math.PI * input * periodCount);
        }
    }
}
