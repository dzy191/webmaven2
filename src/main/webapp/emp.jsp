<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>管理页面</title>
<!-- 引入ui支持 -->
<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css"/>
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css"/>
<script type="text/javascript" src="easyui/jquery-1.9.1.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
  //页面加载触发
  $(function(){
	  $('#win').window('close');  // close a window  
	  $.getJSON('doinit_Emp.do',function(map){
		  var lsdep=map.lsdep;
		  var lswf=map.lswf;
		  //拼接部门下拉列表
		  $('#depid').combobox({    
			 data:lsdep,    
             valueField:'depid',    
             textField:'depname',
             value:1,
             panelHeight:80
            });  

		  //拼接福利复选框
		  for(var i=0;i<lswf.length;i++){
				 var wf=lswf[i];
				 $("#wf").append("<input type='checkbox' id='wids' name='wids' value='"+wf.wid+"'/>"+wf.wname);
			}  
	  });
  });
  /********分页查询begin***********/
  $(function(){
	  $('#dg').datagrid({    
		    url:'findPageAll_Emp.do',
		    pagination:true,
		    pageList:[1,2,3,4,5],
		    pageNumber:1,
		    pageSize:2,
		    striped:true,
		    singleSelect:true,
		    columns:[[    
		        {field:'eid',title:'编号',align:'center',width:100},
		        {field:'ename',title:'姓名',align:'center',width:100},  
		        {field:'sex',title:'性别',align:'center',width:100},  
		        {field:'address',title:'地址',align:'center',width:100},  
		        {field:'sdate',title:'生日',align:'center',width:100},  
		        {field:'photo',title:'照片',align:'center',width:100,
		        	formatter: function(value,row,index){
						
						return "<img src=uppic/"+row.photo+" width=40 height=50/>";
					}	
		        
		        },  
		        {field:'depname',title:'部门',align:'center',width:100},  
		        {field:'opt',title:'操作',align:'center',width:200,
		        	formatter: function(value,row,index){
						var bt1="<input type='button'  value='删除' onclick=doDelById("+row.eid+")>";
						var bt2="<input type='button'  value='编辑' onclick=doEditById("+row.eid+")>";
						var bt3="<input type='button'  value='详情' onclick=doFindById("+row.eid+")>";
						return bt1+bt2+bt3;
					}
		        }
		    ]]    
		});  	  
  });
  /********分页查询end***********/
  /********删除与编辑和详情begin***********/
  function doDelById(eid){   
	  $.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
	      if (r){    
	          $.getJSON('delById_Emp.do?eid='+eid,function(code){
	        	  if(code==1){
			  			$.messager.alert('提示','删除成功');
			  			$('#dg').datagrid('reload');    // 重新载入当前页面数据  
			  		}else{
			  			$.messager.alert('提示','删除失败'); 
			  		}
	          });    
	      }    
	  });  

  }
  function doEditById(eid){
	  $.getJSON('findById_Emp.do?eid='+eid,function(emp){
		  alert("aaaaaaaaaaa");
		  //删除复选框选中
		  $(":checkbox[name='wids']").each(function(){
			  $(this).prop("checked",false);
		  });
		  //表单赋值
		  $('#ffemp').form('load',{
				'eid':emp.eid,
				'ename':emp.ename,
				'sex':emp.sex,
				'address':emp.address,
				'sdate':emp.sdate,
				'depid':emp.depid,
				'emoney':emp.emoney,
			});
		  $("#myimg").attr('src','uppic/'+emp.photo);
          var wids=emp.wids;
          $(":checkbox[name='wids']").each(function(){
        	  for(var i=0;i<wids.length;i++){
        		  if($(this).val()==wids[i]){
            		  $(this).prop("checked",true); 
            	  }  
        	  }
        	  
			  
		  });

	  });
  }
  function doFindById(eid){
	  $.getJSON('findById_Emp.do?eid='+eid,function(emp){
		  $("#enameText").html(emp.ename);
		  $("#sexText").html(emp.sex);
		  $("#addressText").html(emp.address);
		  $("#sdateText").html(emp.sdate);
		  $("#depnameText").html(emp.depname);
		  $("#emoneyText").html(emp.emoney);
		  $("#eidText").html(emp.eid);
		  var lswf=emp.lswf;
		  var wnames=[];
		  for(var i=0;i<lswf.length;i++){
			  var wf=lswf[i];
			  wnames.push(wf.wname);
		  }
		  var strname=wnames.join(",");
		  $("#wfText").html(strname);
		  $("#myPhoto").attr('src','uppic/'+emp.photo);
		  $('#win').window('open');  // open a window    
		  

	  });
  }
  
  
  /********删除与编辑和详情end***********/
  
  
  
  /********保存与修改begin***********/
  $(function(){
	  $("#btok").click(function(){
		  $.messager.progress();	// 显示进度条
		  $('#ffemp').form('submit', {
		  	url:'save_Emp.do',
		  	onSubmit: function(){
		  		var isValid = $(this).form('validate');
		  		if (!isValid){
		  			$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
		  		}
		  		return isValid;	// 返回false终止表单提交
		  	},
		  	success: function(code){
		  		if(code==1){
		  			$.messager.alert('提示','添加成功');
		  			$('#dg').datagrid('reload');    // 重新载入当前页面数据 
		  			$("#ffemp").form("reset");
		  			
		  		}else{
		  			$.messager.alert('提示','添加失败'); 
		  		}
		  		$.messager.progress('close');	// 如果提交成功则隐藏进度条
		  	}
		  });


	  });
	  $("#btedit").click(function(){
		  $.messager.progress();	// 显示进度条
		  $('#ffemp').form('submit', {
		  	url:'update_Emp.do',
		  	onSubmit: function(){
		  		var isValid = $(this).form('validate');
		  		if (!isValid){
		  			$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
		  		}
		  		return isValid;	// 返回false终止表单提交
		  	},
		  	success: function(code){
		  		if(code==1){
		  			$.messager.alert('提示','修改成功');
		  			$('#dg').datagrid('reload');    // 重新载入当前页面数据 
		  			$("#ffemp").form("reset");
		  		}else{
		  			$.messager.alert('提示','修改失败--(薪资不能低于当前)'); 
		  		}
		  		$.messager.progress('close');	// 如果提交成功则隐藏进度条
		  	}
		  });


	  });
  });
  /********保存与修改end***********/
