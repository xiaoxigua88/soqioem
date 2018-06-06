package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.Date;

public class Customerrole implements Serializable {
    private Integer customerid;

    private Integer roleid;

    private Date addtime;

    private String description;

    private static final long serialVersionUID = 1L;

    public Customerrole() {
		super();
	}
    public Customerrole(Integer customerid, Integer roleid) {
		super();
		this.customerid = customerid;
		this.roleid = roleid;
	}

	public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", customerid=").append(customerid);
        sb.append(", roleid=").append(roleid);
        sb.append(", addtime=").append(addtime);
        sb.append(", description=").append(description);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}