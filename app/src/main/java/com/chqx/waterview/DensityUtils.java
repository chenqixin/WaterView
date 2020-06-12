package com.chqx.waterview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * author  ChenQiXin
 * date    2019-11-18
 * 描述   : 尺寸工具
 * 修订版本:
 */
public class DensityUtils {

    /**
     * 获取屏幕尺寸
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2){
            return new Point(display.getWidth(), display.getHeight());
        }else{
            Point point = new Point();
            display.getSize(point);
            return point;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    // 将px值转换为sp值，保证文字大小不变
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值，保证文字大小不变
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }



    /**
     * 获取手机分辨率
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, Object> getMetrics(Activity activity) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        map.put("screenWidth", screenWidth);
        int screenHeight = display.getHeight();
        map.put("screenHeight", screenHeight);
        return map;
    }

    /**
     * 弹出框显示的位置
     */
    public static int showHeight(Activity activity) {
        int d_height = 0;
        HashMap<String, Object> map = getMetrics(activity);
        if (Integer.parseInt(map.get("screenHeight") + "") >= 1800) {
            d_height = dip2px(activity, 100);
        }
        if (Integer.parseInt(map.get("screenHeight") + "") < 1800 && Integer.parseInt(map.get("screenHeight") + "") > 700) {
            d_height = dip2px(activity, 180);
        }
        return d_height;
    }

    /**
     * 得到手机厂商
     *
     * @return
     */
    public static String getIndustrial() {
        if (!TextUtils.isEmpty(android.os.Build.MANUFACTURER)) {
            return android.os.Build.MANUFACTURER;
        } else {
            return "";
        }
    }

    /**
     * ƒ 得到手机型号
     *
     * @return
     */
    public static String getModel() {
        if (!TextUtils.isEmpty(android.os.Build.MODEL)) {
            return android.os.Build.MODEL;
        } else {
            return "";
        }
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

//    /**
//     * 获取设备的各种信息
//     *
//     * @return
//     */
//    public static String getPhoneInfromation(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        StringBuilder sb = new StringBuilder();
//        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
//        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
//        sb.append("\nLine1Number = " + tm.getLine1Number());
//        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
//        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
//        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
//        sb.append("\nNetworkType = " + tm.getNetworkType());
//        sb.append("\nPhoneType = " + tm.getPhoneType());
//        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
//        sb.append("\nSimOperator = " + tm.getSimOperator());
//        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
//        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
//        sb.append("\nSimState = " + tm.getSimState());
//        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
//        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
//        sb.append("\n电话方位 = " + tm.getCellLocation());
//        sb.append("\n手机型号 = " + android.os.Build.MODEL);
//        sb.append("\n系统版本 = " + android.os.Build.VERSION.RELEASE);
//        sb.append("\n手机厂商 = " + android.os.Build.MANUFACTURER);
//        sb.append("\nSDK版本 = " + android.os.Build.VERSION.SDK);
//
//        return sb.toString();
//    }

    public final static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    public final static int px2dp(Context context, float pxValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / density + 0.5f);
    }


    /**
     * 获取屏幕实际高度（也包含虚拟导航栏）
     *
     * @param context
     * @return
     */
    public static int getRealHight(Context context) {
        DisplayMetrics displayMetrics = null;
        if (displayMetrics == null) {
            displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }
}
