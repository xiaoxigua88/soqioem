package com.soqi.oem.gentry;

import java.io.Serializable;

public class Serviceconfig implements Serializable {
    private Integer serviceid;

    private Integer servicename;

    private Boolean status;

    private static final long serialVersionUID = 1L;

    public Integer getServiceid() {
        return serviceid;
    }

    public void setServiceid(Integer serviceid) {
        this.serviceid = serviceid;
    }

    public Integer getServicename() {
        return servicename;
    }

    public void setServicename(Integer servicename) {
        this.servicename = servicename;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serviceid=").append(serviceid);
        sb.append(", servicename=").append(servicename);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}