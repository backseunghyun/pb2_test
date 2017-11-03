package kr.co.igo.pleasebuy.model;

/**
 * Created by 10wonders on 2017-11-03.
 */

public class Favorite {
    private int favoriteGroupId;
    private String name;
    private String productNames;
    private Long regDate;
    private Long updateDate;

    public int getFavoriteGroupId() {
        return favoriteGroupId;
    }

    public void setFavoriteGroupId(int favoriteGroupId) {
        this.favoriteGroupId = favoriteGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
}
