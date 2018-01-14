package com.virus.PhotoFiltering.tools;

import java.io.File;
import java.util.Collection;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.gif.GifMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class ExtInfoTools {
	/*
	 * 获取设备信息
	 * return format : Deriver Info
	 */
	public static String getJPGCamera(File file){
		String camera = "Unknow device";
		String info = "";
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(file);
			Iterable<Directory> directories = metadata.getDirectories();
			for (Directory directory : directories) {
				Collection<Tag> tags = directory.getTags();
				info = tags.toString();
				if (info.contains("Model -")) {
					int index = info.indexOf("Model -");
					String temp = info.substring(index+8, info.length());
					if (temp.indexOf(',') != -1) {
						int i = temp.indexOf(',');
						temp = temp.substring(0, i);
						camera = temp.trim();
						break;
					}else{
						int i = temp.indexOf(']');
						temp = temp.substring(0, i);
						camera = temp.trim();
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("------------Error device------------");
			System.out.println(e.getMessage());
			System.out.println("FilePath => " + file.getAbsolutePath());
			System.out.println("");
			camera = "Error device";
		}
		return camera;
	}
	/*
	 * 获取拍摄日期
	 * return format : Flag + Date
	 * 
	 */
	public static String getJPGDate(File file){
		String date = "", info = "";
		
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(file);
			Iterable<Directory> directories = metadata.getDirectories();
			for (Directory directory : directories) {
				Collection<Tag> tags = directory.getTags();
				info = tags.toString();
				if (info.contains("Date/Time Original -")) {
					int index = info.indexOf("Date/Time Original -");
					date = "YES" + info.substring(index+21, index + 40).replace(" ", "-");
					return date;
				}
			}
		} catch (Exception e) {
			return "Error";
		}
		return "NO";
	}

	public static String getGifDate(File file){
		String date = "";
		try {
			Metadata metadata = GifMetadataReader.readMetadata(file);
			Iterable<Directory> directories = metadata.getDirectories();
			for (Directory directory : directories) {
				Collection<Tag> tags = directory.getTags();
				String info = tags.toString();
				if (info.contains("File Modified Date -")) {
					int index = info.indexOf("File Modified Date -");
					date = info.substring(index+21, index + 51).replace(" ", "-");
				}else{
					date = "NO Time";
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return date;
	}

	public static String getPngDate(File file){
		String date = "";
		try {
			Metadata metadata = PngMetadataReader.readMetadata(file);
			Iterable<Directory> directories = metadata.getDirectories();
			for (Directory directory : directories) {
				Collection<Tag> tags = directory.getTags();
				System.out.println(tags.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());

		}
		return date;
	}

	public static String getImage(File file){
		String date = "";
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);
			Iterable<Directory> directories = metadata.getDirectories();
			for (Directory directory : directories) {
				Collection<Tag> tags = directory.getTags();
				System.out.println(tags.toString());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return date;
	}
}
