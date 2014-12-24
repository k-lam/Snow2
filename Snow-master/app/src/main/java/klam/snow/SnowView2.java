package klam.snow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import java.util.Random;

/**
 * Created by K.Lam on 2014/12/20.
 */
public class SnowView2 extends View{
//    public SnowView2(Context context) {
//        super(context);
//        init();
//    }
//
//    public SnowView2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public SnowView2(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }

//    void init(){
//        setBitmap(generateRid());
//    }

    static int FULLTIME = 25000;
    float xPart,yPart,zPart;
    int mainDuration;//用时间去控制速度,出现位置是限制条件，不是决定条件。
    int initX,initY;
    Rect mRect;//出现区域，经过减去宽高计算的。
    Rect mFloatDownRect;
    Rect mMidRect;
    int displayHeight;//这个其实是雪花的活动区域
    int displayWidth;
    float alphaMax;
    int mRid;
    Paint paint = new Paint();
    Bitmap bitmap;
    int showOutOffset;
    Interpolator decelerateInterpolator = new DecelerateInterpolator();
    Interpolator accelerateInterpolator = new AccelerateInterpolator();
    Interpolator anticipateInterpolator = new AnticipateInterpolator();
    Interpolator overshootInterpolator = new OvershootInterpolator();
    Interpolator sinInterpolator = new InterpolatorCollection.SinInterpolator(random(1,4));
    Interpolator[] interpolators = new Interpolator[]{decelerateInterpolator
            ,accelerateInterpolator
            ,anticipateInterpolator
            ,overshootInterpolator
            ,sinInterpolator};
    public SnowView2(Context context,Rect rect,int displayHeight,int displayWidth){
        super(context);
        showOutOffset = dip2px(40);
        this.displayHeight = displayHeight;
        this.displayWidth = displayWidth;
        setBitmap(generateRid());
        int padding = bitmap.getHeight();
//        mRect = new Rect();
//        mRect.left = rect.left + padding;
//        mRect.top = rect.top + padding;
//        mRect.right = rect.right - padding;
//        mRect.bottom = rect.bottom - padding;
        mRect = rect;
        mFloatDownRect = new Rect(rect.left,rect.top,rect.right,rect.bottom / 2);
        int rpadding = dip2px(50);
        int roffset = (rect.bottom - rect.top) / 5;
        mMidRect = new Rect(rect.left+rpadding,roffset,rect.right - rpadding,rect.bottom - roffset);
        FULLTIME *= (px2dip(displayHeight)  / 332);
        reInit();
    }

    void reInit(){
        initX = random(mRect.left,mRect.right);
        initY = random(mRect.top,mRect.bottom);
        setX(initX);
        setY(initY);
        setRotationX(0f);
        setRotationY(0f);
        setScaleY(1.0f);
        setScaleY(1.0f);
        setPivotX(getWidth() / 2f);
        setPivotY(getHeight() / 2f);
//        setScaleX(1.0f);
//        setScaleY(1.0f);
        yPart = (float)initY / displayHeight;
        yPart = 1f - yPart;
        xPart = (float)initX / displayWidth;
        xPart = 1f - xPart;
        zPart = alphaMax;

        float part = yPart;//这句考虑删去
//        if(part >= 0.11f){
//            part +=  + (random(-1,2) / 10f);
//        }
        mainDuration = (int)(FULLTIME * part);
        setAlpha(alphaMax);
    }

