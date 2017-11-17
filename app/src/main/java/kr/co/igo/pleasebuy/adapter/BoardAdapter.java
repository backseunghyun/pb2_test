package kr.co.igo.pleasebuy.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import kr.co.igo.pleasebuy.R;
import kr.co.igo.pleasebuy.model.Notice;
import kr.co.igo.pleasebuy.popup.TwoButtonPopup;
import kr.co.igo.pleasebuy.trunk.api.APIManager;
import kr.co.igo.pleasebuy.trunk.api.APIUrl;
import kr.co.igo.pleasebuy.trunk.api.RequestHandler;
import kr.co.igo.pleasebuy.ui.BoardEditActivity;
import kr.co.igo.pleasebuy.util.CommonUtils;
import kr.co.igo.pleasebuy.util.Preference;

/**
 * Created by Dong on 2015-12-30.
 */
public class BoardAdapter extends BaseExpandableListAdapter {

    private Context context;
    private static Preference preference;

    private List<Notice> mList = new ArrayList<Notice>();
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private Notice m;

    public BoardAdapter(Context context, List<Notice> list) {
        this.context = context;
        this.mList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return this.mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mList.get(groupPosition).getTitle();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mList.get(groupPosition).getContents();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.adapter_board_item_title, parent, false);
            viewHolder.ll_rootlayout = (LinearLayout) convertView.findViewById(R.id.ll_rootlayout);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        m = mList.get(groupPosition);

        viewHolder.tv_title.setText(m.getTitle());
        viewHolder.tv_date.setText(CommonUtils.ConvertDate(m.getRegDate()));
        viewHolder.tv_summary.setText(m.getContents());
        viewHolder.tv_name.setText(m.getWiter() + "님");

        if (isExpanded) {
            viewHolder.tv_summary.setVisibility(View.GONE);
        } else {
            viewHolder.tv_summary.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.adapter_board_item_contents, parent, false);
            viewHolder.tv_contents = (TextView) convertView.findViewById(R.id.tv_contents);
            viewHolder.ll_editView = (LinearLayout) convertView.findViewById(R.id.ll_editView);
            viewHolder.ll_edit = (LinearLayout) convertView.findViewById(R.id.ll_edit);
            viewHolder.ll_delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (preference == null) {
            preference = new Preference();
        }

        viewHolder.tv_contents.setText(mList.get(groupPosition).getContents());
        if (mList.get(groupPosition).getUserID().equals(preference.getStringPreference(Preference.PREFS_KEY.USER_ID))) {
            viewHolder.ll_editView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ll_editView.setVisibility(View.GONE);
        }

        viewHolder.ll_edit.setTag(groupPosition);
        viewHolder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardEditActivity.class);
                intent.putExtra("boardId", mList.get((Integer) v.getTag()).getBoardId());
                context.startActivity(intent);
            }
        });

        viewHolder.ll_delete.setTag(groupPosition);
        viewHolder.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopup(String.valueOf(mList.get((Integer) v.getTag()).getBoardId()), (Integer) v.getTag());
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        LinearLayout ll_rootlayout;
        TextView tv_title;
        TextView tv_date;
        TextView tv_summary;
        TextView tv_name;

        TextView tv_contents;
        LinearLayout ll_editView;
        LinearLayout ll_edit;
        LinearLayout ll_delete;
    }

    private void confirmPopup(final String id, final int pos) {

        final TwoButtonPopup popup = new TwoButtonPopup(context);
        popup.setCancelable(false);
        popup.setTitle(context.getResources().getString(R.string.s_popup_title));
        popup.setContent(context.getResources().getString(R.string.s_ask_board_bbs_delete));
        popup.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(popup.isConfirm()){
                    bbsDelete(id, pos);
                }
            }
        });
        popup.show();
    }

    private void bbsDelete(String id, final int pos){
        RequestParams param = new RequestParams();
        param.put("boardId", id);

        APIManager.getInstance().callAPI(APIUrl.BOARD_BBS_REMOVE, param, new RequestHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("code") == 0) {
                        mList.remove(pos);
                        Toast.makeText(context, "게시글을 삭제 하였습니다.", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, response.getString("errmsg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}