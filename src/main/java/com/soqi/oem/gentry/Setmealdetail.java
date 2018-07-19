package com.soqi.oem.gentry;

import java.io.Serializable;

public class Setmealdetail implements Serializable {
    
    private Integer mealid;

    private String mealname;
    
    private String comment;
    
    private Integer itemid;
    
    private Integer value;
    
    private String itemname;

    private String itemnameen;
    
    private Integer defvalue;

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
		this.mealname = mealname;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getDefvalue() {
		return defvalue;
	}

	public void setDefvalue(Integer defvalue) {
		this.defvalue = defvalue;
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