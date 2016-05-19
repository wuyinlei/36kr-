package com.cniao5.app36kr_cnk.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.cniao5.app36kr_cnk.R;
import com.cniao5.app36kr_cnk.application.CNKApplication;
import com.cniao5.app36kr_cnk.common.MemoryStatus;


public class UpdateService extends Service {
	// BT字节参考量
	private static final float SIZE_BT = 1024L;
	// KB字节参考量
	private static final float SIZE_KB = SIZE_BT * 1024.0f;
	// MB字节参考量
	private static final float SIZE_MB = SIZE_KB * 1024.0f;

	private final static int DOWNLOAD_COMPLETE = 1;// 完成
	private final static int DOWNLOAD_NOMEMORY = -1;// 内存异常
	private final static int DOWNLOAD_FAIL = -2;// 失败

	private String appName = null;// 应用名字
	private String appUrl = null;// 应用升级地址
	private File updateDir = null;// 文件目录
	private File updateFile = null;// 升级文件

	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;

	private Intent updateIntent = null;// 下载完成
	private PendingIntent updatePendingIntent = null;// 在下载的时候

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		appName = intent.getStringExtra("appname");
		appUrl = intent.getStringExtra("appurl");
		updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		updateNotification = new Notification();
		updateNotification.icon = R.mipmap.ic_launcher;
		updateNotification.tickerText = "正在下载 " + appName;
		updateNotification.when = System.currentTimeMillis();
		updateIntent = new Intent(this, CNKApplication.class);
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
				0);
		updateNotification.contentIntent = updatePendingIntent;
		updateNotification.contentIntent.cancel();
		updateNotification.contentView = new RemoteViews(getPackageName(),
				R.layout.download_notification);
		updateNotification.contentView.setTextViewText(
				R.id.download_notice_name_tv, appName + " 正在下载");
		updateNotification.contentView.setTextViewText(
				R.id.download_notice_speed_tv, "0MB (0%)");
		updateNotificationManager.notify(0, updateNotification);
		new UpdateThread().execute();
	}

	class UpdateThread extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			return downloadUpdateFile(appUrl);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (result == DOWNLOAD_COMPLETE) {
				Log.d("update", "下载成功");
				String cmd = "chmod 777 " + updateFile.getPath();
				try {
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				updatePendingIntent = PendingIntent.getActivity(
						UpdateService.this, 0, installIntent, 0);
				updateNotification.contentIntent = updatePendingIntent;
				updateNotification.contentView.setTextViewText(
						R.id.download_notice_speed_tv,
						getString(R.string.update_notice_finish));
				updateNotification.tickerText = appName + "下载完成";
				updateNotification.when = System.currentTimeMillis();
				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotificationManager.notify(0, updateNotification);
				UpdateService.this.startActivity(installIntent);
				stopSelf();
			} else if (result == DOWNLOAD_NOMEMORY) {
				Log.d("update", "下载失败，内存问题");
				updateNotification.tickerText = appName + "下载失败";
				updateNotification.when = System.currentTimeMillis();
				updateNotification.contentView.setTextViewText(
						R.id.download_notice_speed_tv,
						getString(R.string.update_notice_nomemory));
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotificationManager.notify(0, updateNotification);
				stopSelf();
			} else if (result == DOWNLOAD_FAIL) {
				Log.d("update", "下载失败");
				updateNotification.tickerText = appName + "下载失败";
				updateNotification.when = System.currentTimeMillis();
				updateNotification.contentView.setTextViewText(
						R.id.download_notice_speed_tv,
						getString(R.string.update_notice_error));
				updateNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				updateNotification.defaults = Notification.DEFAULT_SOUND;
				updateNotificationManager.notify(0, updateNotification);
				stopSelf();
			}
		}

	}

	private int downloadUpdateFile(String downloadUrl) {
		int count = 0;
		long totalSize = 0;
		long downloadSize = 0;
		URI encodedUri = null;
		HttpGet httpGet = null;
		try {
			encodedUri = new URI(downloadUrl);
			httpGet = new HttpGet(encodedUri);
		} catch (URISyntaxException e) {
			String encodedUrl = downloadUrl.replace(' ', '+');
			httpGet = new HttpGet(encodedUrl);
			e.printStackTrace();
		}
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse != null) {
				int stateCode = httpResponse.getStatusLine().getStatusCode();
				if (stateCode == HttpStatus.SC_OK) {
					HttpEntity entity = httpResponse.getEntity();
					if (entity != null) {
						totalSize = entity.getContentLength();
						if (MemoryAvailable(totalSize)) {
							is = entity.getContent();
							if (is != null) {
								fos = new FileOutputStream(updateFile, false);
								byte buffer[] = new byte[4096];
								int readsize = 0;
								while ((readsize = is.read(buffer)) > 0) {
									fos.write(buffer, 0, readsize);
									downloadSize += readsize;
									if ((count == 0)
											|| (int) (downloadSize * 100 / totalSize) >= count) {
										count += 5;
										updateNotification.contentView
												.setTextViewText(
														R.id.download_notice_speed_tv,
														getMsgSpeed(downloadSize,totalSize));
										updateNotificationManager.notify(0,
												updateNotification);
									}
								}
								fos.flush();
								if (totalSize >= downloadSize) {
									return DOWNLOAD_COMPLETE;
								} else {
									return DOWNLOAD_FAIL;
								}
							}
						} else {
							if (httpGet != null) {
								httpGet.abort();
							}
							return DOWNLOAD_NOMEMORY;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		return DOWNLOAD_FAIL;
	}

	private boolean MemoryAvailable(long fileSize) {
		fileSize += (1024 << 10);
		if (MemoryStatus.externalMemoryAvailable()) {
			if ((MemoryStatus.getAvailableExternalMemorySize() <= fileSize)) {
				if ((MemoryStatus.getAvailableInternalMemorySize() > fileSize)) {
					createFile(false);
					return true;
				} else {
					return false;
				}
			} else {
				createFile(true);
				return true;
			}
		} else {
			if (MemoryStatus.getAvailableInternalMemorySize() <= fileSize) {
				return false;
			} else {
				createFile(false);
				return true;
			}
		}
	}

	public static String getMsgSpeed(long downSize, long allSize) {
		StringBuffer sBuf = new StringBuffer();
		sBuf.append(getSize(downSize));
		sBuf.append("/");
		sBuf.append(getSize(allSize));
		sBuf.append(" ");
		sBuf.append(getPercentSize(downSize, allSize));
		return sBuf.toString();
	}

	public static String getSize(long size) {
		if (size >= 0 && size < SIZE_BT) {
			return (double) (Math.round(size * 10) / 10.0) + "B";
		} else if (size >= SIZE_BT && size < SIZE_KB) {
			return (double) (Math.round((size / SIZE_BT) * 10) / 10.0) + "KB";
		} else if (size >= SIZE_KB && size < SIZE_MB) {
			return (double) (Math.round((size / SIZE_KB) * 10) / 10.0) + "MB";
		}
		return "";
	}

	public static String getPercentSize(long downSize, long allSize) {
		String percent = (allSize == 0 ? "0.0" : new DecimalFormat("0.0")
				.format((double) downSize / (double) allSize * 100));
		return "(" + percent + "%)";
	}

	private void createFile(boolean sd_available) {
		if (sd_available) {
			updateDir = new File(Environment.getExternalStorageDirectory(),
					UpdateInformation.downloadDir);
		} else {
			updateDir = getFilesDir();
		}
		updateFile = new File(updateDir.getPath(), appName + ".apk");
		if (!updateDir.exists()) {
			updateDir.mkdirs();
		}
		if (!updateFile.exists()) {
			try {
				updateFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			updateFile.delete();
			try {
				updateFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
