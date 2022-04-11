package com.diquest.db.entity;

import java.util.List;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Method;

public class UflixSeriesVO{
	private String resultType;
	private String catGb;
	private String id;
	private String name;
	private String catId;
	private String imgUrl;
	private String imgFileName;
	private String imgType;
	private String price;
	private String prInfo;
	private String runtime;
	private String is51Ch;
	private String isHot;
	private String isCaption;
	private String isHd;
	private String closeYn;
	private String threedYn;
	private String serviceGb;
	private String filterGb;
	private String summary ;
	private String actor;
	private String overseerName;
	private String contsSubname;
	private String genre1;
	private String genre2;
	private String genre3;
	private String seriesNo;
	private String point;
	private String des;
	private String price2;
	private String broadDate;
	private String starringActor;
	private String voiceActor;
	private String broadcaster;
	private String releaseDate;
	private String isFh;
	private String multiMappingFlag;
	private String keywordDesc;
	private String titleEng;
	private String directorEng;
	private String playerEng;
	private String castNameEng;
	private String castName;
	private String titleOrigin;
	private String writerOrigin;
	private String publicCnt;
	private String pointWatcha;
	private String genreUxten;
	private String retentionYn;
	private String keyword;
	private String title;
	private String castNameMax;
	private String castNameMaxEng;
	private String actDispMax;
	private String actDispMaxEng;
	private String pointOrder;
	private String albumNo;
	private String stillImgName;
	private String cpProperty;
	private String cpPropertyUfx;
	private String themeYn;
	private String whereIdx;
	
	// 영화진흥원
	private String koficSupportingActor;
	private String koficExtraActor;
	private String koficSynopsis;
	private String koficKeyword;
	private String koficFilmStudio;
	private String koficFilmDistributor;
	private String koficMakeNation;
	private String koficMakeYear;
	private String koficSumSale;
	private String koficSumAudience;
	
