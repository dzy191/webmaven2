package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AjaxUtil;
import com.util.BizService;
@Controller
public class EmpController implements IEmpAction {
    @Resource(name="BizService")
    private BizService biz;
    
	public BizService getBiz() {
		return biz;
	}

	public void setBiz(BizService biz) {
		this.biz = biz;
	}

	@RequestMapping(value="save_Emp.do")
	public String save(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		System.out.println("save方法正在运行......"+emp.toString());
		//1.处理文件上传
		//获取文件根路径
		   String realPath=request.getSession().getServletContext().getRealPath("/");
		   System.out.println("realPath:"+realPath);
		   /*********文件上传begin*************/
		   MultipartFile multipartFile=emp.getPic();
		   if(multipartFile!=null && !multipartFile.isEmpty()){
			   //获取上传文件名称
			   String fname=multipartFile.getOriginalFilename();
			   //改名字
			   if(fname.lastIndexOf(".")!=-1){//存在后缀
				   //获取后缀
				   String ext=fname.substring(fname.lastIndexOf("."));
				   //限制文件上传
				   if(ext.equalsIgnoreCase(".png")||ext.equalsIgnoreCase(".jpg")){
					   String newFname=new Date().getTime()+ext;
					   //完成文件上传（指定位置新建文件，将上传内容通过流写入）
					   File newFile=new File(realPath+"/uppic/"+newFname);
					   try {
						FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), newFile);
						emp.setPhoto(newFname);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   }
			   }
		   }
		   /*********文件上传end*************/
		//2.完成添加
		   boolean flag=biz.getEmpService().save(emp);
		   if(flag){
			   String jsonStr=JSONObject.toJSONString(1);
			   AjaxUtil.printString(response, jsonStr);
		   }else{
			   String jsonStr=JSONObject.toJSONString(0);
			   AjaxUtil.printString(response, jsonStr);
		   }
		return null;
	}

	@RequestMapping(value="update_Emp.do")
	public String update(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		System.out.println("update方法正在运行......"+emp.toString());
		//1.处理文件上传
		//获取文件根路径
		String realPath=request.getSession().getServletContext().getRealPath("/");
		System.out.println("realPath:"+realPath);
		//先获取原有员工信息
		Emp oldemp=biz.getEmpService().findById(emp.getEid());
		/*********文件上传begin*************/
	    MultipartFile multipartFile=emp.getPic();
		if(multipartFile!=null && !multipartFile.isEmpty()){
		    //获取上传文件名称
			String fname=multipartFile.getOriginalFilename();
			//改名字
			if(fname.lastIndexOf(".")!=-1){//存在后缀
			//获取后缀
			String ext=fname.substring(fname.lastIndexOf("."));
			//限制文件上传
			if(ext.equalsIgnoreCase(".png")||ext.equalsIgnoreCase(".jpg")){
				String newFname=new Date().getTime()+ext;
				//完成文件上传（指定位置新建文件，将上传内容通过流写入）
				File newFile=new File(realPath+"/uppic/"+newFname);
				try {
				FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), newFile);
				emp.setPhoto(newFname);
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			 }
			}
			}else{
				emp.setPhoto(oldemp.getPhoto());
			}
		    /*********文件上传end*************/
			//2.完成修改
			boolean flag=biz.getEmpService().update(emp);
			if(flag){
				String jsonStr=JSONObject.toJSONString(1);
				AjaxUtil.printString(response, jsonStr);
			}else{
			    String jsonStr=JSONObject.toJSONString(0);
				AjaxUtil.printString(response, jsonStr);
			}
		return null;
	}

	@RequestMapping(value="delById_Emp.do")
	public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		System.out.println("delById方法正在运行......"+eid);
		String realPath=request.getSession().getServletContext().getRealPath("/");
		System.out.println("realPath:"+realPath);
		//先获取原有员工信息
		Emp oldemp=biz.getEmpService().findById(eid);
		boolean flag=biz.getEmpService().delById(eid);
		   if(flag){
			   //删除既有照片
			   File newFile=new File(realPath+"/uppic/"+oldemp.getPhoto());
			   if(newFile!=null && !oldemp.getPhoto().equals("default.jpg")){
				   newFile.delete();
			   }
			   String jsonStr=JSONObject.toJSONString(1);
			   AjaxUtil.printString(response, jsonStr);
		   }else{
			   String jsonStr=JSONObject.toJSONString(0);
			   AjaxUtil.printString(response, jsonStr);
		   }
		return null;
	}

	@RequestMapping(value="findById_Emp.do")
	public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		System.out.println("findById方法正在运行......"+eid);
		Emp oldemp=biz.getEmpService().findById(eid);
		String jsonStr=JSONObject.toJSONString(oldemp);
		   AjaxUtil.printString(response, jsonStr);
		return null;
	}

	@RequestMapping(value="findPageAll_Emp.do")
	public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows) {
		System.out.println("findPageAll方法正在运行......"+page+"===="+rows);
		Map<String,Object> map=new HashMap();
		PageBean pb=new PageBean();
		page=page==null||page<1?pb.getPage():page;
		rows=rows==null||rows<1?pb.getRows():rows;
		if(rows>20)rows=10;
		pb.setPage(page);
		pb.setRows(rows);
		List<Emp> lsemp=biz.getEmpService().findPageAll(pb);
		int maxRows=biz.getEmpService().findMaxRows();
		System.out.println(maxRows);
		map.put("page", page);
		map.put("rows", lsemp);
		map.put("total", maxRows);
		String jsonStr=JSONObject.toJSONString(map);
		AjaxUtil.printString(response, jsonStr);
		return null;
	}

	@RequestMapping(value="doinit_Emp.do")
	public String doinit(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("doinit方法正在运行......");
		Map<String, Object> map=new HashMap();
		List<Dep> lsdep=biz.getDepService().findAll();
		List<Welfare> lswf=biz.getWelfareService().findAll();
		map.put("lsdep", lsdep);
		map.put("lswf", lswf);
		String jsonStr=JSONObject.toJSONString(map);
		AjaxUtil.printString(response, jsonStr);
		return null;
	}

}
