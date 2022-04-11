/*
 * (@)# NormalizeDic.java
 * 
 * (C) Copyright Diquest, Inc. 2002-2013 - All Rights Reserved
 */
package com.diquest.dic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.diquest.app.common.Constant;
import com.diquest.dqdic.transmitable.Transmitable;
import com.diquest.dqdic.util.ByteUtil;
import com.diquest.dqdic.util.CharUtil;
import com.diquest.dqdic.util.CharVector;
import com.diquest.util.FileUtil;

/**
 * 
 * 사전을 만들어준다.
 * 사전을 쉽게 사용하기위해 수정 STEP 1~4 따라서 이용
 * @author hoho
 */
public class NormalizeDic extends Transmitable {
	
	
	//STEP1 만들 사전의  .ks 파일이 있어야한다
	// 유형1 단순 SET 형식의 사전일경우 1개의 ROW가 키워드 식으로 나열 
	// 유형2 대표어 형식 일경우 1개의 ROW가 키워드(대표어)	후보|후보|후보 식으로 나열
	
	//STEP2 추가된 ks 파일을 적어줌 필요에 따라서 파일 확장자 수정
	public static final String[] repretermDic = {""}; // 대표어관계
	public static final String[] simpleDic ={Constant.DIC.VIDEO, Constant.DIC.UFLIX_MOBILE, Constant.DIC.UFLIX_TVG}; //Set형식
	public static final String fileKs = ".ks";
	public static final String fileDcd = ".dcd";
	
	//STEP3 NormalizeDic 메인메소드로 사전이 만들어지는지 확인
	
	//STEP4 DicManager로 사전으로 값이 나오는지 확인
	
	
	public static final int DEFAULT_BUCKET_SIZE = 16 * 1024;
	private static final int HASH_SEED = 1159241;
	private static final char[] NULL_CHAR = new char[]{'N'};
	private int bucketSize;

	private int[] hashHeadPosition;
	private int[] next;
	private int[] wordPos;
	private int[] valuePos;
	private int capacity;
	private int used;

	private char[] wordData;
	private int wordDataCapacity;
	private int wordDataUsed;

	private char[] valueData;
	private int valueDataCapacity;
	private int valueDataUsed;
	
	private List<String> noSaveSelfList;
	private List<String> saveAllValueList;

	public NormalizeDic() {
		init(DEFAULT_BUCKET_SIZE);
	}
	
	public NormalizeDic(int bucketSize) {
		init(bucketSize);
	}
	
	/**
	 * DEFAULT_BUCKET_SIZE 로 초기화
	 * @param size
	 */
	protected void init(int size) {
		if(size == 0) size = 1;
		bucketSize = size;
		capacity = bucketSize;
		wordDataCapacity = capacity * 3;
		valueDataCapacity = capacity * 10;
		used = 0;
		wordDataUsed = 0;
		valueDataUsed = 0;

		hashHeadPosition = new int[bucketSize];
		wordPos = new int[capacity];
		valuePos = new int[capacity];
		next = new int[capacity];
		wordData = new char[wordDataCapacity];
		valueData = new char[valueDataCapacity];

		for(int i=0 ; i < bucketSize ; i++) {
			hashHeadPosition[i] = -1;
		}
		
		// 해당 사전은 자신의 값을 저장하지 않는다. 나머지는 정답을 후보에도 등록한다.
		
		//대표어사전
		noSaveSelfList = new ArrayList<String>();
		for(String s : repretermDic){
			noSaveSelfList.add(s + fileKs);
		}
//		noSaveSelfList.add("humanid.ks");
//		noSaveSelfList.add("keyword.ks");
//		noSaveSelfList.add("repreterms.ks");
		
		//단일 SET 같은 사전
		saveAllValueList = new ArrayList<String>();
		for(String s : simpleDic){
			saveAllValueList.add(s + fileKs);
		}
		
//		saveAllValueList.add("keyword.ks");
		
	}

	/* Search hash table for given string, insert if not found */
	public final void add(String str, char[] value, int vStart, int vLength) {
		add(str.toCharArray(), 0, str.length(), value, vStart, vLength);
	}
	
