package klam.snow;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends Activity {
    FrameLayout layout;
    SnowGenerator generator;
    GifImageView gifImageView;
    List<SnowView2> ls_SnowView2 = new LinkedList<SnowView2>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (FrameLayout) findViewById(R.id.ly);
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
//        generator = new SnowGenerator(this,new Rect(50,-100,width - 100, height /  2),30,10,layout);
//        generator.distance = height;
//        generator.generate();
//        generator.jet();

        int count = 15;
        while(count-- > 0) {
            SnowView2 view = new SnowView2(this, new Rect(50, 20, width - 50, height / 5 * 2), height -50, width);
            ls_SnowView2.add(view);
//            layout.addView(view);
//            view.loopAnim();
        }
        final View tree1 = findViewById(R.id.img_tree1);
        final View tree2 = findViewById(R.id.img_tree2);
        final View tree3 = findViewById(R.id.img_tree3);
        gifImageView = (GifImageView) findViewById(R.id.gif_monster);
        layout.removeView(tree1); layout.removeView(tree2);  layout.removeView(tree3); layout.removeView(gifImageView);
        layout.post(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                for(;index < 5;index++){
                    SnowView2 view = ls_SnowView2.get(index);
                    layout.addView(view);
                    view.loopAnim();
                }
                layout.addView(tree2);
                for(;index < 10;index++){
                    SnowView2 view = ls_SnowView2.get(index);
                    layout.addView(view);
                    view.loopAnim();
                }
                layout.addView(tree1);
                for(;index < 15;index++){
                    SnowView2 view = ls_SnowView2.get(index);
                    layout.addView(view);
                    view.loopAnim();
                }
                layout.addView(tree3);
                layout.addView(gifImageView);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int count = 5;
                        while(count-- > 0) {
                            SnowView2 view = new SnowView2(MainActivity.this, new Rect(100, 100, width - 100, height / 2), height, width);
                            layout.addView(view);
                            view.loopAnim();
                        }
                    }
                }, 5000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int count = 5;
                        while(count-- > 0) {
                            SnowView2 view = new SnowView2(MainActivity.this, new Rect(100, 100, width - 100, height / 2), height, width);
                            layout.addView(view);
                            view.loopAnim();
                        }
                    }
                }, 8000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int count = 5;
                        while(count-- > 0) {
                            SnowView2 view = new SnowView2(MainActivity.this, new Rect(100, 100, width - 100, height / 2), height, width);
                            layout.addView(view);
                            view.loopAnim();
                        }
                    }
                }, 11000);
            }
        });



        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gifImageView.setVisibility(View.GONE);
                gifImageView.postDelayed(gif_runnable,60000);
                gifImageView.setClickable(false);
                ((GifDrawable)gifImageView.getDrawable()).pause();
                gifImageView.animate().alpha(0).setDuration(1000);
            }
        });

    }

    Runnable gif_runnable = new Runnable() {
        @Override
        public void run() {
            //gifImageView.setVisibility(View.VISIBLE);
            gifImageView.animate().alpha(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((GifDrawable)gifImageView.getDrawable()).start();
                    gifImageView.setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    };
}
