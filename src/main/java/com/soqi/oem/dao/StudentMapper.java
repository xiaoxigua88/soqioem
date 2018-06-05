package com.soqi.oem.dao;

import com.soqi.oem.gentry.Student;
import java.util.List;

public interface StudentMapper {
    int insert(Student record);

    List<Student> selectAll();
}