package kr.co.igo.pleasebuy.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.adapter.OrderHistoryAdapter;
import kr.co.igo.pleasebuy.adapter.OrderHistoryDateAdapter;
import kr.co.igo.pleasebuy.adapter.OrderStep3Adapter;
import kr.co.igo.pleasebuy.model.OrderHistoryDate;
import kr.co.igo.pleasebuy.model.Product;
import kr.co.igo.pleasebuy.popup.ConfirmPopup;
import kr.co.igo.pleasebuy.popup.CustomYearMonthPickerPopup;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.BaseFragment;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.MainActivity;
import kr.co.igo.pleasebuy.util.ApplicationData;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.FragmentName;

/**
 * Created by baekseunghyun on 09/10/2017.
 */

public class OrderHistoryFragment extends BaseFragment {
    @Bind(R.id.rv_date_list) RecyclerView rv_date_list;
    @Bind(R.id.lv_list) ListView lv_list;
    @Bind(R.id.tv_month)                TextView tv_month;
    @Bind(R.id.tv_reference_date)       TextView tv_reference_date;
    @Bind(R.id.tv_totalOfOrderPrice)    TextView tv_totalOfOrderPrice;
    @Bind(R.id.tv_cntProductInCart)     TextView tv_cntProductInCart;
    @Bind(R.id.tv_date)                 TextView tv_date;
    @Bind(R.id.tv_storeName)            TextView tv_storeName;
    @Bind(R.id.tv_address)              TextView tv_address;
    @Bind(R.id.tv_etc)                  TextView tv_etc;
    @Bind(R.id.rb_order)                RadioButton rb_order;
    @Bind(R.id.rb_delivery)             RadioButton rb_delivery;
    @Bind(R.id.rl_order_cancel)         RelativeLayout rl_order_cancel;

    private OrderHistoryAdapter mAdapter;
    private List<Product> mList = new ArrayList<Product>();

    private OrderHistoryDateAdapter nAdapter;
    private List<OrderHistoryDate> nList = new ArrayList<OrderHistoryDate>();
    private LinearLayoutManager mLayoutManager;
    private Date cDate;
    private String yearMonth;
    private String seletedDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
    private SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd");
    private SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM");