	private void add(char[] symbol, int begin, int length, char[] value, int vStart, int vLength) {
		assert(length > 0);
		// private을 풀고 외부에서 쓸 것이라면 symbol이 변하는 것을 알고 쓸 것
		symbol = toUpperCase(symbol, begin, length);
		// value = CharUtil.toUpperCase(value, vStart, vLength);

		int hashValue = bitwisehash(symbol, begin, length);

		int previousLink = -1;
		int currentLink = hashHeadPosition[hashValue];
		assert(currentLink >= -1 && currentLink < used) : "1,"+currentLink+","+used;
		while (currentLink != -1 && matchWord(symbol, begin,length, currentLink) == false) {
			previousLink = currentLink;
			currentLink = next[currentLink];
			assert(currentLink >= -1 && currentLink < used) : "2,"+currentLink+","+used;
		}

		if(currentLink == -1) {
			currentLink = getNextBucket();
			storeWord(currentLink, symbol, begin, length);
			next[currentLink] = -1;
			if (previousLink == -1)
				hashHeadPosition[hashValue] = currentLink;
			else
				next[previousLink] = currentLink;
			storeValue(currentLink, value, vStart, vLength);
			assert(currentLink >= -1 && currentLink < used) : "3,"+currentLink+","+used;
		} else {
			//너무많이 나와서 우선 주석처리
//			System.out.println("Cannot use duplicated word as a key : " + new String(symbol, begin, length));
			return;
		}
	}

	public boolean get(String str, CharVector vector) {
		return get(str.toCharArray(), 0, str.length(), vector);
	}
	
	public boolean get(char[] symbol, int begin, int length, CharVector vector) {
		assert(length > 0);
		symbol = CharUtil.getUpperCase(symbol, begin, length);

		int hashValue = bitwisehash(symbol, 0, length);

		int currentLink = hashHeadPosition[hashValue];
		assert(currentLink >= -1 && currentLink < used) : "1,"+currentLink+","+used;
		while (currentLink != -1 && matchWord(symbol, 0, length, currentLink) == false) {
			currentLink = next[currentLink];
			assert(currentLink >= -1 && currentLink < used) : "2,"+currentLink+","+used;
		}

		if(currentLink == -1)
			return false;
		else {
			int vPos = valuePos[currentLink];
			int vLength = ((currentLink == used-1) ? valueDataUsed : valuePos[currentLink+1]) - vPos;
			vector.init(valueData, vPos, vLength);
			return true;
		}
	}

	private int getNextBucket() {
		if (used >= capacity) {
			capacity *= 1.2; // XXX capacity가 5미만이라면?
			int[] newWordPos = new int[capacity];
			System.arraycopy(wordPos, 0, newWordPos, 0, used);
			int[] newValuePos = new int[capacity];
			System.arraycopy(valuePos, 0, newValuePos, 0, used);
			int[] newNext = new int[capacity];
			System.arraycopy(next, 0, newNext, 0, used);
			wordPos = newWordPos;
			next = newNext;
			valuePos = newValuePos;
		}
		return used++;
	}

	private int bitwisehash(char[] word, int begin, int length) {
		int h = HASH_SEED;
		for(int i=begin; i < begin+length; i++) {
			h ^= ((h << 5) + word[i] + (h >> 2));
		}
		return (h & 0x7fffffff) % bucketSize;
	}

	public void printEntries()
	{
//		for(int i=0; i < used; i++ ) {
		for(int i=0; i < bucketSize; i++ ) {
			for(int position=hashHeadPosition[i]; position != -1 ; position=next[position]) {
//			for(int position=wordPos[i]; position != -1 ; position=next[position]) {
//				final int pos = wordPos[i];
//				final int wordLength = ((i == used-1) ? wordDataUsed : wordPos[i+1]) - pos;
//				System.out.println(i + " : " + new String(wordData, pos, wordLength));
				final int pos = wordPos[position];
				final int wordLength = ((position == used-1) ? wordDataUsed : wordPos[position+1]) - pos;
				System.out.println(position + " : " + new String(wordData, pos, wordLength));
			}
		}
	}
	
//	WordIterator wordIterator = null;
//	public WordIterator getWordIterator() {
//		if(wordIterator == null)
//			wordIterator = new WordIterator();
//		else
//			wordIterator.initWordIterator();
//		return wordIterator;
//	}
	
	public WordIterator getWordIterator() { return new WordIterator();}
	public ValueIterator getValueIterator() { return new ValueIterator();}
	
	public class WordIterator implements Iterator<String>{
		int readWordPos;
		
		WordIterator() {
//			initWordIterator();	
			readWordPos = 0;
		}
		
		public void initWordIterator() {
			readWordPos = 0;
		}
		
		@Override
		public boolean hasNext() {
			if(readWordPos >= used)
				return false;
			return true;
		}

		@Override
		public String next() {
			final int pos = wordPos[readWordPos];
			final int wordLength = ((readWordPos == used-1) ? wordDataUsed : wordPos[readWordPos+1]) - pos;
			String ret = new String(wordData, pos, wordLength);
//			System.out.println(readWordPos + " : " + ret);
			readWordPos++;
			return ret;
		}

		@Override
		public void remove() {
		}
	}
	
	public class ValueIterator implements Iterator<String>{
		int readValuePos;
		
		ValueIterator() {
//			initWordIterator();	
			readValuePos = 0;
		}
		
		public void initValueIterator() {
			readValuePos = 0;
		}
		
