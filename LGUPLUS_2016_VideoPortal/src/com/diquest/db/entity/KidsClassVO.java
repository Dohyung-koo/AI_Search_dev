package com.diquest.db.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.diquest.app.common.Method;

public class KidsClassVO {
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

	private String section; // 원본데이터에 없지만 genre uxten을 보고 구별해 추가됨

	// 영화 진흥원 데이터
/*	private String koficSupportingActor;
	private String koficExtraActor;
	private String koficSynopsis;
	private String koficKeyword;
	private String koficFilmStudio;
	private String koficFilmDistributor;
	private String koficMakeNation;
	private String koficMakeYear;
	private String koficSumSale;
	private String koficSumAudience;
*/
	// 20160617 초성데이터
	private String catNameChosung;
	private String albumNameChosung;
	private String actorChosung;
	private String overseerNameChosung;
	private String starringActorChosung;
	private String voiceActorChosung;
	private String castNameChosung;
	private String keywordChosung;
	private String titleChosung;
	private String castNameMaxChosung;
	private String actDispMaxChosung;
	
	//Choihu 2018.06.12 컬럼추가 BROAD_DATE_SORT
	private String broadDateSort;

	/*//Choihu 2018.06.25 공연 컬럼 추가
	private String cuesheetType;
	private String actorId;
	private String concertImgUrl;
	private String concertImgFileName;
	private String cuesheetVideoType;*/
/*	private String actorName;
	private String actorNameChosung;*/
	//Choihu 2018.08.08 VIDEO_TYPE 추가
	private String videoType;
	// private String id; // RESULT_TYPE(결과 구분), CAT_GB(카테고리 구분), CAT_ID(카테고리
	// ID), ALBUM_ID(앨범 ID)
	
	//Choihu 2018.10.12 RUN_TIME 추가
	private String run_time;
	//Choihu 2018.11.30 vr_type 추가
	//private String vr_type;	

	private String year;
	private String subTitle;
	private String round;
	//20190219 Choihu
	private String studio;
	private String producer;
	private String actorDisplay;
	
	private String nscGb;
	private String kidsGrade;
	
	
	public void init() {
		if (resultType.equalsIgnoreCase("CALB") == true) {
			if (this.genreUxten.equalsIgnoreCase("애니") == true || this.genreUxten.equalsIgnoreCase("키즈") == true
					|| this.genreUxten.equalsIgnoreCase("TV다시보기") == true || this.genreUxten.equalsIgnoreCase("해외시리즈") == true) {
				this.catName = "";
				this.albumName = "";
				this.actor = "";
				this.starringActor = "";
				this.castName = "";
			}
		}
	}

	
	


	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getKidsGrade() {
		return kidsGrade;
	}

