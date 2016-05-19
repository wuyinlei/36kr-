package com.cniao5.app36kr_cnk.entity;

/**
 * 当前类注释:
 * 项目名：App36Kr_CNK
 * 包名：com.cniao5.app36kr_cnk.entity
 * 作者：江清清 on 16/3/6 20:54
 * 邮箱：jiangqqlmj@163.com
 * QQ： 781931404
 * 公司：江苏中天科技软件技术有限公司
 */
public class Person {
   private String id;
    private String  mobi;
    private String  email;
    private String username;
    private String logo_url;

    public Person() {
    }

    public Person(String id, String mobi, String email, String username, String logo_url) {
        this.id = id;
        this.mobi = mobi;
        this.email = email;
        this.username = username;
        this.logo_url = logo_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobi() {
        return mobi;
    }

    public void setMobi(String mobi) {
        this.mobi = mobi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", mobi='" + mobi + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", logo_url='" + logo_url + '\'' +
                '}';
    }
}