    void setBitmap(int rId){
        this.mRid = rId;
        bitmap = BitmapFactory.decodeResource(getResources(), mRid);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(bitmap.getWidth(),bitmap.getHeight());
        setLayoutParams(layoutParams);
    }
    int[] rids = new int[]{R.drawable.s1,R.drawable.s2,R.drawable.s3,
    R.drawable.ss1,R.drawable.ss2,R.drawable.ss3,
            R.drawable.sss1,R.drawable.sss2,R.drawable.sss3,
            R.drawable.ssss1,R.drawable.ssss2,R.drawable.ssss3,
            R.drawable.sssss1,R.drawable.sssss2,R.drawable.sssss3,
            R.drawable.ssssss1,R.drawable.ssssss2,R.drawable.ssssss3,
    };
    int generateRid(){
        int index = random(0,rids.length - 1);
        alphaMax = 1 - (random(0,4) / 10);
        return rids[index];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,paint);
        //Log.i("snow1","bitmap size:" + bitmap.getWidth() + "," + bitmap.getHeight());
        //Log.i("snow2",getPivotX() + "," + getPivotY());
    }

    boolean isInRect(Rect rect,int x,int y){
        if(x < rect.right && x > rect.left && y > rect.top && y < rect.bottom){
           //Log.i("snow2",rect.toString() + "  (" + x + "," + y + ") true");
            return true;
        }
        //Log.i("snow2",rect.toString() + "  (" + x + "," + y + ") false");
        return false;
    }

    public void loopAnim(){
        loopRunnable.run();
    }
    int[] item_midRect = new int[]{0,1,1,2,3};
    int[] itemNormal = new int[]{0,1,2,3};
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            reInit();
            int pick = 0;
            if(isInRect(mMidRect,initX,initY)){
                pick = item_midRect[random(0,item_midRect.length - 1)];
            }else {
                pick = itemNormal[random(0,itemNormal.length - 1)];
            }
            //ick = 0;
           // Log.i("snow2","pick:"+ pick);
            AnimatorSet animatorSet = new AnimatorSet();
            AnimatorSet animatorSetShowIn = showIn();
            AnimatorSet animatorSetMain = new AnimatorSet();
            switch (pick) {
                case 0: {
                    float part = (((float) random(6, 11) / 10));
                    mainDuration *= part;
                    Rect rect = new Rect();
                    rect.left = mRect.left;
                    rect.right = mRect.right;
                    int temp = (int) ((float) (displayHeight - initY) / 3);
                    rect.top = (int)(-temp * 2);
                    rect.bottom = temp;
                    animatorSetMain.playTogether(anim3DRotation(), anim3DPath(4, 4, 4, displayHeight - initY - random(150,250), rect));
                    animatorSetMain.setDuration(mainDuration);
                    animatorSetMain.addListener(animMainListener);
                    animatorSet.playSequentially(animatorSetShowIn, animatorSetMain);
                    animatorSet.addListener(animatorListener);
                    break;
                }
                case 1:{//环绕
                    float part = (((float) random(2, 4) / 10));
                    mainDuration *= part;
                    int r = dip2px(random(getWidth(),getWidth() + 30));
                    ObjectAnimator objectAnimatorPivot = ObjectAnimator.ofFloat(SnowView2.this, "pivotY", r, 0);
                    ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(SnowView2.this,"scaleX",1,0f);
                    ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(SnowView2.this,"scaleY",1,0f);
                    ObjectAnimator objectAnimatorA = ObjectAnimator.ofFloat(SnowView2.this, "alpha", 1, 0.5f);
                    float fr = part * (RotationFull + 540 ) / 2;
                    if(trueORfalse()){fr = -fr;}
                    ObjectAnimator objectAnimatorR = ObjectAnimator.ofFloat(SnowView2.this,"rotation",0,fr);
                    animatorSetMain.playTogether(objectAnimatorPivot,objectAnimatorX,objectAnimatorY,objectAnimatorA,objectAnimatorR);
                    animatorSetMain.setDuration(mainDuration);
                    animatorSetMain.setInterpolator(decelerateInterpolator);
                    animatorSet.playSequentially(animatorSetShowIn, animatorSetMain);
                    animatorSet.addListener(animatorListener);
                    break;
                }
                case 2:{//闪烁
                    float part = (((float) random(7, 12) / 10));
                    mainDuration *= part;
                    AnimatorSet animPath = null;
                    int count = (int)(4 * yPart);
                    if(count == 0){
                        count = 1;
                    }
                    if(trueORfalse()){
                        Rect rect = new Rect();
                        rect.left = mRect.left;
                        rect.right = mRect.right;
                        int temp = (int) ((float) (displayHeight - initY) / 3);
                        rect.top = (int)(-temp * 2.5);
                        rect.bottom = temp;
                        animPath = anim3DPath(4, 4, 4, displayHeight - initY - random(150,200), rect);
                    }else {
                        int amplitude = dip2px(20);
                        animPath = animFLoatDownWithLR(displayHeight - initY - 100,getWidth() * random(2,4),count);
                    }

                    animatorSetMain.playTogether(animPath,animTwinkle(count * 4),animNormalRotation());
                    animatorSetMain.setDuration(mainDuration);
                    animatorSetMain.addListener(animMainListener);
                    animatorSet.playSequentially(animatorSetShowIn, animatorSetMain);
                    animatorSet.addListener(animatorListener);
                    break;
                }
                default:
                    //普通自转
                    float part = (((float) random(6, 11) / 10));
                    mainDuration *= part;
                    float endR = (float)random((int)(RotationFull / 2 * yPart),(int)(RotationFull * yPart));
                    //if(trueORfalse()){endR = -endR;}
                    //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(SnowView2.this,"rotation",0f,endR);
                    Rect rect = new Rect();
                    rect.left = mRect.left;
                    rect.right = mRect.right;
                    int temp = (int) ((float) (displayHeight - initY) / 4);
                    rect.top = -temp * 2;
                    rect.bottom = temp;
                    animatorSetMain.playTogether(animNormalRotation(), anim3DPath(4, 4, 4, displayHeight - initY - random(150,200), rect));
                    //animatorSetMain.setInterpolator(getRandomInterpolator());
                    animatorSetMain.setDuration(mainDuration);
                    animatorSetMain.addListener(animMainListener);
                    animatorSet.playSequentially(animatorSetShowIn, animatorSetMain);
                    animatorSet.addListener(animatorListener);
                    break;
            }

            animatorSet.start();
        }
    };

    Interpolator getRandomInterpolator(){
        return interpolators[random(0,interpolators.length - 1)];
    }

    Animator.AnimatorListener animMainListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
