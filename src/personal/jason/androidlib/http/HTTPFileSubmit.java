package personal.jason.androidlib.http;

/**
 *  通过HTTP提交文件
 * 
 * @author vb.wbw
 * @create 2010.11.08
 */

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HTTPFileSubmit {
	
    /**
     * 模拟post表单提交方式提交文件 编码默认为：UTF-8
     * @param actionUrl
     * @param params
     * @param files
     * @param ContentType     contentType such as image/jpeg(参看http://blog.csdn.net/fanweiwei/archive/2007/09/17/1787747.aspx)
     * @return
     * @throws IOException
     */
    public static String post(String actionUrl, Map<String, String> params, 
    		Map<String, File> files, String ContentType) throws IOException { 
    	
    	String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--" , LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data"; 
        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl); 
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection(); 
        conn.setReadTimeout(5 * 1000);  
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false); 
        conn.setRequestMethod("POST"); 
        conn.setRequestProperty("connection", "keep-alive"); 
        conn.setRequestProperty("Charsert", "UTF-8"); 
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY); 

        StringBuilder sb = new StringBuilder(); 
        for (Map.Entry<String, String> entry : params.entrySet()) { 
        	sb.append(PREFIX); 
            sb.append(BOUNDARY); 
            sb.append(LINEND); 
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET+LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue()); 
            sb.append(LINEND); 
        } 

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream()); 
        outStream.write(sb.toString().getBytes()); 
        
        if(files!=null)
        for (Map.Entry<String, File> file: files.entrySet()) { 
        	 StringBuilder sb1 = new StringBuilder(); 
             sb1.append(PREFIX); 
             sb1.append(BOUNDARY); 
             sb1.append(LINEND); 
             //sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""+file.getKey()+"\""+LINEND);
             //sb1.append("Content-Type: application/octet-stream; charset="+CHARSET+LINEND);
             sb1.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""+file.getValue().getName()+"\""+LINEND);
             sb1.append("Content-Type: "+"application/octet-stream"+";"+CHARSET+LINEND);
             sb1.append(LINEND);
             outStream.write(sb1.toString().getBytes()); 

             InputStream is = new FileInputStream(file.getValue());
             byte[] buffer = new byte[1024]; 
             int len = 0; 
             while ((len = is.read(buffer)) != -1) { 
            	outStream.write(buffer, 0, len); 
             } 
             is.close(); 
             outStream.write(LINEND.getBytes()); 
        }
        
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes(); 
        outStream.write(end_data); 
        outStream.flush(); 
        
        
        int res = conn.getResponseCode(); 
        StringBuilder sb2 = new StringBuilder(); 
        if (res == 200) {
        	InputStream in = conn.getInputStream(); 
            int ch; 
            while ((ch = in.read()) != -1) 
            	sb2.append((char) ch); 
        }
        outStream.close(); 
        conn.disconnect(); 
        return sb2.toString(); 
    } 
}

