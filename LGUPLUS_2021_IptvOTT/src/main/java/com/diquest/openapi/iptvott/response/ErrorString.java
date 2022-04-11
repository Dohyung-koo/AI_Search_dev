package com.diquest.openapi.iptvott.response;

import com.diquest.openapi.util.Json_Error;
import com.diquest.openapi.util.OpenAPIErrorResponseJson;
import com.diquest.openapi.util.info.ERROR_TYPE;

public class ErrorString {
    public static class GENERAL{
        //일반적인 오류
        public final static String OPERATOR_INTERNAL_ERROR = "OPERATOR_INTERNAL_ERROR";
        public final static String RESPONSE_CODE_UNSPECIFIED = "RESPONSE_CODE_UNSPECIFIED";
    }
    public static class SWITCH_CHANNEL{
        //채널이나 채널ID이 존재하지 않는 경우.
        public final static String CHANNEL_NOT_AVAILABLE = "CHANNEL_NOT_AVAILABLE";
        //사용자가 채널에 대한 권한이 없는 경우.
        public final static String CHANNEL_UNSUBSCRIBED = "CHANNEL_UNSUBSCRIBED";
        //내부 문제
        public final static String OPERATOR_INTERNAL_ERROR = "OPERATOR_INTERNAL_ERROR";
    }
    public static class ENTITY_SEARCH{
        //여러 엔티티 반환될 경우
        public final static String ENTITY_NOT_AVAILABLE_FOR_SEARCH = "ENTITY_NOT_AVAILABLE_FOR_SEARCH";
    }

    public static class SEARCH{
        //결과없음
        public final static String NO_SEARCH_RESULTS_FOUND = "NO_SEARCH_RESULTS_FOUND";
        //내부 문제
        public final static String FEATURE_NOT_SUPPORTED = "FEATURE_NOT_SUPPORTED";
    }
    public static class PLAY_TVM{
        //재생할 수 있는 결과가 없음.
        public final static String ENTITY_NOT_AVAILABLE_FOR_PLAYBACK = "ENTITY_NOT_AVAILABLE_FOR_PLAYBACK";
    }

    public static class PLAY{
        //결과없음
        /*public final static String ENTITY_NOT_AVAILABLE_FOR_RECORDING = "ENTITY_NOT_AVAILABLE_FOR_RECORDING";
        public final static String ENTITY_ALREADY_RECORDED = "ENTITY_ALREADY_RECORDED";
        public final static String ENTITY_NOT_RECORDABLE = "ENTITY_NOT_RECORDABLE";*/

    }

    public static String resultEmpty(String queryIntent){
        if("SWITCH_CHANNEL".equals(queryIntent)){
            return SWITCH_CHANNEL.CHANNEL_NOT_AVAILABLE;
        }else if("PLAY_TVM".equals(queryIntent)){
            return PLAY_TVM.ENTITY_NOT_AVAILABLE_FOR_PLAYBACK;
        }else if("ENTITY_SEARCH".equals(queryIntent)){
            return ENTITY_SEARCH.ENTITY_NOT_AVAILABLE_FOR_SEARCH;
        } else{
            return SEARCH.NO_SEARCH_RESULTS_FOUND;
        }
    }

    public static String makeLgMarinerErrorCode(int rs) {

        if ( rs == -1  ) {
            return "20001000";
        } else if ( rs <-1 && rs > -129) {
            return "4004";
        } else if ( rs == -60003 ) {
            return "L001";
        } else if ( rs == -60004 ) {
            return "4001";
        } else if ( rs == -60005 ) {
            return "4002";
        } else if ( rs == -60006 ) {
            return "4003";
        } else if ( rs == -60011) {
            return "4004";
        } else {
            return "4004";
        }

    }

    public static String makeLgServerErrorCode(String queryIntent) {

        if ( "SWITCH_CHANNEL".equals(queryIntent)  ) {
            return "40004000";
        } else if ( "ENTITY_SEARCH".equals(queryIntent)  ) {
            return "40005000";
        } else if ( "SEARCH".equals(queryIntent)  ) {
            return "40006000";
        } else if ( "PLAY_TVM".equals(queryIntent)  ) {
            return "40007000";
        } else if ( "PLAY".equals(queryIntent)  ) {
            return "40008000";
        } else if ( "EMPTY".equals(queryIntent)  ) {
            return "20001000";
        }  else {
            return "3000S001";
        }

    }
}
