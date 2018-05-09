package com.ljb.baselibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
import static android.telephony.TelephonyManager.NETWORK_TYPE_IWLAN;
import static android.telephony.TelephonyManager.NETWORK_TYPE_TD_SCDMA;

/**
 * 网络相关工具类
 * <p>可能需要的权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * <uses-permission android:name="android.permission.MODIFY_PHONE_STATEi"/>
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
 * <p>
 * /p>
 */

public class NetworkUtil {

    /**
     * 获得活动网络信息
     *
     * @param context
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo(@NonNull Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return info;
    }

    /**
     * 判断网络是否连接
     *
     * @return
     */
    public static boolean isConnected(@NonNull Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isAvailable(@NonNull Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public static NetworkType getNetworkType(@NonNull Context context) {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {//wifi
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//移动数据流量
                switch (info.getSubtype()) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;
                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetworkType.NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") ||
                                subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 判断移动数据是否打开
     *
     * @param context
     * @return
     */
    public static boolean getDataEnable(@NonNull Context context) {

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (method != null) {
                return (boolean) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开获取关闭数据流量
     * 需添加权限{@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>}
     * 部分机型不适配
     *
     * @param enable
     * @param context
     */
    public static void setDataEnable(@NonNull boolean enable, Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method method = tm.getClass().getDeclaredMethod("setDataEnable", boolean.class);
            if (method != null) {
                method.invoke(tm, enable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否为4G
     * 需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}
     *
     * @param context
     * @return
     */
    public static boolean is4G(@NonNull Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否打开
     * 需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}
     *
     * @param context
     * @return
     */
    public static boolean getWifiEnable(Context context) {
        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.isWifiEnabled();
    }

    /**
     * 打开或者关闭wifi
     * 需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}
     * 
     *
     * @param enable
     * @param context
     */
    public static void setWifiEnable(boolean enable,@NonNull Context context) {
        @SuppressLint("WifiManagerLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(Context
                .WIFI_SERVICE);
        if (enable) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);

        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected();
    }

    /**
     * 判断wifi是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiAvailable(@NonNull Context context) {
        return getWifiEnable(context) && isAvailable(context);
    }

    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     */
    public static String getNetworkOperatorName(@NonNull Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings(@NonNull Context context) {
        context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent
                .FLAG_ACTIVITY_NEW_TASK));
    }
    /**
     * 获取 IP 地址
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     *
     * @param useIPv4 是否用 IPv4
     * @return IP 地址
     */
    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    public enum NetworkType {
        NETWORK_WIFI, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO;
    }
}
