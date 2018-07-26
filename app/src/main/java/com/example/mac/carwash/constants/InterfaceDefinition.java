package com.example.mac.carwash.constants;

/**
 * 存放后台接口文档
 * @author xk
 * @version 1.0
 * @date 2016年11月25日
 */
public class InterfaceDefinition {

	/**
	 * 用户状态存储
	 * @author xk
	 * @version 1.0
	 * @date 2016年11月25日
	 */
	public interface PreferencesUser {

		public static final String USER_ID = "USER_ID";				// 账号ID.

		public static final String USER_name = "USER_name";			// 账户ID.

		public static final String TERMINAL_CODE = "TERMINAL_CODE"; 				// 账户ID.

		public static final String Photo = "Photo";		// 电话号码.

		public static final String POSITION = "POSITION"; // 引导页状态.

		public static final String CompanyName = "CompanyName";		// 登录状态.

		public static final String SESSIONID = "SESSIONID";			// 验证ID.

		//-------------------------补充---------------------
		public static final String USER_loginname="USER_loginname"; //存入登录的账号名
		public static final String DEPTNAME="DEPTNAME";  //存入所属的部门
		public static final String URL = "http://192.168.0.193:8080/cetc/";
	}


}
