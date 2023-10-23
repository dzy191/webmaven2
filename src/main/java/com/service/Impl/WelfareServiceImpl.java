package com.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Dep;
import com.po.Welfare;
import com.service.IDepService;
import com.service.IWelfareService;
import com.util.DaoService;
@Service("WelfareService")
@Transactional
public class WelfareServiceImpl implements IWelfareService {
    @Resource(name="DaoService")
    private DaoService dao;
    
	public DaoService getDao() {
		return dao;
	}

	public void setDao(DaoService dao) {
		this.dao = dao;
	}

	@Override
	public List<Welfare> findAll() {
		// TODO Auto-generated method stub
		return dao.getWelfareMapper().findAll();
	}

	

}
