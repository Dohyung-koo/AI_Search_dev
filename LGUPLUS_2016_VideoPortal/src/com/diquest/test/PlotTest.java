package com.diquest.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.diquest.app.common.Method;
import com.diquest.plot.PLOT;
import com.diquest.plot.result.PLOTResult;

public class PlotTest {
	private final static int IDX_PLOT_DIC_DIR = 0;
	private final static int IDX_JIANA_DIC_DIR = 1;
	private final static int IDX_LANGUAGE = 2;
	private final static int IDX_KEYWORD_DIR = 3;
	
	private final static String PLOT_HOME = "/resources/plot/dic/korean/dcd";
	private final static String JIANA_HOME = "/resources/jiana/dic/korean/dcd";
	private final static String KEYWORD_DIR = "/resources/plot/dic/korean/dcd/keyword.txt";
	
	PLOT plot=null;
	
	String category = "KEYWORD";

	
	public PlotTest(String homeDir) throws IOException{
		init(new String[]{homeDir+PLOT_HOME, homeDir+JIANA_HOME, "KOREAN", homeDir+KEYWORD_DIR});
//		plot.init(homeDir+"/resources/plot/dic/korean/dcd/", homeDir);
//		plot.jianaInit(homeDir, JianaConst.BIGPRE_FLAG|JianaConst.CN_FLAG);
	}
	
	/**
	 * PLOT 을 초기화한다. 
	 * 
	 * @param	plotDicFolder  : 
	 * @param	jianaDicFolder : 
	 * @throws IOException 
	 */
	public void init(String[] params) throws IOException {
		if (params.length < 3){
			String[] newParams = new String[3];
			System.arraycopy(params, 0, newParams, 0, 2);
			newParams[IDX_LANGUAGE] = "KOREAN";
			params = newParams;
		}
		plot = PLOT.getInstance(params[IDX_LANGUAGE]); 
		plot.init(params[IDX_PLOT_DIC_DIR], params[IDX_JIANA_DIC_DIR]);
		plot.setDisaCategory(category);
		
		List<String> list = new ArrayList<String>();
		list.add("무한도전");
		list.add("무모한도전");
		list.add("막도전");
		list.add("무도");
		if(params.length == 4){
			plot.keywordCompile(list);
		}
	}
	
	
	public String readFile(String infile, String encoding)throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile), encoding));
		while(true){
			String line = br.readLine();
			if(line == null)
				break;
			sb.append(line).append('\n');
		}
		br.close();
		return sb.toString();
	}
	
	/**
	 * 파일 테스트
	 * @param file
	 * @param encoding
	 * @throws IOException
	 */
	public void test1(String file, String encoding) throws IOException{
		String input = readFile(file, encoding);
		PLOTResult result = plot.analyze(input);
		
		String[] ne = result.getNE();
		String[] pattern = result.getPattern();
		int[] pos = result.getPatternPos();
		int[] range = result.getPatternRange();

		for(int i = 0; i < result.getNENum(); i++){
			int s = range[i*4 + 1];
			int e = range[i*4 + 2];
			String org = input.substring(s, e);
			System.out.println(ne[i] + " = " + org);
		}
	}
	
	/**
	 * PLOT 분석 테스트
	 * @param testLine
	 */
	public void plotTest(String testLine){
//		//분석
//		PLOTResult plotResult = plot.analyze(testLine);
//		
//		//결과출력
//		StringBuffer sb=new StringBuffer();
//		
//		sb.append("Lexical Array:\n");
//		for(int i = 0; i < plotResult.getLexicalArray().length; i++) {
//			sb.append(plotResult.getLexical(i)).append("[").append(plotResult.getMorTag(i)).append(",").append(plotResult.getPloTag(i)).append("]/");
//		}
//		sb.append('\n');
//		
//		sb.append("Extract Result:\n");
//		for(int i = 0; i < plotResult.getNENum(); i++) {
//			sb.append(plotResult.getNE(i)).append('[').append(plotResult.getNETag(i)).append(']').append('\n');
//			sb.append("eojIndex = " + plotResult.getEoj(i) + "\n");
//			if(plotResult.getLeftPatternPos(i) >= 0)
//				sb.append("leftPattern = " + plotResult.getLeftPattern(i) + "(" + plotResult.getLeftPatternPos(i) + " ~ " + (plotResult.getLeftPatternPos(i) + plotResult.getLeftPatternSize(i) - 1) + ")\n");
//			if(plotResult.getCenterPatternPos(i) >= 0)
//				sb.append("centerPattern = " + plotResult.getCenterPattern(i) + "(" + plotResult.getCenterPatternPos(i) + " ~ " + (plotResult.getCenterPatternPos(i) + plotResult.getCenterPatternSize(i) - 1) + ")\n");
//			if(plotResult.getRightPatternPos(i) >= 0)
//				sb.append("rightPattern = " + plotResult.getRightPattern(i) + "(" + plotResult.getRightPatternPos(i) + " ~ " + (plotResult.getRightPatternPos(i) + plotResult.getRightPatternSize(i) - 1) + ")\n");
//			sb.append('\n');
//		}
//		sb.append('\n');
//		
//		System.out.println(sb.toString());
		
		PLOTResult plotResult = plot.analyze(testLine);
		String[] lex = plotResult.getLexicalArray();
		String[] mTag = plotResult.getMorTagArray();
		for(int i = 0; i < plotResult.getNENum(); i++) {
			if(plotResult.getCenterPatternPos(i) >= 0 && plotResult.getCenterPattern(i).equals("@keyword")){
				System.out.println(plotResult.getNE(i));
			}
		}
		
		plot.fine();
	}
	
	public static void main(String[] args)throws IOException{
		PlotTest tester = new PlotTest("./");
		
		 
		
//		String testLine = "";
		String testLine = "무한도전의 예전이름은 무모한도전 이고 줄여서 무도라고 한다. 막도전은 아니다";
		
		
		System.out.println("PLOT Test Start");
		tester.plotTest(testLine);
	}
}
