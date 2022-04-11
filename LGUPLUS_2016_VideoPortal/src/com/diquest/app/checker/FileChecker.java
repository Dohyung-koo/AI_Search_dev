package com.diquest.app.checker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Constant.LOG;
import com.diquest.app.manager.LogMakerManager;
import com.diquest.util.FileUtil;
import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;

public class FileChecker {
	boolean filesize = false;
	private String YYYYMMDD;
	private String YYYYMMDDHHMM;
	private String YYYYMMDDHHMMSS_before1Minute;
	private String YYYYMMDDHHMMSS;
	private String YYYYMMDDHHMMSSSSS;
	
	private static class Singleton {
		private static final FileChecker instance = new FileChecker();
	}

	public static FileChecker getInstance() {
		return Singleton.instance;
	}

	/**
	 * 온전한 파일인지 체크
	 * 
	 * @param dir
	 * @return
	 */
	public List<String> getCompleteFileList(String delimDirs) {
		System.out.println();
		System.out.println("[FILES CHECK]");
		List<String> completeFileList = new ArrayList<String>();
		List allFileList = null;
		try {
			String dirToks[] = delimDirs.split("\\|");
			for (String dir : dirToks) {
				allFileList = FileUtil.findFiles(dir, "*.tar.gz *.txt", true);
				/*
				 * txtFileList = FileUtil.findFiles(dir, "*.txt", true);
				for(int i=0; i < txtFileList.size(); i++){
					allFileList.add(txtFileList.get(i));
				}
				*/
				for (int i = 0; i < allFileList.size(); i++) {
					File f = (File) allFileList.get(i);
					if(f.length()==0){
						filesize=true;
					}
					System.out.print(f.getName() + " check ...");
					// System.out.print( + " lastModified...");
					SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");
					System.out.print(sfd.format(f.lastModified()));
					boolean complete = false;
					for (int j = 0; j < 10; j++) {
						long beforeSize = f.length();
						Thread.sleep(1000);
						long afterSize = f.length();
						long remain = afterSize - beforeSize;
						if (remain == 0) {
							complete = true;
							break;
						}
					}
					if (complete) {
						System.out.print("OK");
						completeFileList.add(f.getPath());
					} else {
						System.out.print("FAIL");
					}
					System.out.println();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 파일 정렬 작업
		for (int i = 0, iSize = completeFileList.size(); i < iSize; i++) {
			File top = new File(completeFileList.get(i));
			for (int j = i + 1; j < iSize; j++) {

				File f = new File(completeFileList.get(j));
				long s1 = top.lastModified();
				long s2 = f.lastModified();

				// ASC ...
				// if sort DESC (s1 > s2)
				// 수정목록이 먼저 인것부터 우선적으로 처리한다
				if (s1 < s2) {
				} else {
					String t1 = completeFileList.get(i);
					String t2 = completeFileList.get(j);
					completeFileList.set(i, t2);
					completeFileList.set(j, t1);
				}

			}
		}

		return completeFileList;
	}

	/**
	 * 파일목록을 해당 디렉토리에 복사한다.
	 * 
	 * @param list
	 * @param backupDir
	 * @return
	 */
	public List<String> copyBackDir(List<String> list, String backupDir) {
		System.out.println("[COPY FILES]");
		List<String> copiedFileList = new ArrayList<String>();
		backupDir = FileUtil.appendEndsPath(backupDir);
		// FileUtil.deleteFolderContents(backupDir);
		for (String s : list) {
			File f = new File(s);
			String copiedFile = backupDir + f.getName();
			File fbak = new File(copiedFile);
			try {
				if (!fbak.exists())
					FileUtil.copy(f, fbak);

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(copiedFile + "... COPIED");
			copiedFileList.add(copiedFile);
		}

		deleteFileList(list);// 복사후 제거하는걸로
		return copiedFileList;
	}

	private void deleteFileList(List<String> list) {
		System.out.println("[DELETE FILES]");
		for (String s : list) {
			File f = new File(s);
			f.delete();
		}
	}

	
	public void getNowTime() {
		Date currentTime = new Date();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDD = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMM = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMMSS = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		mTime = mSimpleDateFormat.format(currentTime);
		YYYYMMDDHHMMSSSSS = mTime;

		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		cal.add(Calendar.MINUTE, -1);
		mTime = mSimpleDateFormat.format(cal.getTime());
		YYYYMMDDHHMMSS_before1Minute = mTime;

	}
	
	
	/**
	 * 1주일 이전 데이터는 삭제한다.
	 * 
	 * @param backupDir
	 */
	public void checkOldFile(String backupDir) {
		List allFileList = null;
		Calendar cal = Calendar.getInstance();
		long todayMil = cal.getTimeInMillis();
		long oneDayMil = 24 * 60 * 60 * 1000;
		Calendar fileCal = Calendar.getInstance();
		Date fileDate = null;

		try {
			allFileList = FileUtil.findFiles(backupDir, "*.tar.gz *.txt", true);
			for (int i = 0; i < allFileList.size(); i++) {
				File f = (File) allFileList.get(i);

				fileDate = new Date(f.lastModified());
				fileCal.setTime(fileDate);
				long diffMil = todayMil - fileCal.getTimeInMillis();
				int diffDay = (int) (diffMil / oneDayMil);
				// 7일이 지난 파일 삭제
				// System.out.println(diffDay);
				if (diffDay > 7 && f.exists()) {
					f.delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void checkOldFiletxt(String backupDir) {
		List allFileList = null;
		Calendar cal = Calendar.getInstance();
		long todayMil = cal.getTimeInMillis();
		long oneDayMil = 24 * 60 * 60 * 1000;
		Calendar fileCal = Calendar.getInstance();
		Date fileDate = null;

		try {
			allFileList = FileUtil.findFiles(backupDir, "*.txt", true);
			for (int i = 0; i < allFileList.size(); i++) {
				File f = (File) allFileList.get(i);

				fileDate = new Date(f.lastModified());
				fileCal.setTime(fileDate);
				long diffMil = todayMil - fileCal.getTimeInMillis();
				int diffDay = (int) (diffMil / oneDayMil);
				// 7일이 지난 파일 삭제
				// System.out.println(diffDay);
				if (diffDay > 7 && f.exists()) {
					f.delete();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private InputStream getInputStream(String tarFileName) throws Exception {
		if (tarFileName.substring(tarFileName.lastIndexOf(".") + 1, tarFileName.lastIndexOf(".") + 3).equalsIgnoreCase("gz")) {
			// System.out.println("Creating an GZIPInputStream for the file");
			return new GZIPInputStream(new FileInputStream(new File(tarFileName)));
		} else {
			System.out.println("printline addlisko.,,.,.");
			// System.out.println("Creating an InputStream for the file");
			return new FileInputStream(new File(tarFileName));
		}
	}

	private List<String> readTar(InputStream in, String untarDir) throws IOException {
		// System.out.println("Reading TarInputStream...");
		List<String> list = new ArrayList<String>();
		TarInputStream tin = new TarInputStream(in);
		TarEntry tarEntry = tin.getNextEntry();
		FileUtil.makeFolder(untarDir);
		if (new File(untarDir).exists()) {
			while (tarEntry != null) {
				File destPath = new File(untarDir + File.separatorChar + tarEntry.getName());
				System.out.println("Processing " + destPath.getAbsoluteFile() + "... UNTAR");
				list.add(destPath.getAbsolutePath().toString());
				FileUtil.makeFolder(destPath.getParent());
				if (!tarEntry.isDirectory()) {
					FileOutputStream fout = new FileOutputStream(destPath);
					tin.copyEntryContents(fout);
					fout.close();
				} else {
					destPath.mkdir();
				}
				tarEntry = tin.getNextEntry();
			}
			tin.close();
		} else {
			// System.out.println("That destination directory doesn't exist! " +
			// untarDir);
		}
		return list;
	}

	/**
	 * tar.gz 압축해제
	 * 
	 * @param fileList
	 * @param workDir
	 */
	public List<String> unTarList(List<String> fileList, String workDir) {
		System.out.println("[UNTAR FILES]");
		List<String> workList = new ArrayList<String>();
		File delF = new File(workDir);
		if (delF.exists())
			FileUtil.delete(delF, true);
		workDir = FileUtil.appendEndsPath(workDir);
		FileUtil.makeFolder(workDir);
		
		for (String s : fileList) {
			if(s.endsWith(".txt")){
				workList.add(s);
					continue;
				}
			try {
				File f = new File(s);
				String dir = workDir + f.getName().substring(0, f.getName().indexOf("."));
					List<String> list = readTar(getInputStream(s), dir);
					for (String s2 : list) {
						if (s2.endsWith(".txt"))
							workList.add(s2);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		return workList;
	}

	public  void logmaker(List<String> fileList , List<String> completeFileList, String workDir) throws IOException {
		String svctype = null;
		
		boolean bIMCS = false;
		boolean bMIMS = false;
		
		boolean Errorfile = false;

		Calendar calendar = Calendar.getInstance();
		java.util.Date date = calendar.getTime();
		String Nowtime = (new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		// String Checkerflag = "Checkerflag = Y";

				
		for (int i = 0; i < fileList.size(); i++) {
			String workFile = fileList.get(i);
			File f = new File(workFile);
//			String fileName = f.getAbsoluteFile().getName();
//			fileName = fileName.substring(0, fileName.indexOf("."));
//			if (fileName.equals("contents") || fileName.equals("count") || fileName.equals("package") || fileName.equals("package_contets")
//					|| fileName.equals("rlevel") || fileName.equals("schedule")) {
//				bIMCS = true;
//			} else if (fileName.equals("banner_event") || fileName.equals("hit_contents") || fileName.equals("theme")) {
//				bMIMS = true;
//			}
			if(f.length()==0 ){
				Errorfile = true;
			}
		}
		for (int i = 0; i < completeFileList.size(); i++) {
			String TarFile = completeFileList.get(i);
			File tarf = new File(TarFile);
			String fileName = tarf.getAbsoluteFile().getName();
			fileName = fileName.substring(0, fileName.indexOf("."));
			
			if (fileName.contains("IPTV")){
				bIMCS = true;
			} else if (fileName.contains("HOTVOD") || fileName.contains("NSC") || fileName.contains("THEME")){ 
				bMIMS = true;
			}
		}
			
	
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Constant.LOG.ADD_LOG_DIR+"addlog"+Nowtime+".txt"), "UTF-8"));

		String IMCSType ;
		String MIMSType ;
		String Errorfilecheck ;
		String filesizecheck = null;
	
		if (bIMCS == true) {
			IMCSType = "IMCS=Y";
		} else {
			IMCSType = "IMCS=N";
		}
		if (bMIMS == true) {
			MIMSType = "MIMS=Y";
		} else {
			MIMSType = "MIMS=N";
		}
		if(Errorfile==true){
			Errorfilecheck = "ERRORFILE=Y";
		}else{
			Errorfilecheck = "ERRORFILE=N";
		}
		if (filesize == true) {
			filesizecheck = "FILESIZE=0";
		}
		
		
		bw.write("AddLog=N");
		bw.newLine();
		bw.write(Errorfilecheck);
		bw.newLine();
		bw.write(IMCSType);
		bw.newLine();
		bw.write(MIMSType);
		bw.newLine();
		if(filesizecheck!=null){
		bw.write(filesizecheck);
		}
		bw.newLine();
		bw.write(Nowtime);
		bw.flush();
		bw.close();

	}

	public static void main(String[] args) throws IOException {
		FileChecker instance = FileChecker.getInstance();
		List<String> list = instance.getCompleteFileList("D:/tmp/bak");
		System.out.println(list);
		List<String> copiedFileList = instance.copyBackDir(list, "D:/tmp/bak");
		List<String> workFileList = instance.unTarList(copiedFileList, "D:/tmp/work/");
		instance.checkOldFile("D:/tmp/data");

		for (String s : workFileList) {
			System.out.println(s);
		}

	}

}
