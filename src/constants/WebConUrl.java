package constants;

public class WebConUrl {
	
	public static final String LOGIN_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/mobLogin.jsp";
	public static final String INSERT_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/InsertData.jsp";
	public static final String DEVICE_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/insertDeviceDetails.jsp";
//	public static final String SERIAL_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/check_simserial.jsp";
//	public static final String PHONE_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/get_backup_phone.jsp";
	public static final String BACKUP_URL="http://"+ServerSetting.SERVER_IP+":"+ServerSetting.SERVER_PORT+"/"+ServerSetting.SERVER_NAME+"/backup_contact.jsp";
}
