package com.jinanlongen.ldap;
/**  
* 类说明   
*  
* @author shangyao  
* @date 2017年12月6日  
*/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 用户登陆认证,LDAP跨域认证，通过LDAP对用户进行更新
 * 
 * @author xlj
 * @date 2015.07.10
 */
public class LdapUtil {

	private static DirContext ctx;

	// LDAP服务器端口默认为389
	private static final String LDAP_URL = "ldap://152.168.200.125:389/";

	// ROOT根据此参数确认用户组织所在位置
	private static final String LDAP_PRINCIPAL = "dc=longen,dc=com";

	// LDAP驱动
	private static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	private static Logger logger = Logger.getLogger(LdapUtil.class);

	/**** 测试 ****/
	public static void main(String[] args) {

		connetLDAP();
		// new LdapUtil().testSearch();
		// LdapUtil.addUserLdap("10000", "123456");
		// LdapUtil.updatePasswordLdap("10000", "1234567");
		// LdapUtil.deleteUserLdap("10000");
	}

	/**
	 * 连接LDAP
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LdapContext connetLDAP() {
		// 连接Ldap需要的信息
		String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
		String ldapUrl = "ldap://192.168.200.125:389";// url
		String ldapAccount = "cn=admin,dc=longen,dc=com"; // 用户名
		String ldapPwd = "meiguogou5";// 密码
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
		// LDAP server
		env.put(Context.PROVIDER_URL, ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapAccount);
		env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
		env.put("java.naming.referral", "follow");
		LdapContext ctxTDS = null;

		try {
			// 连接LDAP进行认证
			ctxTDS = new InitialLdapContext(env, null);
			System.out.println("认证成功");
			logger.info("【" + ldapAccount + "】用户于【" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
					+ "】登陆系统成功");
		} catch (javax.naming.AuthenticationException e) {
			System.out.println("认证失败");
		} catch (NamingException err) {
			logger.info("--------->>【" + ldapAccount + "】用户验证失败");
		} catch (Exception e) {
			System.out.println("认证出错：");
			e.printStackTrace();
		}
		System.out.println("sdsd");
		return ctxTDS;
	}

	public void testSearch() {
		LdapContext ctx = connetLDAP();
		// 设置过滤条件
		String uid = "admin";
		String filter = "(&(objectClass=top)(objectClass=organizationalPerson)(uid=" + uid + "))";
		// 限制要查询的字段内容
		String[] attrPersonArray = { "uid", "userPassword", "displayName", "cn", "sn", "mail", "description" };
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		// 设置将被返回的Attribute
		searchControls.setReturningAttributes(attrPersonArray);
		// 三个参数分别为：
		// 上下文；
		// 要搜索的属性，如果为空或 null，则返回目标上下文中的所有对象；
		// 控制搜索的搜索控件，如果为 null，则使用默认的搜索控件
		NamingEnumeration<SearchResult> answer;
		try {
			answer = ctx.search("cn=admin,dc=longen,dc=com", filter.toString(), searchControls);
			while (answer.hasMore()) {
				SearchResult result = answer.next();
				NamingEnumeration<? extends Attribute> attrs = result.getAttributes().getAll();
				while (attrs.hasMore()) {
					Attribute attr = attrs.next();
					System.out.println(attr.getID() + "=" + attr.get());
				}
				System.out.println("============");
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 输出查到的数据

	}

	// 通过连接LDAP服务器对用户进行认证，返回LDAP对象
	public static DirContext getLoginContext() {
		String account = "admin"; // 模拟用户名
		String password = "meiguogou5.com"; // 模拟密码
		for (int i = 0; i < 5; i++) { // 验证次数
			Hashtable env = new Hashtable();
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_CREDENTIALS, password);
			// cn=属于哪个组织结构名称，ou=某个组织结构名称下等级位置编号
			env.put(Context.SECURITY_PRINCIPAL, "cn=" + account);
			env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
			env.put(Context.PROVIDER_URL, LDAP_URL + LDAP_PRINCIPAL);
			try {
				// 连接LDAP进行认证
				ctx = new InitialDirContext(env);
				System.out.println("认证成功");
				logger.info("【" + account + "】用户于【" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
						+ "】登陆系统成功");
			} catch (javax.naming.AuthenticationException e) {
				System.out.println("认证失败");
			} catch (NamingException err) {
				logger.info("--------->>【" + account + "】用户验证失败【" + i + "】次");
			} catch (Exception e) {
				System.out.println("认证出错：");
				e.printStackTrace();
			}
		}
		return ctx;
	}

	// 将输入用户和密码进行加密算法后验证
	public static boolean verifySHA(String ldappw, String inputpw) {

		// MessageDigest 提供了消息摘要算法，如 MD5 或 SHA，的功能，这里LDAP使用的是SHA-1
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 取出加密字符
		if (ldappw.startsWith("{SSHA}")) {
			ldappw = ldappw.substring(6);
		} else if (ldappw.startsWith("{SHA}")) {
			ldappw = ldappw.substring(5);
		}

		// 解码BASE64
		byte[] ldappwbyte = Base64.decode(ldappw);
		byte[] shacode;
		byte[] salt;

		// 前20位是SHA-1加密段，20位后是最初加密时的随机明文
		if (ldappwbyte.length <= 20) {
			shacode = ldappwbyte;
			salt = new byte[0];
		} else {
			shacode = new byte[20];
			salt = new byte[ldappwbyte.length - 20];
			System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
			System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
		}

		// 把用户输入的密码添加到摘要计算信息
		md.update(inputpw.getBytes());
		// 把随机明文添加到摘要计算信息
		md.update(salt);

		// 按SSHA把当前用户密码进行计算
		byte[] inputpwbyte = md.digest();

		// 返回校验结果
		return MessageDigest.isEqual(shacode, inputpwbyte);
	}

	// 添加用户
	public static boolean addUserLdap(String account, String password) {
		boolean success = false;
		try {
			ctx = LdapUtil.getLoginContext();
			BasicAttributes attrsbu = new BasicAttributes();
			BasicAttribute objclassSet = new BasicAttribute("objectclass");
			objclassSet.add("person");
			objclassSet.add("top");
			objclassSet.add("organizationalPerson");
			objclassSet.add("inetOrgPerson");
			attrsbu.put(objclassSet);
			attrsbu.put("sn", account);
			attrsbu.put("uid", account);
			attrsbu.put("userPassword", password);
			ctx.createSubcontext("cn=" + account + ",ou=People", attrsbu);
			ctx.close();
			return true;
		} catch (NamingException ex) {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException namingException) {
				namingException.printStackTrace();
			}
			logger.info("--------->>添加用户失败");
		}
		return false;
	}

	// 修改密码
	public static boolean updatePasswordLdap(String account, String password) {
		boolean success = false;
		try {
			ctx = LdapUtil.getLoginContext();
			ModificationItem[] modificationItem = new ModificationItem[1];
			modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
					new BasicAttribute("userPassword", password));
			ctx.modifyAttributes("cn=" + account + ",ou=People", modificationItem);
			ctx.close();
			return true;
		} catch (NamingException ex) {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException namingException) {
				namingException.printStackTrace();
			}
			logger.info("--------->>修改密码失败");
		}
		return success;
	}

	// 删除用户
	public static boolean deleteUserLdap(String account) {
		try {
			ctx = LdapUtil.getLoginContext();
			ctx.destroySubcontext("cn=" + account);
		} catch (Exception ex) {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException namingException) {
				namingException.printStackTrace();
			}
			logger.info("--------->>删除用户失败");
			return false;
		}
		return true;
	}

	// 关闭LDAP服务器连接
	public static void closeCtx() {
		try {
			ctx.close();
		} catch (NamingException ex) {
			logger.info("--------->> 关闭LDAP连接失败");
		}
	}
}
