package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.Date;

public class Setmeal implements Serializable {
    private Integer mealid;

    private String mealname;

    private String comment;

    private Date createtime;

    private static final long serialVersionUID = 1L;

    public Integer getMealid() {
        return mealid;
    }

    public void setMealid(Integer mealid) {
        this.mealid = mealid;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname == null ? null : mealname.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mealid=").append(mealid);
        sb.append(", mealname=").append(mealname);
        sb.append(", comment=").append(comment);
        sb.append(", createtime=").append(createtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}