package com.soqi.oem.gentry;

import java.io.Serializable;

public class Webtemplate implements Serializable {
    private Integer tempid;

    private String tempname;

    private String tempdir;

    private static final long serialVersionUID = 1L;

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public String getTempname() {
        return tempname;
    }

    public void setTempname(String tempname) {
        this.tempname = tempname == null ? null : tempname.trim();
    }

    public String getTempdir() {
        return tempdir;
    }

    public void setTempdir(String tempdir) {
        this.tempdir = tempdir == null ? null : tempdir.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tempid=").append(tempid);
        sb.append(", tempname=").append(tempname);
        sb.append(", tempdir=").append(tempdir);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}