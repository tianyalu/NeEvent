### NeEvent 简要手写模拟Android事件分发流程
#### 一、本项目示例
##### 1. down(事件未被消费-不对view设置onClickListener --> 参考show/u_event.png)
```android
    ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
    viewGroup.setName("顶层容器");
    ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
    viewGroup1.setName("第二层容器");

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
```
打印日志如下：
```android
     顶层容器->ViewGroup dispatchTouchEvent
     第二层容器->ViewGroup dispatchTouchEvent
     子控件View->View dispatchTouchEvent
     子控件View的 onTouch 事件
     第二层容器->View dispatchTouchEvent
     第二层容器的 onTouch 事件
     顶层容器->View dispatchTouchEvent
     顶层容器的 onTouch 事件
```
参考：  
![image](https://github.com/tianyalu/NeEvent/blob/master/show/u_event.png)  
##### 2. down(事件被消费--对view设置onClickListener --> 参考show/l_event.png)
```android
   //在1.的基础上添加如下代码
    view.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("子控件view的 onClick 事件");
        }
    });
```
打印日志如下：
```android
     顶层容器->ViewGroup dispatchTouchEvent
     第二层容器->ViewGroup dispatchTouchEvent
     子控件View->View dispatchTouchEvent
     子控件View的 onTouch 事件
     子控件view的 onClick 事件
```
参考：  
![image](https://github.com/tianyalu/NeEvent/blob/master/show/l_event.png)  
##### 3. down->up(事件未被消费-不对view设置onClickListener)
```android
   //在1.的基础上添加如下代码
    System.out.println("-----第二次事件分发-----");
    MotionEvent motionEvent2 = new MotionEvent(100, 100);
    motionEvent2.setActionMasked(MotionEvent.ACTION_UP);
    //顶层容器分发事件
    viewGroup.dispatchTouchEvent(motionEvent2);
```
打印日志如下：
```android
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
```
##### 4. down-up(事件被消费-对view设置onClickListener)
```android
   //在2.的基础上添加如下代码
    System.out.println("-----第二次事件分发-----");
    MotionEvent motionEvent2 = new MotionEvent(100, 100);
    motionEvent2.setActionMasked(MotionEvent.ACTION_UP);
    //顶层容器分发事件
    viewGroup.dispatchTouchEvent(motionEvent2);
```
打印日志如下：
```android
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
```
##### 5. down-up(两个子view-事件被消费-对view设置onClickListener)
```android
   //在4.的基础上添加如下代码
    View view2 = new View(0, 0, 300, 300);
    view2.setName("子控件View2");
    
    viewGroup1.addView(view2);
```
打印日志如下：
```android
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
```
`注意：事件被消费后第二次事件分发就不会走view2了`
#### 二、源码解析
##### 1. Activity事件分发流程
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event_activity.jpg)  
##### 2. ViewGroup事件分发流程
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event_viewgroup.jpg) 
##### 4. View事件分发流程
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event_view.jpg) 
 
*** 

##### 5. 事件分发流程U型图
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event_down_u.png)  
##### 6. 事件分发流程down_up流程图  
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event_down_up_consume.png)  
参考：  
[Android事件分发流程
]( https://www.jianshu.com/p/488100d60cad
)  
[讲讲事件分发机制
](https://www.jianshu.com/p/d3758eef1f72
)   
[图解Android事件分发机制
]( https://www.jianshu.com/p/e99b5e8bd67b
) 


#### 三、结论
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event1.png)  
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event2.png)  
![image](https://github.com/tianyalu/NeEvent/blob/master/show/event3.png)