</script>
</head>
<body>
<p align="center">员工列表</p>
<!--列表组件  -->
<table id="dg"></table> 
<hr>
<form action="" method="post" enctype="multipart/form-data" id="ffemp" name="ffemp">
 <table border="1px" align="center" width="600px">
   <tr bgcolor="#FFFFCC" align="center">
     <td colspan="3">员工管理</td>
   </tr>
   <tr>
     <td width="100px">姓名</td>
     <td>
      <input type="text" id="ename" name="ename" class="easyui-validatebox" data-options="required:true">
     </td>
     <td rowspan="7">
      <img id="myimg" alt="" src="uppic/default.jpg" width="160px" height="160px">
     </td>
   </tr>
   <tr>
     <td>性别</td>
     <td>
       <input type="radio" id="sex" name="sex" value="男" checked="checked">男
       <input type="radio" id="sex1" name="sex" value="女" >女
     </td>
    
   </tr>
   <tr>
     <td>地址</td>
     <td>
     <input type="text" id="address" name="address">
     </td>
    
   </tr>
   <tr>
     <td>生日</td>
     <td>
     <input type="date" id="sdate" name="sdate" value="1998-01-01">
     </td>
     
   </tr>
   <tr>
     <td>照片</td>
     <td>
     <input type="file" id="pic" name="pic">
     </td>
     
   </tr>
   <tr>
     <td>部门</td>
     <td>
     <input type="text" id="depid" name="depid">
     </td>
     
   </tr>
   <tr>
     <td>薪资</td>
     <td>
     <input type="text" id="emoney" name="emoney" value="2000">
     </td>
     
   </tr>
   <tr>
     <td>福利</td>
     <td colspan="2">
     <span id="wf"></span>
     </td>
     
   </tr>
   <tr bgcolor="#FFFFCC" align="center">
     <td colspan="3">
       <input type="hidden" id="eid" name="eid">
       <input type="button" id="btok" name="btok" value="保存">
       <input type="button" id="btedit" name="btedit" value="修改">
       <input type="button" id="btreset" name="btreset" value="重置">
     </td>
   </tr>
 
 </table>
</form>

<!-- 弹窗 -->
<div id="win" class="easyui-window" title="详情弹窗" style="width:600px;height:400px"   
        data-options="iconCls:'icon-save',modal:true">   
    <table border="1px" align="center" width="100%">
   <tr bgcolor="#FFFFCC" align="center">
     <td colspan="3">详情展示</td>
   </tr>
   <tr>
     <td width="100px">姓名</td>
     <td>
      <span id="enameText"></span>
     </td>
     <td rowspan="7">
      <img id="myPhoto" alt="" src="uppic/default.jpg" width="160px" height="160px">
     </td>
   </tr>
   <tr>
     <td>性别</td>
     <td>
       <span id="sexText"></span>
     </td>
    
   </tr>
   <tr>
     <td>地址</td>
     <td>
     <span id="addressText"></span>
     </td>
    
   </tr>
   <tr>
     <td>生日</td>
     <td>
     <span id="sdateText"></span>
     </td>
     
   </tr>
   <tr>
     <td>编号</td>
     <td>
     <span id="eidText"></span>
     </td>
     
   </tr>
   <tr>
     <td>部门</td>
     <td>
     <span id="depnameText"></span>
     </td>
     
   </tr>
   <tr>
     <td>薪资</td>
     <td>
     <span id="emoneyText"></span>
     </td>
     
   </tr>
   <tr>
     <td>福利</td>
     <td colspan="2">
     <span id="wfText"></span>
     </td>  
   </tr>
 </table>
</div> 
</body>
</html>