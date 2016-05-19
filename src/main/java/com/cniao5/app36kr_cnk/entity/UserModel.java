package com.cniao5.app36kr_cnk.entity;

public class UserModel {
    private String coinvestorType;
    private String id;
    private String nickName;
    private String name;
    private String inviterUid;
    private String investorType;
    private String status;
    private String linkedin;
    private String leadInvestorType;
    private String avatar;
    private String intro;
    private String enterpriser;
    private String source;
    private String weibo;
	public UserModel() {
		super();
	}
	public UserModel(String coinvestorType, String id, String nickName,
			String name, String inviterUid, String investorType, String status,
			String linkedin, String leadInvestorType, String avatar,
			String intro, String enterpriser, String source, String weibo) {
		super();
		this.coinvestorType = coinvestorType;
		this.id = id;
		this.nickName = nickName;
		this.name = name;
		this.inviterUid = inviterUid;
		this.investorType = investorType;
		this.status = status;
		this.linkedin = linkedin;
		this.leadInvestorType = leadInvestorType;
		this.avatar = avatar;
		this.intro = intro;
		this.enterpriser = enterpriser;
		this.source = source;
		this.weibo = weibo;
	}
	public String getCoinvestorType() {
		return coinvestorType;
	}
	public void setCoinvestorType(String coinvestorType) {
		this.coinvestorType = coinvestorType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInviterUid() {
		return inviterUid;
	}
	public void setInviterUid(String inviterUid) {
		this.inviterUid = inviterUid;
	}
	public String getInvestorType() {
		return investorType;
	}
	public void setInvestorType(String investorType) {
		this.investorType = investorType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLinkedin() {
		return linkedin;
	}
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}
	public String getLeadInvestorType() {
		return leadInvestorType;
	}
	public void setLeadInvestorType(String leadInvestorType) {
		this.leadInvestorType = leadInvestorType;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getEnterpriser() {
		return enterpriser;
	}
	public void setEnterpriser(String enterpriser) {
		this.enterpriser = enterpriser;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getWeibo() {
		return weibo;
	}
	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}
	@Override
	public String toString() {
		return "UserModel [coinvestorType=" + coinvestorType + ", id=" + id
				+ ", nickName=" + nickName + ", name=" + name + ", inviterUid="
				+ inviterUid + ", investorType=" + investorType + ", status="
				+ status + ", linkedin=" + linkedin + ", leadInvestorType="
				+ leadInvestorType + ", avatar=" + avatar + ", intro=" + intro
				+ ", enterpriser=" + enterpriser + ", source=" + source
				+ ", weibo=" + weibo + "]";
	}
	
}
