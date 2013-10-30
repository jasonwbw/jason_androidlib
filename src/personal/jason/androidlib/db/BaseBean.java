package personal.jason.androidlib.db;

import java.io.Serializable;

/**
 * 使用带有id的父类构建JavaBean，其中id作为主键，更方便的使用DataBaseHelper类
 *
 * @author vb.wbw
 *
 * @create 2010.12.14
 */

public class BaseBean implements Serializable {
	/**
	 * default version id
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;

	public BaseBean() {
	}

	public BaseBean(String id, String version) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
