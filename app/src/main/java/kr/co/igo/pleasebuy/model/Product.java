package kr.co.igo.pleasebuy.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Back on 2017-02-20.
 */
public class Product implements Parcelable {
    private int selectedCount;
    private String imgUrl;
    private boolean isChecked;
    private String productId;
    private String origin;
    private Long regDate;
    private String productName;
    private String pricingId;
    private String manufacturer;
    private String unit;
    private String price;
    private String categoryId;
    private String totalPrice;
    private String quantity;
    private String cartId;
    private String favoriteId;
    private boolean selected;
    private int isInCart;
    private String categoryValue;


    @Override
    public int describeContents() {
        return 0;
    }

    public Product() {
        this.selectedCount = 0;
    }

    public Product(Parcel in) {
        selectedCount = in.readInt();
        imgUrl = in.readString();
        isChecked = in.readInt() == 1;
        productId = in.readString();
        origin = in.readString();
//        regDate = in.readLong();
        productName = in.readString();
        pricingId = in.readString();
        manufacturer = in.readString();
        unit = in.readString();
        price = in.readString();
        categoryId = in.readString();
        totalPrice = in.readString();
        quantity = in.readString();
        cartId = in.readString();
        favoriteId = in.readString();
        selected = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(selectedCount);
        dest.writeString(imgUrl);
        dest.writeInt(isChecked ? 1 : 0);
        dest.writeString(productId);
        dest.writeString(origin);
//        dest.writeLong(regDate);
        dest.writeString(productName);
        dest.writeString(pricingId);
        dest.writeString(manufacturer);
        dest.writeString(unit);
        dest.writeString(price);
        dest.writeString(categoryId);
        dest.writeString(totalPrice);
        dest.writeString(quantity);
        dest.writeString(cartId);
        dest.writeString(favoriteId);
        dest.writeInt(selected ? 1 : 0);

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(int isInCart) {
        this.isInCart = isInCart;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }
}