//            showOut().start();
            animate().alpha(0)
                    .scaleX(0).scaleY(0)
                    .xBy(random(-showOutOffset,showOutOffset)).yBy(random(-showOutOffset,showOutOffset))
                    .setDuration(500)
                    .setInterpolator(decelerateInterpolator);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    Animator.AnimatorListener animatorListener =new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            //reInit();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            SnowView2.this.postDelayed(loopRunnable,random(300,1000));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    AnimatorSet showIn(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, alphaMax);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this,"scaleX",0,1f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this,"scaleY",0,1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(decelerateInterpolator);
        return  animatorSet;
    }
//
//    AnimatorSet showOut(){//错了，现在还没能获取到animatorMain后的状态，要延迟加载
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 0);
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(this,"scaleX",getScaleX(),0f);
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(this,"scaleY",getScaleY(),0f);
//
//        ObjectAnimator objectAnimatorDown = ObjectAnimator.ofFloat(this, "y", getY(), getY() + random(-showOutOffset,showOutOffset));
//        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(this, "x", getX(), getX() + random(-showOutOffset,showOutOffset));
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(objectAnimator).with(objectAnimator1).with(objectAnimator2).with(objectAnimatorDown).with(objectAnimatorX);
//        animatorSet.setDuration(300);
//        animatorSet.setInterpolator(new DecelerateInterpolator());
//        return  animatorSet;
//    }

    ObjectAnimator animFloatDown(){
        return ObjectAnimator.ofFloat(this,"y",getY(),displayHeight - 100).setDuration(mainDuration);
    }

    ObjectAnimator animFloatDown(int distance,int duration){
        return ObjectAnimator.ofFloat(this,"y",getY(),distance).setDuration(duration);
    }

    AnimatorSet animFLoatDownWithLR(int distance,int amplitude,int count){
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(this, "y", getY(), distance);
        if(trueORfalse()){amplitude = - amplitude;}
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(this, "x", getX(), amplitude + getX());
        objectAnimatorX.setInterpolator(new InterpolatorCollection.SinInterpolator(count));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorX,objectAnimatorY);
        return animatorSet;
    }

    public  AnimatorSet animTwinkle(int n) {
        float[] ss = new float[n + 1];
        float[] as = new float[n + 1];
        ss[0] = as[0] = 1;
        for (int i = 1; i < ss.length; i++) {
            if ((i & 1) != 1) {
                ss[i] = 0.5f;
                as[i] = 0.4f;
            } else {
                ss[i] = 1f;
                as[i] = 1f;
            }
        }
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(this,"scaleX",ss);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(this,"scaleY",ss);
        ObjectAnimator objectAnimatorA = ObjectAnimator.ofFloat(this,"alpha",as);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorX).with(objectAnimatorY).with(objectAnimatorA);
        return animatorSet;
    }

