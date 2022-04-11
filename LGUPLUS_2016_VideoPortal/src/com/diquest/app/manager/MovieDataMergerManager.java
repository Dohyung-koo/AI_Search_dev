package com.diquest.app.manager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diquest.app.common.Constant;
import com.diquest.app.common.Method;
import com.diquest.db.MovieKoficDB;
import com.diquest.db.MovieSumInfoDB;
import com.diquest.db.entity.I30VO;
import com.diquest.db.entity.MovieKoficVO;
import com.diquest.db.entity.MovieSumInfoVO;
import com.diquest.db.entity.UflixPackageVO;
import com.diquest.db.entity.UflixSeriesVO;
import com.diquest.db.entity.VodVO;

public class MovieDataMergerManager {
	
	private MovieKoficDB movieKofic;
	private MovieSumInfoDB movieSumInfo;
	
	private List<MovieKoficVO> movieKoficList;
	private Map<String, MovieSumInfoVO> movieSumInfoMap;
	private Map<String, Integer> movieIdMap;
	private Map<String, List<String>> movieDirectorMap;
	
	private boolean matchTitle;
	private boolean matchDirector;
	private boolean matchPerfact ;
	
	public MovieDataMergerManager(Connection conn){
		this.movieIdMap = new HashMap<String, Integer>();
		this.movieDirectorMap = new HashMap<String, List<String>>();
		this.movieKofic = new MovieKoficDB(Constant.DB_NAME.MOVIE_KOFIC, true);
		this.movieSumInfo = new MovieSumInfoDB(Constant.DB_NAME.MOVIE_SUM_INFO, true);
		openSelectPstmt(conn);
		setKoficData();
		closeSelectPstmt();
	}
	
