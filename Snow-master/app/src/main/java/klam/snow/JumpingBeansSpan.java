package klam.snow;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.text.TextPaint;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/*package*/ final class JumpingBeansSpan extends SuperscriptSpan implements ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator jumpAnimator;
    private WeakReference<TextView> textView;
    private int shift;
    private int delay;
    private int loopDuration;
    private float animatedRange;

    public JumpingBeansSpan(TextView textView, int loopDuration, int position, int waveCharOffset,
                            float animatedRange) {
        this.textView = new WeakReference(textView);
        this.delay = waveCharOffset * position;
        this.loopDuration = loopDuration;
        this.animatedRange = animatedRange;
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        initIfNecessary(tp);
        tp.baselineShift = shift;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        initIfNecessary(tp);
        tp.baselineShift = shift;
    }

    private void initIfNecessary(TextPaint tp) {
        if (jumpAnimator != null) {
            return;
        }

        shift = (int) tp.ascent() / 2;
        jumpAnimator = ValueAnimator.ofInt(0, shift, 0);
        jumpAnimator
                .setDuration(loopDuration)
                .setStartDelay(delay);
        jumpAnimator.setInterpolator(new JumpInterpolator(animatedRange));
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        jumpAnimator.addUpdateListener(this);
        jumpAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        // No need for synchronization as this always run on main thread anyway
        TextView v = textView.get();
        if (v != null) {
            if (isAttachedToHierarchy(v)) {
                shift = (Integer) animation.getAnimatedValue();
                v.invalidate();
            } else {
                animation.setCurrentPlayTime(0);
                animation.start();
            }
        } else {
            // The textview has been destroyed and teardown() hasn't been called
            teardown();
            if (BuildConfig.DEBUG) {
                Log.e("JumpingBeans", "!!! Remember to call JumpingBeans.stopJumping() when appropriate !!!");
            }
        }
    }

    private boolean isAttachedToHierarchy(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return v.isAttachedToWindow();
        } else {
            return v.getParent() != null;   // Best-effort fallback
        }
    }

    /*package*/ void teardown() {
        if (jumpAnimator != null) {
            jumpAnimator.cancel();
            jumpAnimator.removeAllListeners();
        }
        if (textView.get() != null) {
            textView.clear();
        }
    }

    /**
     * A tweaked {@link android.view.animation.AccelerateDecelerateInterpolator}
     * that covers the full range in a fraction of its input range, and holds on
     * the final value on the rest of the input range. By default, this fraction
     * is half of the full range.
     *
     * @see kl.lib3rd.other.JumpingBeans#DEFAULT_ANIMATION_DUTY_CYCLE
     */
    private class JumpInterpolator implements TimeInterpolator {

        private float animRange;

        public JumpInterpolator(float animatedRange) {
            animRange = Math.abs(animatedRange);
        }

        @Override
        public float getInterpolation(float input) {
            if (input <= animRange) {
                return (float) (Math.cos((input / animRange + 1) * Math.PI) / 2f) + 0.5f;
            }
            return 1.0f;
        }
    }
}
