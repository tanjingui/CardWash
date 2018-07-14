package com.example.mac.carwash.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtil {


    public static final String EMPTY = "";

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true 不为空, false 为空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !"null".equals(str) && str.trim().length() != 0;
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDeviceId(Context context) {
//        String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//        return deviceId == null ? "888888" : deviceId;
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getDeviceType() {
        return android.os.Build.BRAND + " " + android.os.Build.MODEL;
    }

    /**
     * 获取OS版本
     *
     * @return
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return true 为空，false 不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || "null".equals(str) || str.trim().length() == 0;
    }

    public static final SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm", Locale.CHINA);

    public static String getCurrentTime() {
        return dateformat.format(new Date());
    }

    public static final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static String formatDateTime(long millseconds) {
        return sdformat.format(new Date(millseconds));
    }

    public static String getCurrentDateTime() {
        return sdformat.format(new Date());
    }

    /**
     * 判断集合是否为空
     */
    public static <T> boolean isCollectionsNotEmpty(Collection<T> collection) {
        return collection != null && collection.size() > 0;
    }

    /**
     * 判断MAP是否为空
     */
    public static <K, V> boolean isMapNotEmpty(Map<K, V> map) {
        return map != null && map.size() > 0;
    }

    /**
     * 判断List是否为空
     */
    public static boolean isListEmpty(List<?> array) {
        return array == null || array.size() == 0;
    }

    /**
     * 判断JSON数组是否为空
     */
    public static boolean isJSONArrayEmpty(JSONArray array) {
        return array == null || array.length() == 0;
    }

    public static boolean isObjectNotNull(Object object) {
        if (object != null && object.getClass().isArray()) {
            // 如果是数组类型
            throw new UnsupportedOperationException("isObjectNotNull not supported operation :" + object);
        }
        return object != null;
    }

    /**
     * 判断JSON数据不空为
     */
    public static boolean isJSONArrayNotEmpty(JSONArray array) {
        return array != null && array.length() > 0;
    }

    /**
     * 判断JSON数组是否为空
     */
    public static boolean isJSONObjectEmpty(JSONObject object) {
        return object == null || object.length() == 0;
    }

    /**
     * 判断JSON数据不空为
     */
    public static boolean isJSONObjectNotEmpty(JSONObject object) {
        return object != null && object.length() > 0;
    }

    public static boolean isIntArrayNotEmpty(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * 判断List数据不空为
     */
    public static boolean isListNotEmpty(List<?> array) {
        return array != null && array.size() > 0;
    }

    /**
     * 判断long数组不为空
     *
     * @param array
     * @return
     */
    public static boolean isLongArrayNotEmpty(long[] array) {
        return array != null && array.length > 0;
    }

    /**
     * 判断float数组不为空
     *
     * @param array
     * @return
     */
    public static boolean isFloatArrayNotEmpty(float[] array) {
        return array != null && array.length > 0;
    }

    /**
     * 判断double数组不为空
     *
     * @param array
     * @return
     */
    public static boolean isDoubleArrayNotEmpty(double[] array) {
        return array != null && array.length > 0;
    }

    public static boolean isNotBlank(String str) {
        return (str != null) && (str.length() != 0);
    }

    public static boolean isBlank(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNotTrimBlank(String str) {
        return (str != null) && (str.trim().length() != 0);
    }

    public static boolean isTrimBlank(String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    /**
     * 判断是否是身份证
     *
     * @param idNo
     * @return
     */
    public static boolean isIdNo(String idNo) {
        if (isTrimBlank(idNo)) {
            return false;
        }
        Pattern p = Pattern.compile("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[X,x]))$");
        Matcher matcher = p.matcher(idNo);
        return matcher.find();
    }

    /**
     * 判断是否为数字
     *
     * @param
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为邮箱号
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (isTrimBlank(email)) {
            return false;
        }
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 在HTML特殊字符的处理
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtil.isEmpty(source) ? source : source.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"")
                .replaceAll("&copy;", "©")
                .replaceAll("&yen;", "¥")
                .replaceAll("&divide;", "÷")
                .replaceAll("&times;", "×")
                .replaceAll("&reg;", "®")
                .replaceAll("&sect;", "§")
                .replaceAll("&pound;", "£")
                .replaceAll("&cent;", "￠");
    }

    /**
     * 验证用户名是否合法
     *
     * @param id
     * @return
     */
    public static boolean isNotUserName(String id) {
        if (isTrimBlank(id)) {
            return false;
        }
        // 字母开头，由字母，数字和下划线组成的长度为2到16的字符串
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{2,16}$");
        Matcher m = p.matcher(id);
        return !m.find();
    }

    public static boolean isNotPassWord(String password) {
        if (isTrimBlank(password)) {
            return false;
        }
        // 就是以大小写字母开头，由大小写字母，数字和下划线组成的长度为6到18的字符串
        Pattern p = Pattern.compile("^[a-zA-Z0-9_]{6,18}$");
        Matcher m = p.matcher(password);
        return !m.find();
    }

    /**
     * 判断银行卡号是否合法
     *
     * @param bankCard
     * @return
     */
    public static boolean isNotBank(String bankCard) {
        if (isTrimBlank(bankCard)) {
            return false;
        }
        // 一共16或19位，都是数字。
        Pattern p = Pattern.compile("^\\d{16}$|^\\d{17}$|^\\d{19}$");
        Matcher m = p.matcher(bankCard);
        return !m.find();
    }

    /**
     * @param context
     * @param resId
     * @param str
     * @return
     */
    public static String isStringFormat(Context context, int resId, String str) {
        return String.format(context.getResources().getString(resId), str);
    }

    /**
     * 从Raw文件中读取
     *
     * @param context
     * @param resId
     * @return
     */
    public static String getFromRaw(Context context, int resId) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 直接从assets读取
    public static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证手机格式
     * 是手机号码返回folse,不是手机号码返回true
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][34587]\\d{9}";    // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    //使用String的split 方法
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }

    //使用String的split 方法
    public static String[] convertStrToArra2(String str) {
        String[] strArray = null;
        strArray = str.split("/"); //拆分字符为"/" ,然后把结果交给数组strArray
        return strArray;
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap转base64字符串格式
     *
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        // 要返回的字符串
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();
                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reslut;
    }

    /**
     * 根据uri获取图片的绝对路径（从相册中获取能用）
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        String result;
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = uri.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    /**
     * 回收bitmao资源
     */
    public static void RecycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 获取当前Apk的VersionName.
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            String versionName = packInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException er) {
            er.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前APK的VersionCode.
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException er) {
            er.printStackTrace();
        }
        return 0;
    }

}
