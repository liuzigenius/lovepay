package yml.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;


public class ReplaceYml {
	
	public static void main(String[] args) throws Exception {
		dealConfig();
		printPath();
	}
	
	private static void printPath() throws Exception{ 
        File f = new File(ReplaceYml.class.getResource("/").getPath());
        System.out.println(f);

        File f2 = new File(ReplaceYml.class.getResource("").getPath());
        System.out.println(f2);

        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);

        URL xmlpath = ReplaceYml.class.getClassLoader().getResource("");
        System.out.println(xmlpath);

        System.out.println(System.getProperty("user.dir"));

        System.out.println(System.getProperty("java.class.path"));
	}
	
	private static void dealConfig(){
        Thread.currentThread().getContextClassLoader();
		String classLoaderPath = ClassLoader.getSystemResource("").getPath();
        String propertiesPath = classLoaderPath.substring(0, classLoaderPath.lastIndexOf("bin")) + "conf/config.properties";
        String ymlPath = classLoaderPath.substring(0, classLoaderPath.lastIndexOf("bin")) + "config/elasticsearch.yml";
		System.out.println(propertiesPath);
		System.out.println(ymlPath);
		try {
			write(ymlPath, read(ymlPath, getProperties(propertiesPath))); // 读取修改文件
        } catch (Exception e) {
	        e.printStackTrace();
        }
	}
	
	private static Map<String, String> getProperties(String propertiesPath) throws Exception {
		Properties configProperties = new Properties();
		configProperties.load(new FileInputStream(propertiesPath));
		Map<String, String> configMap = new HashMap<String, String>();
		for (Entry<Object, Object> prop : configProperties.entrySet()) {
			String k = String.valueOf(prop.getKey());
			String v = String.valueOf(prop.getValue());
			String[] kParts = k.split("\\.");
			if (kParts.length == 3 && kParts[0].equals("elasticsearch")) {
		        if (kParts[2].equals("@ip")) {
		        	configMap.put("network.host", v);
	            } else if (kParts[2].equals("httpPort")) {
		        	configMap.put("http.port", v);
				} else if (kParts[2].equals("transportPort")) {
		        	configMap.put("transport.tcp.port", v);
				} else if (kParts[2].equals("clusterName")) {
		        	configMap.put("cluster.name", v);
				}
            }
        }
		return configMap;
	}
	
	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	private static String read(String filePath, Map<String, String> replaceMap) {
		BufferedReader br = null;
		String line = null;
		StringBuffer buf = new StringBuffer();
		
		try {
			// 根据文件路径创建缓冲输入流
			br = new BufferedReader(new FileReader(filePath));
			
			// 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
			while ((line = br.readLine()) != null) {
				boolean done = false;
				for (Entry<String, String> replaceEntry : replaceMap.entrySet()) {
                    if(line.contains(replaceEntry.getKey())){
                    	String kv = replaceEntry.getKey() + ": " + replaceEntry.getValue();
    					buf.append(kv);
    					done = true;
    					break;
                    } 
                }
				if (!done) {
					buf.append(line);
                }
				buf.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
				}
			}
		}
		
		return buf.toString();
	}
	
	/**
	 * 将内容回写到文件中
	 * 
	 * @param filePath
	 * @param content
	 */
	private static void write(String filePath, String content) {
		BufferedWriter bw = null;
		try {
			// 根据文件路径创建缓冲输出流
			bw = new BufferedWriter(new FileWriter(filePath));
			// 将内容写入文件中
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}
	
}
