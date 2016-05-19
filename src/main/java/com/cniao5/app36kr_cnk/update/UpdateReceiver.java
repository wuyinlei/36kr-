package com.cniao5.app36kr_cnk.update;

import java.io.File;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.application.CNKApplication;
import com.cniao5.app36kr_cnk.common.DeliverConsts;
import com.cniao5.utils.SharedPreferencesHelper;
import com.cniao5.utils.SharedPreferencesTag;


/**
 * 版本更新升级 广播接受者
 * 
 * @author Jiangqq
 * @time 2013/12/27 14:44
 */
public class UpdateReceiver extends BroadcastReceiver {
	private AlertDialog.Builder mDialog;
	public static final String UPDATE_ACTION = "com.cniao5.app36kr_cnk";
	private SharedPreferencesHelper mSharedPreferencesHelper;
	private boolean isShowDialog;

	public UpdateReceiver() {
	}

	public UpdateReceiver(boolean isShowDialog) {
		super();
		this.isShowDialog = isShowDialog;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("zttupdate", "收到软件更新信息广播!");
		mSharedPreferencesHelper = mSharedPreferencesHelper
				.getInstance(CNKApplication.getInstance());
		HashMap<String, Object> tempMap = CNKApplication.getInstance()
				.getTempMap();
		UpdateInfoModel model = (UpdateInfoModel) tempMap
				.get(DeliverConsts.KEY_APP_UPDATE);
		try {
			UpdateInformation.localVersion = CNKApplication
					.getInstance()
					.getPackageManager()
					.getPackageInfo(
							CNKApplication.getInstance()
									.getPackageName(), 0).versionCode;
			UpdateInformation.versionName = CNKApplication
					.getInstance()
					.getPackageManager()
					.getPackageInfo(
							CNKApplication.getInstance()
									.getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		UpdateInformation.appname = CNKApplication.getInstance()
				.getResources().getString(R.string.app_name);
		UpdateInformation.serverVersion = Integer.parseInt(model
				.getServerVersion());
		UpdateInformation.serverFlag = Integer.parseInt(model.getServerFlag());
		UpdateInformation.lastForce = Integer.parseInt(model.getLastForce());
		UpdateInformation.updateurl = model.getUpdateurl();
		UpdateInformation.upgradeinfo = model.getUpgradeinfo();
		Log.d("zttjiangqq", "APP名称:" + UpdateInformation.appname);
		Log.d("zttjiangqq", "本地版本:" + UpdateInformation.localVersion);
		Log.d("zttjiangqq", "服务器版本:" + UpdateInformation.serverVersion);
		Log.d("zttjiangqq", "升级标志:" + UpdateInformation.serverFlag);
		Log.d("zttjiangqq", "上次强制升级版本:" + UpdateInformation.lastForce);
		Log.d("zttjiangqq", "升级地址:" + UpdateInformation.updateurl);
		Log.d("zttjiangqq", "升级内容:" + UpdateInformation.upgradeinfo);
		checkVersion(context);

	}

	public void checkVersion(Context pContext) {
		if (UpdateInformation.localVersion < UpdateInformation.serverVersion) {
			// 需要进行更新
			mSharedPreferencesHelper.putIntValue(
					SharedPreferencesTag.IS_HAVE_NEW_VERSION, 1);
			update(pContext);
		} else {
			mSharedPreferencesHelper.putIntValue(
					SharedPreferencesTag.IS_HAVE_NEW_VERSION, 0);
			Log.d("zttjiangqq", "不要升级,清理升级目录!");
			if (isShowDialog) {
				noNewVersion(pContext);
			}
			clearUpateFile(pContext);
		}
	}

	/**
	 * 进行升级
	 * 
	 * @param pContext
	 */
	private void update(Context pContext) {
		if (UpdateInformation.serverFlag == 1) {
			// 官方推荐升级
			if (UpdateInformation.localVersion < UpdateInformation.lastForce) {
				Log.d("zttjiangqq", "本地版本小于之前强制升级版本号,强制升级");
				forceUpdate(pContext);
			} else {
				Log.d("zttjiangqq", "正常升级");
				normalUpdate(pContext);
			}

		} else if (UpdateInformation.serverFlag == 2) {
			// 官方强制升级
			forceUpdate(pContext);
		}
	}

	private void noNewVersion(final Context pContext) {
		mDialog = new AlertDialog.Builder(pContext);
		mDialog.setTitle("版本更新");
		mDialog.setMessage("当前为最新版本");
		mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}

	/**
	 * 强制升级
	 * 
	 * @param pContext
	 */
	private void forceUpdate(final Context pContext) {
		mDialog = new AlertDialog.Builder(pContext);
		mDialog.setTitle("版本更新");
		mDialog.setMessage(UpdateInformation.upgradeinfo);

		mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent mIntent = new Intent(pContext, UpdateService.class);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.putExtra("appname", UpdateInformation.appname);
				mIntent.putExtra("appurl", UpdateInformation.updateurl);
				pContext.startService(mIntent);
			}
		}).setNegativeButton("退出", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 直接退出应用
				//ManagerActivity.getInstance().finishActivity();
				System.exit(0);
			}
		}).setCancelable(false).create().show();
	}

	/**
	 * 正常升级
	 * 
	 * @param pContext
	 */
	private void normalUpdate(final Context pContext) {
		mDialog = new AlertDialog.Builder(pContext);
		mDialog.setTitle("版本更新");
		mDialog.setMessage(UpdateInformation.upgradeinfo);
		mDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent mIntent = new Intent(pContext, UpdateService.class);
				mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mIntent.putExtra("appname", UpdateInformation.appname);
				mIntent.putExtra("appurl", UpdateInformation.updateurl);
				pContext.startService(mIntent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).create().show();
	}

	/**
	 * 清理升级目录
	 * 
	 * @param pContext
	 */
	private void clearUpateFile(final Context pContext) {
		File updateDir;
		File updateFile;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(),
					UpdateInformation.downloadDir);
		} else {
			updateDir = pContext.getFilesDir();
		}
		updateFile = new File(updateDir.getPath(), pContext.getResources()
				.getString(R.string.app_name) + ".apk");
		if (updateFile.exists()) {
			Log.d("update", "升级包存在，删除升级包");
			updateFile.delete();
		} else {
			Log.d("update", "升级包不存在，不用删除升级包");
		}
	}
}
