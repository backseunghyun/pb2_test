package kr.co.igo.pleasebuy.trunk.api;

public enum APIUrl {
    PUSH_DEVICE_TEST("/public/push/test"), // 테스트
    PUSH_DEVICE_UPDATE("/public/push/device/update"), // Push Device Id 저장
    USER_TEST("/api/user/test"), // 로그인 쿠키 테스트
    FILE_TEST("/api/public/image"), // 파일 업로드 테스트
    USER_INAPP_BUYREQUEST("/api/user/inapp/buyRequest"), // In App 결제 요청
    USER_INAPP_BUYCOMPLETE("/api/user/inapp/buyCompleteGoogle"), // In App 결제 확인
    USER_XMPP_MUCLIST("/api/user/xmpp/info"), // MUC join 대상, username, password 받아오기


    PUBLIC_LOGIN("/public/login"),   // 로그인
    PUBLIC_LOGOUT("/public/logout"), // 로그아웃
    PUBLIC_AUTO_LOGIN("/public/autoLogin"),  // 자동 로그인
    PASSWORD_MODIFY("/user/password/reset"),   // 비밀번호 변경
    USER_TERMS_OF_USE(""),   // 이용약관
    PUSH_GET("/user/push/get"), // 푸시 알림 가져오기
    PUSH_SET("/user/push/set"), // 푸시 알림 설정



    PRODUCT_SEARCH("/product/search"), // 상품 검색

    CART_INSERT("/cart/add/product"), // 장바구니 추가
    CART_ADD_PRODUCTS("/cart/add/products"), // 장바구니 여러개 추가
    CART_UPDATE("/cart/update"), // 장바구니 수정
    CART_REMOVE("/cart/remove"), // 장바구니 삭제
    CART_ADD_FAVORITE("/cart/add/favorite"), // 즐겨찾기 불러오기

    USER_HISTORY_LIST("/order/list"), // 지난내역 리스트
    USER_HISTORY_DETAIL("/order/detail"), // 지난내역 상세

    USER_STATISTICS("/user/statistics"), // 통계
    USER_NO_COMPLETE("/user/nocomplete"), // 미출내역 -- 사용 안함

    USER_NOTICE_LIST("/board/list"),// 공지사항 리스트
    USER_NOTICE_SELECT("/board/select"),// 공지사항 자세히

    USER_CATEGORY_LIST("/user/category/list"), // 카테고리 리스트

    FAVORITE_LIST("/favorite/list"), // 자주 찾는 상품 리스트
    FAVORITE_INSERT("/favorite/insert"), // 자주 찾는 상품 추가
    FAVORITE_UPDATE("/favorite/update"), // 자주 찾는 상품 수정
    FAVORITE_REMOVE("/favorite/remove"), // 자주 찾는 상품 삭제

    ORDER_ORDER("/order/order"), // 주문
    ORDER_ORDER_DIRECT("/order/order/direct"), // 바로주문
    ORDER_FAVORITE("/order/favorite"), // 즐겨찾기 등록 상품 가져오기
    ORDER_CANCEL("/order/cancel"), // 주문취소

    STORE_INFO("/store/info"), // 가게 정보
    MODIFY_INFORMATION("/store/update"),   // 내정보 수정


    DELIVERER_ORDER_LIST("/deliverer/order/list"), // 오늘 구매 목록 - 리스트
    DELIVERER_ORDER_LIST2("/deliverer/order/list2"), // 오늘 구매 목록 - 리스트2
    DELIVERER_ORDER_DETAIL("/deliverer/order/detail"), // 오늘 구매 목록 - 상품별 주문 내용 확인
    DELIVERER_ORDER_RELEASE("/deliverer/order/release"), // 오늘 구매 목록 - 출고 완료(오늘 배송 끝)
    DELIVERER_ORDER_CLOSED("/deliverer/order/closed"), // 오늘 구매 목록 - 주문 완료(고객에게 message)

    DELIVERER_STORE_LIST("/deliverer/store/list"), // 오늘 배달 목록 - 리스트
    DELIVERER_STORE_DETAIL("/deliverer/store/detail"), // 오늘 배달 목록 - 상세
    DELIVERER_LIST("/deliverer/list"), // 오늘 배달 목록 - 리스트 v2

    DELIVERER_HISTORY_LIST("/deliverer/history/list"), // 지난 주문 내역 - 리스트
    DELIVERER_HISTORY_DETAIL("/deliverer/history/detail"), // 지난 주문 내역 - 상세

    DELIVERER_PRICING_LIST("/deliverer/pricing/list"), // 공시가 리스트
    DELIVERER_PRICING_UPDATE("/deliverer/pricing/update"), // 공시가 수정

    DELIVERER_BRAND_LIST("/deliverer/brand/list"), // 가맹점 리스트
    DELIVERER_BRAND_BRANCH_LIST("/deliverer/brand/branch/list"), // 가맹점 별 호점 리스트

    DELIVERER_COMPLETE("/deliverer/complete"), // 배송완료



    // v2.0
    MAIN("/main"),                          // 메인
    PRODUCT_LIST("/product"),               // 상품 리스트
    CART_ADD_PRODUCT("/cart/add/product"),  // 장바구니 추가
    CART_LIST("/cart/list"),                // 장바구니 리스트
    BOARD_BBS("/board/bbs"),                // 게시판 리스트
    BOARD_BBS_ADD("/board/bbs/add"),        // 게시판 등록
    BOARD_BBS_DETAIL("/board/bbs/detail"),  // 게시판 상제
    BOARD_BBS_UPDATE("/board/bbs/update"),  // 게시판 수정
    BOARD_BBS_REMOVE("/board/bbs/remove"),  // 게시판 삭제
    BOARD_NOTICE("/board/notice"),          // 공지사항
    FAVORITE_GROUP("/favorite-group"),      // 즐겨찾기 그룹
    FAVORITE_GROUP_DETAILS("/favorite-group/details"),      // 즐겨찾기 그룹 상세

    BOARD_NOTICE_DETAIL("/board/notice/detail"),    // 공지사항 상세

    ;

    private String url;

    APIUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }
}
