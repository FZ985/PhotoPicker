package otherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Create by JFZ
 * date: 2020-06-11 14:46
 * 跳转抖音和淘宝的直播室
 **/
public class ThirdAppLiveRoom {

    private static boolean checkAppInstalled(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    /**
     * 跳转抖音直播室
     *
     * @param activity
     * @param url      抖音直播室的赋值链接
     *                 <p>
     *                 String url = "https://v.douyin.com/JdrhQPG/";
     */
    public static void openDouYinRoom(Activity activity, String url) {
        boolean isDouYin = checkAppInstalled(activity, "com.ss.android.ugc.aweme");
        if (isDouYin) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "请安装抖音App", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转淘宝的直播室
     *
     * @param context
     * @param url     解析淘口令 获取url
     */
//    http://h5.m.taobao.com/taolive/video.html?id=268686351025&sharerId=2200661430357&anchorGuard=true×tamp=1591854333602&signature=ab0b1e99bd75f13b003f18afb2f5458b&livesource=guard&cp_origin=taobaozhibo%7Ca2141.8001249%7C%7B%22account_id%22%3A%22111671365%22%2C%22app_key%22%3A%2225443018%22%2C%22feed_id%22%3A%22268686351025%22%2C%22os%22%3A%22android%22%2C%22spm-cnt%22%3A%22a2141.8001249%22%7D&sourceType=talent&suid=78697675-ca41-44be-ae47-e60cf3b88c16&ut_sk=1.WkkJoIoWA24DAL56hMjA0f3G_25443018_1591854239744.Copy.tblive_guard_app&un=f5cb90f742fad439860ae73f2f940a96&share_crt_v=1&spm=a2159r.13376460.0.0&sp_tk=77+lRzNSTTFHV3N1OWnvv6U=
    public static void openTaoBaoRoom(Activity context, String url) {
        boolean bool = checkAppInstalled(context, "com.taobao.taobao");
        if (bool) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.setClassName("com.taobao.taobao", "com.taobao.browser.BrowserActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                System.out.println("淘宝跳转失败:" + e.getMessage());
                if (toSystemBrowser(context, url)) {
                } else {
                    Toast.makeText(context, "打开直播室失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, "请安装淘宝客户端!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开淘宝商品详情页
     *
     * @param context
     * @param url
     */
    public static void openTaoBaoDetail(Activity context, String url) {
        if (TextUtils.isEmpty(url)) return;
        boolean bool = checkAppInstalled(context, "com.taobao.taobao");
        if (bool) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.setClassName("com.taobao.taobao", "com.taobao.browser.BrowserActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("淘宝", "淘宝_淘宝浏览器方式打开失败:" + e.getMessage());
                boolean c = openTaoBaoShopDetail(context, url);
                if (!c) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e1) {
                        Log.e("淘宝", "打开淘宝失败:" + e1.getMessage());
                        Toast.makeText(context, "打开淘宝客户端失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(context, "请安装淘宝客户端!", Toast.LENGTH_SHORT).show();
        }
    }

    //打开淘宝商品详情
    private static boolean openTaoBaoShopDetail(Activity context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e("淘宝", "淘宝_详情页方式打开失败:" + e.getMessage());
            return false;
        }
    }

    private static boolean toSystemBrowser(Activity context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e("跳转", "跳转系统浏览器失败:" + e.getMessage());
            return false;
        }
    }
}
