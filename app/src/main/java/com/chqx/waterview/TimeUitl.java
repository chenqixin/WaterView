package com.chqx.waterview;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author  ChenQiXin
 * date    2019-12-10
 * 描述   :
 * 修订版本:
 */
public class TimeUitl {

    /**
     * 8.将指定milliseconds转换成指定格式的字符串
     *
     * @param format 1："yyyy年MM月dd日 HH时mm分ss秒" 2:"yyyy-MM-dd HH:mm:ss" 3:"yyyy/MM/dd HH:mm:ss" 4:"yyyy年MM月dd日 HH时mm分ss秒 E " 5:"yyyy/MM/dd E" 6."yyyy/MM/dd"7.yyyy-MM-dd HH:mm
     * @return dateStr 指定格式字符串
     */
    public static final String millisecondsToString(int format, Long milliseconds) {
        return dateToString(format, new Date(milliseconds));
    }


    /**
     * <p>
     * 9.将指定Date转换成指定格式的字符串
     * </p>
     *
     * @param format 格式 <li>1."yyyy年MM月dd日 HH时mm分ss秒"</li> <li>2."yyyy-MM-dd HH:mm:ss"</li> <li>3."yyyy/MM/dd HH:mm:ss"</li> <li>4."yyyy年MM月dd日 HH时mm分ss秒 E "</li> <li>5."yyyy/MM/dd E"</li> <li>
     *               6."yyyy/MM/dd"</li> <li>7."yyyy-MM-dd HH:mm"</li> <li>8."yyyy年MM月dd日"</li> <li>9."yyyy-MM-dd"</li><li>10."HH:mm"</li> 13 yy/MM/dd 14 yy/MM/dd HH:mm;16<li>."yyyy年MM月dd日 HH:mm"</li>
     * @return dateStr 指定格式字符串
     */
    @SuppressLint("SimpleDateFormat")
    public static final String dateToString(int format, Date date) {
        SimpleDateFormat simpleDateFormat = null;
        String dateStr = null;
        switch (format) {
            case 1:
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                dateStr = simpleDateFormat.format(date);
                break;
            case 2:
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateStr = simpleDateFormat.format(date);
                break;
            case 3:
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                dateStr = simpleDateFormat.format(date);
                break;
            case 4:
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
                dateStr = simpleDateFormat.format(date);
                break;
            case 5:
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd E");
                dateStr = simpleDateFormat.format(date);
                break;
            case 6:
                simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd ");
                dateStr = simpleDateFormat.format(date);
                break;
            case 7:
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateStr = simpleDateFormat.format(date);
                break;
            case 8:
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                dateStr = simpleDateFormat.format(date);
                break;
            case 9:
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStr = simpleDateFormat.format(date);
                break;
            case 10:
                simpleDateFormat = new SimpleDateFormat("HH:mm");
                dateStr = simpleDateFormat.format(date);
                break;
            case 12:
                simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
                dateStr = simpleDateFormat.format(date);
                break;
            case 13:
                simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                dateStr = simpleDateFormat.format(date);
                break;
            case 14:
                simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");
                dateStr = simpleDateFormat.format(date);
                break;
            case 15:
                simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                dateStr = simpleDateFormat.format(date);
                break;
            case 16:
                simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                dateStr = simpleDateFormat.format(date);
                break;
            case 17:
                simpleDateFormat = new SimpleDateFormat("HH小时mm分钟");
                dateStr = simpleDateFormat.format(date);
                break;
            case 18:
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                dateStr = simpleDateFormat.format(date);
                break;


        }


        return dateStr;
    }


    /**
     * 展示剩余08:06格式
     *
     * @param milliseconds
     * @return
     */
    public static String remianTime(Long milliseconds) {
        String formatString;
        long hours, minute, second;
        second = milliseconds;
        hours = milliseconds / (60 * 60 * 1000);
        minute = (second - hours * (1000 * 60 * 60)) / (60 * 1000);
        second = (second - hours * (1000 * 60 * 60) - minute * (60 * 1000)) / 1000;
        if (hours == 0 && minute == 0) {
            formatString = doublePlace(hours) + ":" + doublePlace(minute) + ":" + doublePlace(second);
        } else {
            formatString = doublePlace(hours) + ":" + doublePlace(minute);
        }

        return formatString;

    }


    public static String doublePlace(long time) {
        if (time == 0) {
            return "00";
        }
        if (String.valueOf(time).length() < 2) {
            return "0" + time;
        }
        return time + "";
    }
}
