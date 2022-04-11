package com.diquest.db.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.diquest.app.common.Method;

public class VrContentsVO {
	private String ctgryId;
	private String ctgryNm;
	private String cntntsGroupId;
	private String cntntsGroupNm;
	private String genre;
	private String sersCntntsAt;
	private String sersDfk;
	private String sersComptAt;
	private String makr;
	private String useGradCode;
	private String useGradName;
	private String useGradAge;
	private String contId;
	private String contNm;
	private String contTme;
	private String contDc;
	private String svcBeginDt;	
	private String reprThumbUrl;
	private String contImageUrl;
	private String gameVodUrl;
	private String contUrl;
	private String contCpcty;
	private String contPrc;
	private String gameHmd;
	private String gameCntlr;
	private String gameCmtDe;
	private String multGameAt;
	private String ntwrkUseAt;
	private String cntrlUseAt;
	private String pnrmPlace;
	private String pnrmScrinCo;
	private String pnrmPlayUrl;
	private String cntntsGroupNmChosung;
	private String contNmChosung;
	private String thumbVUrl;
	private String imageVUrl;
	

	public String getThumbVUrl() {
		return thumbVUrl;
	}

	public void setThumbVUrl(String thumbVUrl) {
		this.thumbVUrl = thumbVUrl;
	}

	public String getImageVUrl() {
		return imageVUrl;
	}

	public void setImageVUrl(String imageVUrl) {
		this.imageVUrl = imageVUrl;
	}

	public String getSvcBeginDt() {
		return svcBeginDt;
	}

	public void setSvcBeginDt(String svcBeginDt) {
		this.svcBeginDt = svcBeginDt;
	}

	public String getCtgryId() {
		return ctgryId;
	}

	public void setCtgryId(String ctgryId) {
		this.ctgryId = ctgryId;
	}

	public String getCtgryNm() {
		return ctgryNm;
	}

	public void setCtgryNm(String ctgryNm) {
		this.ctgryNm = ctgryNm;
	}

	public String getCntntsGroupId() {
		return cntntsGroupId;
	}

	public void setCntntsGroupId(String cntntsGroupId) {
		this.cntntsGroupId = cntntsGroupId;
	}

	public String getCntntsGroupNm() {
		return cntntsGroupNm;
	}

