package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.Dep;
import com.po.Salary;
@Service("SalaryDAO")
public interface ISalaryMapper {
   public int save(Salary sa);//添加薪资
   //根据员工编号修改薪资
   public int updateByEid(Salary sa);
   //根据员工编号删除薪资
   public int delByEid(Integer eid);
   //根据员工编号查询薪资
   public Salary findByEid(Integer eid);
   
}
