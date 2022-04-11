package com.diquest.db.entity;

import java.util.List;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Method;

public class UflixPackageVO{
	private String resultType;
	private String catGb;
	private String catId;
	private String catName;
	private String albumId;
	private String albumName;
	private String imgUrl;
	private String imgFileName;
	private String description;
	private String type;
	private String flashYn;
	private String applId;
	private String serviceGb;
	private String applUrl;
	private String createDate;
	private String updateDate;
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
	private String filterGb;
	private String actor;
	private String overseerName;
	private String contsSubname;
	private String genre1;
	private String genre2;
	private String genre3;
	private String seriesNo;
	private String pt;
	private String des;
	private String price2;
	private String broadDate;
	private String starringActor;
	private String voiceActor;
	private String broadcaster;
	private String releaseDate;
	private String isFh;
	private String serCatId;
	private String multiMappingFlag;
	private String posterFileUrl;
	private String posterFileName10;
	private String posterFileName30;
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
	
	//영화진흥원 데이터
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
	private String catNameChosung;
	private String albumNameChosung;
	private String actorChosung;
	private String overseerNameChosung;
	private String starringActorChosung;
	private String voiceActorChosung;
	private String castNameChosung;
	private String keywordChosung;
	private String castNameMaxChosung;
	private String actDispMaxChosung;
	
