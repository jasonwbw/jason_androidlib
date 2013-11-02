package personal.jason.androidlib.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA. User: Gin Date: 11-3-18 Time: 下午7:06 To change this
 * template use File | Settings | File Templates.
 * 
 * @author firend Gu
 */
public class PicDownloader {
	public static final PicDownloader downloader = new PicDownloader();
	private final String TAG = "pictureDownload";

	public static PicDownloader getInstance() {
		return downloader;
	}

	/**
	 * 下载网络图片到本地sdcard
	 * 
	 * @param fileUrl
	 * @param id
	 * @param suffix
	 * @param picDir
	 * @return
	 */
	public boolean downloadPicture(String fileUrl, String id, String suffix, String picDir) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Bitmap bitmap = GetNetBitmap(fileUrl);
			File dir = new File(picDir);
			dir.mkdirs();
			File file = new File(picDir, id + suffix);// 在SDcard的目录下创建图片文件

			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try // 将网络上读取的图片保存到SDCard中
			{
				FileOutputStream out = new FileOutputStream(file);// 为图片文件实例化输出流
				if (bitmap != null) {
					if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out))// 对图片保存
					{
						out.flush();
						Log.v(TAG, "Success");
						out.close();
						return true;
					}
				}

			} catch (FileNotFoundException e) {
				Log.v(TAG, "no file found");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Log.v(TAG, "dataStream error");
			}
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 读取网络图片
	 * @param url
	 * @return
	 */
	private Bitmap GetNetBitmap(String url) {
		URL imageUrl = null;
		Bitmap bitmap = null;
		try {
			imageUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setDoInput(true);// 设置请求的方式
			conn.connect();

			InputStream is = conn.getInputStream();// 将得到的数据转化为inputStream
			bitmap = BitmapFactory.decodeStream(is);// 将inputstream转化为Bitmap
			is.close();// 关闭数据
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
}
