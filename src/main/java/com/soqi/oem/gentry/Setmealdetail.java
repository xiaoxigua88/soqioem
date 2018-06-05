package com.soqi.oem.gentry;

import java.io.Serializable;

public class Setmealdetail implements Serializable {
    private Integer itemid;

    private String itemname;

    private String itemnameen;

    private String value;

    private static final long serialVersionUID = 1L;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname == null ? null : itemname.trim();
    }

    public String getItemnameen() {
        return itemnameen;
    }

    public void setItemnameen(String itemnameen) {
        this.itemnameen = itemnameen == null ? null : itemnameen.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", itemid=").append(itemid);
        sb.append(", itemname=").append(itemname);
        sb.append(", itemnameen=").append(itemnameen);
        sb.append(", value=").append(value);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}