package com.cniao5.app36kr_cnk.update;

/**
 * 版本更新 信息实体类
 * 
 * @author jiangqq
 * @time 2013/12/18 10:49
 * 
 */
public class UpdateInfoModel {
	private String appname;
	private String serverVersion;
	private String serverFlag;
	private String lastForce;
	private String updateurl;
	private String upgradeinfo;

	public UpdateInfoModel() {
		super();
	}

	public UpdateInfoModel(String appname, String serverVersion,
			String serverFlag, String lastForce, String updateurl,
			String upgradeinfo, String serverName) {
		super();
		this.appname = appname;
		this.serverVersion = serverVersion;
		this.serverFlag = serverFlag;
		this.lastForce = lastForce;
		this.updateurl = updateurl;
		this.upgradeinfo = upgradeinfo;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getServerFlag() {
		return serverFlag;
	}

	public void setServerFlag(String serverFlag) {
		this.serverFlag = serverFlag;
	}

	public String getLastForce() {
		return lastForce;
	}

	public void setLastForce(String lastForce) {
		this.lastForce = lastForce;
	}

	public String getUpdateurl() {
		return updateurl;
	}

	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}

	public String getUpgradeinfo() {
		return upgradeinfo;
	}

	public void setUpgradeinfo(String upgradeinfo) {
		this.upgradeinfo = upgradeinfo;
	}

	@Override
	public String toString() {
		return "UpdateInfoModel [appname=" + appname + ", serverVersion="
				+ serverVersion + ", serverFlag=" + serverFlag + ", lastForce="
				+ lastForce + ", updateurl=" + updateurl + ", upgradeinfo="
				+ upgradeinfo + "]";
	}

}