//    int RotationXFull = 900;
//    int RotationYFull = 1800;
    int RotationXFull = 45;
    int RotationYFull = 45;
    int RotationFull = 2700;
    AnimatorSet anim3DRotation(){
        AnimatorSet animatorSet = new AnimatorSet();
        int yn = (int)(yPart * 10);
        int xn = (int)(xPart * 10);
        if(yn <= 1){yn = 2;}
        if(xn <= 1){xn = 2;}
        yn = random(yn / 2,yn); xn = random(xn / 2,xn);
        float[] yr = new float[yn + 1]; yr[0] = 0;
        float[] xr = new float[xn + 1]; xr[0] = 0;
        for(int i = 1;i <= yn;i++){
            yr[i] = random(-RotationYFull,RotationYFull);
        }
        for(int i = 1;i <= xn;i++){
            xr[i] = random(-RotationXFull,RotationXFull);
        }
        ObjectAnimator objectAnimatorRX = ObjectAnimator.ofFloat(this,"rotationX",xr);
        ObjectAnimator objectAnimatorRY = ObjectAnimator.ofFloat(this,"rotationY",yr);
        ObjectAnimator objectAnimatorR = ObjectAnimator.ofFloat(this,"rotation",0f,RotationFull * yPart / 6);
        //animatorSet.play(objectAnimatorR).with(objectAnimatorRX).with(objectAnimatorRY).with(animFloatDown());
        animatorSet.playTogether(objectAnimatorR,objectAnimatorRX,objectAnimatorRY);
        //animatorSet.play(objectAnimatorRX).with(objectAnimatorRY).with(objectAnimatorR);
        //animatorSet.setDuration(mainDuration);
        return  animatorSet;
    }

    ObjectAnimator animRotation(float part){
        return ObjectAnimator.ofFloat(this,"rotation",0,part * RotationFull);
    }

    //任意路劲，rect是范围
    AnimatorSet animPath(int maxYN,int maxXN,int normalDownDistance,Rect rect){
        AnimatorSet animatorSet = new AnimatorSet();
        int xn = random(1,maxXN);
        int yn = random(1,maxYN);
        float[] xpath = new float[xn + 1];
        float[] ypath = new float[yn + 1];
        xpath[0] = getX();
        ypath[0] = getY();
        float perPart = ((float)normalDownDistance) / yn;
        for(int i = 1;i <= xn;i++){
            xpath[i] = random(rect.left,rect.right);
        }

        //这样做就能保证y方向匀速下降，但可能遇到风的情况
        for(int i = 1;i <= yn;i++){
            ypath[i] = ypath[i - 1] + perPart;
        }
        int temp = 0;
        for(int i = 1;i <= yn;i++){
            temp = random(rect.top,rect.bottom);
            for(int j = i;j < yn;j++){
                ypath[j] += temp;
            }
            //Log.i("snow1","temp:" + temp + "," + ypath[i]);
        }
        animatorSet.play(ObjectAnimator.ofFloat(this,"x",xpath)).with(ObjectAnimator.ofFloat(this,"y",ypath));
        return animatorSet;
    }

    AnimatorSet anim3DPath(int maxYN,int maxXN,int maxZN,int normalDownDistance,Rect rect){
        AnimatorSet animatorSet = animPath(maxYN,maxXN,normalDownDistance,rect);
        int zn = random(maxZN,maxZN);
        if(zn > 0) {
            float[] alpha = new float[zn + 1];
            float[] scale = new float[zn + 1];
            alpha[0] = getAlpha();
            scale[0] = 1f;
            String s = "";
            for (int i = 1; i <= zn; i++) {
                float temp = random(3,(int)(alphaMax * 10)) / 10f;
                alpha[i] = temp;
                scale[i] =1 - (1 - temp / alphaMax) / 2;
                s += " a:" + alpha[i] + "," + " scale:" + scale[i] + ";";
            }
//            Log.i("snow2",s);
//            animatorSet.play(ObjectAnimator.ofFloat(this,"alpha",alpha));
            animatorSet.playTogether(ObjectAnimator.ofFloat(this,"alpha",alpha)
                                     ,ObjectAnimator.ofFloat(this,"scaleX",scale)
                                     ,ObjectAnimator.ofFloat(this,"scaleY",scale));
        }
        return  animatorSet;
    }

    ObjectAnimator animNormalRotation(){
        float r = 360f;
        if(trueORfalse()){r = -r;}
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,"rotation",0,r);
        int count = ((int)(yPart * 4));
        if(count <= 0){
            count = 2;
        }
        objectAnimator.setInterpolator(new InterpolatorCollection.SinInterpolator(random(1,count)));
        return objectAnimator;
    }

//    AnimatorSet animPath(){
//        Path path = new Path();
//        path.addCircle(300,300,300, Path.Direction.CCW);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this,"x","y",path);
//        objectAnimator.setDuration(10000);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(objectAnimator);
//        return  animatorSet;
//    }

    static  boolean trueORfalse(){
        return  random(0,1) == 0;
    }

    static int random(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public int dip2px( float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public int px2dip(float px){
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(px / scale + 0.5f);
    }
}
