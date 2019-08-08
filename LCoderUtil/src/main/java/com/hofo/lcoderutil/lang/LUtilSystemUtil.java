package com.hofo.lcoderutil.lang;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class LUtilSystemUtil {
    /**
     * 获取屏幕大小（宽高），第一个为宽，第二个为高
     *
     * @param context
     * @return
     */

    public static int[] getWindowsSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * @param context  创建Vibrator实例的上下文
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     * @param cancel   TRUE 取消震动 FALSE 震动
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void Vibrate(Context context, long[] pattern, boolean isRepeat, boolean cancel) {
        Vibrator vib = null;
        if (!cancel) {
            vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(pattern, isRepeat ? 1 : -1);
        } else {
            if (vib != null) {
                vib.cancel();
            }
        }
    }

    /**
     * @param context      创建Vibrator实例的上下文
     * @param milliseconds 震动时长 单位为毫秒
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void Vibrate(Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     */
    public static void hideSoftKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 隐藏底部虚拟按键
     */
    public static void hideNavigation(final Activity activity) {
        final Handler mHandler = new Handler();
        final Runnable hideRunnable = new Runnable() {
            @Override
            public void run() {
                int flags;
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                // must be executed in main thread
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
        };
        final View decorView = activity.getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {

                mHandler.post(hideRunnable);
            }
        });
        mHandler.post(hideRunnable);
    }


    /**
     * 获得当前软件版本
     *
     * @param context
     * @return
     */

    public static String getVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取登录设备mac地址
     *
     * @return
     */
    public static String getMacByWifiManager(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
        }
        String mac = wm.getConnectionInfo().getMacAddress();
        return mac == null ? "" : mac;
    }


    /**
     * 判断网络连接是否可用
     *
     * @param context 上下文
     * @return 如果为true 可用，否则不可用
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (cm == null) {
            return false;
        } else {
            //如果仅仅是用来判断网络连接,则可以使用 cm.getActiveNetworkInfo().isAvailable();
            if (networkInfo == null) return false;
            NetworkInfo.State state = networkInfo.getState();
            if (state == null) {
                return false;
            }
            return state.equals(NetworkInfo.State.CONNECTED);
        }
    }

    /**
     * 安装APK
     *
     * @param mContext
     * @param filePath
     * @param packageName 版本大于24需要填写
     */
    public static void installApk(Context mContext, String filePath, String packageName) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    mContext
                    , packageName + ".fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    /**
     * @param oldFragmentID   需要替换的Fragment id
     * @param newFragment     需要替换成的Fragment
     * @param fragmentManager 替换时使用的Manager
     * @param tag             Tag为FALSE时可以为null，否则需要输入Tag字段
     */
    public static void replaceFragment(int oldFragmentID, Fragment newFragment, FragmentManager fragmentManager, String tag) {
        if (fragmentManager.findFragmentById(oldFragmentID) != newFragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (tag != null && !tag.equals("")) {
                fragmentTransaction.replace(oldFragmentID, newFragment, tag);
            } else {
                fragmentTransaction.replace(oldFragmentID, newFragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    public static void replaceFragmentByAddToBackStack(int oldFragmentID, Fragment newFragment, FragmentManager fragmentManager, @NonNull String tag) {
        if (fragmentManager.findFragmentById(oldFragmentID) != newFragment) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(oldFragmentID, newFragment, tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    /**
     * 获得有线网卡的MAC地址
     *
     * @return
     */
    public static String getWiredMacAddress() {
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();

            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }

                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    protected static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress) en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


    /**
     * 通过网络接口取Mac地址
     *
     * @return
     */
    private static String getNetMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
