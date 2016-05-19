package com.cniao5.app36kr_cnk.biz;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cniao5.app36kr_cnk.entity.InvestData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 当前类注释:
 * ProjectName：36KrDataCrawler
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class InvestDataManager {
	 public static List<InvestData> getInvestDatas(String data){
		List<InvestData> investComs=null;
		try {
			JSONObject result_object=new JSONObject(data);
            JSONArray data_object=result_object.getJSONObject("data").getJSONArray("data");
            Gson gson=new Gson();
            investComs = gson.fromJson(data_object.toString(), new TypeToken<List<InvestData>>() {
            }.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return investComs;
	 }
}
