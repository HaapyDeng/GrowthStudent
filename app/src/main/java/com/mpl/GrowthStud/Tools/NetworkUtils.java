package com.mpl.GrowthStud.Tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NetworkUtils {
    /**
     * 检测当的网络状态
     *
     * @param context
     *            Context
     * @return true 表示网络可用
     */
    /**
     * 网络不可用
     */
    public static final int NONETWORK = 0;
    /**
     * 是wifi连接
     */
    public static final int WIFI = 1;
    /**
     * 不是wifi连接
     */
    public static final int NOWIFI = 2;

    /**
     * 检验网络连接 并判断是否是wifi连接
     *
     * @param context
     * @return <li>没有网络：Network.NONETWORK;</li> <li>wifi 连接：Network.WIFI;</li>
     * <li>mobile 连接：Network.NOWIFI</li>
     */
    public static int checkNetWorkType(Context context) {

        if (!checkNetWork(context)) {
            return NetworkUtils.NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting())
            return NetworkUtils.WIFI;
        else
            return NetworkUtils.NOWIFI;
    }

    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        // 1.获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * @prama: str 要判断是否包含特殊字符的目标字符串
     * 返回true则有非法字符
     */

    public static Boolean compileExChar(String str) {

        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);

        return m.find();

    }
    /**
     * 校验手机号
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinaPhoneLegal(String str)
            throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
