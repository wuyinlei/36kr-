package com.cniao5.app36kr_cnk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.application.CNKApplication;
import com.cniao5.app36kr_cnk.common.DefineView;
import com.cniao5.app36kr_cnk.common.DeliverConsts;
import com.cniao5.app36kr_cnk.common.RequestURL;
import com.cniao5.app36kr_cnk.entity.Person;
import com.cniao5.app36kr_cnk.utils.DESUtil;
import com.cniao5.app36kr_cnk.utils.OkhttpManager;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * 登录界面
 */
public class LoginActivity extends Activity  implements DefineView{
    private Button btn_back,btn_confirm_login;
    private TextView tv_title;
    private EditText ed_username,ed_password;
    private ImageView qq_login;
    private Person person;
    private HashMap<String,Object> tempMap= CNKApplication.getInstance().getTempMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cnaio5);
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {
        btn_back=(Button)this.findViewById(R.id.btn_back);
        tv_title=(TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("用户登录");
        btn_confirm_login=(Button)this.findViewById(R.id.btn_confirm_login);
        ed_username=(EditText)this.findViewById(R.id.ed_username);
        ed_password=(EditText)this.findViewById(R.id.ed_password);
        qq_login=(ImageView)this.findViewById(R.id.qq_login);
    }

    @Override
    public void initValidata() {

    }

    @Override
    public void initListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        //开始进行登录
        btn_confirm_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("phone",ed_username.getText().toString().trim());
                map.put("password", DESUtil.encode("Cniao5_123456",ed_password.getText().toString().trim()));
                OkhttpManager.postAsyncParams(RequestURL.LOGIN_URL, map, new OkhttpManager.DataCallBack() {
                    @Override
                    public void requestFailure(Request request, Exception e) {
                        Log.d("zttjiangqq","请求失败...");
                    }
                    @Override
                    public void requestSuccess(String result) {
                        Log.d("zttjiangqq","返回的信息为:"+result);
                        //进行解析数据
                        person=new Person();
                        try {
                            JSONObject result_object=new JSONObject(result);
                            String status=result_object.getString("status");
                            if(status.equals("1")){
                                //用户登录成功
                                JSONObject person_object=result_object.getJSONObject("data");
                                person.setId(person_object.getString("id"));
                                person.setEmail(person_object.getString("email"));
                                person.setMobi(person_object.getString("mobi"));
                                person.setUsername(person_object.getString("username"));
                                person.setLogo_url(person_object.getString("logo_url"));
                                tempMap.put(DeliverConsts.KEY_LOGIN,person);
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                LoginActivity.this.finish();
                            }else{
                                //用户登录失败
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        //QQ登录
        qq_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public void bindData() {

    }
}