	//초성
	private String nameChosung;
	private String actorChosung;
	private String starringActorChosung;
	private String voiceActorChosung;
	private String castNameChosung;
	private String keywordChosung;
	
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getIs51Ch() {
		return is51Ch;
	}

	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public String getIsCaption() {
		return isCaption;
	}

	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}

	public String getIsHd() {
		return isHd;
	}

	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getThreedYn() {
		return threedYn;
	}

	public void setThreedYn(String threedYn) {
		this.threedYn = threedYn;
	}

	public String getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}

	public String getFilterGb() {
		return filterGb;
	}

	public void setFilterGb(String filterGb) {
		this.filterGb = filterGb;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getOverseerName() {
		return overseerName;
	}

	public void setOverseerName(String overseerName) {
		this.overseerName = overseerName;
	}

	public String getContsSubname() {
		return contsSubname;
	}

	public void setContsSubname(String contsSubname) {
		this.contsSubname = contsSubname;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getGenre2() {
		return genre2;
	}

	public void setGenre2(String genre2) {
		this.genre2 = genre2;
	}

	public String getGenre3() {
		return genre3;
	}

	public void setGenre3(String genre3) {
		this.genre3 = genre3;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getPrice2() {
		return price2;
	}

	public void setPrice2(String price2) {
		this.price2 = price2;
	}

	public String getBroadDate() {
		return broadDate;
	}

	public void setBroadDate(String broadDate) {
		this.broadDate = broadDate;
	}

	public String getStarringActor() {
		return starringActor;
	}

	public void setStarringActor(String starringActor) {
		this.starringActor = starringActor;
	}

	public String getVoiceActor() {
		return voiceActor;
	}

	public void setVoiceActor(String voiceActor) {
		this.voiceActor = voiceActor;
	}

	public String getBroadcaster() {
		return broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getIsFh() {
		return isFh;
	}

	public void setIsFh(String isFh) {
		this.isFh = isFh;
	}

	public String getMultiMappingFlag() {
		return multiMappingFlag;
	}

	public void setMultiMappingFlag(String multiMappingFlag) {
		this.multiMappingFlag = multiMappingFlag;
	}

	public String getKeywordDesc() {
		return keywordDesc;
	}

	public void setKeywordDesc(String keywordDesc) {
		this.keywordDesc = keywordDesc;
	}

	public String getTitleEng() {
		return titleEng;
	}

	public void setTitleEng(String titleEng) {
		this.titleEng = titleEng;
	}

	public String getDirectorEng() {
		return directorEng;
	}

	public void setDirectorEng(String directorEng) {
		this.directorEng = directorEng;
	}

	public String getPlayerEng() {
		return playerEng;
	}

	public void setPlayerEng(String playerEng) {
		this.playerEng = playerEng;
	}

	public String getCastNameEng() {
		return castNameEng;
	}

	public void setCastNameEng(String castNameEng) {
		this.castNameEng = castNameEng;
	}

	public String getCastName() {
		return castName;
	}

	public void setCastName(String castName) {
		this.castName = castName;
	}

	public String getTitleOrigin() {
		return titleOrigin;
	}

	public void setTitleOrigin(String titleOrigin) {
		this.titleOrigin = titleOrigin;
	}

	public String getWriterOrigin() {
		return writerOrigin;
	}

	public void setWriterOrigin(String writerOrigin) {
		this.writerOrigin = writerOrigin;
	}

	public String getPublicCnt() {
		return publicCnt;
	}

	public void setPublicCnt(String publicCnt) {
		this.publicCnt = publicCnt;
	}

	public String getPointWatcha() {
		return pointWatcha;
	}

	public void setPointWatcha(String pointWatcha) {
		this.pointWatcha = pointWatcha;
	}

	public String getGenreUxten() {
		return genreUxten;
	}

	public void setGenreUxten(String genreUxten) {
		this.genreUxten = genreUxten;
	}

	public String getRetentionYn() {
		return retentionYn;
	}

	public void setRetentionYn(String retentionYn) {
		this.retentionYn = retentionYn;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCastNameMax() {
		return castNameMax;
	}

	public void setCastNameMax(String castNameMax) {
		this.castNameMax = castNameMax;
	}

	public String getCastNameMaxEng() {
		return castNameMaxEng;
	}

	public void setCastNameMaxEng(String castNameMaxEng) {
		this.castNameMaxEng = castNameMaxEng;
	}

	public String getActDispMax() {
		return actDispMax;
	}

	public void setActDispMax(String actDispMax) {
		this.actDispMax = actDispMax;
	}

	public String getActDispMaxEng() {
		return actDispMaxEng;
	}

	public void setActDispMaxEng(String actDispMaxEng) {
		this.actDispMaxEng = actDispMaxEng;
	}

	public String getPointOrder() {
		return pointOrder;
	}

	public void setPointOrder(String pointOrder) {
		this.pointOrder = pointOrder;
	}

	public String getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(String albumNo) {
		this.albumNo = albumNo;
	}

	public String getStillImgName() {
		return stillImgName;
	}

	public void setStillImgName(String stillImgName) {
		this.stillImgName = stillImgName;
	}

	public String getCpProperty() {
		return cpProperty;
	}

	public void setCpProperty(String cpProperty) {
		this.cpProperty = cpProperty;
	}

	public String getCpPropertyUfx() {
		return cpPropertyUfx;
	}

	public void setCpPropertyUfx(String cpPropertyUfx) {
		this.cpPropertyUfx = cpPropertyUfx;
	}

	public String getThemeYn() {
		return themeYn;
	}

	public void setThemeYn(String themeYn) {
		this.themeYn = themeYn;
	}

	public String getWhereIdx() {
		return whereIdx;
	}

	public void setWhereIdx(String whereIdx) {
		this.whereIdx = whereIdx;
	}

	public String getKoficSupportingActor() {
		return koficSupportingActor;
	}

	public void setKoficSupportingActor(String koficSupportingActor) {
		this.koficSupportingActor = koficSupportingActor;
	}

	public String getKoficExtraActor() {
		return koficExtraActor;
	}

	public void setKoficExtraActor(String koficExtraActor) {
		this.koficExtraActor = koficExtraActor;
	}

	public String getKoficSynopsis() {
		return koficSynopsis;
	}

	public void setKoficSynopsis(String koficSynopsis) {
		this.koficSynopsis = koficSynopsis;
	}

	public String getKoficKeyword() {
		return koficKeyword;
	}

	public void setKoficKeyword(String koficKeyword) {
		this.koficKeyword = koficKeyword;
	}

	public String getKoficFilmStudio() {
		return koficFilmStudio;
	}

	public void setKoficFilmStudio(String koficFilmStudio) {
		this.koficFilmStudio = koficFilmStudio;
	}

	public String getKoficFilmDistributor() {
		return koficFilmDistributor;
	}

	public void setKoficFilmDistributor(String koficFilmDistributor) {
		this.koficFilmDistributor = koficFilmDistributor;
	}

	public String getKoficMakeNation() {
		return koficMakeNation;
	}

	public void setKoficMakeNation(String koficMakeNation) {
		this.koficMakeNation = koficMakeNation;
	}

	public String getKoficMakeYear() {
		return koficMakeYear;
	}

	public void setKoficMakeYear(String koficMakeYear) {
		this.koficMakeYear = koficMakeYear;
	}

	public String getKoficSumSale() {
		return koficSumSale;
	}

	public void setKoficSumSale(String koficSumSale) {
		this.koficSumSale = koficSumSale;
	}

	public String getKoficSumAudience() {
		return koficSumAudience;
	}

	public void setKoficSumAudience(String koficSumAudience) {
		this.koficSumAudience = koficSumAudience;
	}

	public String getNameChosung() {
		return nameChosung;
	}

	public void setNameChosung(String nameChosung) {
		this.nameChosung = nameChosung;
	}

	public String getActorChosung() {
		return actorChosung;
	}

	public void setActorChosung(String actorChosung) {
		this.actorChosung = actorChosung;
	}

	public String getStarringActorChosung() {
		return starringActorChosung;
	}

	public void setStarringActorChosung(String starringActorChosung) {
		this.starringActorChosung = starringActorChosung;
	}

	public String getVoiceActorChosung() {
		return voiceActorChosung;
	}

	public void setVoiceActorChosung(String voiceActorChosung) {
		this.voiceActorChosung = voiceActorChosung;
	}

	public String getCastNameChosung() {
		return castNameChosung;
	}

	public void setCastNameChosung(String castNameChosung) {
		this.castNameChosung = castNameChosung;
	}

	public String getKeywordChosung() {
		return keywordChosung;
	}

	public void setKeywordChosung(String keywordChosung) {
		this.keywordChosung = keywordChosung;
	}

	public void setAll(UflixSeriesVO vo, List<String> list){
		vo.setResultType(list.get(0));
		vo.setCatGb(list.get(1));
		vo.setId(list.get(2));
		vo.setName(list.get(3));
		vo.setCatId(list.get(4));
		vo.setImgUrl(list.get(5));
		vo.setImgFileName(list.get(6));
		vo.setImgType(list.get(7));
		vo.setPrice(list.get(8));
		vo.setPrInfo(list.get(9));
		vo.setRuntime(list.get(10));
		vo.setIs51Ch(list.get(11));
		vo.setIsHot(list.get(12));
		vo.setIsCaption(list.get(13));
		vo.setIsHd(list.get(14));
		vo.setCloseYn(list.get(15));
		vo.setThreedYn(list.get(16));
		vo.setServiceGb(list.get(17));
		vo.setFilterGb(list.get(18));
		vo.setSummary (list.get(19));
		vo.setActor(list.get(20));
		vo.setOverseerName(list.get(21));
		vo.setContsSubname(list.get(22));
		vo.setGenre1(list.get(23));
		vo.setGenre2(list.get(24));
		vo.setGenre3(list.get(25));
		vo.setSeriesNo(list.get(26));
		vo.setPoint(list.get(27));
		vo.setDes(list.get(28));
		vo.setPrice2(list.get(29));
		vo.setBroadDate(list.get(30));
		vo.setStarringActor(list.get(31));
		vo.setVoiceActor(list.get(32));
		vo.setBroadcaster(list.get(33));
		vo.setReleaseDate(list.get(34));
		vo.setIsFh(list.get(35));
		vo.setMultiMappingFlag(list.get(36));
		vo.setKeywordDesc(list.get(37));
		vo.setTitleEng(list.get(38));
		vo.setDirectorEng(list.get(39));
		vo.setPlayerEng(list.get(40));
		vo.setCastNameEng(list.get(41));
		vo.setCastName(list.get(42));
		vo.setTitleOrigin(list.get(43));
		vo.setWriterOrigin(list.get(44));
		vo.setPublicCnt(list.get(45));
		vo.setPointWatcha(list.get(46));
		vo.setGenreUxten(list.get(47));
		vo.setRetentionYn(list.get(48));
//		vo.setKeyword(list.get(49)); //20160617 키워드 데이터 제외
		vo.setKeyword("");
		vo.setTitle(list.get(50));
		vo.setCastNameMax(list.get(51));
		vo.setCastNameMaxEng(list.get(52));
		vo.setActDispMax(list.get(53));
		vo.setActDispMaxEng(list.get(54));
		vo.setPointOrder(list.get(55));
		vo.setAlbumNo(list.get(56));
		vo.setStillImgName(list.get(57));
		vo.setCpProperty(list.get(58));
		vo.setCpPropertyUfx(list.get(59));
		vo.setThemeYn(list.get(60));
		
		vo.setNameChosung(Method.normalizeChosung(vo.getName()));
		vo.setActorChosung(Method.normalizeHumanChosung(vo.getActor()));
		vo.setStarringActorChosung(Method.normalizeHumanChosung(vo.getStarringActor()));
		vo.setVoiceActorChosung(Method.normalizeHumanChosung(vo.getVoiceActor()));
		vo.setCastNameChosung(Method.normalizeHumanChosung(vo.getCastName()));
		
		if(Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())){
			vo.setKeywordChosung(Method.normalizeChosung(vo.getKeyword()));
		}
	}
	
	public void setDramaFields(UflixSeriesVO vo, List<String> list){
//		vo.setResultType(list.get(0));
//		vo.setCatGb(list.get(1));
//		vo.setId(list.get(2));
		vo.setName(list.get(3));
//		vo.setCatId(list.get(4));
//		vo.setImgUrl(list.get(5));
//		vo.setImgFileName(list.get(6));
//		vo.setImgType(list.get(7));
//		vo.setPrice(list.get(8));
//		vo.setPrInfo(list.get(9));
//		vo.setRuntime(list.get(10));
//		vo.setIs51Ch(list.get(11));
//		vo.setIsHot(list.get(12));
//		vo.setIsCaption(list.get(13));
//		vo.setIsHd(list.get(14));
//		vo.setCloseYn(list.get(15));
//		vo.setThreedYn(list.get(16));
//		vo.setServiceGb(list.get(17));
//		vo.setFilterGb(list.get(18));
//		vo.setSummary (list.get(19));
		vo.setActor(list.get(20));
		vo.setOverseerName(list.get(21));
//		vo.setContsSubname(list.get(22));
//		vo.setGenre1(list.get(23));
//		vo.setGenre2(list.get(24));
//		vo.setGenre3(list.get(25));
//		vo.setSeriesNo(list.get(26));
//		vo.setPoint(list.get(27));
//		vo.setDes(list.get(28));
//		vo.setPrice2(list.get(29));
//		vo.setBroadDate(list.get(30));
//		vo.setStarringActor(list.get(31));
//		vo.setVoiceActor(list.get(32));
//		vo.setBroadcaster(list.get(33));
//		vo.setReleaseDate(list.get(34));
//		vo.setIsFh(list.get(35));
//		vo.setMultiMappingFlag(list.get(36));
//		vo.setKeywordDesc(list.get(37));
//		vo.setTitleEng(list.get(38));
//		vo.setDirectorEng(list.get(39));
//		vo.setPlayerEng(list.get(40));
//		vo.setCastNameEng(list.get(41));
//		vo.setCastName(list.get(42));
//		vo.setTitleOrigin(list.get(43));
//		vo.setWriterOrigin(list.get(44));
//		vo.setPublicCnt(list.get(45));
//		vo.setPointWatcha(list.get(46));
//		vo.setGenreUxten(list.get(47));
//		vo.setRetentionYn(list.get(48));
		vo.setKeyword(list.get(49));
//		vo.setTitle(list.get(50));
		vo.setCastNameMax(list.get(51));
		vo.setCastNameMaxEng(list.get(52));
		vo.setActDispMax(list.get(53));
		vo.setActDispMaxEng(list.get(54));
//		vo.setPointOrder(list.get(55));
//		vo.setAlbumNo(list.get(56));
//		vo.setStillImgName(list.get(57));
//		vo.setCpProperty(list.get(58));
//		vo.setCpPropertyUfx(list.get(59));
//		vo.setThemeYn(list.get(60));
	}
}
