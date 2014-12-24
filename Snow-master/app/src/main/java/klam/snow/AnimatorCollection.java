package klam.snow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2014/12/12.
 */
public class AnimatorCollection {
    SnowView view;
    public AnimatorCollection(SnowView view){
        this.view = view;
    }

    public ObjectAnimator getFloatDown(float distance){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"y",view.getY(),distance + view.getY());
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(5000);
        return  objectAnimator;
    }

    public  ObjectAnimator getFloatDown(float distance,int duration){
        return  getFloatDown(distance).setDuration(duration);
    }

    public  ObjectAnimator getRotation(int n){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0f,(float)(n * 360));
        return  objectAnimator;
    }

    public  ObjectAnimator getRotation(int n,int duration){
        return  getRotation(n).setDuration(duration);
    }

    public  ObjectAnimator getTRotation(){
        float pi = (float)360;
        //float[] rs = new float[]{pi / 4, 0, pi / 5, 0,pi/6};
        //ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",rs);
        //objectAnimator.setInterpolator(new BounceInterpolator());
        Keyframe[] keyframes = new Keyframe[4];
        keyframes[0] = Keyframe.ofFloat(0f,0f);
        keyframes[1] = Keyframe.ofFloat(0.3f,pi / 8);
        keyframes[2] = Keyframe.ofFloat(.6f,0);
        keyframes[3] = Keyframe.ofFloat(1f,pi / 8);
//        keyframes[4] = Keyframe.ofFloat(1f,-pi / 6);
//        keyframes[0].setInterpolator(new DecelerateInterpolator());
//        keyframes[2].setInterpolator(new DecelerateInterpolator());
////        keyframes[4].setInterpolator(new DecelerateInterpolator());
////        keyframes[1].setInterpolator(new AccelerateInterpolator());
////        keyframes[3].setInterpolator(new AccelerateInterpolator());
//        keyframes[1].setInterpolator(new DecelerateInterpolator());
//        keyframes[3].setInterpolator(new DecelerateInterpolator());
        //keyframes[5].setInterpolator(new AccelerateInterpolator());
        PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("rotation", keyframes);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,pvh);
        return  objectAnimator;
    }

    public  ObjectAnimator getSurround(int n){
        view.setPivotX(-20);
        view.setPivotY(-20);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0f,(float)(n * 360));
        //objectAnimator.setInterpolator(new BounceInterpolator());
        return objectAnimator;
    }

    public  AnimatorSet getScale(int n,int duration) {
        float[] ss = new float[n];
        float[] as = new float[n];
        for (int i = 0; i != ss.length; i++) {
            if ((i & 1) != 1) {
                ss[i] = 0.5f;
                as[i] = 0.4f;
            } else {
                ss[i] = 1.1f;
                as[i] = 1f;
            }
        }
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view,"scaleX",ss).setDuration(duration);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view,"scaleY",ss).setDuration(duration);
        ObjectAnimator objectAnimatorA = ObjectAnimator.ofFloat(view,"alpha",as).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimatorX).with(objectAnimatorY).with(objectAnimatorA);
        return animatorSet;
    }


    public  ObjectAnimator getAlpha(int n) {
        float[] ss = new float[n];
        for (int i = 0; i != ss.length; i++) {
            if ((i & 1) != 1) {
                ss[i] = 0.3f;
            } else {
                ss[i] = 1f;
            }
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"alpha",ss);
        return objectAnimator;
    }

    ObjectAnimator getFloatLeft(float distance) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "x", view.getX(), distance + view.getX());
        return objectAnimator;
    }

    ObjectAnimator getFadeIn(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        objectAnimator.setDuration(500);
        return objectAnimator;
    }

    ObjectAnimator getFadeOut(int delay){
        ObjectAnimator objectAnimator =  ObjectAnimator.ofFloat(view,"alpha",1f,0).setDuration(500);
        objectAnimator.setStartDelay(delay);
        return objectAnimator;

    }

    /*
    * fdis 向下飘落距离
    * fdur 向下飘落时间
    * rn 自转圈数
    * srn 公转圈数
    * sn 缩放次数
    * dd
    * ld 水平左移距离
     */
    public AnimatorSet pickOne(float fdis,int fdur,int rn,int srn,int sn,int ld,int duration){
        int n = SnowGenerator.random(0,5);
       // n = 2;
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = animatorSet.play(getFadeIn());
        switch (n) {
            case 0://左下飘
                builder.with(getFloatDown(fdis, fdur)).with(getFloatLeft(ld).setDuration(duration));
                break;
            case 1://旋转下降
                builder.with(getFloatDown(fdis, duration)).with(getRotation(rn,duration));
                break;
            case 2://缩放
                builder.with(getFloatDown(fdis, fdur)).with(getScale(sn,duration));
                break;
            case 3://向左缩放
                builder.with(getFloatDown(fdis, fdur)).with(getScale(1,duration)).with(getFloatLeft(ld / 2));
                break;
            case 4://surround  有点傻逼,不飘落会更漂亮,但必须在屏幕中间区域
                //builder.with(getFloatDown(fdis, fdur)).with(getSurround(srn));
                view.setY(view.mfy * 1.5f);
                builder.with(getSurround(srn).setDuration(duration));
                break;
            case 5://左右转  左右切换的时候太生硬了
                builder.with(getFloatDown(fdis, fdur)).with(getTRotation().setDuration(duration));
                break;
//            case  6://alpha 试试和2一起
//                builder.with(getAlpha(3)).with(getFloatDown(fdis,fdur));
//                break;
        }
       // animatorSet.setDuration(duration);
        builder.with(getFadeOut(duration - 500));
       // AnimatorSet animatorSet1 = new AnimatorSet();
        //animatorSet1.play(animatorSet).before(getFadeOut());

        return animatorSet;
    }



