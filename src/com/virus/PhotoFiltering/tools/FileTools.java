package com.virus.PhotoFiltering.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map.Entry;

import com.virus.PhotoFiltering.constant.Constants;

public class FileTools {
	public static String origFinderPath;
	public static String restFinderPath;
	public static String[] fileType = {};

	// 文件Map，key是文件的MD5值，value是文件对象
	static HashMap<String, File> map = new HashMap<>();
	// IO流 这种流会完全拷贝文件，稳定且效率更高
	private static FileInputStream fis;
	private static FileOutputStream fos;

	//获取origFinderPath路径下的所有文件
	public static HashMap<String, File> getFiles(){
		File file = new File(origFinderPath);
		if (!file.isDirectory()) {
			return map;
		}
		// 递归获取目标路径下的所有文件
		findFile(origFinderPath);
		return map;
	}
	// 递归存储文件对象
	static void findFile(String path){
		File file = new File(path);
		File[] listFiles = file.listFiles();
		for (File sonFile : listFiles) {
			if (sonFile.isFile()) {
				// 获取文件类型 小写
				String type = getFileType(sonFile);
				int flag = 0;
				// 判断是否为指定文件类型
				for(int i = 0 ; i < fileType.length ; i++){
					if (type.endsWith(fileType[i])) {
						flag = 1;
					}
				}
				// 属于需要的类型
				if (flag == 1) {
					// 获取MD5值
					String MD5 = MDTools.fileMD5(sonFile.getAbsolutePath());
					map.put(MD5, sonFile);
				}
				continue;
			}else if (sonFile.isDirectory()) {
				findFile(sonFile.getAbsolutePath());
			}
		}
	}

	public static void saveFile(){
		for (Entry<String, File> entry  : map.entrySet()) {
			String MD5 = entry.getKey();
			File file = entry.getValue();
			String[] filePath = getFilePath(file,MD5);
			// 结果文件所在文件夹路径
			String finderPath = restFinderPath + filePath[0];
			// 结果文件完整的绝对路径
			String fileAbsPath = finderPath+"//"+filePath[1];
			// 执行拷贝操作
			copyFile(file, finderPath, fileAbsPath);

		}
	}

	private static void copyFile(File file, String finderPath, String fileAbsPath) {
		File dir = new File(finderPath);
		File outFile = new File(fileAbsPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			if (!outFile.exists()) {
				outFile.createNewFile();
			}else if (MDTools.fileMD5(file.getAbsolutePath()).equals(MDTools.fileMD5(fileAbsPath))) {
				System.out.println("-----------------文件已经存在---------------");
				System.out.println("已存在文件路径:" + fileAbsPath);
				System.out.println("");
				return;
			}
			fis = new FileInputStream(file);
			fos = new FileOutputStream(outFile);
			FileChannel sourceCh = fis.getChannel();
			FileChannel destCh = fos.getChannel();
			MappedByteBuffer mbb = sourceCh.map(FileChannel.MapMode.READ_ONLY, 0, sourceCh.size());
			destCh.write(mbb);
			sourceCh.close();  
			destCh.close(); 
		} catch (Exception e) {
			System.out.println("---------------存储失败-------------");
			System.out.println("异常：" + e.toString());
			System.out.println("原文件路径：\t" + file.getAbsolutePath());
			System.out.println("存储路径：\t" + outFile.getAbsolutePath());
		}
	}

	// .xxx
	static String getFileType(File file) {
		String name = file.getName();
		Integer loc = name.lastIndexOf('.');
		Integer len = name.length();
		String res = "";
		try {
			res = name.substring(loc, len).toLowerCase();
		} catch (Exception e) {
			res = "NOType";
			System.out.println("无后缀名文件："+file.getAbsolutePath());
		}
		return res;
	}

	// 文件是否是图片文件
	static boolean isJPGImg(File file){
		String imgType[] = {"jpg","jpeg"};
		String type = getFileType(file);
		boolean flag = false;
		for (int i = 0; i < imgType.length; i++) {
			if (type.endsWith(imgType[i])) {
				flag = true;
				return flag;
			}
		}
		return flag;
	}

