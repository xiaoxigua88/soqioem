package com.soqi.oem.gentry;

import java.io.Serializable;

public class Privilege implements Serializable {
    private Integer priid;

    private String name;

    private Integer valueconfig;

    private Integer defvalue;

    private Integer groupid;
    
    private Integer value;
    
    private String priname;
    
    private String groupname;
    
    private static final long serialVersionUID = 1L;
    
    private Privilege(){
    	
    }
    
    public Integer getPriid() {
        return priid;
    }

    public void setPriid(Integer priid) {
        this.priid = priid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getValueconfig() {
        return valueconfig;
    }

    public void setValueconfig(Integer valueconfig) {
        this.valueconfig = valueconfig;
    }

    public Integer getDefvalue() {
        return defvalue;
    }

    public void setDefvalue(Integer defvalue) {
        this.defvalue = defvalue;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	
	public String getPriname() {
		return priname;
	}

	public void setPriname(String priname) {
		this.priname = priname;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", priid=").append(priid);
        sb.append(", name=").append(name);
        sb.append(", valueconfig=").append(valueconfig);
        sb.append(", defvalue=").append(defvalue);
        sb.append(", groupid=").append(groupid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}