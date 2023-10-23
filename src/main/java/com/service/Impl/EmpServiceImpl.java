package com.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Dep;
import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.IDepService;
import com.service.IEmpService;
import com.util.DaoService;
@Service("EmpService")
@Transactional
public class EmpServiceImpl implements IEmpService {
    @Resource(name="DaoService")
    private DaoService dao;
    
	public DaoService getDao() {
		return dao;
	}

	public void setDao(DaoService dao) {
		this.dao = dao;
	}
	@Override
	public boolean save(Emp emp) {
		if(emp!=null){
			//1.添加员工数据到员工表
			int code=dao.getEmpMapper().save(emp);
			if(code>0){
				int maxEid=dao.getEmpMapper().findMaxEid();
			    //2.添加该员工（编号）的薪资（薪资表）
				Salary sa=new Salary(maxEid,emp.getEmoney());
				dao.getSalaryMapper().save(sa);
			    //3.添加该员工（编号）对应福利（员工福利表）
				if(emp.getWids().length>0){
				for (String wid : emp.getWids()) {
					EmpWelfare ewf=new EmpWelfare(maxEid,Integer.parseInt(wid));
					dao.getEmpWelfareMapper().save(ewf);
				}
				}
			  return true;
			}
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		//完成emp修改
		int code=dao.getEmpMapper().update(emp);
		if(code>0){
		//修改薪资(限制降薪)
		Float emoney=dao.getSalaryMapper().findByEid(emp.getEid()).getEmoney();
		if(emoney<emp.getEmoney()){
		Salary sa=new Salary(emp.getEid(),emp.getEmoney());
		dao.getSalaryMapper().updateByEid(sa);
		}
		//修改员工福利
		dao.getEmpWelfareMapper().delByEid(emp.getEid());
		if(emp.getWids().length>0){
			for (String wid : emp.getWids()) {
				EmpWelfare ewf=new EmpWelfare(emp.getEid(),Integer.parseInt(wid));
				dao.getEmpWelfareMapper().save(ewf);
			}	
		}
		return true;
		}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//2.删除薪资信息
		dao.getSalaryMapper().delByEid(eid);
	    //3.删除员工福利信息
		dao.getEmpWelfareMapper().delByEid(eid);
		//1.删除员工信息
		int code=dao.getEmpMapper().delById(eid);
		if(code>0){
		
		return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//查询员工对象
		Emp oldemp=dao.getEmpMapper().findById(eid);
		//查询薪资
		oldemp.setEmoney(dao.getSalaryMapper().findByEid(eid).getEmoney());
		List<Welfare> lswf=dao.getEmpWelfareMapper().findByEid(eid);
		//查询员工福利
		oldemp.setLswf(lswf);
		String[] wids=new String[lswf.size()];
		//福利编号数组
		for (int i=0;i<lswf.size();i++) {
			wids[i]=lswf.get(i).getWid()+"";
		}
		oldemp.setWids(wids);
		return oldemp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
		// TODO Auto-generated method stub
		return dao.getEmpMapper().findPageAll(pb);
	}

	@Override
	public int findMaxRows() {
		// TODO Auto-generated method stub
		return dao.getEmpMapper().findMaxRows();
	}

}
