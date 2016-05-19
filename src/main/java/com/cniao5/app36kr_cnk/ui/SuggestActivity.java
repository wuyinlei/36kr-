package com.cniao5.app36kr_cnk.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.adapter.SuggestTypeListAdapter;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.squareup.okhttp.Request;


public class SuggestActivity extends Activity {
	private Button top_bar_linear_back; // 返回
	private TextView top_bar_title; // 标题
	private LinearLayout layout_type;
	private ImageView img_type;
	private TextView tv_type;
	private EditText et_suggest;// 建议内容
	private EditText et_phone;// 联系方式
	private Button bt_confirm;// 提交
	private String mTitle; // 意见反馈
	private String[] types;
	private String type;
	private PopupWindow typeWindow;
	private ProgressDialog pDialog;
	private HashMap<String, String> params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_layout);
		top_bar_linear_back=(Button)this.findViewById(R.id.btn_back);
		top_bar_title=(TextView)this.findViewById(R.id.tv_title);
		layout_type=(LinearLayout)this.findViewById(R.id.linear_question_classify);
		img_type=(ImageView)this.findViewById(R.id.img_suggest_type);
		tv_type=(TextView)this.findViewById(R.id.tv_suggest_type);
		et_suggest=(EditText)this.findViewById(R.id.et_suggest_content);
		et_phone=(EditText)this.findViewById(R.id.et_suggest_phone);
		bt_confirm=(Button)this.findViewById(R.id.btn_confirm);
		// 设置信息
		top_bar_title.setText(mTitle);
		types = getResources().getStringArray(R.array.array_suggest_type);
		top_bar_linear_back.setOnClickListener(new MyOnClickListener());
		bt_confirm.setOnClickListener(new MyOnClickListener());
		layout_type.setOnClickListener(new MyOnClickListener());
	}

	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				SuggestActivity.this.finish();
				break;
			case R.id.linear_question_classify:
				if (typeWindow == null) {
					typeWindow = new PopupWindow(SuggestActivity.this);
					typeWindow.setHeight(LayoutParams.WRAP_CONTENT);
					typeWindow.setWidth(LayoutParams.WRAP_CONTENT);
					View contentView = LayoutInflater
							.from(SuggestActivity.this).inflate(
									R.layout.suggest_type_window_layout, null);
					ListView lv_type = (ListView) contentView
							.findViewById(R.id.lv_suggest_type_window);
					SuggestTypeListAdapter adapter = new SuggestTypeListAdapter(
							 SuggestActivity.this, types);
					lv_type.setAdapter(adapter);
					lv_type.setOnItemClickListener(new MyOnItemClickListener());
					typeWindow.setContentView(contentView);
					typeWindow.setFocusable(true);
					typeWindow.setOutsideTouchable(true);
					typeWindow.setBackgroundDrawable(new ColorDrawable(0));
					typeWindow.showAsDropDown(tv_type);
				} else {
					if (typeWindow.isShowing()) {
						typeWindow.dismiss();
					} else {
						typeWindow.showAsDropDown(tv_type);
					}
				}
				break;
			case R.id.btn_confirm:
				String suggestStr = et_suggest.getText().toString();
				String contractStr = et_phone.getText().toString();
				if (suggestStr.equals("")) {
					Toast.makeText(SuggestActivity.this, "请填写建议内容",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (contractStr.equals("")) {
					Toast.makeText(SuggestActivity.this, "请填写联系方式",
							Toast.LENGTH_SHORT).show();
					return;
				}

				//开始提交数据到服务器
				HashMap<String,String> map=new HashMap<>();
				map.put("phone","1244");
				OkhttpManager.postAsyncParams("", map, new OkhttpManager.DataCallBack() {
					@Override
					public void requestFailure(Request request, Exception e) {

					}

					@Override
					public void requestSuccess(String result) {
						Toast.makeText(SuggestActivity.this, "感谢你的反馈！",
								Toast.LENGTH_SHORT).show();
						SuggestActivity.this.finish();
					}
				});
				break;
			}
		}
	}

	private void getDataOk() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
		Toast.makeText(SuggestActivity.this, "感谢您的宝贵建议",
				Toast.LENGTH_SHORT).show();
		SuggestActivity.this.finish();
	}

	private void getDataError() {
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.dismiss();
		}
		Toast.makeText(SuggestActivity.this, "网络错误,请重试",
				Toast.LENGTH_SHORT).show();
	}

	private class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			tv_type.setText(types[(int) id]);
			type = types[(int) id];
			if (typeWindow != null) {
				typeWindow.dismiss();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();

	}

}
