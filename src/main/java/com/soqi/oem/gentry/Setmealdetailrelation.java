package com.soqi.oem.gentry;

import java.io.Serializable;

public class Setmealdetailrelation implements Serializable {
    private Integer mealid;

    private Integer itemid;

    private static final long serialVersionUID = 1L;

    public Integer getMealid() {
        return mealid;
    }

    public void setMealid(Integer mealid) {
        this.mealid = mealid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mealid=").append(mealid);
        sb.append(", itemid=").append(itemid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}