	public void setKidsGrade(String kidsGrade) {
		this.kidsGrade = kidsGrade;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getActorDisplay() {
		return actorDisplay;
	}

	public void setActorDisplay(String actorDisplay) {
		this.actorDisplay = actorDisplay;
	}

	public String getRun_time() {
		return run_time;
	}

	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getBroadDateSort() {
		return broadDateSort;
	}

	public void setBroadDateSort(String broadDateSort) {
		this.broadDateSort = broadDateSort;
	}

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
		int len = description.length();
		//System.out.println(len);
		if(len > 296) {
			description = description.substring(0,296);
		}
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

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

/*	public String getKoficSupportingActor() {
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
*/
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

	public String getTitleChosung() {
		return titleChosung;
	}

	public void setTitleChosung(String titleChosung) {
		this.titleChosung = titleChosung;
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

	public void setAll(KidsClassVO vo, List<String> list) {
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
		vo.setKeyword(list.get(60));
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
		vo.setBroadDateSort(list.get(72));
		vo.setVideoType(list.get(78));
		vo.setRun_time(list.get(79));
		vo.setYear(list.get(81));
		vo.setSubTitle(list.get(82));
		vo.setRound(list.get(83));
		vo.setStudio(list.get(84));
		vo.setProducer(list.get(85));
		vo.setActorDisplay(list.get(86));
		vo.setNscGb(list.get(87));
		vo.setKidsGrade(list.get(88));
		
		// 초성값 체워넣기
		vo.setCatNameChosung(Method.normalizeChosung(list.get(3)));
		vo.setAlbumNameChosung(Method.normalizeChosung(list.get(5)));
		vo.setActorChosung(Method.normalizeHumanChosung(list.get(27)));
		vo.setOverseerNameChosung(Method.normalizeHumanChosung(list.get(28)));
		vo.setStarringActorChosung(Method.normalizeHumanChosung(list.get(38)));
		vo.setVoiceActorChosung(Method.normalizeHumanChosung(list.get(39)));
		vo.setCastNameChosung(Method.normalizeHumanChosung(list.get(53)));
		vo.setKeywordChosung(Method.normalizeChosung(list.get(60)));
		vo.setTitleChosung(Method.normalizeChosung(list.get(61)));
		vo.setCastNameMaxChosung(Method.normalizeHumanChosung(list.get(62)));
		vo.setActDispMaxChosung(Method.normalizeChosung(list.get(64)));

		
		
		
		
		
		// ID 값
		// StringBuilder idBuilder = new StringBuilder();
		// idBuilder.append(list.get(0));
		// idBuilder.append("_");
		// idBuilder.append(list.get(1));
		// idBuilder.append("_");
		// idBuilder.append(list.get(2));
		// idBuilder.append("_");
		// idBuilder.append(list.get(4));
		// vo.setId(idBuilder.toString());

		// Section 값
		/*
		 * String section = ""; String genreUxten = ""; genreUxten = list.get(58);
		 * genreUxten = genreUxten.trim(); genreUxten = genreUxten.replaceAll(" ", "");
		 */

		section = "";

		vo.setSection(section);
	}

	public void setAppend(KidsClassVO vo, List<String> list) {

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
		vo.setFlashYn("");
		vo.setApplId("");
		vo.setServiceGb(list.get(10));
		vo.setApplUrl("");
		vo.setCreateDate(list.get(11));
		vo.setUpdateDate(list.get(12));
		vo.setImgType(list.get(13));
		vo.setPrice(list.get(14));
		vo.setPrInfo(list.get(15));
		vo.setRuntime(list.get(16));
		vo.setIs51Ch(list.get(17));
		vo.setIsHot(list.get(18));
		vo.setIsCaption(list.get(19));
		vo.setIsHd(list.get(20));
		vo.setCloseYn(list.get(21));
		vo.setThreedYn(list.get(22));
		vo.setFilterGb(list.get(23));
		vo.setActor(list.get(24));
		vo.setOverseerName(list.get(25));
		vo.setContsSubname(list.get(26));
		vo.setGenre1(list.get(27));
		vo.setGenre2(list.get(28));
		vo.setGenre3(list.get(29));
		vo.setSeriesNo(list.get(30));
		vo.setPt(list.get(31));
		vo.setDes(list.get(32));
		vo.setPrice2(list.get(33));
		vo.setBroadDate(list.get(34));
		vo.setStarringActor(list.get(35));
		vo.setVoiceActor(list.get(36));
		vo.setBroadcaster(list.get(37));
		vo.setReleaseDate(list.get(38));
		vo.setIsFh(list.get(39));
		vo.setSerCatId(list.get(40));
		vo.setMultiMappingFlag(list.get(41));
		vo.setPosterFileUrl(list.get(42));
		vo.setPosterFileName10(list.get(43));
		vo.setPosterFileName30(list.get(44));
		vo.setKeywordDesc(list.get(45));
		vo.setTitleEng(list.get(46));
		vo.setDirectorEng(list.get(47));
		vo.setPlayerEng(list.get(48));
		vo.setCastNameEng(list.get(49));
		vo.setCastName(list.get(50));
		vo.setTitleOrigin(list.get(51));
		vo.setWriterOrigin(list.get(52));
		vo.setPublicCnt(list.get(53));
		vo.setPointWatcha(list.get(54));
		vo.setGenreUxten(list.get(55));
		vo.setRetentionYn(list.get(56));
		vo.setKeyword(list.get(57));
		vo.setTitle(list.get(58));
		vo.setCastNameMax(list.get(59));
		vo.setCastNameMaxEng(list.get(60));
		vo.setActDispMax(list.get(61));
		vo.setActDispMaxEng(list.get(62));
		vo.setPointOrder(list.get(63));
		vo.setAlbumNo(list.get(64));
		vo.setStillImgName(list.get(65));
		vo.setCpProperty(list.get(66));
		vo.setCpPropertyUfx(list.get(67));
		vo.setThemeYn(list.get(68));
		//Choihu
		vo.setBroadDateSort(list.get(69));
		//Choihu 2018.06.26
/*		vo.setCuesheetType(list.get(70));
		vo.setActorId(list.get(71));
		vo.setConcertImgUrl(list.get(72));
		vo.setConcertImgFileName(list.get(73));
		vo.setCuesheetVideoType(list.get(74));
		//Choihu 20180808*/
		vo.setVideoType(list.get(75));
		//Choihu 20181012 RUN_TIME추가
		vo.setRun_time(list.get(76));
/*		vo.setVr_type(list.get(77));*/
		vo.setYear(list.get(78));
		vo.setSubTitle(list.get(79));
		vo.setRound(list.get(80));
		vo.setStudio(list.get(81));
		vo.setProducer(list.get(82));
		vo.setActorDisplay(list.get(83));
		vo.setNscGb(list.get(84));
		vo.setKidsGrade(list.get(85));
		// 초성값 체워넣기
		vo.setCatNameChosung(Method.normalizeChosung(list.get(3)));
		vo.setAlbumNameChosung(Method.normalizeChosung(list.get(5)));
		vo.setActorChosung(Method.normalizeHumanChosung(list.get(24)));
		vo.setOverseerNameChosung(Method.normalizeHumanChosung(list.get(25)));
		vo.setStarringActorChosung(Method.normalizeHumanChosung(list.get(35)));
		vo.setVoiceActorChosung(Method.normalizeHumanChosung(list.get(36)));
		vo.setCastNameChosung(Method.normalizeHumanChosung(list.get(50)));
		vo.setKeywordChosung(Method.normalizeChosung(list.get(57)));
		vo.setTitleChosung(Method.normalizeChosung(list.get(58)));
		vo.setCastNameMaxChosung(Method.normalizeHumanChosung(list.get(59)));
		vo.setActDispMaxChosung(Method.normalizeChosung(list.get(61)));

		// Section 값
		//String section = "";
/*		String genreUxten = "";
		genreUxten = list.get(55);
		genreUxten = genreUxten.trim();
		genreUxten = genreUxten.replaceAll(" ", "");
	*/
		
		vo.setSection("");
	}
}
