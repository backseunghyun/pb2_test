package kr.co.igo.pleasebuy.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.SelectPhotoPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseActivity;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.BackPressCloseSystem;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.Dir;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by baekseunghyun on 15/10/2017.
 */

public class RequestAddProductEditActivity extends BaseActivity {
    @Bind(R.id.iv_back)     ImageView iv_back;
    @Bind(R.id.tv_title)    TextView tv_title;
    @Bind(R.id.rl_add)      RelativeLayout rl_add;
    @Bind(R.id.iv_image)    ImageView iv_image;
    @Bind(R.id.iv_add)      ImageView iv_add;
    @Bind(R.id.et_name)     EditText et_name;
    @Bind(R.id.et_unit)     EditText et_unit;
    @Bind(R.id.et_etc)      EditText et_etc;
    @Bind(R.id.tv_count)    TextView tv_count;
    @Bind(R.id.rl_cart)     RelativeLayout rl_cart;
    @Bind(R.id.tv_save)     TextView tv_save;

    public static final int ACTION_PICK_FROM_GALLERY = 0;
    public static final int ACTION_PICK_FROM_CAMERA = 1;

    private Uri mImageUri;
    private String absolutePath1, absolutePath2;
    private String path;
    public Preference preference;
    private BackPressCloseSystem backPressCloseSystem;
    private int boardId;
    private String name;
    private String unit;
    private String etc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add_product_edit);
        ButterKnife.bind(this);

        preference = new Preference();
        backPressCloseSystem = new BackPressCloseSystem(this);

        tv_title.setText(getResources().getString(R.string.s_menu_request_add_product));
        name = "";
        unit = "";
        etc = "";

        if(getIntent().hasExtra("boardId")) {
            boardId = getIntent().getIntExtra("boardId",0);
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        checkData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartCount(preference.getIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART));
    }

    @OnClick({R.id.iv_back, R.id.tv_cancel, R.id.tv_save, R.id.rl_add})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.tv_cancel:
                checkData();
                break;
            case R.id.tv_save:
                if (boardId == 0) {
                    add();
                } else {
                    update();
                }
                break;
            case R.id.rl_add:
                PermissionListener permissionListener1 = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        final SelectPhotoPopup photoPopup = new SelectPhotoPopup(RequestAddProductEditActivity.this);
                        photoPopup.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                switch (photoPopup.getResult()){
                                    case "new" :
                                        captureImage(ACTION_PICK_FROM_CAMERA);
                                        break;
                                    case "gallery":
                                        pickGallery(ACTION_PICK_FROM_GALLERY);
                                        break;
                                    case "del":
                                        path = null;
                                        iv_add.setVisibility(View.VISIBLE);
                                        break;
                                    case "cancel":
                                        break;
                                }
                            }
                        });
                        photoPopup.show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {

                    }
                };

                new TedPermission(this)
                        .setPermissionListener(permissionListener1)
                        .setDeniedMessage("앨범 접근을 거절하시면 서비스를 이용 할 수 없습니다.\n설정 변경 부탁드립니다.")
                        .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        .check();
                break;
        }
    }

    public void checkData(){
        if (boardId > 0 && (
            !et_name.getText().toString().equals(name) ||
            !et_unit.getText().toString().equals(unit) ||
            !et_etc.getText().toString().equals(etc)) ) {
            showConfirm();
        } else {
            finish();
        }
    }

    private void showConfirm() {
        final TwoButtonPopup popup = new TwoButtonPopup(this);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(getResources().getString(R.string.s_ask_request_add_finish));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    finish();
                }
            }
        });
        popup.show();
    }

    private void getData() {
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);

        APIManager.getInstance().callAPI(APIUrl.BOARD_QNA_DETAIL, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {

                        JSONObject item = response.getJSONObject("item");
                        name = item.optString("title");
                        unit = item.optString("contents");
                        etc = item.optString("contents");

                        et_name.setText(name);
                        et_unit.setText(unit);
                        et_etc.setText(etc);
                        Glide.with(RequestAddProductEditActivity.this)
                                .load(ApplicationData.getImgPrefix() + item.optString("imageUrl"))
                                .centerCrop()
                                .into(iv_image);
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void add() {
        RequestParams param = new RequestParams();
        param.put("title", et_name.getText().toString());
        param.put("contents", et_unit.getText().toString());
        param.put("contents", et_etc.getText().toString());

        if (path != null) {
            final File file = new File(path);
            try {
                param.put("img", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        param.setForceMultipartEntityContentType(true);

        APIManager.getInstance().callAPI(APIUrl.BOARD_QNA_ADD, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSuccess(getResources().getString(R.string.s_request_add_success));
                    } else {
                        Toast.makeText(RequestAddProductEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update() {
        RequestParams param = new RequestParams();
        param.put("boardId", boardId);
        param.put("title", et_name.getText().toString());
        param.put("contents", et_unit.getText().toString());
        param.put("contents", et_etc.getText().toString());
        if (path != null) {
            final File file = new File(path);
            try {
                param.put("img", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        param.setForceMultipartEntityContentType(true);

        APIManager.getInstance().callAPI(APIUrl.BOARD_QNA_UPDATE, param, new RequestHandler(this, uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        showSuccess(getResources().getString(R.string.s_request_update_success));
                    } else {
                        Toast.makeText(RequestAddProductEditActivity.this, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showSuccess(String msg) {
        ConfirmPopup popup = new ConfirmPopup(this);
//        notiPopup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_confirm));
        popup.setContent(msg);
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        popup.show();
    }

    private void setCartCount(int num){
        if (num > 0) {
            tv_count.setText(num + "");
            tv_count.setVisibility(View.VISIBLE);
            rl_cart.setEnabled(false);
        } else {
            tv_count.setText("");
            tv_count.setVisibility(View.GONE);
            rl_cart.setEnabled(true);
        }
        preference.setIntPreference(Preference.PREFS_KEY.CNT_PRODUCT_IN_CART, num);
    }

    /**
     * 사진 촬영
     */
    private void captureImage(int i) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        mImageUri = Uri.fromFile(Dir.getPublicTempImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, i);
    }

    /**
     * 갤러리 호출
     */
    private void pickGallery(int i) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ACTION_PICK_FROM_CAMERA:
                    if (null != mImageUri) {
                        String tPath = CommonUtils.getPathFromUri(this, mImageUri);
                        if (StringUtils.isNotBlank(tPath)) {
                            absolutePath1 = tPath;
                            getImage(absolutePath1);
                        } else {
                            //잘못된 이미지
                        }
                    }
                    break;
                case ACTION_PICK_FROM_GALLERY:
                    Uri imageUri = data.getData();
                    absolutePath2 = Dir.getRealPathFromURI(this, imageUri);
                    getImage(absolutePath2);
                    break;
            }
        }
    }

    private void setImage(ImageView v, String s) {
        Glide.with(this).load(s).into(v);
        iv_add.setVisibility(View.GONE);
    }

    private void getImage(String absolutePath){
        try {
            int imageWidth = CommonUtils.pxToDp(this, (int)(getResources().getDimension(R.dimen.image_width)));
            Bitmap image_bitmap = Dir.decodeScaledBitmap(absolutePath, imageWidth, imageWidth);

            ExifInterface exif = new ExifInterface(absolutePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix m = new Matrix();
            Bitmap bitmap = null;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                m.postRotate(180);
                bitmap = Bitmap.createBitmap(image_bitmap, 0, 0, image_bitmap.getWidth(),
                        image_bitmap.getHeight(), m, true);

            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                bitmap = Bitmap.createBitmap(image_bitmap, 0, 0, image_bitmap.getWidth(),
                        image_bitmap.getHeight(), m, true);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                bitmap = Bitmap.createBitmap(image_bitmap, 0, 0, image_bitmap.getWidth(),
                        image_bitmap.getHeight(), m, true);
            } else {
                bitmap = image_bitmap;
            }

            if (bitmap != null) {
                long now = System.currentTimeMillis();
                bitmap = Dir.resizeBitmapImage(bitmap, 1080);
                path = CommonUtils.getPathSavedBitmapToJpeg(bitmap, "IGO/pb", "ImageFile_" + now);
                setImage(iv_image, path);
                bitmap.recycle();
                image_bitmap.recycle();
            } else {
                throw new FileNotFoundException();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
