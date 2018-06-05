package com.soqi.oem.dao;

import com.soqi.oem.gentry.Customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {
    int deleteByPrimaryKey(Integer customerid);

    int insert(Customer record);

    Customer selectByPrimaryKey(Integer customerid);
    
    Customer selectByDomainAndMobile(@Param("domain") String domain, @Param("mobile") String mobile);

    List<Customer> selectAll();
    
    List<Customer> qryCustomersByCusIdAndOemid(@Param("customerid") Integer customerid, @Param("oemid") Integer oemid, @Param("start") int start, @Param("size") int size);
    
    public int qryCountByCusIdAndOemid(@Param("customerid") Integer customerid, @Param("oemid") Integer oemid);
    
    int updateByPrimaryKey(Customer record);
}