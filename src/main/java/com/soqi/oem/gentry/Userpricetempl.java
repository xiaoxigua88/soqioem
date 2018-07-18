package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Userpricetempl implements Serializable {
    private Integer userid;

    private Byte discounttype;
    
    private Short searchtype;

    private String searchname;
    
    private BigDecimal fixprice;

    private BigDecimal minprice;

    private BigDecimal maxprice;

    private BigDecimal ratio;

    private static final long serialVersionUID = 1L;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Short getSearchtype() {
        return searchtype;
    }

    public void setSearchtype(Short searchtype) {
        this.searchtype = searchtype;
    }

  
    public BigDecimal getFixprice() {
		return fixprice;
	}

	public void setFixprice(BigDecimal fixprice) {
		this.fixprice = fixprice;
	}

	public BigDecimal getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(BigDecimal maxprice) {
		this.maxprice = maxprice;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public Byte getDiscounttype() {
		return discounttype;
	}

	public void setDiscounttype(Byte discounttype) {
		this.discounttype = discounttype;
	}

	public BigDecimal getMinprice() {
		return minprice;
	}

	public void setMinprice(BigDecimal minprice) {
		this.minprice = minprice;
	}

	
	public String getSearchname() {
		return searchname;
	}

	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userid=").append(userid);
        sb.append(", searchtype=").append(searchtype);
        sb.append(", fixprice=").append(fixprice);
        sb.append(", mixprice=").append(minprice);
        sb.append(", maxprice=").append(maxprice);
        sb.append(", ratio=").append(ratio);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}