package com.yunapp.libx.page;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.yunapp.libx.AppConfig;
import com.yunapp.libx.AppListener;

public class PageManager {

    private static final int MAX_COUNT = 5;

    private Context mContext;
    private AppConfig mAppConfig;
    private FrameLayout mPageContainer;

    public PageManager(Context context, AppConfig appConfig) {
        mContext = context;
        mAppConfig = appConfig;
        mPageContainer = new FrameLayout(context);

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        LayoutTransition transition = new LayoutTransition();
        // View 页面转场动画
        transition.setAnimator(LayoutTransition.APPEARING, getAppearingAnimation(dm.widthPixels));
        transition.setAnimator(LayoutTransition.DISAPPEARING, getDisappearingAnimation(dm.widthPixels));
        // 动画开始延迟
        transition.setStartDelay(LayoutTransition.APPEARING, 0);
        transition.setStartDelay(LayoutTransition.DISAPPEARING, 0);
        transition.setStartDelay(LayoutTransition.CHANGE_APPEARING, 0);
        transition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 0);
        // 动画执行时间
        transition.setDuration(LayoutTransition.APPEARING, 300);
        transition.setDuration(LayoutTransition.DISAPPEARING, 300);
        transition.setDuration(LayoutTransition.CHANGE_APPEARING, 300);
        transition.setDuration(LayoutTransition.CHANGE_DISAPPEARING, 300);
        mPageContainer.setLayoutTransition(transition);
    }

    private Animator getAppearingAnimation(int start) {
        AnimatorSet mSet = new AnimatorSet();
        mSet.playTogether(ObjectAnimator.ofFloat(null, "Alpha", 0.0f, 1.0f),
                ObjectAnimator.ofFloat(null, "translationX", start, 0));
        return mSet;
    }

    private Animator getDisappearingAnimation(int end) {
        AnimatorSet mSet = new AnimatorSet();
        mSet.playTogether(ObjectAnimator.ofFloat(null, "Alpha", 1.0f, 0.0f),
                ObjectAnimator.ofFloat(null, "translationX", 0, end));
        return mSet;
    }

    private void enableAnimation() {
        LayoutTransition transition = mPageContainer.getLayoutTransition();
        if (transition != null) {
            transition.enableTransitionType(LayoutTransition.APPEARING);
            transition.enableTransitionType(LayoutTransition.DISAPPEARING);
            transition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
            transition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            transition.enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    private void disableAnimation() {
        LayoutTransition transition = mPageContainer.getLayoutTransition();
        if (transition != null) {
            transition.disableTransitionType(LayoutTransition.APPEARING);
            transition.disableTransitionType(LayoutTransition.DISAPPEARING);
            transition.disableTransitionType(LayoutTransition.CHANGE_APPEARING);
            transition.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
            transition.disableTransitionType(LayoutTransition.CHANGING);
        }
    }

    /**
     * 获取页面的容器
     *
     * @return 页面容器
     */
    public FrameLayout getContainer() {
        return mPageContainer;
    }

    /**
     * 获取当前页面数
     *
     * @return 当前页面数
     */
    public int getPageCount() {
        return mPageContainer.getChildCount();
    }

    /**
     * 获取顶部的页面，即当前显示的页面
     *
     * @return 当前显示的页面
     */
    public Page getTopPage() {
        int count = mPageContainer.getChildCount();
        if (count <= 0) {
            return null;
        }
        return (Page) mPageContainer.getChildAt(count - 1);
    }

    /**
     * 获取指定索引的页面
     *
     * @param index 索引值
     * @return 索引位置的页面
     */
    public Page getPageAt(int index) {
        return (Page) mPageContainer.getChildAt(index);
    }


    public void launchEntryPage(AppListener listener) {
        String entryPagePath = mAppConfig.getHomePage();
        //
        mPageContainer.removeAllViews();
        createAndAddPage(entryPagePath, listener);
    }

    /**
     * 创建并添加一个页面
     *
     * @param url      页面路径
     * @param listener 页面触发的事件监听
     * @return 新创建的页面
     */
    public Page createAndAddPage(String url, AppListener listener) {
        if (isTabPage(url)) {
            disableAnimation();
            mPageContainer.removeAllViews();
        } else {
            int pageCount = getPageCount();
            if (pageCount >= MAX_COUNT) {
                // TODO: 2018/11/21 不限制页数的话怎么处理呢？
                return null;
            }
            if (pageCount == 0) {
                disableAnimation();
            } else {
                enableAnimation();
            }
        }
        Page page = new Page(mContext, url, mAppConfig);
        page.setAppListener(listener);
        mPageContainer.addView(page, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return page;
    }

    public static boolean isTabPage(String url) {
        return false;
    }

}