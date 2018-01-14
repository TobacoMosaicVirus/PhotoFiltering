package com.virus.PhotoFiltering.constant;

public interface Constants {

	// 小文件大小边界:80kb
	public static final int IMG_SMILL_SIZE = 1024*80;
	// 小文件
	public static final int IMG_SMILL = 10240;
	// JPG或JPEG文件，包含META元信息
	public static final int IMG_JPG_HAVE_META = 10241;
	// JPG或JPEG文件，不包含META元信息
	public static final int IMG_JPG_NOT_META = 10242;
	// JPG或JPEG文件，但是后缀名是假的
	public static final int IMG_JPG_FAKE_TYPE = 10243;
	// GIF图
	public static final int IMG_GIF = 10244;
	// PNG
	public static final int IMG_PNG = 10245;
	// 视频
	public static final int VIDEO = 10246;
	public static final int VIDEO_MOV = 10247;
	public static final int VIDEO_MP4 = 10248;
	public static final int VIDEO_3GP = 10249;
	public static final int VIDEO_AVI = 102410;
	public static final int VIDEO_WMV = 102411;
	public static final int UNKNOW_TYPE = 102412;
	
}
