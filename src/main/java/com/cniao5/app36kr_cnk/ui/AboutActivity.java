package com.cniao5.app36kr_cnk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cniao5.app36kr_cnk.R;

public class AboutActivity extends Activity {
    private Button btn_back;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tv_title=(TextView)this.findViewById(R.id.tv_title);
        tv_title.setText("关于我们");
        btn_back=(Button)this.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }
}
