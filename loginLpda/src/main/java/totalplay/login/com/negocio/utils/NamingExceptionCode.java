package totalplay.login.com.negocio.utils;

public class NamingExceptionCode {
	
	public static String getMsgErrorCode(String errorMsg) {
		Boolean isErrorCode = false;
		
		for(String str : errorMsg.split(" ")) {
			if(str.equals("code")) {
				isErrorCode = true;
			}
			if(isErrorCode == true && !str.equals("code")) {
				return LDAPCode.valueOf("LDAP_"+str).msgCode();
			}
		}
		return "";
	}
}