package kr.co.igo.pleasebuy.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.util.Date;

public class Dir {
	public static final String HOME = "/AJPark";
	public static final String TEMP = HOME + "/Temp";
	public static final String CAPTURE = HOME + "/Capture";
	public static final String LOG = HOME + "/Log";
	public static final String PROFILE = "profile.png";

	public static File getCacheDir(Context context, String fileName) {
		File folder = context.getCacheDir();
		return new File(folder, fileName);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static File getPublicDir(String dirName) {
		File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + dirName);

		if (!folder.exists()) {
			folder.mkdirs();
		}

		return folder;
	}

	public static File getTempLogFile() {
		File folder = getPublicDir(LOG);
		return new File(folder, DateFormatUtils.format(new Date(), "yyyyMMdd") + ".log");
	}

	public static File getPublicTempImageFile() {
		File folder = getPublicDir(TEMP);
		return new File(folder, "temp_" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".png");
	}

	public static File getPublicCaptureImageFile() {
		File folder = getPublicDir(CAPTURE);
		return new File(folder, "capture_" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".png");
	}

	public static File getPublicImageFile(String fileName) {
		File folder = getPublicDir(HOME);
		return new File(folder, fileName);
	}

	public static String getRealPathFromURI(Context context, Uri contentUri)
	{
		Cursor cursor = null;
		try
		{
			String[] proj = {MediaStore.Images.Media.DATA};
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}

	public static Bitmap decodeScaledBitmap(String filePath, int reqWidth, int reqHeight)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
		final int height       = options.outHeight;
		final int width        = options.outWidth;
		int       inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Bitmap getCenteredSquareBitmap(Bitmap bitmap)
	{
		int width  = bitmap.getWidth();
		int height = bitmap.getHeight();

		int newEdge = width > height ? height : width;
		// Calculate the center
		int widthStart  = (width - newEdge) / 2;
		int heightStart = (height - newEdge) / 2;

		return Bitmap.createBitmap(bitmap, widthStart, heightStart, newEdge, newEdge);
	}

	/**
	 * Bitmap이미지의 가로, 세로 사이즈를 리사이징 한다.
	 *
	 * @param source 원본 Bitmap 객체
	 * @param maxResolution 제한 해상도
	 * @return 리사이즈된 이미지 Bitmap 객체
	 */
	public static Bitmap resizeBitmapImage(Bitmap source, int maxResolution)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		int newWidth = width;
		int newHeight = height;
		float rate = 0.0f;

		if(width > height)
		{
			if(maxResolution < width)
			{
				rate = maxResolution / (float) width;
				newHeight = (int) (height * rate);
				newWidth = maxResolution;
			}
		}
		else
		{
			if(maxResolution < height)
			{
				rate = maxResolution / (float) height;
				newWidth = (int) (width * rate);
				newHeight = maxResolution;
			}
		}

		return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
	}
}
