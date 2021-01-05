//package cn.notification;
//
//import android.app.Activity;
//import android.app.AppOpsManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.net.Uri;
//import android.os.Build;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
///**
// * Description:
// * Author: jfz
// * Date: 2021-01-04 18:33
// */
//public class Notification {
//
//    public static boolean areNotificationsEnabled(Context mContext) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            NotificationManager manager = (NotificationManager) mContext.getSystemService(android.app.Service.NOTIFICATION_SERVICE);
//            return manager.areNotificationsEnabled();
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            AppOpsManager appOps = (AppOpsManager) mContext.getSystemService(android.app.Service.APP_OPS_SERVICE);
//            ApplicationInfo appInfo = mContext.getApplicationInfo();
//            String pkg = mContext.getApplicationContext().getPackageName();
//            int uid = appInfo.uid;
//            try {
//                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
//                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
//                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
//                int value = (Integer) opPostNotificationValue.get(Integer.class);
//                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
//            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException var9) {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }
//
//    public static void createNotificaton(Context context, int id, String channgelId, String title, String content, int icon, Intent intent) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(android.app.Service.NOTIFICATION_SERVICE);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        android.app.Notification notification;
//        if (Build.VERSION.SDK_INT >= 26) {
//            NotificationChannel channel = new android.app.NotificationChannel(channgelId, title, android.app.NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableLights(true);
//            channel.setLightColor(0xff0000);
//            channel.setShowBadge(false);
//            manager.createNotificationChannel(channel);
//            notification = new android.app.Notification.Builder(context, channgelId)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setWhen(System.currentTimeMillis())
//                    .setContentIntent(contentIntent)
//                    .setSmallIcon(icon)
//                    .setTicker(content)
//                    .build();
//        } else {
//            notification = new android.app.Notification.Builder(context)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setContentIntent(contentIntent)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(icon)
//                    .setTicker(content)
//                    .build();
//        }
//        //通知栏不清除
//        notification.flags |= android.app.Notification.FLAG_NO_CLEAR;
//        manager.notify(id, notification);
//    }
//
//    public static void cancelNotification(Context context, int id) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(android.app.Service.NOTIFICATION_SERVICE);
//        manager.cancel(id);
//    }
//
//    public static void cancelAllNotification(Context context) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(android.app.Service.NOTIFICATION_SERVICE);
//        manager.cancelAll();
//    }
//
//    public static String openNotification(Activity activity) {
//        try {
//            Intent localIntent = new Intent();
//            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (Build.VERSION.SDK_INT >= 9) {
//                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//            } else if (Build.VERSION.SDK_INT <= 8) {
//                localIntent.setAction(Intent.ACTION_VIEW);
//                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
//                localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
//            }
//            activity.startActivity(localIntent);
//            return "";
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }
//}