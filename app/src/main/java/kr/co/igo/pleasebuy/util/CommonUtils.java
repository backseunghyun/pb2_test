package kr.co.igo.pleasebuy.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CommonUtils {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * px 을 dp로 변환
     * @param context
     * @param px
     * @return dp
     */
    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getDateDay(String date, String dateType) throws Exception {
        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;
        }
        return day ;
    }

    /**
     * Long형 날짜를 형식에 맞게 변경
     *
     * @param date Long 형의 날짜
     * @return 예) 2016.01.11
     */
    public static String ConvertDate(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(date);
    }

    /**
     * Long형 날짜를 형식에 맞게 변경
     *
     * @param date Long 형의 날짜
     * @return 예) 2016.01.11 14:50:31
     */
    public static String ConvertDateFull(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return df.format(date);
    }


    /**
     * 숫자에 3자리마다 콤마
     */
    public static String getNumberThreeEachFormat(int input) {
        String string = "";

        NumberFormat nf = NumberFormat.getInstance();
        string = nf.format(input);

        return string;
    }

    /**
     * 숫자에 3자리마다 콤마
     */
    public static String getNumberThreeEachFormat(String str) {
        String string = "";

        if (!TextUtils.isEmpty(str) && isStringDouble(str)) {
            int input = Integer.valueOf(str);

            NumberFormat nf = NumberFormat.getInstance();
            string = nf.format(input);
        }

        return string;
    }

    /**
     * 숫자에 3자리마다 콤마
     */
    public static String getNumberThreeEachFormatWithWon(int input) {
        String string = "";

        NumberFormat nf = NumberFormat.getInstance();
        string = nf.format(input) + "원";

        return string;
    }

    /**
     * 숫자에 3자리마다 콤마
     */
    public static String getNumberThreeEachFormatWithWon(String str) {
        String string = "";

        if (!TextUtils.isEmpty(str) && isStringDouble(str)) {
            int input = Integer.valueOf(str);

            NumberFormat nf = NumberFormat.getInstance();
            string = nf.format(input) + "원";
        }

        return string;
    }

    /**
     * 8자리 숫자를 앱의 형식에 맞게 변경
     *
     * @param date 8자리 숫자의 문자열
     * @return 예) 2016.01.11
     */
    public static String convertDateOfEightNumber(String date) {
        if (date.length() > 0 && date.length() < 9) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);

            return year + "." + month + "." + day;
        }

        return null;
    }

    /**
     * 8자리 숫자를 앱의 형식에 맞게 변경
     *
     * @param date 8자리 숫자의 문자열
     * @return 예) 2016.01.11
     */
    public static String convertDateOfEightNumber(String date, String separator) {
        if (date.length() == 8) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);

            return year + separator + month + separator + day;
        } else {
            return date;
        }
    }

    /**
     * 8자리 숫자를 앱의 형식에 맞게 변경
     *
     * @param date 8자리 숫자의 문자열
     * @return 예) 2016년 01월 11일
     */
    public static String convertDateOfEightNumberKR(String date) {
        if (date.length() > 0 && date.length() < 9) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);

            return year + "년 " + month + "월 " + day + "일";
        }

        return null;
    }

    /**
     * 현재날짜와 같은지 비교
     *
     * @param compareValue SimpleDateFormat("yyyyMMdd");
     * @return
     */
    public static boolean isToday(String compareValue) {
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String currentDate = sdf.format(now);

        int compare = currentDate.compareTo(compareValue);
        if (compare == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 현재날짜와 같은지 비교
     *
     * @param compareValue SimpleDateFormat("yyyyMMdd");
     * @return
     */
    public static boolean isToday(long compareValue) {
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String currentDate = sdf.format(now);
        String compareDate = sdf.format(compareValue);

        int compare = currentDate.compareTo(compareDate);
        if (compare == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 현재날짜와 날짜 비교
     *
     * @param compareValue SimpleDateFormat("yyyyMMdd");
     * @return 0    :   현재 날짜 = compareValue
     * 1    :   현재 날짜 > compareValue
     * -1   :   현재 날짜 < compareValue
     */
    public static int TodayCompare(long compareValue) {
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String currentDate = sdf.format(now);
        String compareDate = sdf.format(compareValue);

        return currentDate.compareTo(compareDate);
    }


    /**
     * 현재시간이 지정시간보다 이후인지 비교
     *
     * @param compareValue SimpleDateFormat("HHmmss");
     * @return
     */
    public static boolean isTimeAfter(String compareValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        Time nowTime = new Time(System.currentTimeMillis());

        String currentTime = sdf.format(nowTime.getTime());

        if (compareValue.compareTo(currentTime) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 비트맵을 JPEG로 저장하고 경로를 반환
     */
    public static String getPathSavedBitmapToJpeg(Bitmap bitmap, String subDirName, String imageName) {
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath(); // root dir
        String subDir = "/" + subDirName + "/"; // 하위 폴더 이름
        String fileNm = imageName + ".jpg"; // 파일이름

        String fullDir = rootDir + subDir; // 전체 폴더경로
        String fullPath = fullDir + fileNm; // 전체 경로

        File fileDir;
        try {
            fileDir = new File(fullDir);
            if (!fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            File file = makeFile(fileDir, fullPath);
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.close();

            return fullPath;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }

        return null;
    }

    /**
     * 파일 생성
     *
     * @param dir
     * @return file
     */
    public static File makeFile(File dir, String filePath) {
        File file = null;
        boolean isSuccess = false;
        if (dir.isDirectory()) {
            file = new File(filePath);
            if (file != null && !file.exists()) {
                Log.i("", "!file.exists");
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.i("", "파일생성 여부 = " + isSuccess);
                }
            } else {
                Log.i("", "file.exists");
            }
        }
        return file;
    }

    /**
     * null 확인
     */
    public static boolean isEmpty(CharSequence str) {
        if (TextUtils.isEmpty(str) || "null".equals(str)) {
            return true;
        }

        return false;
    }

    /**
     * null 확인 후 문자열 return
     */
    public static String getJSONObjectString(CharSequence str) {
        if (TextUtils.isEmpty(str) || "null".equals(str)) {
            return "";
        }
        return (String) str;
    }

    /**
     * 숫자 인지 아닌지 판단
     * @param s
     * @return
     */
    public static boolean isStringDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * 둥근 이미지 만들기
     */
    public static Bitmap cropCircle(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }


    /**
     * 둥근 네모 이미지 만들기
     */
    public static Bitmap getRoundedCenteredSquareBitmap(Bitmap bitmap) {
        return getRoundedBitmap(getCenteredSquareBitmap(bitmap));
    }

    private static Bitmap getCenteredSquareBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newEdge = width > height ? height : width;
        // Calculate the center
        int widthStart = (width - newEdge) / 2;
        int heightStart = (height - newEdge) / 2;

        return Bitmap.createBitmap(bitmap, widthStart, heightStart, newEdge, newEdge);
    }

    private static Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        int color = Color.GRAY;
        paint.setColor(color);

        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Bitmap reSizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        canvas.drawBitmap(reSizedBitmap, rect, rect, paint);

        return output;
    }


    /**
     * 유투브 링크 인지 아닌지 확인
     */
    public static boolean isYoutubeLink(String link) {
        if (link.contains("youtu.be") ||
                link.contains("m.youtube.com") ||
                link.contains("youtube.com")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 유투브 링크 얻기
     */
    public static String getYoutubeThumbnailLink(String link) {
        String imageUrl = "";

        String values[] = link.split("&");
        String temp = values[0];
        if (temp.contains("youtu.be/")) {
            int i = temp.indexOf("be/");
            imageUrl = "http://img.youtube.com/vi/" + temp.substring(i + 3) + "/0.jpg";
        } else if (temp.contains("m.youtube.com/")) {
            int i = temp.indexOf("v=");
            imageUrl = "http://img.youtube.com/vi/" + temp.substring(i + 2) + "/0.jpg";
        } else if (temp.contains("youtube.com/")) {
            int i = temp.indexOf("v=");
            imageUrl = "http://img.youtube.com/vi/" + temp.substring(i + 2) + "/0.jpg";
        }
        return imageUrl;
    }

    /**
     * 유투브 동영상 ID 얻기
     */
    public static String getYoutubeVideoID(String link) {
        String id = "";

        String values[] = link.split("&");
        String temp = values[0];
        if (temp.contains("youtu.be/")) {
            id = temp.substring(temp.indexOf("be/") + 3);
        } else if (temp.contains("m.youtube.com/")) {
            id = temp.substring(temp.indexOf("v=") + 2);
        } else if (temp.contains("youtube.com/")) {
            id = temp.substring(temp.indexOf("v=") + 2);
        }
        return id;
    }

    /**
     * 초를 시분초로 변환
     * @param second 초
     * @return 예) 00:00:00
     */
    public static String convertSecondToTime(int second) {
        int tmp1 = second % 3600;
        int tmp2 = tmp1 % 60;
        int hour = (second - tmp1) / 3600;
        int minute = (tmp1 - tmp2) / 60;
        return (hour > 0 ) ? String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", tmp2) : String.format("%02d", minute) + ":" + String.format("%02d", tmp2) ;
    }


    /**
     * 특정글자에 Color 적용
     * str : 원글
     * subStr : 적용할 문자열
     * color  : 적용 Color
     */
    public static SpannableStringBuilder spannableStringSetColor(String str, String subStr, int color) {
        int sPos = 0;
        int ePos = 0;
        SpannableStringBuilder builder = new SpannableStringBuilder(str);

        if (subStr != null && str.contains(subStr)) {
            sPos = str.indexOf(subStr);
            ePos = sPos + subStr.length();
            builder.setSpan(new ForegroundColorSpan(color), sPos, ePos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }

    /**
     * 특정글자에 Color 적용
     * str : 원글
     * subStr1 : 적용할 문자열1
     * subStr2 : 적용할 문자열2
     * color  : 적용 Color
     */
    public SpannableStringBuilder spannableStringSetColor(String str, String subStr1, String subStr2, int color) {
        int sPos1 = 0, sPos2 = 0;
        int ePos1 = 0, ePos2 = 0;
        SpannableStringBuilder builder = new SpannableStringBuilder(str);

        if (subStr1 != null && str.contains(subStr1)) {
            sPos1 = str.indexOf(subStr1);
            ePos1 = sPos1 + subStr1.length();
            builder.setSpan(new ForegroundColorSpan(color), sPos1, ePos1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (subStr2 != null && str.contains(subStr2)) {
            sPos2 = str.indexOf(subStr2);
            ePos2 = sPos2 + subStr2.length();
            builder.setSpan(new ForegroundColorSpan(color), sPos2, ePos2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }


    /**
     * URI 에서 파일 경로를 버전별로 조회한다.
     *
     * @param context Context
     * @param uri     Uri
     * @return 파일 경로
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(Context context, Uri uri) {
        String result = "";

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) { // MediaProvider
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) { // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) { // File
            return uri.getPath();
        }

        return result;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);

                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    // 현재 날짜아 지정날짜의 차이 구하기
    public static long doDiffOfDate(String end){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

//            Date endDate = formatter.parse(end);
            long now = System.currentTimeMillis();
            Date beginDate = new Date(now);
            Date endDate = formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return diffDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 해당하는 요일 구하기
    public static String getDayOfWeek(String date) {
        int year = 0;
        int month = 0;
        int day = 0;
        if (date.length() > 7) {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(4, 6));
            day = Integer.parseInt(date.substring(6, 8));
        }
        String dayOfWeek = "";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        switch (day_of_week) {
            case 1:
                dayOfWeek = "일";
                break;
            case 2:
                dayOfWeek = "월";
                break;
            case 3:
                dayOfWeek = "화";
                break;
            case 4:
                dayOfWeek = "수";
                break;
            case 5:
                dayOfWeek = "목";
                break;
            case 6:
                dayOfWeek = "금";
                break;
            case 7:
                dayOfWeek = "토";
                break;
        }
        return dayOfWeek;
    }


    public static Typeface NotoSansRegular(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansKR-Regular-Alphabetic.ttf");
    }

    public static Typeface NotoSansMedium(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansKR-Medium-Alphabetic.ttf");
    }

    public static Typeface RobotoRegular(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public static Typeface RobotoMedium(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
    }

    // 가로 길이에 맞춰 이미지 크기 조절
    static public Bitmap resizeBitmap(Bitmap original, int width) {

        int resizeWidth = width;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }

    // 줄임말 만들기
    public static String ChangeInShort(String str, int digit, int subStringSize) {
        String result = str;
        if (str.length() > digit) {
            result = str.substring(0, subStringSize) + "...";
        }
        return result;
    }

    public static String getTimeStampString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, java.util.Locale.KOREA);
        return formatter.format(date);
    }

    public static String ChangeMtoKm(int m) {
        String result = String.valueOf(m) + "m";

        if (m >= 1000) {
            DecimalFormat form = new DecimalFormat("#.#");
            double dNumber = (double)m/1000;

            result = form.format(dNumber) + "km";
        }


        return result;
    }


}