	/*
	 * 功能：获取文件路径
	 * 参数：源文件  源文件MD5
	 * 返回值：String数组，[0]结果文件存储路径  [1]存储文件名
	 */
	public static String[] getFilePath(File file,String md5){
		String type = getFileType(file);
		String res[] = new String[2];
		res[0] = "null";
		res[1] = "null";
		// 文件小于80KB
		if (file.length()/1024 < 80) {
			res[0] = "小文件"+ type.substring(1, type.length()).toUpperCase();
			res[1] = file.getName().substring(0, file.getName().indexOf('.')) + type;
			return  res;
		}
		if (type.endsWith("jpg")||type.endsWith("jpeg")) {
			String jpgDate = ExtInfoTools.getJPGDate(file);
			String jpgCamera = ExtInfoTools.getJPGCamera(file);
			if (jpgDate.startsWith("YES")) {
				res[0] = jpgDate.substring(3, 7) + "年//" + jpgDate.substring(8, 10)+"月";
				res[1] =  jpgDate.substring(3, 7) + "年" + jpgDate.substring(8, 10)+"月" 
						+ jpgDate.substring(11, 13) + "日" +jpgDate.substring(14, 16) + "点"
						+ jpgDate.substring(17, 19) + "分" + jpgDate.substring(20, 22) + "秒"
						+ "@镜头-"+jpgCamera;
			}else if(jpgDate.startsWith("NO")){
				res[0] = "拍摄日期未知";
				res[1] = file.getName().substring(0, file.getName().indexOf('.'));
			}else if (jpgDate.startsWith("Error")) {
				res[0] = "文件格式有误";
				res[1] = file.getName().substring(0, file.getName().indexOf('.'));
			}
		}else if (type.endsWith("gif")) {
			res[0] = "GIF图片";
			res[1] = file.getName().substring(0, file.getName().indexOf('.'));
		}else if (type.endsWith("png")) {
			res[0] = "PNG图片";
			res[1] = file.getName().substring(0, file.getName().indexOf('.'));
		}else if (type.endsWith("gif")) {
			res[0] = "GIF图片";
			res[1] = file.getName().substring(0, file.getName().indexOf('.'));
		}else if(type.endsWith(".mov") 
				|| type.endsWith(".mp4")
				|| type.endsWith(".3gp")
				|| type.endsWith(".avi")
				|| type.endsWith(".wmv")
				){
			res[0] = "短片//" + type.substring(1, type.length()).toUpperCase();
			res[1] = file.getName().substring(0, file.getName().indexOf('.'));
		}else {
			res[0] = "其他//" + type.substring(1, type.length()).toUpperCase();
			res[1] = file.getName().substring(0, file.getName().indexOf('.'));
		}
		// 避免文件名一样，后缀加上MD5的两位
		res[1] += ("@MD5-" + md5.substring(0, 2)+type);
		return  res;
	}
	/*
	 * 功能：获取文件分类
	 * 参数：源文件 
	 * 返回值：文件分类  文件拍摄日期
	 */
	public static Object[] getFileCategory(File file){
		Object res[] = new Object[2];
		String type = getFileType(file);
		// 文件小于80KB
		if (file.length()< Constants.IMG_SMILL_SIZE) {
			// 文件大小小于 80KB
			res[0] = Constants.IMG_SMILL;
		}
		if (type.endsWith("jpg")||type.endsWith("jpeg")) {
			//文件类型为JPG
			String jpgDate = ExtInfoTools.getJPGDate(file);
			if (jpgDate.startsWith("YES")) {
				// 文件为JPG或jpeg，且存在拍摄日期
				res[0] = Constants.IMG_JPG_HAVE_META;
			}else if(jpgDate.startsWith("NO")){
				// 文件为JPG或jpeg，且拍摄日期未知
				res[0] = Constants.IMG_JPG_NOT_META;
			}else if (jpgDate.startsWith("Error")) {
				// 文件格式有误，且拍摄日期未知
				res[0] = Constants.IMG_JPG_FAKE_TYPE;
			}
			res[1] = jpgDate;
		}else if (type.endsWith("gif")) {
			//GIF图片";
			res[0] = Constants.IMG_GIF;
		}else if (type.endsWith("png")) {
			//"PNG图片";
			res[0] = Constants.IMG_PNG;
		}else if(type.endsWith(".mov") 
				|| type.endsWith(".mp4")
				|| type.endsWith(".3gp")
				|| type.endsWith(".avi")
				|| type.endsWith(".wmv")
				){
			//"短片
			res[0] = Constants.VIDEO;
		}else {
			//	"其他//" + type.substring(1, type.length()).toUpperCase();
			res[0] = Constants.UNKNOW_TYPE;
		}
		return  res;
	}

	/*
	 * 功能：获取文件路径
	 * 参数：源文件  源文件MD5
	 * 返回值：String数组，[0]结果文件存储路径  [1]存储文件名
	 */
	public static String[] getFilePath3(File file,String md5){
		String res[] = new String[2];
		Object[] fileCategoryInfo= getFileCategory(file);
		int fileCategory = (int) fileCategoryInfo[0];
		String fileTime = (String) fileCategoryInfo[1];
		String fileName = "";
		switch (fileCategory) {
		case Constants.IMG_SMILL:

			break;
		case Constants.IMG_JPG_HAVE_META:

			break;
		case Constants.IMG_JPG_NOT_META:

			break;
		case Constants.IMG_JPG_FAKE_TYPE:

			break;
		case Constants.IMG_GIF:

			break;
		case Constants.IMG_PNG:

			break;
		case Constants.VIDEO:

			break;
		case Constants.UNKNOW_TYPE:

			break;
		default:
			break;
		}
		return  res;
	}
}