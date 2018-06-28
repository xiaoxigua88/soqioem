package com.soqi.oem.dao;

import com.soqi.oem.gentry.Useraccount;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UseraccountMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(Useraccount record);

    Useraccount selectByPrimaryKey(Integer userid);

    List<Useraccount> selectAll();

    int updateByPrimaryKey(Useraccount record);
    
    /**批量更新用户账户、每条记录中的userid可能不同，也可能相同、主要代理批量操作关键词使用
     * @param userAccounts
     * @return
     */
    int batchUpdateAccountByDiffUser(List<Useraccount> userAccounts);
    
    int updateFreezeAmountByUserId(@Param("userid") Integer userid, @Param("freezeamount") BigDecimal freezeamount);
}