package com.virus.PhotoFiltering.main;
import java.util.Date;

import com.virus.PhotoFiltering.tools.FileTools;

public class Main {

	// 源文件路径
	static String origFinderPath = "E://04-照片//02-新的照片";
	// 输出文件路径
	static String restFinderPath = "E://00-处理结果//";
	// 需要获取的文件类型
	static String[] fileType = {"jpg","jpeg","png","gif","mov","mp4","avi","3gp","wmv"};

	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// 配置文件路径和文件类型
		FileTools.origFinderPath = origFinderPath;
		FileTools.restFinderPath = restFinderPath;
		FileTools.fileType = fileType;
		// 打印开始时间
		Date date = new Date();
		System.out.println("开始遍历:" +"\t\t"+ date.getHours()+"点"+date.getMinutes() +"分"+date.getSeconds()+"秒");

		// 执行查找任务
		FileTools.getFiles();

		// 打印结束查找任务结束时间
		date = new Date();
		System.out.println("遍历结束,开始存储:" +"\t\t"+ date.getHours()+"点"+date.getMinutes() +"分"+date.getSeconds()+"秒");

		// 按照规则进行存储
		FileTools.saveFile();
		date = new Date();
		System.out.println("存储结束:" +"\t\t"+ date.getHours()+"点"+date.getMinutes() +"分"+date.getSeconds()+"秒");

	}
}
