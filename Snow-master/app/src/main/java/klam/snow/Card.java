package klam.snow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.michaelevans.colorart.library.ColorArt;

import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by KL on 2014/12/24.
 */
public class Card extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        int rid = R.drawable.animated_gif_christmas;
        GifImageView gifImageView = (GifImageView) findViewById(R.id.gif);
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable( getResources(), rid );
            gifImageView.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),rid);
        ColorArt colorArt = new ColorArt(bitmap);
        bitmap.recycle();

        View layout = findViewById(R.id.layout);
        TextView tv_who = (TextView)findViewById(R.id.tv_who);
        TextView tv_mc_word = (TextView)findViewById(R.id.tv_mc_word);
        TextView tv_fromWho = (TextView)findViewById(R.id.tv_fromWho);

        JumpingBeans jumpingBeans = new JumpingBeans.Builder().appendJumpingDots(tv_mc_word).build();
//        Button btn = (Button)findViewById(R.id.btn);

        layout.setBackgroundColor(colorArt.getBackgroundColor());
        tv_who.setTextColor(colorArt.getPrimaryColor());
        tv_mc_word.setTextColor(colorArt.getPrimaryColor());
        tv_fromWho.setTextColor(colorArt.getPrimaryColor());

//        btn.setTextColor(colorArt.getSecondaryColor());
//        btn.setBackgroundColor(colorArt.getDetailColor());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Card.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
