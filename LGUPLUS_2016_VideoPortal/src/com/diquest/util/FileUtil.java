package com.diquest.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtil {
	
	/**
	 * <p>해당 경로의 파일을 삭제한다.</p>
	 * 
	 * @param filePath (삭제할 파일의 경로)
	 * @param fileName (삭제할 파일의 이름)
	 * @return boolean (파일삭제 성공여부)
	 */
	public static boolean delete(String filePath, String fileName) {
		return delete(new File(filePath, fileName));
	}
	
	/**
	 * <p>해당 위치의 파일 또는 디렉토리를 삭제한다. (디렉토리는 비어있을 경우에만 삭제함)</p>
	 * 
	 * @param location (삭제할 파일 또는 디렉토리 위치)
	 * @return boolean (파일 또는 디렉토리 삭제 성공여부)
	 */
	public static boolean delete(String location) {
		return delete(new File(location));
	}
	
	/**
	 * <p>파일 또는 디렉토리를 삭제한다. (디렉토리는 비어있을 경우에만 삭제함)</p>
	 * 
	 * @param file     (삭제할 파일 또는 디렉토리 객체)
	 * @return boolean (파일 또는 디렉토리 삭제 성공여부)
	 */
	public static boolean delete(File file) {
		return delete(file, false);
	}
	
	/**
	 * <p>파일 또는 디렉토리를 삭제한다.</p>
	 * <p>디렉토리일 경우 디렉토리에 있는 모든것을 포함해서 삭제할지 선택한다.</p>
	 * 
	 * @param file        (삭제할 파일 또는 디렉토리 객체)
	 * @param isDeleteAll (디렉토리에 있는 모든것 포함 삭제여부)
	 * @return boolean    (파일 또는 디렉토리 삭제 성공여부)
	 */
	public static boolean delete(File file, boolean isDeleteAll) {
		if (file.exists()) {
			if (file.isFile() || (file.isDirectory() && !isDeleteAll)) {
				return file.delete();
			} else {
				File[] fileList = file.listFiles();
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isFile()) {
						fileList[i].delete();
					} else {
						delete(fileList[i], true);
					}
				}
				return file.delete();
			}
		} else {
			System.out.println("파일 또는 디렉토리가 존재하지 않습니다. [경로 : " + file.getPath() + "]");
			return false;
		}
	}
	
	/**
	 * <p>파일 경로에서 디렉토리 구분자를 시스템 환경에 맞게 변경후 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.toSystemPath(null)      = ""
     * FileUtil.toSystemPath("")        = ""
     * FileUtil.toSystemPath("  ")      = ""
     * FileUtil.toSystemPath("\aa/bb\") = "/aa/bb/"  (유닉스계열 서버 일때)
     * FileUtil.toSystemPath("\aa/bb\") = "\aa\bb\"  (윈도우계열 서버 일때)
	 * </pre>
	 * 
	 * @param filePath (변경할 파일경로)
	 * @return String  (변경후 파일경로)
	 */
	public static String toSystemPath(String filePath) {
		if (StringUtil.isBlank(filePath)) return "";
		filePath = filePath.replaceAll("\\\\", java.io.File.separator);
		filePath = filePath.replaceAll("/", java.io.File.separator);
		return filePath;
	}
	
	/**
	 * <p>파일 경로에서 "\" 를 "/" 로 변경후 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.toUnixPath(null)      = ""
     * FileUtil.toUnixPath("")        = ""
     * FileUtil.toUnixPath("  ")      = ""
     * FileUtil.toUnixPath("\aa\bb\") = "/aa/bb/"
     * FileUtil.toUnixPath("/cc\dd/") = "/cc/dd/"
	 * </pre>
	 * 
	 * @param filePath (변경할 파일경로)
	 * @return String  (변경후 파일경로)
	 */
	public static String toUnixPath(String filePath) {
		return StringUtil.isBlank(filePath) ? "" : filePath.replaceAll("\\\\", "/");
	}
	
	/**
	 * <p>파일 경로에서 "/" 를 "\" 로 변경후 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.toWindowsPath(null)      = ""
     * FileUtil.toWindowsPath("")        = ""
     * FileUtil.toWindowsPath("  ")      = ""
     * FileUtil.toWindowsPath("/aa/bb/") = "\aa\bb\"
     * FileUtil.toWindowsPath("\cc/dd\") = "\cc\dd\"
	 * </pre>
	 * 
	 * @param filePath (변경할 파일경로)
	 * @return String  (변경후 파일경로)
	 */
	public static String toWindowsPath(String filePath) {
		return StringUtil.isBlank(filePath) ? "" : filePath.replaceAll("/", "\\\\");
	}
	
	/**
	 * <p>파일 경로를 정리해서 올바른 경로를 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.cleanPath(null)            = ""
	 * FileUtil.cleanPath("")              = ""
	 * FileUtil.cleanPath("  ")            = ""
	 * FileUtil.cleanPath("D:/aa\bb.jsp")  = "D:/aa/bb.jsp"
	 * FileUtil.cleanPath("D:/aa//bb.jsp") = "D:/aa/bb.jsp"
	 * FileUtil.cleanPath("D:\aa\\bb.jsp") = "D:\aa\bb.jsp"
	 * FileUtil.cleanPath("/D:/aa/bb.jsp") = "D:/aa/bb.jsp"
	 * FileUtil.cleanPath("/aa//bb.jsp")   = "/aa/bb.jsp"
	 * FileUtil.cleanPath("\aa/bb.jsp")    = "/aa/bb.jsp"
	 * </pre>
	 * 
	 * @param filePath (정리할 파일경로)
	 * @return String  (정리후 파일경로)
	 */
	public static String cleanPath(String filePath) {
		if (StringUtil.isBlank(filePath)) return "";
		if (filePath.indexOf("/") != -1 && filePath.indexOf("\\") != -1) {
			filePath = toUnixPath(filePath);
		}
		if (filePath.indexOf(":") != -1 && (filePath.startsWith("/") || filePath.startsWith("\\"))) {
			filePath = filePath.replaceAll("^/*", "");
			filePath = filePath.replaceAll("^\\\\*", "");
		}
		filePath = filePath.replaceAll("/+", "/");
		filePath = filePath.replaceAll("\\\\+", "\\\\");
		return filePath;
	}
	
	
	
	/**
	 * <p>해당 클래스를 기준으로 지정된 위치의 리소스를 검색하여 URL 객체를 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.getResource(null, null)              = null
	 * FileUtil.getResource(null, *)                 = null
	 * FileUtil.getResource(Class, null)             = null
	 * FileUtil.getResource(Class, "")               = URL 객체 (Root Path + Class Package Path)
	 * FileUtil.getResource(Class, "  ")             = URL 객체 (Root Path + Class Package Path)
	 * FileUtil.getResource(Class, "mapping.xml")    = URL 객체 (Root Path + Class Package Path + "/mapping.xml")
	 * FileUtil.getResource(Class, "./mapping.xml")  = URL 객체 (Root Path + Class Package Path + "/mapping.xml")
	 * FileUtil.getResource(Class, "../poi/poi.xml") = URL 객체 (Root Path + Class Package Path + "/../poi/poi.xml")
	 * FileUtil.getResource(Class, "/log4j.xml")     = URL 객체 (Root Path + "/WEB-INF/classes/log4j.xml")
	 * FileUtil.getResource(Class, "kr/co/")         = URL 객체 (Root Path + Class Package Path + "/kr/co/")
	 * FileUtil.getResource(Class, "/kr/co/")        = URL 객체 (Root Path + "/WEB-INF/classes/kr/co/")
	 * </pre>
	 * 
	 * @param clazz    (Class 객체)
	 * @param location (검색할 리소스 위치)
	 * @return URL     (URL 객체)
	 */
	public static URL getResource(Class<?> clazz, String location) {
		if (clazz == null || location == null) return null;
		return clazz.getResource(StringUtil.trim(location));
	}
	
	/**
	 * <p>해당 클래스를 기준으로 지정된 위치의 리소스를 검색하여 리소스 경로를 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.getResourcePath(null, null)              = null
	 * FileUtil.getResourcePath(null, *)                 = null
	 * FileUtil.getResourcePath(Class, null)             = null
	 * FileUtil.getResourcePath(Class, "")               = Root Path + Class Package Path
	 * FileUtil.getResourcePath(Class, "  ")             = Root Path + Class Package Path
	 * FileUtil.getResourcePath(Class, "mapping.xml")    = Root Path + Class Package Path + "/mapping.xml"
	 * FileUtil.getResourcePath(Class, "./mapping.xml")  = Root Path + Class Package Path + "/mapping.xml"
	 * FileUtil.getResourcePath(Class, "../poi/poi.xml") = Root Path + Class Package Path + "/../poi/poi.xml"
	 * FileUtil.getResourcePath(Class, "/log4j.xml")     = Root Path + "/WEB-INF/classes/log4j.xml"
	 * FileUtil.getResourcePath(Class, "kr/co/")         = Root Path + Class Package Path + "/kr/co/"
	 * FileUtil.getResourcePath(Class, "/kr/co/")        = Root Path + "/WEB-INF/classes/kr/co/"
	 * </pre>
	 * 
	 * @param clazz    (Class 객체)
	 * @param location (검색할 리소스 위치)
	 * @return String  (리소스 경로)
	 */
	public static String getResourcePath(Class<?> clazz, String location) {
		try {
			return cleanPath(getResource(clazz, location).getPath());
		} catch (Exception e) {
			String resourcePath = cleanPath(clazz.getResource("").getPath()) + location;
			if (location.startsWith("/")) resourcePath = cleanPath(clazz.getResource("/").getPath()) + location.substring(1);
//			logger.error(ExceptionUtil.addMessage(e, "java.io.FileNotFoundException: 리소스가 존재하지 않거나 액세스할 수 없습니다. [" + resourcePath + "]"));
			System.out.println(e);
			System.out.println("java.io.FileNotFoundException: 리소스가 존재하지 않거나 액세스할 수 없습니다. [" + resourcePath + "]");
			return null;
		}
	}
	
	/**
	 * <p>파일의 확장자를 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.getFileExt(null)       = ""
	 * FileUtil.getFileExt("")         = ""
	 * FileUtil.getFileExt("alin")     = ""
	 * FileUtil.getFileExt("alin.jpg") = "jpg"
	 * FileUtil.getFileExt("alin.GIF") = "GIF"
	 * </pre>
	 * 
	 * @param fileName (파일명)
	 * @return String  (파일의 확장자)
	 */
	public static String getFileExt(String fileName) {
		if (StringUtil.isBlank(fileName)) { return ""; }
		if (fileName.lastIndexOf(".") == -1) { return ""; }
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * <p>파일의 확장자를 소문자로 변경후 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.getFileExtLowerCase(null)       = ""
	 * FileUtil.getFileExtLowerCase("")         = ""
	 * FileUtil.getFileExtLowerCase("alin")     = ""
	 * FileUtil.getFileExtLowerCase("alin.jpg") = "jpg"
	 * FileUtil.getFileExtLowerCase("alin.GIF") = "gif"
	 * </pre>
	 * 
	 * @param fileName (파일명)
	 * @return String  (파일의 확장자)
	 */
	public static String getFileExtLowerCase(String fileName) {
		return getFileExt(fileName).toLowerCase();
	}
	
	/**
	 * <p>파일의 확장자를 통해서 파일의 종류를 리턴한다.</p>
	 * 
	 * <pre>
	 * FileUtil.getFileCategory(null)        = "unknown"
	 * FileUtil.getFileCategory("")          = "unknown"
	 * FileUtil.getFileCategory("alin")      = "unknown"
	 * FileUtil.getFileCategory("alin.egg")  = "unknown"
	 * FileUtil.getFileCategory("alin.jpg")  = "jpg"
	 * FileUtil.getFileCategory("alin.jpeg") = "jpg"
	 * FileUtil.getFileCategory("alin.GIF")  = "gif"
	 * </pre>
	 * 
	 * @param fileName (파일명)
	 * @return String  (파일의 종류)
	 */
	public static String getFileCategory(String fileName) {
		String extension = getFileExtLowerCase(fileName);
		if (!StringUtil.isBlank(extension)) {
			if (StringUtil.isContain("alz", extension, "|")) {
				return "alz";
			} else if (StringUtil.isContain("avi|divx|asx|asf|wmv|mpg|mpeg|mpeg4|flv|mov", extension, "|")) {
				return "avi";
			} else if (StringUtil.isContain("bmp", extension, "|")) {
				return "bmp";
			} else if (StringUtil.isContain("cab", extension, "|")) {
				return "cab";
			} else if (StringUtil.isContain("doc|docx", extension, "|")) {
				return "doc";
			} else if (StringUtil.isContain("exe", extension, "|")) {
				return "exe";
			} else if (StringUtil.isContain("fla", extension, "|")) {
				return "fla";
			} else if (StringUtil.isContain("gif", extension, "|")) {
				return "gif";
			} else if (StringUtil.isContain("htm|html", extension, "|")) {
				return "html";
			} else if (StringUtil.isContain("hwp", extension, "|")) {
				return "hwp";
			} else if (StringUtil.isContain("jpg|jpeg", extension, "|")) {
				return "jpg";
			} else if (StringUtil.isContain("aac|ac3|ape|flac|mp3|ogg|wma", extension, "|")) {
				return "mp3";
			} else if (StringUtil.isContain("mp4", extension, "|")) {
				return "mp4";
			} else if (StringUtil.isContain("pdf", extension, "|")) {
				return "pdf";
			} else if (StringUtil.isContain("png", extension, "|")) {
				return "png";
			} else if (StringUtil.isContain("ppt|pptx", extension, "|")) {
				return "ppt";
			} else if (StringUtil.isContain("psd", extension, "|")) {
				return "psd";
			} else if (StringUtil.isContain("swf", extension, "|")) {
				return "swf";
			} else if (StringUtil.isContain("txt", extension, "|")) {
				return "txt";
			} else if (StringUtil.isContain("xls|xlsx", extension, "|")) {
				return "xls";
			} else if (StringUtil.isContain("xml", extension, "|")) {
				return "xml";
			} else if (StringUtil.isContain("zip", extension, "|")) {
				return "zip";
			}
		}
		return "unknown";
	}

	/**
	 * <p>디렉토리의 파일 리스트를 읽는 메소드.</p>
	 * 
	 * @param dirPath (디렉토리 패스정보, 풀패스임)
	 * @return List<File> (파일리스트)
	 */
	public static List<File> getDirFileList(String dirPath)
	{
		// 디렉토리 파일 리스트
		List<File> dirFileList = null;
		
		// 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
		File dir = new File(dirPath);
		
		// 디렉토리가 존재한다면
		if (dir.exists())
		{
			// 파일 목록을 구함
			File[] files = dir.listFiles();
			
			// 파일 배열을 파일 리스트로 변화함 
			dirFileList = Arrays.asList(files);
		}

		return dirFileList;
	}
	
	/**
	 * <p>파일을 존재여부를 확인하는 메소드</p>
	 * 
	 * @param isLivefile (파일 풀네임, 디렉토리 풀패스 정보 포함)
	 * @return Boolean (파일존재여부)
	 */
	public static Boolean fileIsLive(String isLivefile) {
		File f1 = new File(isLivefile);
		
		if(f1.exists())
		{
			return true;
		}else
		{
			return false;
		}
	}

	/**
	 * <p>파일을 생성하는 메소드</p>
	 * 
	 * @param makeFileName (생성할 파일 풀네임, 디렉토리 풀패스 정보 포함)
	 * @return 없음
	 */
	public static void fileMake(String makeFileName) {
		File f1 = new File(makeFileName);
		try {
			f1.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <p>파일을 삭제하는 메소드</p>
	 * 
	 * @param deleteFileName (삭제할 파일 풀네임, 디렉토리 풀패스 정보 포함)
	 * @return 없음
	 */
	public static void fileDelete(String deleteFileName) {
		File I = new File(deleteFileName);
		I.delete();
	}

	/**
	 * <p>파일을 복사하는 메소드</p>
	 * 
	 * @param inDir (파일복사 대상  디렉토리 풀패스 정보 포함)
	 * @param outDir (파일복사 타켓 디렉토리 풀패스 정보 포함)
	 * @param fileName (파일복사 대상 파일명, 순수파일명)
	 * @return 없음
	 */
	public static void fileCopy(String inDir, String outDir, String fileName) {
		if(fileName == null || "".equals(fileName)) return;
		try {
			String inFileName = inDir+"\\"+fileName;
			String outFileName = outDir+"\\"+fileName;
			
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);
			
			int data = 0;
			while((data=fis.read())!=-1) {
				fos.write(data);
			}
			fis.close();
			fos.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * <p>파일을 이동하는 메소드</p>
	 * 
	 * @param inDir (파일이동 대상  디렉토리 풀패스 정보 포함)
	 * @param outDir (파일이동 타켓 디렉토리 풀패스 정보 포함)
	 * @param fileName (파일이동 대상 파일명, 순수파일명)
	 * @return 없음
	 */
	public static void fileMove(String inDir, String outDir, String fileName) {
		if(fileName == null || "".equals(fileName)) return;
		try {
			String inFileName = inDir+"\\"+fileName;
			String outFileName = outDir+"\\"+fileName;

			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);
			
			int data = 0;
			while((data=fis.read())!=-1) {
				fos.write(data);
			}
			fis.close();
			fos.close();
			
			//복사한뒤 원본파일을 삭제함
			fileDelete(inFileName);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * <p>디렉토리내의 파일을 다른 디렉토리로 이동하는 메소드</p>
	 * 
	 * @param inDir (파일이동 대상  디렉토리 풀패스 정보 포함)
	 * @param outDir (파일이동 타켓 디렉토리 풀패스 정보 포함)
	 * @param gubun ('del', 'cpy', 'mov')
	 * @return 없음
	 */
	public static void dirHandling(String inDir, String outDir, String gubun) {

		try {
		   //이동전의 폴더에 있는 파일들을 읽는다.
		   List<File> dirList = getDirFileList(inDir);

		   //폴더의 사이즈만큼 돌면서 파일을 이동시킨다.
		   for (int i=0; i<dirList.size(); i++)
		   {
			    //i번째 저장되어 있는 파일을 불러온다.
			    String fileName = dirList.get(i).getName();
			    
			    if("del".equals(gubun)){
			    	fileDelete(inDir+"\\"+fileName);	
			    }else if("cpy".equals(gubun)){
			    	fileCopy(inDir, outDir, fileName);	
			    }else if("mov".equals(gubun)){
			    	fileMove(inDir, outDir, fileName);	
			    }
		   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	
	
	
	public static BufferedReader getBufferedReader(String file) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static BufferedReader getBufferedReader(File file) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedReader getBufferedReader(File file,String encode) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static BufferedReader getBufferedReader(String file,String encode) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedWriter getBufferedWriter(String file,String encode) {
		return getBufferedWriter(file, true,encode);
	}
	public static BufferedWriter getBufferedWriter(File file,String encode) {
		return getBufferedWriter(file, true,encode);
	}
	public static BufferedWriter getBufferedWriter(String file) {
		return getBufferedWriter(file, true);
	}
	public static BufferedWriter getBufferedWriter(File file) {
		return getBufferedWriter(file, true);
	}
	public static BufferedWriter getBufferedWriter(String file, boolean append,String encode) {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), encode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedWriter getBufferedWriter(File file, boolean append ,String encode) {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), encode));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static BufferedWriter getBufferedWriter(String file, boolean append) {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedWriter getBufferedWriter(File file, boolean append) {
		try {
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void closeBufferedReader(BufferedReader br) {
		try {
			if(br != null)
				br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeBufferedWriter(BufferedWriter bw) {
		try {
			if(bw != null)
				bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 폴더를 생성한다
	 * @param folderPath
	 */
	public static void makeFolder(String folderPath){
		File f = new File(folderPath);
		if(!f.exists())
			f.mkdirs();
		return;
	}
	
	/**
	 * 폴더안의 내용을 비운다.
	 * @param folderPath
	 */
	public static void deleteFolderContents(String folderPath){
		File f = new File(folderPath);
		File[] list = f.listFiles();
		if(list != null){
			for(int i=0,j=list.length ; i<j ; i++){
				 list[i].delete();
			}
		}
	}
	
	/**
	 * 타겟 파일을 삭제한다.
	 * @param folderPath
	 * @param targetFileName
	 */
	public static void deleteTargetFile(String folderPath,String targetFileName){
		File f = new File(folderPath);
		File[] list = f.listFiles();
		if(list != null){
			for(int i=0,j=list.length ; i<j ; i++){
				if(list[i].getName().equals(targetFileName)){ 
					list[i].delete();
				}
			}
		}
	}
	
	/**
	 * file을 copy , no filtering or recursive.
	 * 
	 * @param sourceFileName The full file path name to the source
	 * @param targetFileName The full file path name to the target
	 */
	public static void copy(String sourceFileName, String targetFileName)
			throws IOException {
		copy(sourceFileName, targetFileName, null, false);
	}

	/**
	 * file을 copy
	 * 
	 * @param sourceFileName The full file path name to the source
	 * @param targetFileName The full file path name to the target
	 * @param fileFilter (ex. "*.java *.jav", "*.class")
	 * @param recursive - recurse down thru the directories.
	 */
	public static void copy(String sourceFileName, String targetFileName,
			String filterName, boolean recursive) throws IOException {
		if (sourceFileName == null) {
			throw new IOException("Source file name is not set.");
		}

		if (targetFileName == null) {
			throw new IOException("Target file name is not set.");
		}

		copy(new File(sourceFileName), new File(targetFileName),
			newFileFilter(filterName), recursive);
	}

	/**
	 * file을 copy , no filtering or recursive.
	 * 
	 * @param sourceFile The full file path name to the source
	 * @param targetFile The full file path name to the target
	 */
	public static void copy(File sourceFile, File targetFile)
			throws IOException {
		copy(sourceFile, targetFile, null, false);
	}

	/**
	 * file을 copy. <p>FileUtil.copy("c:/temp/", "c:/copy", "w*", true); 을 실행하면,
	 * <br>temp 밑의 디렉토리 구조를 그대로 copy하고 w로 시작하는 file들을 각각의 디렉토리에 copy한다.
	 * c:/copy라는 디렉토리가 존재하지 않으면 자동으로 생성한다.
	 * 
	 * @param sourceFile The full file path name to the source
	 * @param targetFile The full file path name to the target
	 * @param fileFilter (ex. "*.java *.jav", "*.class")
	 * @param recursive - recurse down thru the directories.
	 */
	public static void copy(File sourceFile, File targetFile,
			FileFilter filter, boolean recursive) throws IOException {
		if (sourceFile == null) {
			throw new IOException("Source file is not set.");
		}

		if (targetFile == null) {
			throw new IOException("Target file is not set.");
		}

		if (!sourceFile.exists()) {
			throw new IOException("Source file " + sourceFile.getName()
					+ " does not exist.");
		}

		copy(sourceFile, targetFile, filter, recursive, false);
	}

	protected static void copy(File sourceFile, File targetFile,
			FileFilter filter, boolean recursive, boolean first)
			throws IOException {
		if (sourceFile.isDirectory()) {
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}

			if (first || recursive) {
				File[] files = sourceFile.listFiles();
				for (int index = 0; files != null && index < files.length; index++) {
					copy(files[index], new File(targetFile + "/"
							+ files[index].getName()), filter, recursive);
				}
			}
		} else {
			if (filter != null && !filter.accept(sourceFile)) {
				return;
			}

			File parent = targetFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			} else if (targetFile.exists() && targetFile.isDirectory()) {
				throw new IOException("Target file " + targetFile.getName()
						+ " cannot be a directory.");
			} else if (targetFile.exists()) {
				targetFile.delete();
			}

			copyFile(sourceFile, targetFile);
		}
	}

	protected static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;

		if (sourceFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
			throw new IOException("The file cannot be copied onto itself: "
					+ sourceFile.getAbsolutePath());
		}

		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			bis = new BufferedInputStream(fis);

			FileOutputStream fos = new FileOutputStream(targetFile);
			bos = new BufferedOutputStream(fos);

			int i = -1;

			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	public static List findFiles(String fileName, String filterName, boolean recursive) throws IOException {
		if (fileName == null) {
			throw new IOException("File name not set.");
		}

		if (filterName == null) {
			throw new IOException("Filter name not set.");
		}

		return findFiles(new File(fileName), newFileFilter(filterName), recursive);
	}
	
	public static List findFiles(File file, FileFilter filter, boolean recursive) throws IOException {
		if (file == null) {
			throw new IOException("File not set.");
		}

		if (filter == null) {
			throw new IOException("Filter not set.");
		}

		return findFiles(new ArrayList(), file, filter, recursive, true);
	}

	protected static List findFiles(List list, File file, FileFilter filter, boolean recursive, boolean first) throws IOException {
		if (filter != null && !filter.accept(file)) {
			return list;
		}

		if (file.isFile()) {
			list.add(file);
		} else if (first || recursive) {
			File[] files = file.listFiles();
			for (int index = 0; files != null && index < files.length; index++) {
				findFiles(list, files[index], filter, recursive, false);
			}
		}

		return list;
	}
	
	/**
	 * 새로운 FileFilter를 생성한다. <p>Filter의 유형은 *, *[comparitor], [comparitor]*,
	 * [comparitor] <p>복수개의 filter들을 space로 분리하여 사용할 수 있다.
	 */
	public static FileFilter newFileFilter(String filterName) {
		if (filterName == null)
			return null;

		return new DefaultFileFilter(filterName);
	}

	public static class DefaultFileFilter implements FileFilter {

		public DefaultFileFilter(String filterName) {
			this.filterName = filterName;

			StringTokenizer st = new StringTokenizer(filterName, " ");

			String filter = null;
			filters = new String[st.countTokens()];
			comparitors = new String[st.countTokens()];

			for (int index = 0; st.hasMoreTokens(); index++) {
				filter = st.nextToken();

				filters[index] = filter;

				if (filter.startsWith("*") && filter.endsWith("*")) {
					comparitors[index] = null;
				} else if (filter.startsWith("*")) {
					comparitors[index] = filter.substring(1, filter.length());
				} else if (filter.endsWith("*")) {
					comparitors[index] = filter.substring(0,
						filter.length() - 1);
				} else {
					comparitors[index] = filter;
				}

				if (comparitors[index] != null
						&& comparitors[index].length() == 0) {
					throw new IllegalArgumentException("Use * in filter "
							+ filterName);
				}
			}
		}

		public boolean accept(File pathName) {
			if (pathName == null)
				return false;

			if (pathName.isDirectory())
				return true;

			String name = pathName.getName();

			for (int index = 0; index < filters.length; index++) {
				if (filters[index].startsWith("*")
						&& filters[index].endsWith("*")) {
					return true;
				} else if (filters[index].startsWith("*")) {
					if (name.endsWith(comparitors[index]))
						return true;
				} else if (filters[index].endsWith("*")) {
					if (name.startsWith(comparitors[index]))
						return true;
				} else {
					if (name.equals(comparitors[index]))
						return true;
				}
			}

			return false;
		}

		public String toString() {
			return filterName;
		}

		protected String filterName;

		protected String[] filters;

		protected String[] comparitors;
	}
	
	/**
	 * 파일 경로뒤에 / 이나 \\ 가 빠져있으면 추가해준다. 
	 * @param path
	 * @return
	 */
	public static String appendEndsPath(String path){
		if(path.contains("/")){
			path = path.endsWith("/") ? path : path + "/";
		} else if(path.contains("\\")) {
			path = path.endsWith("\\") ? path : path + "\\";
		}
		return path;
	}
}