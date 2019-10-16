package event.ne.sty.com.neeventlib.listener;

import event.ne.sty.com.neeventlib.MotionEvent;
import event.ne.sty.com.neeventlib.View;

/**
 * Created by tian on 2019/10/16.
 */

public interface OnTouchListener {
    boolean onTouch(View view, MotionEvent event);
}