    public OrderHistoryFragment()  {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new OrderHistoryAdapter(getActivity(), mList);
        lv_list.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_date_list.setLayoutManager(mLayoutManager);
        nAdapter = new OrderHistoryDateAdapter(getActivity(), nList);
        rv_date_list.setAdapter(nAdapter);

        setInit();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.rl_order_cancel, R.id.rb_order, R.id.rb_delivery, R.id.iv_date, R.id.tv_reference_date, R.id.ll_month})
    public void OnClick(View v){
        switch (v.getId()) {
            case R.id.rl_order_cancel:
                orderCancelPopup();
                break;
            case R.id.rb_order:
                rb_order.setChecked(true);
                rb_delivery.setChecked(false);
                getMonthlyOrderStatusList();
                break;
            case R.id.rb_delivery:
                rb_order.setChecked(false);
                rb_delivery.setChecked(true);
                getMonthlyOrderStatusList();
                break;
            case R.id.iv_date:
            case R.id.tv_reference_date:

                break;
            case R.id.ll_month:
                showSelectMonth();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setHederTitle(FragmentName.HISTORY.tag());
        }
    }

    public void setInit(){
        long now = System.currentTimeMillis();
        cDate = new Date(now);
        tv_month.setText(sdf.format(cDate) + "월");
        yearMonth = sdf4.format(cDate);

        rb_order.setChecked(true);
        getMonthlyOrderStatusList();
    }

    public void getMonthlyOrderStatusList(){
        RequestParams param = new RequestParams();
        param.put("yearMonth", yearMonth);
        param.put("kind", rb_order.isChecked() ? "order" : "delivery");

        APIManager.getInstance().callAPI(APIUrl.ORDER_HISTORY, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        nList.clear();

                        JSONArray jsonArray = response.getJSONArray("monthlyOrderStatusList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                OrderHistoryDate item = new OrderHistoryDate();
                                item.setDate(obj.optString("date").split("-")[2]);
                                item.setFulldate(obj.optString("date"));
                                item.setSelected(false);
                                item.setStatus(obj.optString("status"));

                                if (item.getStatus().equals("배송완료")) {
                                    item.setActivated(false);
                                } else if (item.getStatus().equals("배송중")) {
                                    item.setActivated(true);
                                } else if (item.getStatus().equals("주문접수") || item.getStatus().equals("주문중")) {
                                    item.setActivated(true);
                                } else {
                                    item.setActivated(false);
                                }

                                nList.add(item);
                            }
                        }
                    }
                } catch (JSONException ignored) {
                } finally {
                    if (nList.size() > 0) {
                        OrderHistoryDate date = nList.get(nList.size() - 1);

                        date.setSelected(true);
                        orderHistoryChangeData(date);
                        seletedDate = date.getFulldate();

                        tv_reference_date.setText(seletedDate);

                        nAdapter.notifyDataSetChanged();
                        rv_date_list.scrollToPosition(nAdapter.getItemCount() - 1);

                        getData(seletedDate);


                    } else {
                        nAdapter.notifyDataSetChanged();
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void getData(String date) {
        RequestParams param = new RequestParams();
        param.put("date", date);
        param.put("kind", rb_order.isChecked() ? "order" : "delivery");

        APIManager.getInstance().callAPI(APIUrl.ORDER_HISTORY_DETAIL, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.clear();

                        JSONArray jsonArray = response.getJSONArray("orderDetailList");
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            if (obj != null) {
                                Product item = new Product();
                                item.setCartId(obj.optString("cartId"));
                                item.setProductId(obj.optString("productId"));
                                item.setProductName(obj.optString("productName"));
                                item.setUnit(obj.optString("unit"));
                                item.setPrice(obj.optString("price"));
                                item.setCategoryId(obj.optString("categoryId"));
                                item.setIsInCart(obj.optInt("isInCart"));
                                item.setImgUrl(ApplicationData.getImgPrefix() + obj.optString("url"));

                                item.setManufacturer(obj.optString("manufacturer"));
                                item.setOrigin(obj.optString("origin"));

                                item.setCategoryValue(obj.optString("categoryValue"));
                                item.setQuantity(obj.optString("quantity"));
                                item.setSelectedCount(obj.optInt("quantity"));

                                mList.add(item);
                            }
                        }

                        tv_date.setText("");
                        tv_storeName.setText("");
                        tv_address.setText("");
                        tv_etc.setText("");
                        tv_cntProductInCart.setText("");
                        tv_totalOfOrderPrice.setText("");

                        JSONObject obj = response.getJSONObject("orderInfo");
                        tv_date.setText(obj.optString("deliveryReqDate").equals("") ? "" : obj.optString("deliveryReqDate"));
                        tv_storeName.setText(obj.optString("ownerName"));
                        tv_address.setText(obj.optString("address"));
                        tv_etc.setText(obj.optString("etc"));
                        tv_cntProductInCart.setText(obj.optInt("totalQuantity", 0) + "개");
                        tv_totalOfOrderPrice.setText(CommonUtils.getNumberThreeEachFormatWithWon(obj.optInt("totalPrice", 0)));

                        seletedDate = CommonUtils.ConvertDate(obj.optLong("regDate"), "-");
                    }
                } catch (JSONException ignored) {
                } finally {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void orderCancel(String date) {
        RequestParams param = new RequestParams();
        param.put("date", date);

        APIManager.getInstance().callAPI(APIUrl.ORDER_CANCEL_DATE, param, new RequestHandler(getActivity(), uuid) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        orderCancelSuccessPopup();
                    }
                } catch (JSONException ignored) {
                }
            }
        });
    }

    private void orderCancelPopup() {
        final TwoButtonPopup popup = new TwoButtonPopup(getActivity());
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_popup_title));
        popup.setContent(getResources().getString(R.string.s_ask_order_cancel));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    orderCancel(seletedDate);
                }
            }
        });
        popup.show();
    }

    private void orderCancelSuccessPopup() {
        final ConfirmPopup popup = new ConfirmPopup(getActivity());
        popup.setCancelable(false);
        popup.setTitle(getResources().getString(R.string.s_popup_title));
        popup.setContent(getResources().getString(R.string.s_ask_order_cancel_success));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getMonthlyOrderStatusList();
            }
        });
        popup.show();
    }

    public void orderHistoryChangeData(OrderHistoryDate date){
        seletedDate = date.getFulldate();
        getData(seletedDate);

        tv_reference_date.setText(date.getFulldate());

        if(date.getStatus().equals("주문접수")) {
            rl_order_cancel.setEnabled(true);
        } else {
            rl_order_cancel.setEnabled(false);
        }
    }

    private void showSelectMonth(){
        final CustomYearMonthPickerPopup popup = new CustomYearMonthPickerPopup(getActivity(),  tv_reference_date.getText().toString().substring(0, 4), tv_reference_date.getText().toString().substring(5, 7));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(popup.getResult().equals("ok")) {
                    yearMonth = popup.getResultMonth("-");
                    getMonthlyOrderStatusList();
                }
            }
        });
        popup.show();
    }
}
