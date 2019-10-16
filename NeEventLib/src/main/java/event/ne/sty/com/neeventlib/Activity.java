package event.ne.sty.com.neeventlib;

import event.ne.sty.com.neeventlib.listener.OnTouchListener;

/**
 * Created by tian on 2019/10/16.
 */

public class Activity {
    public static void main(String[] args) {
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶层容器");
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
        viewGroup.setName("第二层容器");

        View view = new View(0, 0, 200, 200);
        view.setName("子控件View");

        viewGroup1.addView(view);
        viewGroup.addView(viewGroup1);

        viewGroup.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("顶层容器的 onTouch 事件");
                return false;
            }
        });
        viewGroup1.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("第二层容器的 onTouch 事件");
                return false;
            }
        });
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                System.out.println("子控件View的 onTouch 事件");
                return false;
            }
        });

        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
        //顶层容器事件分发
        viewGroup.dispatchTouchEvent(motionEvent);
    }
}
