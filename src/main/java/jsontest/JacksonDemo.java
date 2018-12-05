package jsontest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonDemo {
	public static void main(String[] args) throws ParseException, IOException {
		Role role = new Role("admin");
		/*
		 * ObjectMapper roleMapper = new ObjectMapper(); String roleJson =
		 * roleMapper.writeValueAsString(role);
		 */

		User user = new User();
		user.setName("小明");
		user.setAge(20);
		user.setEmail("781546821@qq.com");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		user.setBirthday(format.parse("1996-10-1"));
		user.setRole(role);

		User user1 = new User();
		user1.setName("小红");
		user1.setAge(20);
		user1.setEmail("781546821@qq.com");
		user1.setBirthday(format.parse("1996-10-1"));
		user1.setRole(role);

		/**
		 * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
		 * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
		 * writeValue(File arg0, Object arg1)把arg1转成json序列，并保存到arg0文件中。
		 * writeValue(OutputStream arg0, Object arg1)把arg1转成json序列，并保存到arg0输出流中。
		 * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
		 * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串。
		 */

		ObjectMapper mapper = new ObjectMapper();
		// 设置FAIL_ON_EMPTY_BEANS属性，告诉Jackson空对象不要抛异常；,getter和setter要有
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String json = mapper.writeValueAsString(user);
		System.out.println(json);

		// byte[] bytes = mapper.writeValueAsBytes(user);

		// Java集合转JSON (多个对象)
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user1);
		String jsonList = mapper.writeValueAsString(users);
		System.out.println(jsonList);

		User jsonToUser = mapper.readValue(json, User.class);
		
		System.out.println(jsonToUser.toString());
	}
}
