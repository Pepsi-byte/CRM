package com.crmdemo.crm.setting.domain;

public class User {

    private String id;//编号：主键
    private String loginAct;//登录账号
    private String name;//真实姓名
    private String loginPwd;//登录密码
    private String email;//邮箱
    private String expireTime;//失效时间  19位
    private String lockState;//锁定状态 0锁定  1:启动
    private String deptno;//部门编号
    private String allowIps;//允许访问的ip地址
    private String createTime;//允许创建时间  19
    private String creatBy;//创建人
    private String editTime;//修改时间  19
    private String editBy;//修改人

    public User() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCreatBy(String creatBy) {
        this.creatBy = creatBy;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public String getId() {
        return id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public String getName() {
        return name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreatBy() {
        return creatBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public String getEditBy() {
        return editBy;
    }
}
