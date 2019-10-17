package event.ne.sty.com.neeventlib;

import event.ne.sty.com.neeventlib.listener.OnClickListener;
import event.ne.sty.com.neeventlib.listener.OnTouchListener;

/**
 * Created by tian on 2019/10/16.
 */

public class Activity {
    public static void main(String[] args) {
        ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶层容器");
        ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
        viewGroup1.setName("第二层容器");

        View view = new View(0, 0, 200, 200);
        view.setName("子控件View");
        View view2 = new View(0, 0, 300, 300);
        view2.setName("子控件View2");

        viewGroup1.addView(view);
        viewGroup1.addView(view2);
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
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("子控件view的 onClick 事件");
            }
        });

        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
        //顶层容器事件分发
        viewGroup.dispatchTouchEvent(motionEvent);
        /**
         * down(事件未被消费-不对view设置onClickListener --> 参考show/u_event.png)
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             第二层容器->View dispatchTouchEvent
             第二层容器的 onTouch 事件
             顶层容器->View dispatchTouchEvent
             顶层容器的 onTouch 事件
         */

        /**
         * down(事件被消费--对view设置onClickListener --> 参考show/l_event.png)
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             子控件view的 onClick 事件
         */

        System.out.println("-----第二次事件分发-----");
        MotionEvent motionEvent2 = new MotionEvent(100, 100);
        motionEvent2.setActionMasked(MotionEvent.ACTION_UP);
        //顶层容器分发事件
        viewGroup.dispatchTouchEvent(motionEvent2);
        /**
         * down->up(事件未被消费-不对view设置onClickListener)
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             第二层容器->View dispatchTouchEvent
             第二层容器的 onTouch 事件
             顶层容器->View dispatchTouchEvent
             顶层容器的 onTouch 事件
             -----第二次事件分发-----
             顶层容器->ViewGroup dispatchTouchEvent
             顶层容器->View dispatchTouchEvent
             顶层容器的 onTouch 事件
         */

        /**
         * down-up(事件被消费-对view设置onClickListener)
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             子控件view的 onClick 事件
             -----第二次事件分发-----
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             子控件view的 onClick 事件
         */

        /**
         * down-up(两个子view-事件被消费-对view设置onClickListener)
         * 事件被消费后第二次事件分发就不会走view2了
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View2->View dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             子控件view的 onClick 事件
             -----第二次事件分发-----
             顶层容器->ViewGroup dispatchTouchEvent
             第二层容器->ViewGroup dispatchTouchEvent
             子控件View->View dispatchTouchEvent
             子控件View的 onTouch 事件
             子控件view的 onClick 事件
         */
    }
}
