package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.List;

public class Pricetempl implements Serializable {
    private Integer pricetemplid;

    private Integer oemid;

    private String templname;
    
    private Byte templtype;

    private List<Pricetempldtail> ptdsList;
    
    private static final long serialVersionUID = 1L;

    public Integer getPricetemplid() {
        return pricetemplid;
    }

    public void setPricetemplid(Integer pricetemplid) {
        this.pricetemplid = pricetemplid;
    }

    public Integer getOemid() {
        return oemid;
    }

    public void setOemid(Integer oemid) {
        this.oemid = oemid;
    }

    public String getTemplname() {
        return templname;
    }

    public void setTemplname(String templname) {
        this.templname = templname == null ? null : templname.trim();
    }

    
    public List<Pricetempldtail> getPtdsList() {
		return ptdsList;
	}

	public void setPtdsList(List<Pricetempldtail> ptdsList) {
		this.ptdsList = ptdsList;
	}
	
	public Byte getTempltype() {
		return templtype;
	}

	public void setTempltype(Byte templtype) {
		this.templtype = templtype;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pricetemplid=").append(pricetemplid);
        sb.append(", oemid=").append(oemid);
        sb.append(", templname=").append(templname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}