//    static class FloatDown{
//        static ObjectAnimator get(View view,float distance){
//            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"y",view.getY(),distance + view.getY());
//            objectAnimator.setInterpolator(new LinearInterpolator());
//            objectAnimator.setDuration(5000);
//            return  objectAnimator;
//        }
//
//        static ObjectAnimator get(View view,float distance,int duration){
//            return  get(view,distance).setDuration(duration);
//        }
//
//        static void restore(SnowView view){
//            view.mGenerator.setPosition(view);
//        }
//    }

        static class Roation{
             static ObjectAnimator get(View view,int n){
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0f,(float)(n * Math.PI));
                    return  objectAnimator;
              }
            static ObjectAnimator get(View view,int n,int duration){
                return  get(view,n).setDuration(duration);
            }

        static class TRotation{
            static ObjectAnimator get(View view){
                float pi = (float)Math.PI;
                float[] rs = new float[]{pi / 2, - pi / 3, 0, -pi,pi,pi / 4,0};
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",rs);
                objectAnimator.setInterpolator(new BounceInterpolator());
                return  objectAnimator;
            }
        }

        static class Surround{
            static ObjectAnimator get(View view,int n){
                view.setPivotX(0);
                view.setPivotY(0);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0f,(float)(n * Math.PI));
                objectAnimator.setInterpolator(new BounceInterpolator());
                return objectAnimator;
            }
        }

        static class Scale{
            static ObjectAnimator get(View view,int n){
                float[] ss = new float[n * 2];
                for(int i = 0;i != ss.length;i++){
                    if((i & 1) != 1){
                        ss[i] = 0.5f;
                    }else {
                        ss[i] = 1.1f;
                    }
                }
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"scale",ss);
                return objectAnimator;
            }
        }

        static class FloatLeft{
            static ObjectAnimator get(View view,float distance) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "x", view.getX(), distance + view.getX());
                return objectAnimator;
            }
        }
    }

}

interface ISnowAnimator{
    void restore();

}
