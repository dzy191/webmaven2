package com.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class AjaxUtil {
  /**输出响应到客户端*/
	public static void printString(HttpServletResponse response,String str){
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter out=response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