	public void setCntntsGroupNm(String cntntsGroupNm) {
		this.cntntsGroupNm = cntntsGroupNm;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSersCntntsAt() {
		return sersCntntsAt;
	}

	public void setSersCntntsAt(String sersCntntsAt) {
		this.sersCntntsAt = sersCntntsAt;
	}

	public String getSersDfk() {
		return sersDfk;
	}

	public void setSersDfk(String sersDfk) {
		this.sersDfk = sersDfk;
	}

	public String getSersComptAt() {
		return sersComptAt;
	}

	public void setSersComptAt(String sersComptAt) {
		this.sersComptAt = sersComptAt;
	}

	public String getMakr() {
		return makr;
	}

	public void setMakr(String makr) {
		this.makr = makr;
	}

	public String getUseGradCode() {
		return useGradCode;
	}

	public void setUseGradCode(String useGradCode) {
		this.useGradCode = useGradCode;
	}

	public String getUseGradName() {
		return useGradName;
	}

	public void setUseGradName(String useGradName) {
		this.useGradName = useGradName;
	}

	public String getUseGradAge() {
		return useGradAge;
	}

	public void setUseGradAge(String useGradAge) {
		this.useGradAge = useGradAge;
	}

	public String getContId() {
		return contId;
	}

	public void setContId(String contId) {
		this.contId = contId;
	}

	public String getContNm() {
		return contNm;
	}

	public void setContNm(String contNm) {
		this.contNm = contNm;
	}

	public String getContTme() {
		return contTme;
	}

	public void setContTme(String contTme) {
		this.contTme = contTme;
	}

	public String getContDc() {
		return contDc;
	}

	public void setContDc(String contDc) {
		this.contDc = contDc;
	}

	public String getReprThumbUrl() {
		return reprThumbUrl;
	}

	public void setReprThumbUrl(String reprThumbUrl) {
		this.reprThumbUrl = reprThumbUrl;
	}

	public String getContImageUrl() {
		return contImageUrl;
	}

	public void setContImageUrl(String contImageUrl) {
		this.contImageUrl = contImageUrl;
	}

	public String getGameVodUrl() {
		return gameVodUrl;
	}

	public void setGameVodUrl(String gameVodUrl) {
		this.gameVodUrl = gameVodUrl;
	}

	public String getContUrl() {
		return contUrl;
	}

	public void setContUrl(String contUrl) {
		this.contUrl = contUrl;
	}

	public String getContCpcty() {
		return contCpcty;
	}

	public void setContCpcty(String contCpcty) {
		this.contCpcty = contCpcty;
	}

	public String getContPrc() {
		return contPrc;
	}

	public void setContPrc(String contPrc) {
		this.contPrc = contPrc;
	}

	public String getGameHmd() {
		return gameHmd;
	}

	public void setGameHmd(String gameHmd) {
		this.gameHmd = gameHmd;
	}

	public String getGameCntlr() {
		return gameCntlr;
	}

	public void setGameCntlr(String gameCntlr) {
		this.gameCntlr = gameCntlr;
	}

	public String getGameCmtDe() {
		return gameCmtDe;
	}

	public void setGameCmtDe(String gameCmtDe) {
		this.gameCmtDe = gameCmtDe;
	}

	public String getMultGameAt() {
		return multGameAt;
	}

	public void setMultGameAt(String multGameAt) {
		this.multGameAt = multGameAt;
	}

	public String getNtwrkUseAt() {
		return ntwrkUseAt;
	}

	public void setNtwrkUseAt(String ntwrkUseAt) {
		this.ntwrkUseAt = ntwrkUseAt;
	}

	public String getCntrlUseAt() {
		return cntrlUseAt;
	}

	public void setCntrlUseAt(String cntrlUseAt) {
		this.cntrlUseAt = cntrlUseAt;
	}

	public String getPnrmPlace() {
		return pnrmPlace;
	}

	public void setPnrmPlace(String pnrmPlace) {
		this.pnrmPlace = pnrmPlace;
	}

	public String getPnrmScrinCo() {
		return pnrmScrinCo;
	}

	public void setPnrmScrinCo(String pnrmScrinCo) {
		this.pnrmScrinCo = pnrmScrinCo;
	}

	public String getPnrmPlayUrl() {
		return pnrmPlayUrl;
	}

	public void setPnrmPlayUrl(String pnrmPlayUrl) {
		this.pnrmPlayUrl = pnrmPlayUrl;
	}

	public String getCntntsGroupNmChosung() {
		return cntntsGroupNmChosung;
	}

	public void setCntntsGroupNmChosung(String cntntsGroupNmChosung) {
		this.cntntsGroupNmChosung = cntntsGroupNmChosung;
	}

	public String getContNmChosung() {
		return contNmChosung;
	}

	public void setContNmChosung(String contNmChosung) {
		this.contNmChosung = contNmChosung;
	}

	// public String getId() {
	// return id;
	// }
	// public void setId(String id) {
	// this.id = id;
	// }
	private String getRandomFourNumber() {
		double r = Math.random();
		double squre = 10000;
		int aa = (int) (r * squre);
		String result = String.valueOf(aa);
		while (result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}

	public String getNowTime() {
		Date currentTime = new Date();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		String mTime = mSimpleDateFormat.format(currentTime);

		return mTime;
	}

	public void setAll(VrContentsVO vo, List<String> list) {
		vo.setCtgryId(list.get(0));
		vo.setCtgryNm(list.get(1));
		vo.setCntntsGroupId(list.get(2));
		vo.setCntntsGroupNm(list.get(3));
		vo.setGenre(list.get(4));
		vo.setSersCntntsAt(list.get(5));
		vo.setSersDfk(list.get(6));
		vo.setSersComptAt(list.get(7));
		vo.setMakr(list.get(8));
		vo.setUseGradCode(list.get(9));
		vo.setUseGradName(list.get(10));
		vo.setUseGradAge(list.get(11));
		vo.setContId(list.get(12));
		vo.setContNm(list.get(13));
		vo.setContTme(list.get(14));
		vo.setContDc(list.get(15));
		vo.setSvcBeginDt(list.get(16));
		vo.setReprThumbUrl(list.get(17));
		vo.setContImageUrl(list.get(18));
		vo.setGameVodUrl(list.get(19));
		vo.setContUrl(list.get(20));
		vo.setContCpcty(list.get(21));
		vo.setContPrc(list.get(22));
		vo.setGameHmd(list.get(23));
		vo.setGameCntlr(list.get(24));
		vo.setGameCmtDe(list.get(25));
		vo.setMultGameAt(list.get(26));
		vo.setNtwrkUseAt(list.get(27));
		vo.setCntrlUseAt(list.get(28));
		vo.setPnrmPlace(list.get(29));
		vo.setPnrmScrinCo(list.get(30));
		vo.setPnrmPlayUrl(list.get(31));
		vo.setThumbVUrl(list.get(32));
		vo.setImageVUrl(list.get(33));
		vo.setCntntsGroupNmChosung(Method.normalizeChosung(list.get(3)));
		vo.setContNmChosung(Method.normalizeChosung(list.get(13)));
	}

}
