package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.po.Emp;
import com.po.PageBean;

public interface IEmpAction {
	  public String save(HttpServletRequest request, HttpServletResponse response, Emp emp);
	  public String update(HttpServletRequest request, HttpServletResponse response, Emp emp);
	  public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid);
	  public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid);
	  public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows);
	  public String doinit(HttpServletRequest request, HttpServletResponse response);
}