	private String whereIdx;
	
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
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFlashYn() {
		return flashYn;
	}
	public void setFlashYn(String flashYn) {
		this.flashYn = flashYn;
	}
	public String getApplId() {
		return applId;
	}
	public void setApplId(String applId) {
		this.applId = applId;
	}
	public String getServiceGb() {
		return serviceGb;
	}
	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}
	public String getApplUrl() {
		return applUrl;
	}
	public void setApplUrl(String applUrl) {
		this.applUrl = applUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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
	public String getFilterGb() {
		return filterGb;
	}
	public void setFilterGb(String filterGb) {
		this.filterGb = filterGb;
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
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
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
	public String getSerCatId() {
		return serCatId;
	}
	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}
	public String getMultiMappingFlag() {
		return multiMappingFlag;
	}
	public void setMultiMappingFlag(String multiMappingFlag) {
		this.multiMappingFlag = multiMappingFlag;
	}
	public String getPosterFileUrl() {
		return posterFileUrl;
	}
	public void setPosterFileUrl(String posterFileUrl) {
		this.posterFileUrl = posterFileUrl;
	}
	public String getPosterFileName10() {
		return posterFileName10;
	}
	public void setPosterFileName10(String posterFileName10) {
		this.posterFileName10 = posterFileName10;
	}
	public String getPosterFileName30() {
		return posterFileName30;
	}
	public void setPosterFileName30(String posterFileName30) {
		this.posterFileName30 = posterFileName30;
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
	public String getAlbumNameChosung() {
		return albumNameChosung;
	}
	public void setAlbumNameChosung(String albumNameChosung) {
		this.albumNameChosung = albumNameChosung;
	}
	public String getCatNameChosung() {
		return catNameChosung;
	}
	public void setCatNameChosung(String catNameChosung) {
		this.catNameChosung = catNameChosung;
	}
	public String getActorChosung() {
		return actorChosung;
	}
	public void setActorChosung(String actorChosung) {
		this.actorChosung = actorChosung;
	}
	public String getOverseerNameChosung() {
		return overseerNameChosung;
	}
	public void setOverseerNameChosung(String overseerNameChosung) {
		this.overseerNameChosung = overseerNameChosung;
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
	public String getCastNameMaxChosung() {
		return castNameMaxChosung;
	}
	public void setCastNameMaxChosung(String castNameMaxChosung) {
		this.castNameMaxChosung = castNameMaxChosung;
	}
	public String getActDispMaxChosung() {
		return actDispMaxChosung;
	}
	public void setActDispMaxChosung(String actDispMaxChosung) {
		this.actDispMaxChosung = actDispMaxChosung;
	}
	public void setAll(UflixPackageVO vo, List<String> list){
		vo.setResultType(list.get(0));
		vo.setCatGb(list.get(1));
		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
		vo.setAlbumId(list.get(4));
		vo.setAlbumName(list.get(5));
		vo.setImgUrl(list.get(6));
		vo.setImgFileName(list.get(7));
		vo.setDescription(list.get(8));
		vo.setType(list.get(9));
		vo.setFlashYn(list.get(10));
		vo.setApplId(list.get(11));
		vo.setServiceGb(list.get(12));
		vo.setApplUrl(list.get(13));
		vo.setCreateDate(list.get(14));
		vo.setUpdateDate(list.get(15));
		vo.setImgType(list.get(16));
		vo.setPrice(list.get(17));
		vo.setPrInfo(list.get(18));
		vo.setRuntime(list.get(19));
		vo.setIs51Ch(list.get(20));
		vo.setIsHot(list.get(21));
		vo.setIsCaption(list.get(22));
		vo.setIsHd(list.get(23));
		vo.setCloseYn(list.get(24));
		vo.setThreedYn(list.get(25));
		vo.setFilterGb(list.get(26));
		vo.setActor(list.get(27));
		vo.setOverseerName(list.get(28));
		vo.setContsSubname(list.get(29));
		vo.setGenre1(list.get(30));
		vo.setGenre2(list.get(31));
		vo.setGenre3(list.get(32));
		vo.setSeriesNo(list.get(33));
		vo.setPt(list.get(34));
		vo.setDes(list.get(35));
		vo.setPrice2(list.get(36));
		vo.setBroadDate(list.get(37));
		vo.setStarringActor(list.get(38));
		vo.setVoiceActor(list.get(39));
		vo.setBroadcaster(list.get(40));
		vo.setReleaseDate(list.get(41));
		vo.setIsFh(list.get(42));
		vo.setSerCatId(list.get(43));
		vo.setMultiMappingFlag(list.get(44));
		vo.setPosterFileUrl(list.get(45));
		vo.setPosterFileName10(list.get(46));
		vo.setPosterFileName30(list.get(47));
		vo.setKeywordDesc(list.get(48));
		vo.setTitleEng(list.get(49));
		vo.setDirectorEng(list.get(50));
		vo.setPlayerEng(list.get(51));
		vo.setCastNameEng(list.get(52));
		vo.setCastName(list.get(53));
		vo.setTitleOrigin(list.get(54));
		vo.setWriterOrigin(list.get(55));
		vo.setPublicCnt(list.get(56));
		vo.setPointWatcha(list.get(57));
		vo.setGenreUxten(list.get(58));
		vo.setRetentionYn(list.get(59));
//		vo.setKeyword(list.get(60)); //20160617 키워드 데이터 제외
		vo.setKeyword(""); //20160617 키워드 데이터 제외
		vo.setTitle(list.get(61));
		vo.setCastNameMax(list.get(62));
		vo.setCastNameMaxEng(list.get(63));
		vo.setActDispMax(list.get(64));
		vo.setActDispMaxEng(list.get(65));
		vo.setPointOrder(list.get(66));
		vo.setAlbumNo(list.get(67));
		vo.setStillImgName(list.get(68));
		vo.setCpProperty(list.get(69));
		vo.setCpPropertyUfx(list.get(70));
		vo.setThemeYn(list.get(71));

		
		vo.setCatNameChosung(Method.normalizeChosung(vo.getCatName()));
		vo.setAlbumNameChosung(Method.normalizeChosung(vo.getAlbumName()));
		vo.setActorChosung(Method.normalizeHumanChosung(vo.getActor()));
		vo.setOverseerNameChosung(Method.normalizeHumanChosung(vo.getOverseerName()));
		vo.setStarringActorChosung(Method.normalizeHumanChosung(vo.getStarringActor()));
		vo.setVoiceActorChosung(Method.normalizeHumanChosung(vo.getVoiceActor()));
		vo.setCastNameChosung(Method.normalizeHumanChosung(vo.getCastName()));
		if(Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())){
			vo.setKeywordChosung(Method.normalizeChosung(vo.getKeyword()));
		}
		vo.setCastNameMaxChosung(Method.normalizeHumanChosung(vo.getCastNameMax()));
		vo.setActDispMaxChosung(Method.normalizeHumanChosung(vo.getActDispMax()));
	}
	
	public void setDramaFields(UflixPackageVO vo, List<String> list){
//		vo.setResultType(list.get(0));
//		vo.setCatGb(list.get(1));
//		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
//		vo.setAlbumId(list.get(4));
		vo.setAlbumName(list.get(5));
//		vo.setImgUrl(list.get(6));
//		vo.setImgFileName(list.get(7));
//		vo.setDescription(list.get(8));
//		vo.setType(list.get(9));
//		vo.setFlashYn(list.get(10));
//		vo.setApplId(list.get(11));
//		vo.setServiceGb(list.get(12));
//		vo.setApplUrl(list.get(13));
//		vo.setCreateDate(list.get(14));
//		vo.setUpdateDate(list.get(15));
//		vo.setImgType(list.get(16));
//		vo.setPrice(list.get(17));
//		vo.setPrInfo(list.get(18));
//		vo.setRuntime(list.get(19));
//		vo.setIs51Ch(list.get(20));
//		vo.setIsHot(list.get(21));
//		vo.setIsCaption(list.get(22));
//		vo.setIsHd(list.get(23));
//		vo.setCloseYn(list.get(24));
//		vo.setThreedYn(list.get(25));
//		vo.setFilterGb(list.get(26));
		vo.setActor(list.get(27));
		vo.setOverseerName(list.get(28));
//		vo.setContsSubname(list.get(29));
//		vo.setGenre1(list.get(30));
//		vo.setGenre2(list.get(31));
//		vo.setGenre3(list.get(32));
//		vo.setSeriesNo(list.get(33));
//		vo.setPt(list.get(34));
//		vo.setDes(list.get(35));
//		vo.setPrice2(list.get(36));
//		vo.setBroadDate(list.get(37));
//		vo.setStarringActor(list.get(38));
//		vo.setVoiceActor(list.get(39));
//		vo.setBroadcaster(list.get(40));
//		vo.setReleaseDate(list.get(41));
//		vo.setIsFh(list.get(42));
//		vo.setSerCatId(list.get(43));
//		vo.setMultiMappingFlag(list.get(44));
//		vo.setPosterFileUrl(list.get(45));
//		vo.setPosterFileName10(list.get(46));
//		vo.setPosterFileName30(list.get(47));
//		vo.setKeywordDesc(list.get(48));
//		vo.setTitleEng(list.get(49));
//		vo.setDirectorEng(list.get(50));
//		vo.setPlayerEng(list.get(51));
//		vo.setCastNameEng(list.get(52));
//		vo.setCastName(list.get(53));
//		vo.setTitleOrigin(list.get(54));
//		vo.setWriterOrigin(list.get(55));
//		vo.setPublicCnt(list.get(56));
//		vo.setPointWatcha(list.get(57));
//		vo.setGenreUxten(list.get(58));
//		vo.setRetentionYn(list.get(59));
		vo.setKeyword(list.get(60));
//		vo.setTitle(list.get(61));
		vo.setCastNameMax(list.get(62));
		vo.setCastNameMaxEng(list.get(63));
		vo.setActDispMax(list.get(64));
		vo.setActDispMaxEng(list.get(65));
//		vo.setPointOrder(list.get(66));
//		vo.setAlbumNo(list.get(67));
//		vo.setStillImgName(list.get(68));
//		vo.setCpProperty(list.get(69));
//		vo.setCpPropertyUfx(list.get(70));
//		vo.setThemeYn(list.get(71));
	}
	/*public void setAppend(UflixPackageVO vo, List<String> list) {
 
		vo.setResultType(list.get(0));
		vo.setCatGb(list.get(1));
		vo.setCatId(list.get(2));
		vo.setCatName(list.get(3));
		vo.setAlbumId(list.get(4));
		vo.setAlbumName(list.get(5));
		vo.setImgUrl(list.get(6));
		vo.setImgFileName(list.get(7));
		vo.setDescription(list.get(8));
		vo.setType(list.get(9));
		vo.setFlashYn(list.get(10));
		vo.setApplId(list.get(11));
		vo.setServiceGb(list.get(12));
		vo.setApplUrl(list.get(13));
		vo.setCreateDate(list.get(14));
		vo.setUpdateDate(list.get(15));
		vo.setImgType(list.get(16));
		vo.setPrice(list.get(17));
		vo.setPrInfo(list.get(18));
		vo.setRuntime(list.get(19));
		vo.setIs51Ch(list.get(20));
		vo.setIsHot(list.get(21));
		vo.setIsCaption(list.get(22));
		vo.setIsHd(list.get(23));
		vo.setCloseYn(list.get(24));
		vo.setThreedYn(list.get(25));
		vo.setFilterGb(list.get(26));
		vo.setActor(list.get(27));
		vo.setOverseerName(list.get(28));
		vo.setContsSubname(list.get(29));
		vo.setGenre1(list.get(30));
		vo.setGenre2(list.get(31));
		vo.setGenre3(list.get(32));
		vo.setSeriesNo(list.get(33));
		vo.setPt(list.get(34));
		vo.setDes(list.get(35));
		vo.setPrice2(list.get(36));
		vo.setBroadDate(list.get(37));
		vo.setStarringActor(list.get(38));
		vo.setVoiceActor(list.get(39));
		vo.setBroadcaster(list.get(40));
		vo.setReleaseDate(list.get(41));
		vo.setIsFh(list.get(42));
		vo.setSerCatId(list.get(43));
		vo.setMultiMappingFlag(list.get(44));
		vo.setPosterFileUrl(list.get(45));
		vo.setPosterFileName10(list.get(46));
		vo.setPosterFileName30(list.get(47));
		vo.setKeywordDesc(list.get(48));
		vo.setTitleEng(list.get(49));
		vo.setDirectorEng(list.get(50));
		vo.setPlayerEng(list.get(51));
		vo.setCastNameEng(list.get(52));
		vo.setCastName(list.get(53));
		vo.setTitleOrigin(list.get(54));
		vo.setWriterOrigin(list.get(55));
		vo.setPublicCnt(list.get(56));
		vo.setPointWatcha(list.get(57));
		vo.setGenreUxten(list.get(58));
		vo.setRetentionYn(list.get(59));
//		vo.setKeyword(list.get(60)); //20160617 키워드 데이터 제외
		vo.setKeyword(""); //20160617 키워드 데이터 제외
		vo.setTitle(list.get(61));
		vo.setCastNameMax(list.get(62));
		vo.setCastNameMaxEng(list.get(63));
		vo.setActDispMax(list.get(64));
		vo.setActDispMaxEng(list.get(65));
		vo.setPointOrder(list.get(66));
		vo.setAlbumNo(list.get(67));
		vo.setStillImgName(list.get(68));
		vo.setCpProperty(list.get(69));
		vo.setCpPropertyUfx(list.get(70));
		vo.setThemeYn(list.get(71));
		
		vo.setCatNameChosung(Method.normalizeChosung(vo.getCastName()));
		vo.setAlbumNameChosung(Method.normalizeChosung(vo.getAlbumName()));
		vo.setActorChosung(Method.normalizeHumanChosung(vo.getActor()));
		vo.setOverseerNameChosung(Method.normalizeHumanChosung(vo.getOverseerName()));
		vo.setStarringActorChosung(Method.normalizeHumanChosung(vo.getStarringActor()));
		vo.setVoiceActorChosung(Method.normalizeHumanChosung(vo.getVoiceActor()));
		vo.setCastNameChosung(Method.normalizeHumanChosung(vo.getCastName()));
		if(Constant.TYPE.UFLIX_CATGB_TV.equalsIgnoreCase(vo.getCatGb())){
			vo.setKeywordChosung(Method.normalizeChosung(vo.getKeyword()));
		}
		vo.setCastNameMaxChosung(Method.normalizeHumanChosung(vo.getCastNameMax()));
		vo.setActDispMaxChosung(Method.normalizeHumanChosung(vo.getActDispMax()));
	}*/
	
	
}
