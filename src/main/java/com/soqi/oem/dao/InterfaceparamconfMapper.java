package com.soqi.oem.dao;

import com.soqi.oem.gentry.Interfaceparamconf;
import java.util.List;

public interface InterfaceparamconfMapper {
    int insert(Interfaceparamconf record);

    List<Interfaceparamconf> selectAll();
    
    Interfaceparamconf selectByOemid(Integer oemid);
}