	/**
	 * 영화 진흥원 데이터를 메모리에 가지고 있는다.
	 */
	public void setKoficData(){
		this.movieKoficList =  selectMovieKofic();
		this.movieSumInfoMap = selectMovieSumInfo();
		
		int idx=0;
		for(MovieKoficVO vo : movieKoficList){
			String title = vo.getNameKo();
			String directors = vo.getDirector();
			
			title = Method.normalizeTitle(title);
			movieIdMap.put(title, idx);
			idx++;
			
			if(directors == null){
				List<String> dList = new ArrayList<String>();
				movieDirectorMap.put(title, dList);
				continue;
			}
			
			String[] directorToks = (directors.contains(",")) ? directors.split(",") : new String[]{directors};
			List<String> dList = new ArrayList<String>();
			for(String director : directorToks){
				director = Method.normalizeDirector(director);
				dList.add(director);
			}
			movieDirectorMap.put(title, dList);
		}
	}
	/**
	 * 	
	 * WAY 2방식으로 진행
	 * 
		WAY 1 부분일치가 있을경우 
		영화 제목이 정확히 일치 안하더라도 matchTitle & matchDirector 두개 true 면 데이터를 넣어준다 
		ex)	koficDATA - title:더 저지 director:데이빗 돕킨
				VodDATA - title:로버트 다우니 주니어의 더 저지 director:데이빗 돕킨
		
		WAY 2 완전일치할경우만 
		시리즈 물에서 감독이 같은 영화중 간혹 2데이터가 없는데  1데이터가 contains 되는경우.. 잘못된 데이터를 체워 넣는다
		ex) 
			koficDATA - title:매드맥스 director:조지 밀러
			VodDATA - title:매드맥스 2 director:조지 밀러
		
		
	 * @param title
	 * @param directors
	 * @return
	 */
	private int compareData(String title, String directors){
		int movieIdx = 0;
		matchTitle = false;
		matchDirector = false;
		matchPerfact = false;
		List<String> koficDirectorList = null; 
		
		String[] vodDirectorToks = (directors != null && directors.contains(",")) ? directors.split(",") : new String[]{directors};
		
		/*
		 * 영화 진흥원데이터가  비교대상데이터에 포함되는지를 확인해야한다.
		 * 영화진흥원 모든 데이터의 key값 (영화제목을) 반복하여 찾는다.
		 * 
		 * koficDATA : 매드맥스
		 * VodDATA : 설날특집매드맥스2부작
		 */
		for(String koficTitle : movieIdMap.keySet()){
			if(!"".equals(koficTitle) && title.contains(koficTitle)){
				matchTitle = true;
				koficDirectorList = movieDirectorMap.get(koficTitle);
				movieIdx = movieIdMap.get(koficTitle);
				
				for(String vodDirector : vodDirectorToks){
					vodDirector = Method.normalizeDirector(vodDirector); 
					for(String koficDirector : koficDirectorList){
						if((vodDirector.contains(koficDirector))){
							matchDirector = true;
							break;
						}
					}
				}
			}
			
			//영진원 제목이 포함되있고,  영진원 감독이 포함되있고, 정확히 영화제목이 일치하면 탐색을 중지
			if(matchTitle && matchDirector && title.equals(koficTitle)){
				matchPerfact = true;
				break;
			}
		}
		return movieIdx;
	}
	
	
	/**
	 * VodVO 에 영화진흥원 데이터를 set 해준다.
	 * @param vo
	 */
	public void updateVod(VodVO vo){
		
		if(!"영화".equals(vo.getGenreUxten()))
			return;
		
		String vodTitle = Method.normalizeTitle(vo.getAlbumName());
		String vodDirectors = vo.getOverseerName();
		int movieIdx=compareData(vodTitle, vodDirectors);
		
//		if(matchTitle && matchDirector){
		if(matchTitle && matchDirector && matchPerfact){
			MovieKoficVO movieKoficVO = movieKoficList.get(movieIdx);
			
			vo.setKoficSupportingActor(movieKoficVO.getActor02());
			vo.setKoficExtraActor(movieKoficVO.getActor03());
			vo.setKoficSynopsis(movieKoficVO.getSynopsis());
//			vo.setKoficKeyword(movieKoficVO.getNameKo());
			vo.setKoficKeyword("");
			vo.setKoficFilmStudio(movieKoficVO.getFilmStudio());
			vo.setKoficFilmDistributor(movieKoficVO.getFilmDistributors());
			vo.setKoficMakeNation(movieKoficVO.getMakeNation());
			vo.setKoficMakeYear(Method.normalizeYear(movieKoficVO.getMakeYear()));
			
			MovieSumInfoVO movieSumInfoVO = movieSumInfoMap.get(movieKoficVO.getCode());
			if(movieSumInfoVO != null){
				vo.setKoficSumSale(Method.normalizeMovieNumberData(movieSumInfoVO.getSumSale()));
				vo.setKoficSumAudience(Method.normalizeMovieNumberData(movieSumInfoVO.getSumAudience()));
			}
			
		}
	}
	
	/**
	 * UFLIX  에 영화진흥원 데이터를 set 해준다.
	 * @param vo
	 *///Choihu 20180806 UflixPackageVO -> VodVO 변경
	public void updateUflixPackage(UflixPackageVO vo){
		if(!"영화".equals(vo.getGenreUxten()))
			return;
		
		String vodTitle = Method.normalizeTitle(vo.getAlbumName());
		String vodDirectors = vo.getOverseerName();
		int movieIdx=compareData(vodTitle, vodDirectors);
		
		if(matchTitle && matchDirector && matchPerfact){
			MovieKoficVO movieKoficVO = movieKoficList.get(movieIdx);
			vo.setKoficSupportingActor(movieKoficVO.getActor02());
			vo.setKoficExtraActor(movieKoficVO.getActor03());
			vo.setKoficSynopsis(movieKoficVO.getSynopsis());
//			vo.setKoficKeyword(movieKoficVO.getNameKo());
			vo.setKoficKeyword("");
			vo.setKoficFilmStudio(movieKoficVO.getFilmStudio());
			vo.setKoficFilmDistributor(movieKoficVO.getFilmDistributors());
			vo.setKoficMakeNation(movieKoficVO.getMakeNation());
			vo.setKoficMakeYear(Method.normalizeYear(movieKoficVO.getMakeYear()));
			
			MovieSumInfoVO movieSumInfoVO = movieSumInfoMap.get(movieKoficVO.getCode());
			if(movieSumInfoVO != null){
				vo.setKoficSumSale(Method.normalizeMovieNumberData(movieSumInfoVO.getSumSale()));
				vo.setKoficSumAudience(Method.normalizeMovieNumberData(movieSumInfoVO.getSumAudience()));
			}
			
		}
	}
	
