package klam.snow;

import android.content.Context;
import android.graphics.Rect;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Handler;

/**
 * Created by Administrator on 2014/12/12.
 */
public class SnowGenerator {
    int standardFolatingCount;
    int initCount;
    Rect rect;
    Context mContext;

    int distance;

    Queue<SnowView> queue_ready = new LinkedList<SnowView>();
    Queue<SnowView> queue_floating = new LinkedList<SnowView>();
    //LinkedList<SnowView> ls_blocking = new LinkedList<SnowView>();
    FrameLayout mLayout;
    public SnowGenerator(Context context,Rect rect,int count,int standardFolatingCount,FrameLayout layout){
        this.rect = rect;
        this.initCount = count;
        this.mContext = context;
        this.mLayout = layout;
        this.standardFolatingCount = standardFolatingCount;
    }

    Queue<SnowView> generate(){
        for(int i = 0;i <= initCount;i++){
            SnowView view = new SnowView(mContext,generateRid(),this,random(rect.left,rect.right),random(rect.top,rect.bottom));
            view.setBitmap(generateRid());
            //setPosition(view);
            queue_ready.add(view);
        }
        return queue_ready;
    }

    /**
     * 喷雪花
     */
    void jet(){
        mLayout.post(runnable_jet);
    }

    boolean stop = false;

    Runnable runnable_jet = new Runnable() {
        @Override
        public void run() {
            if(!stop) {
                int offset = standardFolatingCount / 2;
                int count = (standardFolatingCount - queue_floating.size() + random(-offset, offset)) / 4;
                for (int i = 0; i < count && queue_ready.size() > 0; i++) {
                    SnowView view = queue_ready.poll();
                    view.start(random(0, 1000));
                }
                mLayout.postDelayed(this,random(0,2000));
            }
        }
    };

    void setPosition(SnowView view){
        int x = random(rect.left,rect.right);
        int y = random(rect.top,rect.bottom);
        view.setX(x);   view.setY(y);
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
        return rids[index];
    }

    int delay(){
        return random(0,1000);
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

}
