package com.chqx.waterview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * author  ChenQiXin
 * date    2020-06-11
 * 描述   :
 * 修订版本:
 */
public class WaterView extends FrameLayout {
    private static final int WHAT_ADD_PROGRESS = 1;
    /**
     * view变化的y抖动范围
     */
    private static final int CHANGE_RANGE = 10;
    /**
     * 控制抖动动画执行的快慢，人眼不能识别16ms以下的
     */
    public static final int PROGRESS_DELAY_MILLIS = 12;
    /**
     * 控制移除view的动画执行时间
     */
    public static final int REMOVE_DELAY_MILLIS = 500;
    /**
     * 添加水滴时动画显示view执行的时间
     */
    public static final int ANIMATION_SHOW_VIEW_DURATION = 500;
    /**
     * 控制水滴动画的快慢
     */
    private List<Float> mSpds = Arrays.asList(0.5f, 0.3f, 0.2f, 0.1f);
    /**
     * x最多可选取的随机数值
     */
    private static final List<Float> X_MAX_CHOSE_RANDOMS = Arrays.asList(
            0.01f, 0.05f, 0.1f, 0.6f, 0.11f, 0.16f, 0.21f, 0.26f, 0.31f, 0.4f, 0.2f, 0.65f, 0.65f, 0.75f);//
    /**
     * y最多可选取的随机数值
     */
    private static final List<Float> Y_MAX_CHOSE_RANDOMS = Arrays.asList(
            0.01f, 0.06f, 0.11f, 0.17f, 0.23f, 0.29f, 0.35f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.45f, 0.50f, 0.50f);//
    /**
     * x坐标当前可选的随机数组
     */
    private List<Float> mXCurrentCanShoseRandoms = new ArrayList<>();
    /**
     * y坐标当前可选的随机数组
     */
    private List<Float> mYCurrentCanShoseRandoms = new ArrayList<>();

    /**
     * 已经选取x的随机数值
     */
    private List<Float> mXRandoms = new ArrayList<>();
    /**
     * 已经选取y的随机数值
     */
    private List<Float> mYRandoms = new ArrayList<>();

    /**
     * 6个小时内展示
     */
    private long MaxTime = 21600000;


    private Random mRandom = new Random();
    private List<View> mViews = new ArrayList<>();
    private int mChildViewRes = R.layout.water_item;//子view的资源文件

    private LayoutInflater mInflater;
    private int mTotalConsumeWater;//总的已经点击的水滴
    private boolean isOpenAnimtion;//是否开启动画
    private boolean isCancelAnimtion;//是否销毁动画
    private int maxX, maxY;//子view的x坐标和y坐标的最大取值
    private float mMaxSpace;//父控件对角线的距离
    private Point mDestroyPoint;//view销毁时的点
    private OnCoinClickListener grablistener = null;
    /**
     * 当一下三种情况金币size为0；用户把金币都获取完；倒计时结束三种情况时候那么重新掉setWaters 并把查看跟多data塞进去。
     * 点击时候 如果是查看更多回调出去 showDialog
     */
    private OnMoreClickListener moreListener = null;
    //位置定死
    private String location = "[{\"x\":0.2,\"y\":0.2},{\"x\":0.16,\"y\":0.4},{\"x\":0.35,\"y\":0.12},{\"x\":0.3,\"y\":0.3},{\"x\":0.5,\"y\":0.35},{\"x\":0.55,\"y\":0.25},{\"x\":0.65,\"y\":0.35},{\"x\":0.15,\"y\":0.6},{\"x\":0.75,\"y\":0.45},{\"x\":0.75,\"y\":0.6}]";
    private List<WaterLocation> waterLocationList = new ArrayList<>();
    private Context context;
    //60000毫米一次countDown
    private long countDownInterval = 1000;


    public CountDownTimer countDownTimer = new CountDownTimer(Long.MAX_VALUE, countDownInterval) {
        @Override
        public void onTick(long l) {
            for (int i = 0; i < mViews.size(); i++) {
                GoldCoinBean bean = (GoldCoinBean) mViews.get(i).getTag();
                //定时器不走type -1的数据
                if (bean.getType() != -1) {
                    long time = (Long.valueOf(bean.getCountdownTime()) - countDownInterval);
                    if (time > 0) {
                        bean.setCountdownTime(String.valueOf(time));
                        mViews.get(i).setTag(bean);
                        setTimeFormat((TextView) mViews.get(i).findViewById(R.id.tv_name), time);
                        if (21300000 < time && time <= MaxTime) {
                            mViews.get(i).findViewById(R.id.iv_icon).setBackgroundResource(R.mipmap.ic_worktime);
                        }
                    } else {
                        //移除当前集合中的该view

                        //消除
                        removeView(mViews.get(i));
                        mViews.remove(mViews.get(i));
                        if (mViews.size() == 0) {
                            setMoreWaters(assembleBean());
                        }
                    }
                }

            }
        }

        @Override
        public void onFinish() {

        }
    }.start();