		@Override
		public boolean hasNext() {
			if(readValuePos >= used)
				return false;
			return true;
		}
		
		@Override
		public String next() {
			final int pos = valuePos[readValuePos];
			final int valueLength = ((readValuePos == used-1) ? valueDataUsed : valuePos[readValuePos+1]) - pos;
			String ret = new String(valueData, pos, valueLength);
//			System.out.println(readWordPos + " : " + ret);
			readValuePos++;
			return ret;
		}
		
		@Override
		public void remove() {
		}
	}
	
	private boolean matchWord(char[] symbol, int begin, int length, int wordID) {
		assert(wordID >= 0 && wordID < used) : wordID+","+used;
		final int pos = wordPos[wordID];
		final int wordLength = ((wordID == used-1) ? wordDataUsed : wordPos[wordID+1]) - pos;
		if (length == wordLength) {
			for (int i=0; i < length; i++) {
				if (symbol[begin+i] != wordData[pos+i])
					return false;
			}
			return true;
		}
		return false;
	}

	private void storeWord(int currentLink, char[] symbol, int begin, int length) {
		if (wordDataUsed + length >= wordDataCapacity) {
			wordDataCapacity = (int) ((wordDataUsed + length)*1.2);
			char[] newWordData = new char[wordDataCapacity];
			System.arraycopy(wordData, 0, newWordData, 0, wordDataUsed);
			wordData = newWordData;
		}
		wordPos[currentLink] = wordDataUsed;
		// for (int i=0; i < length; i++) {
		// wordData[wordDataUsed] = symbol[begin + i];
		// wordDataUsed++;
		//		}
		System.arraycopy(symbol, begin, wordData, wordDataUsed, length);
		wordDataUsed += length;
	}

	private void storeValue(int currentLink, char[] value, int start, int length) {
		if (valueDataUsed + length >= valueDataCapacity) {
			valueDataCapacity = (int) ((valueDataUsed + length)*1.2);
			char[] newValueData = new char[valueDataCapacity];
			System.arraycopy(valueData, 0, newValueData, 0, valueDataUsed);
			valueData = newValueData;
		}
		valuePos[currentLink] = valueDataUsed;
		// for (int i=0; i < length; i++) {
		// valueData[valueDataUsed] = value[start+i];
		// valueDataUsed++;
		//		}
		System.arraycopy(value, start, valueData, valueDataUsed, length);
		valueDataUsed+=length;
	}

	public boolean isNull() {
		return false;
	}
	public void serialize(OutputStream os) throws IOException {
		ByteUtil.writeInt(os, bucketSize);
		for (int i=0; i < bucketSize; i++)
			ByteUtil.writeInt(os, hashHeadPosition[i]);

		ByteUtil.writeInt(os, used);
		for (int i=0; i < used; i++)
			ByteUtil.writeInt(os, wordPos[i]);
		for (int i=0; i < used; i++)
			ByteUtil.writeInt(os, valuePos[i]);
		for (int i=0; i < used; i++)
			ByteUtil.writeInt(os, next[i]);

		ByteUtil.writeInt(os, wordDataUsed);
		for (int i=0; i < wordDataUsed; i++)
			ByteUtil.writeChar(os, wordData[i]);
		ByteUtil.writeInt(os, valueDataUsed);
		for (int i=0; i < valueDataUsed; i++)
			ByteUtil.writeChar(os, valueData[i]);
	}
	public void deserialize(InputStream is) throws IOException {
		bucketSize = ByteUtil.readInt(is);
		hashHeadPosition = new int[bucketSize];
		for (int i=0; i < bucketSize; i++)
			hashHeadPosition[i] = ByteUtil.readInt(is);

		used = capacity = ByteUtil.readInt(is);
		wordPos = new int[used];
		for (int i=0; i < used; i++)
			wordPos[i] = ByteUtil.readInt(is);
		valuePos = new int[used];
		for (int i=0; i < used; i++)
			valuePos[i] = ByteUtil.readInt(is);
		next = new int[used];
		for (int i=0; i < used; i++)
			next[i] = ByteUtil.readInt(is);

		wordDataUsed = wordDataCapacity = ByteUtil.readInt(is);
		wordData = new char[wordDataUsed];
		for (int i=0; i < wordDataUsed; i++)
			wordData[i] = ByteUtil.readChar(is);

		valueDataUsed = valueDataCapacity = ByteUtil.readInt(is);
		valueData = new char[valueDataUsed];
		for (int i=0; i < valueDataUsed; i++)
			valueData[i] = ByteUtil.readChar(is);
	}
	public Transmitable getInstance() {
		return new NormalizeDic();
	}
	
