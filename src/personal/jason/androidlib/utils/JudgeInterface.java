package personal.jason.androidlib.utils;

/**
 * 与接口相关的判断类
 *
 * @author vb.wbw
 *
 * @create 2010.12.14
 */

public class JudgeInterface {
	
	/**
	 * 避免实例化工具类
	 */
	private JudgeInterface(){}
	
	/**
	 * 检查是否是指定接口的实现类
	 * @param c
	 * @param s
	 * @return
	 */
	public static boolean isInterface(Class<?> c, String s) {
		Class<?>[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(s)) {
				return true;
			} else {
				Class<?>[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(s)) {
						return true;
					} else if (isInterface(face1[x], s)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), s);
		}
		return false;

	}
}