    public WaterView(@NonNull Context context) {
        this(context, null);
    }

    public WaterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mInflater = LayoutInflater.from(getContext());
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //根据isCancelAnimtion来标识是否退出，防止界面销毁时，再一次改变UI
            if (isCancelAnimtion) {
                return;
            }
            setOffSet();
            mHandler.sendEmptyMessageDelayed(WHAT_ADD_PROGRESS, PROGRESS_DELAY_MILLIS);
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMaxSpace = (float) Math.sqrt(w * w + h * h);
        mDestroyPoint = new Point(w, h);
        //mDestroyPoint =new Point((int) getX(), h);
        maxX = w;
        maxY = h;
    }

    /**
     * 界面销毁时回调
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    /**
     * 重置子view
     */
    private void reset() {
        isCancelAnimtion = true;
        isOpenAnimtion = false;
        for (int i = 0; i < mViews.size(); i++) {
            removeView(mViews.get(i));
        }
        mViews.clear();
        mXRandoms.clear();
        mYRandoms.clear();
        mYCurrentCanShoseRandoms.clear();
        mXCurrentCanShoseRandoms.clear();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置水滴
     *
     * @param waters
     */
    public void setWaters(final List<GoldCoinBean> waters) {
        /**
         * 如果没有值那么显示更多
         */
        if (waters == null || waters.isEmpty()) {
            setMoreWaters(assembleBean());
            return;
        }
        //确保初始化完成
        post(new Runnable() {
            @Override
            public void run() {
                if (waters.size() > 0) {
                    setDatas(waters);
                }

            }
        });
    }

    /**
     * 设置more
     *
     * @param waters
     */
    public void setMoreWaters(final List<GoldCoinBean> waters) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("type", "7");
//        RequestUtils.getInstance().getDataPath(ConstantURL.VIEW_ShOW, map, SystemParams.GET, true, true, new RequestResultListener() {
//            @Override
//            public void success(String url, String data) {
//                super.success(url, data);
//                try {
//                    boolean isShow = new org.json.JSONObject(data).getBoolean("show");
//                    if (isShow) {
//                        //确保初始化完成
//                        post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (waters.size() > 0) {
//                                    setDatas(waters);
//                                }
//
//                            }
//                        });
//                    } else {
//                        reset();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//

    }

    /**
     * 设置数据
     *
     * @param waters
     */
    private void setDatas(List<GoldCoinBean> waters) {
        reset();
        initWaterLocation();
        isCancelAnimtion = false;
        setCurrentCanChoseRandoms();
        addWaterView(waters);
        setViewsSpd();
        startAnimation();
    }

    private void setCurrentCanChoseRandoms() {
        mXCurrentCanShoseRandoms.addAll(X_MAX_CHOSE_RANDOMS);
        mYCurrentCanShoseRandoms.addAll(Y_MAX_CHOSE_RANDOMS);
    }

    /**
     * 添加水滴view
     */
    private void addWaterView(List<GoldCoinBean> waters) {
        for (int i = 0; i < waters.size(); i++) {
            final GoldCoinBean water = waters.get(i);
            View view = mInflater.inflate(mChildViewRes, this, false);
            TextView tvWater = view.findViewById(R.id.tv_water);
            view.setTag(water);
            if (water.getAmt() > 0) {
                tvWater.setText(Utils.getBigDecimal(Integer.valueOf(water.getAmt()), 100));
                tvWater.setVisibility(VISIBLE);
            } else {
                tvWater.setVisibility(GONE);
            }

            TextView tvTypeName = view.findViewById(R.id.tv_name);
            ImageView ivIcoin = view.findViewById(R.id.iv_icon);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    handViewClick(view);
                }
            });
            //随机设置view动画的方向
            view.setTag(R.string.isUp, mRandom.nextBoolean());
            if (i < 10) {
                setChildViewLocation(view, waterLocationList.get(i).getX(), waterLocationList.get(i).getY());
            } else {
                setChildViewLocation(view);
            }
            setCoinBottomContent(water.getType(), tvTypeName, ivIcoin, Long.valueOf(water.getCountdownTime()));
            mViews.add(view);

            addShowViewAnimation(view);
        }
    }

    /**
     * 展示 HH:ss
     *
     * @param tvName
     * @param time
     */
    private void setTimeFormat(TextView tvName, Long time) {
        if (time <= MaxTime) {
            tvName.setText(TimeUitl.remianTime(Long.valueOf(time)));
        }
    }

    /**
     * 逻辑：如果小于6小时 那么展示倒计时 否则根据type 展示今日分红或者平台分红
     *
     * @param type   1：今日分红 2平台分红
     * @param tvName
     * @param ivIcon
     * @param time
     */
    private void setCoinBottomContent(int type, TextView tvName, ImageView ivIcon, Long time) {
        //如果type<0 说明是查看更多 所以不用判断时间戳
        if (time <= MaxTime && type > 0) {
            setTimeFormat(tvName, time);
            ivIcon.setBackgroundResource(R.mipmap.ic_worktime);
        } else {
            switch (type) {
                case 0:
                    tvName.setText("今日分红");
                    ivIcon.setBackgroundResource(R.mipmap.ic_feng_hong);
                    break;
                case 1:
                    tvName.setText("平台补贴");
                    ivIcon.setBackgroundResource(R.mipmap.ic_pingtai);
                    break;
                case -1:
                    tvName.setText("领取更多");
                    ivIcon.setBackgroundResource(R.mipmap.ic_coin_more);
                    break;
            }
        }

    }

    /**
     * 添加显示动画
     *
     * @param view
     */
    private void addShowViewAnimation(View view) {
        addView(view);
        view.setAlpha(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate().alpha(1).scaleX(1).scaleY(1).setDuration(ANIMATION_SHOW_VIEW_DURATION).start();
    }

    private void initWaterLocation() {
        waterLocationList.clear();
        waterLocationList.addAll(JSONObject.parseArray(location, WaterLocation.class));
        Collections.shuffle(waterLocationList);
    }

    /**
     * 处理view点击
     *
     * @param view
     */
    private void handViewClick(View view) {
        //移除当前集合中的该view
        mViews.remove(view);
        Object tag = view.getTag();
        if (tag instanceof GoldCoinBean) {
            GoldCoinBean waterTag = (GoldCoinBean) tag;
            if (waterTag.getType() != -1) {
                if (grablistener != null) {
                    grablistener.onClick(waterTag);
                }
                if (mViews.size() == 0) {
                    setMoreWaters(assembleBean());
                }
            } else {
                //移除当前集合中的该view
                mViews.remove(view);
                //消除
                removeView(view);
                if (moreListener != null) {
                    moreListener.onMoreClick();
                }
                return;
            }
        }
        view.setTag(R.string.original_y, view.getY());
        animRemoveView(view);
    }


    /**
     * 设置view在父控件中的位置
     *
     * @param view
     */
    private void setChildViewLocation(View view) {
        view.setX((float) (maxX * getX_YRandom(mXCurrentCanShoseRandoms, mXRandoms)));
        view.setY((float) (maxY * getX_YRandom(mYCurrentCanShoseRandoms, mYRandoms)));
        view.setTag(R.string.original_y, view.getY());
    }

    /**
     * water 位置写死
     *
     * @param view
     * @param x
     * @param y
     */
    private void setChildViewLocation(View view, float x, float y) {
        Log.d("laocaitonWater", x + "||" + y + "~~~~~~" + (float) ((maxX * x)) + "|||" + (float) ((maxY * y)));
        view.setX((float) ((maxX * x)));
        view.setY((float) ((maxY * y)));
        view.setTag(R.string.original_y, view.getY());
    }

    /**
     * 获取x轴或是y轴上的随机值
     *
     * @return
     */
    private double getX_YRandom(List<Float> choseRandoms, List<Float> saveRandoms) {

        if (choseRandoms.size() <= 0) {
            //防止水滴别可选项的个数还要多，这里就重新对可选项赋值
            setCurrentCanChoseRandoms();
        }
        //取用一个随机数，就移除一个随机数，达到不用循环遍历来确保获取不一样的值
        float random = choseRandoms.get(mRandom.nextInt(choseRandoms.size()));
        choseRandoms.remove(random);
        saveRandoms.add(random);
        return random;
    }

    /**
     * 设置所有子view的加速度
     */
    private void setViewsSpd() {
        for (int i = 0; i < mViews.size(); i++) {
            View view = mViews.get(i);
            setSpd(view);
        }
    }

    /**
     * 设置View的spd
     *
     * @param view
     */
    private void setSpd(View view) {
        float spd = mSpds.get(mRandom.nextInt(mSpds.size()));
        view.setTag(R.string.spd, spd);
    }

    /**
     * 设置偏移
     */
    private void setOffSet() {
        for (int i = 0; i < mViews.size(); i++) {
            View view = mViews.get(i);
            //拿到上次view保存的速度
            float spd = (float) view.getTag(R.string.spd);
            //水滴初始的位置
            float original = (float) view.getTag(R.string.original_y);
            float step = spd;
            boolean isUp = (boolean) view.getTag(R.string.isUp);
            float translationY;
            //根据水滴tag中的上下移动标识移动view
            if (isUp) {
                translationY = view.getY() - step;
            } else {
                translationY = view.getY() + step;
            }
            //对水滴位移范围的控制
            if (translationY - original > CHANGE_RANGE) {
                translationY = original + CHANGE_RANGE;
                view.setTag(R.string.isUp, true);
            } else if (translationY - original < -CHANGE_RANGE) {
                translationY = original - CHANGE_RANGE;
                // FIXME:每次当水滴回到初始点时再一次设置水滴的速度，从而达到时而快时而慢
                setSpd(view);
                view.setTag(R.string.isUp, false);
            }
            view.setY(translationY);
        }
    }


    /**
     * 获取两个点之间的距离
     *
     * @param p1
     * @param p2
     * @return
     */
    public float getDistance(Point p1, Point p2) {
        float _x = Math.abs(p2.x - p1.x);
        float _y = Math.abs(p2.y - p1.y);
        return (float) Math.sqrt(_x * _x + _y * _y);
    }

    /**
     * 动画移除view
     *
     * @param view
     */
    private void animRemoveView(final View view) {
        final float x = view.getX();
        final float y = view.getY();
        //计算直线距离
        float space = getDistance(new Point((int) x, (int) y), mDestroyPoint);

        ValueAnimator animator = ValueAnimator.ofFloat(x, 0);
        //根据距离计算动画执行时间
        //animator.setDuration((long) (REMOVE_DELAY_MILLIS / mMaxSpace * space));
        animator.setDuration((long) REMOVE_DELAY_MILLIS);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isCancelAnimtion) {
                    return;
                }
                float value = (float) valueAnimator.getAnimatedValue();
                float alpha = value / x;
                float translationY = y + (x - value) * ((maxY - DensityUtils.dip2px(context, 60)) - y) / x;
                float translationX = x + (x - value) * ((maxX - DensityUtils.dip2px(context, 60)) - x) / x;
                Log.d("animatorY:", translationY + "x|||||" + translationX);
                setViewProperty(view, alpha, translationY, translationX);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //结束时从容器移除水滴
                removeView(view);
            }
        });
        animator.start();
    }

    /**
     * 设置view的属性
     *
     * @param view
     * @param alpha
     * @param translationY
     * @param translationX
     */
    private void setViewProperty(View view, float alpha, float translationY, float translationX) {
        view.setTranslationY(translationY);
        view.setTranslationX(translationX);
        view.setAlpha(alpha);
        view.setScaleY(alpha);
        view.setScaleX(alpha);
    }

    /**
     * 开启水滴抖动动画
     */
    private void startAnimation() {
        if (isOpenAnimtion) {
            return;
        }

        mHandler.sendEmptyMessage(WHAT_ADD_PROGRESS);
        isOpenAnimtion = true;
    }

    /**
     * 销毁
     */
    private void onDestroy() {
        isCancelAnimtion = true;
        mHandler.removeCallbacksAndMessages(this);
    }


    private List<GoldCoinBean> assembleBean() {
        List<GoldCoinBean> goldCoinBeanList = new ArrayList<>();
        GoldCoinBean goldCoinBean = new GoldCoinBean();
        goldCoinBean.setAmt(0);
        goldCoinBean.setCountdownTime("0");
        goldCoinBean.setType(-1);
        goldCoinBeanList.add(goldCoinBean);
        return goldCoinBeanList;

    }


    /**
     * 点击领取更过事件监听
     */
    public void setMoreClickListener(OnMoreClickListener l) {
        this.moreListener = l;
    }


    /**
     * 点击金币回调接口
     */
    public interface OnMoreClickListener {
        void onMoreClick();
    }

    /**
     * 绑定监听事件
     *
     * @param l
     */
    public void setGrabCoinListener(OnCoinClickListener l) {
        this.grablistener = l;
    }


    /**
     * 点击金币回掉接口
     */
    public interface OnCoinClickListener {
        void onClick(GoldCoinBean goldCoin);
    }
}