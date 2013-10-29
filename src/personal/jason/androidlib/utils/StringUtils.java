package personal.jason.androidlib.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * String的工具类，用于String与其他类型的转换
 * 
 * @author vb.wbw
 * @create 2010.11.08
 */

public class StringUtils {

	/**
	 * 将InputStream获取为String类型
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(final InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}

}
