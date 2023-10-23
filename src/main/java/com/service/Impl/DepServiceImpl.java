package com.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.po.Dep;
import com.service.IDepService;
import com.util.DaoService;
@Service("DepService")
@Transactional
public class DepServiceImpl implements IDepService {
    @Resource(name="DaoService")
    private DaoService dao;
    
	public DaoService getDao() {
		return dao;
	}

	public void setDao(DaoService dao) {
		this.dao = dao;
	}


	public List<Dep> findAll() {
		// TODO Auto-generated method stub
		return dao.getDepMapper().findAll();
	}

}
