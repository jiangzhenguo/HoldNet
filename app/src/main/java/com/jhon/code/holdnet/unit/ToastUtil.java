package com.jhon.code.holdnet.unit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.jhon.code.holdnet.VpnApplication;

/**
 * creater : Jhon
 * time : 2019/1/8 0008
 */
public class ToastUtil {
    private static Toast toast = null;
    private static Handler handler = new Handler(Looper.getMainLooper());

    private final static short[] synObj = new short[1];

    /**
     * 显示toast期间不重复创建toast
     *
     * @param context context
     * @param msg     msg
     */
    public static void showOnce(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * 显示toast期间不重复创建toast
     *
     * @param context  context
     * @param stringId stringId
     */
    public static void showOnce(Context context, int stringId) {
        if (toast == null) {
            toast = Toast.makeText(context, stringId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(stringId);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int stringId) {
        show(context, stringId, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        try {
            if (null == context) {
                context = VpnApplication.getContext();
            }
            Toast.makeText(context, msg, duration).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Context context, int stringId, int duration) {
        try {
            if (null == context) {
                context = VpnApplication.getContext();
            }
            Toast.makeText(context, stringId, duration).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final String msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        toast.cancel();
                        toast =  Toast.makeText(act, msg, len);
                    } else {
                        toast =  Toast.makeText(act, msg, len);
                    }
                    toast.show();
                }
            }
        });
    }


    public static void showMessage(final Context act, final int msg, final int len) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        toast.cancel();
                        toast =  Toast.makeText(act, msg, len);
                    } else {
                        toast =  Toast.makeText(act, msg, len);
                    }
                    toast.show();
                }
            }
        });
    }
}
