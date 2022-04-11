package com.diquest.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import com.diquest.util.FileUtil;
import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;

public class TestTar {
	 
//	 CORE HOME > JAVA > J2SE > CORE
//	2012.09.17 / 15:25
//	[펌]java로 tar파일 압축풀기
//	왕상희백
//	추천 수 98
//	출처: http://maluchi.com/bbs/zboard.php?id=PGQnA&page=3&sn1=&divpage=1&sn=off&ss=on&sc=on&select_arrange=subject&desc=asc&no=67
//	 
//	 
//	>java 로 유닉스에서 사용하는 tar 파일 만들수 있나요?
//	>
//	>만들수 있다면 어떤걸 사용해야 하나요??
//	>
//	>혹시 소스가 있으면...
//
//	글쎄요!!
//	tar 포맷에 대한 스펙을 보면 구현할 수 있겠지만...
//	꽤나 시간과 노력이 필요할 성 싶네요...
//
//	제 생각엔 커맨드라인으로 입력 받아서 유닉스 있는 tar를 불어와서 하는게 나을것 같은데요...
//
//	Runtime.getRuntime().exec("command.dat");
//	command.dat 
//	tar cvf test.tar /home/userhome
//
//	이런식으로 하면 되지 않을까요??
//	지금은 일하는 중이라 좀 더 생각해보고 답변 올려드리겠습니다...^^
//	혹시, 제가 아는 태윤인가요??
//
//	아... 다시 찾아보니 자바로 구현한  tar 패키지가 있었네요...
//	해당 사이트는 http://www.trustice.com/java/tar/ 입니다.
//	자료를 올려드립니다...
//
//	tar.zip - 버젼업된 java용 tar
//	tartool-1-4.zip - 임포트할 패키지가 들어있음, 이전버젼용 java용 tar 도 있음. 
//
//	간단하게 사용법은 이렇습니다...

	public static InputStream getInputStream(String tarFileName) throws Exception{
		if(tarFileName.substring(tarFileName.lastIndexOf(".") + 1, tarFileName.lastIndexOf(".") + 3).equalsIgnoreCase("gz")){
//			System.out.println("Creating an GZIPInputStream for the file");
			return new GZIPInputStream(new FileInputStream(new File(tarFileName)));
		}else{
			System.out.println("Creating an InputStream for the file");
			return new FileInputStream(new File(tarFileName));
		}
	}

   public static void readTar(InputStream in, String untarDir) throws IOException{
	   System.out.println("Reading TarInputStream... (using classes from http://www.trustice.com/java/tar/)");
      TarInputStream tin = new TarInputStream(in);
      TarEntry tarEntry = tin.getNextEntry();
      if(new File(untarDir).exists()){
              while (tarEntry != null){
                 File destPath = new File(untarDir + File.separatorChar + tarEntry.getName());
                 System.out.println("Processing " + destPath.getAbsoluteFile());
                 FileUtil.makeFolder(destPath.getParent());
                 if(!tarEntry.isDirectory()){
                    FileOutputStream fout = new FileOutputStream(destPath);
                    tin.copyEntryContents(fout);
                    fout.close();
                 }else{
                    destPath.mkdir();
                 }
                 tarEntry = tin.getNextEntry();
              }
              tin.close();
      }else{
         System.out.println("That destination directory doesn't exist! " + untarDir);
      }
   }
	   
	public static void main(String[] args) {
		
		String in = "D:/tmp/data/HOTVOD20160618180000.tar.gz";
//		String in = "D:/tmp/data/NSC_2016060212.tar.gz";
		
		String untarDir = "D:/tmp/data/databak/";
		try {
			FileUtil.makeFolder(untarDir);
			TestTar.readTar(TestTar.getInputStream(in), untarDir);
			
		} catch (IOException e) {
			e.printStackTrace();;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
