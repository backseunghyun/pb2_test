package kr.co.igo.pleasebuy.util;

/**
 * Created by Back on 2016-05-12.
 */
public enum FragmentName {
    ORDER_STEP_3(12,""),    // 주문 1단계
    ORDER_STEP_2(11,""),    // 주문 1단계
    ORDER_STEP_1(10,""),    // 주문 1단계

    SETTING(9,"설정"),    // 설정
    REPORT(8,"월간리포트"),    // 월간리포트
    NOTI(7,"공지사항"),    // 공지사항
    BOARD(6,"게시판"),           // 게시판
    HISTORY(5, "주문 히스토리"),      // 주문 히스토리
    GRAPH(4, "통계"),              // 통계
    ADD(3, "상품추가 요청"),         // 상품추가 요청
    FAVORITE(2, "즐겨찾기"),         // 즐겨찾기
    ORDER(1, "주문하기"),   // 주문하기
    HOME(0, "홈");                    // 홈


    int mValue;
    String mTag;

    FragmentName(int value, String tag) {
        mValue = value;
        mTag = tag;
    }

    public int value() {
        return this.mValue;
    }
    public String tag() {return this.mTag;}
}
