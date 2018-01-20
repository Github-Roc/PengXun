package cn.peng.pxun.ui.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.peng.pxun.R;

/**
 * 下拉刷新的ListView
 * @author Peng
 */
public class SuperListView extends ListView {
    /** 下拉刷新的头部View */
    private View headerView;
    /** 下拉刷新View的ImageView */
    private ImageView iv_slv_arrow;
    /** 下拉刷新View的ProgressBar */
    private ProgressBar pb_slv_load;
    /** 下拉刷新View的TextView */
    private TextView tv_slv_state;
    /** 下拉刷新头部View的高 */
    private int headerViewHeight;
    /** 下拉刷新箭头向上翻转的动画*/
    private RotateAnimation animUP;
    /** 下拉刷新箭头向下翻转的动画*/
    private RotateAnimation animDOWN;
    /** 下拉刷新ProgressBar旋转的动画*/
    private RotateAnimation pbRotate;
    /** 触摸时的Y轴坐标 */
    private int downY;
    /**SuperListView的回调接口 */
    private OnLoadDataListener mListener;

    /** 下拉刷新目前的状态 */
    private int refreshState = -1;
    /** 下拉刷新状态：下拉刷新 */
    private static final int STATE_PULL_TO_REFRESH = 0;
    /** 下拉刷新状态：松开刷新 */
    private static final int STATE_RELEASE_TO_REFRESH = 1;
    /** 下拉刷新状态：正在刷新 */
    private static final int STATE_REFRESHING = 2;

    private boolean isTopPull = false;

    /**
     * SuperListView的回调接口
     */
    public interface OnLoadDataListener{
        /**
         * 正在刷新的回调
         */
        void onRefresh();
    }
    /**
     * 设置回调接口
     * @param listener
     */
    public void setOnLoadDataListener(OnLoadDataListener listener){
        mListener = listener;
    }
    /**
     * 下拉刷新结束,隐藏头部View
     */
    public void onRefreshFinish(){
        tv_slv_state.setText("刷新成功");
        pb_slv_load.setVisibility(INVISIBLE);
        pb_slv_load.clearAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshState = STATE_PULL_TO_REFRESH;
                startTraslate(headerView, 0, -headerViewHeight);
                tv_slv_state.setText("下拉刷新");
                iv_slv_arrow.setVisibility(VISIBLE);
            }
        },500);
    }

    public SuperListView(Context context) {
        this(context, null);
    }

    public SuperListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initHeaderView();
    }

    /**
     * 初始化下拉刷新的头部View
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.item_slv_header, null);
        iv_slv_arrow = (ImageView) headerView.findViewById(R.id.iv_slv_arrow);
        pb_slv_load = (ProgressBar) headerView.findViewById(R.id.pb_slv_load);
        tv_slv_state = (TextView) headerView.findViewById(R.id.tv_slv_state);
        addHeaderView(headerView);

        headerView.measure(0,0);
        headerViewHeight = headerView.getMeasuredHeight();
        setTopPadding(headerView,-headerViewHeight);

        initAnim();
    }
    /**
     * 初始化下拉刷新的动画
     */
    private void initAnim() {
        animUP = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUP.setDuration(300);
        animUP.setFillAfter(true);

        animDOWN = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDOWN.setDuration(300);
        animDOWN.setFillAfter(true);

        pbRotate = new RotateAnimation(0,359, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        LinearInterpolator lir = new LinearInterpolator();
        pbRotate.setInterpolator(lir);
        pbRotate.setDuration(800);
        pbRotate.setRepeatCount(-1);
    }

    /**
     * 设置控件的上内边距
     * @param view 控件
     * @param topPadding 上内边距
     */
    private void setTopPadding(View view, int topPadding) {
        view.setPadding(0,topPadding,0,0);
    }

    /**
     * 下拉刷新状态发生改变对应的操作
     */
    private void refreshState() {
        switch (refreshState){
            case STATE_PULL_TO_REFRESH:
                tv_slv_state.setText("下拉刷新");
                pb_slv_load.setVisibility(View.INVISIBLE);
                iv_slv_arrow.setVisibility(View.VISIBLE);
                iv_slv_arrow.startAnimation(animDOWN);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tv_slv_state.setText("释放立即刷新");
                pb_slv_load.setVisibility(View.INVISIBLE);
                iv_slv_arrow.setVisibility(View.VISIBLE);
                iv_slv_arrow.startAnimation(animUP);
                break;
            case STATE_REFRESHING:
                tv_slv_state.setText("正在刷新...");
                iv_slv_arrow.clearAnimation();
                iv_slv_arrow.setVisibility(View.INVISIBLE);
                pb_slv_load.setVisibility(View.VISIBLE);
                pb_slv_load.startAnimation(pbRotate);
                break;
        }
    }

    /**
     * @param traslateView
     * @param start
     * @param end
     */
    public void startTraslate(final View traslateView, int start,int end){
        final ValueAnimator va = ValueAnimator.ofInt(start, end);
        //动画更新的监听
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                setTopPadding(traslateView, animatedValue);
            }
        });
        //设置回弹的动画插值器
        //va.setInterpolator(new OvershootInterpolator(3));
        va.setDuration(300);
        va.start();
    }

    public void startRefresh(){
        final ValueAnimator va = ValueAnimator.ofInt(-headerViewHeight, 0);
        //动画更新的监听
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                setTopPadding(headerView, animatedValue);
                if (animatedValue < 0){
                    refreshState = STATE_PULL_TO_REFRESH;
                }else {
                    refreshState = STATE_REFRESHING;
                }
                refreshState();
            }
        });
        va.setDuration(200);
        va.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int dy = moveY - downY;

                if (refreshState == STATE_REFRESHING){
                    break;
                }

                if (dy > 0 && getFirstVisiblePosition() == 0){
                    int topPadding = -headerViewHeight + dy/3;

                    if (topPadding > 0 && refreshState != STATE_RELEASE_TO_REFRESH){
                        refreshState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    }else if (topPadding <= 0 && refreshState != STATE_PULL_TO_REFRESH){
                        refreshState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    setTopPadding(headerView,topPadding);
                    return true;
                }

                if(dy < 0 && getLastVisiblePosition() == getCount() - 1){
                    isTopPull = true;
                    int topPadding = dy/3;
                    setTopPadding(this,topPadding);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (refreshState == STATE_PULL_TO_REFRESH && headerView.getPaddingTop() != -headerViewHeight){
                    startTraslate(headerView, headerView.getPaddingTop(), -headerViewHeight);
                    if (!isTopPull){
                        return true;
                    }
                }else if (refreshState == STATE_RELEASE_TO_REFRESH){
                    refreshState = STATE_REFRESHING;
                    setTopPadding(headerView,0);
                    refreshState();

                    if (mListener != null){
                        mListener.onRefresh();
                    }
                    return true;
                }
                if(isTopPull){
                    isTopPull = false;
                    startTraslate(SuperListView.this, getPaddingTop(), 0);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 获取当前是否是正在刷新状态
     * @return
     */
    public boolean isRefresh(){
        return refreshState == STATE_REFRESHING;
    }
}
