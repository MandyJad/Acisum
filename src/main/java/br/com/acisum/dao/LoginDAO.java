package br.com.acisum.dao;

//import org.json.JSONObject;
import br.com.acisum.util.RestUtil;

public class LoginDAO {
	
	public static void main(String[] args) {

		
		String user = "mandy_jad";
		String senha = "mandy1996";
		String email = "mandy_jad@hotmail.com";

		 System.out.println(RestUtil.consumeServiceBackforApp("https://parseapi.back4app.com/login?username="+user+"&password="+senha,
		 "GET","" ));

//		JSONObject obj = new JSONObject();
//		obj.put("username", user);
//		obj.put("password", senha);
//		obj.put("email", email);

		//System.out.println(
			//	RestUtil.consumeServiceBackforApp("https://parseapi.back4app.com/users", "POST", obj.toString()));

		/*
		 * EventQueue.invokeLater(new Runnable() { public void run() { try { MainView
		 * frame = new MainView(); frame.setVisible(true); } catch (Exception e) {
		 * e.printStackTrace(); } } });
		 */
	}

}