	public boolean regist(String dicPath, String ext) {
		File txtDir = new File(dicPath);
		if(txtDir.isDirectory()) {
			File[] files = txtDir.listFiles();
			int flen = files.length;
			for(int i = 0; i < flen; i++) {
				if(files[i].isFile() == false)
					continue;
				String name = files[i].getAbsolutePath();
				if(name.endsWith(ext)) {
					try {
						// 자신의 값을 저장할지 저장 안할지를 결정한다.
						boolean saveSelf = true;
						boolean saveAllValue = true;
						for (int j = 0; j < noSaveSelfList.size(); j++) {
							if(name.endsWith(noSaveSelfList.get(j))) {
								saveSelf = false;
							}
						}
						for (int j = 0; j < saveAllValueList.size(); j++) {
							if(name.endsWith(saveAllValueList.get(j))) {
								saveAllValue = true;
							}
						}
						if(registDic(new InputStreamReader(new FileInputStream(name), "utf8"), saveSelf, saveAllValue) == false)
							return false;
						
					} catch (IOException e) {
						return false;
					}
				}
			}
		} else {
			System.err.println("Invalid Dic Directory. (" + dicPath + ")");
			return false;
		}
		return true;
	}
	
	/**
	 * 라인 단위로 탭으로 구분된 키-값 쌍을 읽는다.
	 * 라인의 포맷 : 키(대표화)	값|값2|값3
	 * 값들을 '|'로 분리하여, 사전의 키로 저장하고, 키(대표화)를 값으로 저장한다. 
	 * 해당 어휘 입력시 키(대표화)를 값으로 돌려준다.
	 * 
	 * @param txtDicReader
	 * @return
	 */
	private boolean registDic(Reader txtDicReader) {
		return registDic(txtDicReader, true);
	}
	
	private boolean registDic(Reader txtDicReader, boolean saveSelf) {
		return registDic(txtDicReader, saveSelf, false);
	}
	
	private boolean registDic(Reader txtDicReader, boolean saveSelf, boolean saveAllValue) {
		BufferedReader br = new BufferedReader(txtDicReader);		
		String line = null;
		
		try {
			while((line = br.readLine()) != null){
				line = line.trim();
				if (line.length() == 0)
					continue;
				
				String[] kv = line.split("\t");
				if(kv.length < 2) {
					// 키나 값이나 하나만 있는 경우에는 모두 키로 저장한다.
					//TODO 길이제한?
					if(saveSelf) {
						char[] ch = line.trim().toCharArray();
						add(ch, 0, ch.length, ch, 0, ch.length);
					} else {
						add(line.trim(), NULL_CHAR, 0, 1);
					}
					continue;
				}
				
				if(isEmptyTrimmed(kv[0])) {
					continue;
				}
				
				char[] key = kv[0].trim().toCharArray();
//				int keyLen = key.length;
				if(saveSelf) {
					add(key, 0, key.length, key, 0, key.length);		// 정답도 저장한다.
				}
				if(saveAllValue) {
					char[] val = kv[1].trim().toCharArray();
					add(key, 0, key.length, val, 0, val.length);		// value를 parsing 없이 그냥 적제
				}
				
				String[] values = kv[1].split("\\|");
				for(int i = 0; i < values.length; i++) {
					if(isEmptyTrimmed(values[i]))
						continue;
					String trimmed = removeWhitepace(values[i]);
					//TODO 길이제한?
					add(trimmed, key, 0, key.length);
				}
			}
		} catch (IOException e) {
			System.out.println("사전 등록 중 오류 발생");
			return false;
		}

		return true;
	}

	/**
	 * 모든 공백을 제거한다. 사전 탐색용 키 생성등에 사용한다.
	 * @param s
	 * @return
	 */
	public static String removeWhitepace(String s) {
		char[] ch = s.toCharArray();
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < ch.length; i++) {
			if(!Character.isWhitespace(ch[i])){
				b.append(ch[i]);
			}
		}
		s = b.toString();
		return s;
	}
	
	public static final boolean isEmptyTrimmed(String s) {
		return (s == null || s.trim().length() == 0);
	}
	
	public static final char[] toUpperCase(char[] ca, int offset, int length){
		for(int i = offset ; i < offset+length ; i++)
			ca[i] = Character.toUpperCase(ca[i]);
		return ca;
	}
	
	public int getEntrySize() {
		return used;
	}
	
	public void makeDic(String home, String fileName){
		BufferedOutputStream bof;
		regist(home, fileName + fileKs);
		try {
			bof = new BufferedOutputStream(new FileOutputStream(home + fileName + fileDcd));
			writeTo(bof);
			bof.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Dictionary make Sample");
		String dicHome = "./data/dic/";
		FileUtil.makeFolder(dicHome);
		String fileName = "keyword";
		NormalizeDic app = new NormalizeDic();
		app.makeDic(dicHome, fileName);
		System.out.println("Complete make Dic");
	}
}