	/**
	 * 영화데이터는 20160519 현재 없는걸로 추정된다
	 * 주로 미드 자료가 많치만 구현해 두는걸로..
	 * 
	 * @param vo
	 */
	public void updateUflixSeries(UflixSeriesVO vo){
		if(!"영화".equals(vo.getGenreUxten()))
			return;
		
		String vodTitle = Method.normalizeTitle(vo.getName());
		String vodDirectors = vo.getOverseerName();
		int movieIdx=compareData(vodTitle, vodDirectors);
		
		if(matchTitle && matchDirector && matchPerfact){
			MovieKoficVO movieKoficVO = movieKoficList.get(movieIdx);
			vo.setKoficSupportingActor(movieKoficVO.getActor02());
			vo.setKoficExtraActor(movieKoficVO.getActor03());
			vo.setKoficSynopsis(movieKoficVO.getSynopsis());
//			vo.setKoficKeyword(movieKoficVO.getNameKo());
			vo.setKoficKeyword("");
			vo.setKoficFilmStudio(movieKoficVO.getFilmStudio());
			vo.setKoficFilmDistributor(movieKoficVO.getFilmDistributors());
			vo.setKoficMakeNation(movieKoficVO.getMakeNation());
			vo.setKoficMakeYear(Method.normalizeYear(movieKoficVO.getMakeYear()));
			
			MovieSumInfoVO movieSumInfoVO = movieSumInfoMap.get(movieKoficVO.getCode());
			if(movieSumInfoVO != null){
				vo.setKoficSumSale(Method.normalizeMovieNumberData(movieSumInfoVO.getSumSale()));
				vo.setKoficSumAudience(Method.normalizeMovieNumberData(movieSumInfoVO.getSumAudience()));
			}
			
		}
	}
	

	/**
	 * VodVO 에 영화진흥원 데이터를 set 해준다.
	 * @param vo
	 */
	public void updateI30(I30VO vo){
		
		if(!"영화".equals(vo.getGenreUxten()))
			return;
		
		String vodTitle = Method.normalizeTitle(vo.getAlbumName());
		String vodDirectors = vo.getOverseerName();
		int movieIdx=compareData(vodTitle, vodDirectors);
		
//		if(matchTitle && matchDirector){
		if(matchTitle && matchDirector && matchPerfact){
			MovieKoficVO movieKoficVO = movieKoficList.get(movieIdx);
			
			vo.setKoficSupportingActor(movieKoficVO.getActor02());
			vo.setKoficExtraActor(movieKoficVO.getActor03());
			vo.setKoficSynopsis(movieKoficVO.getSynopsis());
//			vo.setKoficKeyword(movieKoficVO.getNameKo());
			vo.setKoficKeyword("");
			vo.setKoficFilmStudio(movieKoficVO.getFilmStudio());
			vo.setKoficFilmDistributor(movieKoficVO.getFilmDistributors());
			vo.setKoficMakeNation(movieKoficVO.getMakeNation());
			vo.setKoficMakeYear(Method.normalizeYear(movieKoficVO.getMakeYear()));
			
			MovieSumInfoVO movieSumInfoVO = movieSumInfoMap.get(movieKoficVO.getCode());
			if(movieSumInfoVO != null){
				vo.setKoficSumSale(Method.normalizeMovieNumberData(movieSumInfoVO.getSumSale()));
				vo.setKoficSumAudience(Method.normalizeMovieNumberData(movieSumInfoVO.getSumAudience()));
			}
			
		}
	}
	
	

	public void openSelectPstmt(Connection conn){
		movieKofic.createSelectPstmt(conn);
		movieSumInfo.createSelectPstmt(conn);
	}
	
	public void closeSelectPstmt(){
		movieKofic.closeSelectPstmt();
		movieSumInfo.closeSelectPstmt();
	}
	
	public List<MovieKoficVO> selectMovieKofic(){
		return movieKofic.select();
	}
	
	public Map<String, MovieSumInfoVO> selectMovieSumInfo(){
		return movieSumInfo.select();
	}
	
}
