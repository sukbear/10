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
		user.setName("С��");
		user.setAge(20);
		user.setEmail("781546821@qq.com");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		user.setBirthday(format.parse("1996-10-1"));
		user.setRole(role);

		User user1 = new User();
		user1.setName("С��");
		user1.setAge(20);
		user1.setEmail("781546821@qq.com");
		user1.setBirthday(format.parse("1996-10-1"));
		user1.setRole(role);

		/**
		 * ObjectMapper��JSON�����ĺ��ģ�Jackson������JSON����������ObjectMapper��ʵ�֡�
		 * ObjectMapper�ж��JSON���л��ķ��������԰�JSON�ַ�������File��OutputStream�Ȳ�ͬ�Ľ����С�
		 * writeValue(File arg0, Object arg1)��arg1ת��json���У������浽arg0�ļ��С�
		 * writeValue(OutputStream arg0, Object arg1)��arg1ת��json���У������浽arg0������С�
		 * writeValueAsBytes(Object arg0)��arg0ת��json���У����ѽ��������ֽ����顣
		 * writeValueAsString(Object arg0)��arg0ת��json���У����ѽ��������ַ�����
		 */

		ObjectMapper mapper = new ObjectMapper();
		// ����FAIL_ON_EMPTY_BEANS���ԣ�����Jackson�ն���Ҫ���쳣��,getter��setterҪ��
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		String json = mapper.writeValueAsString(user);
		System.out.println(json);

		// byte[] bytes = mapper.writeValueAsBytes(user);

		// Java����תJSON (�������)
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user1);
		String jsonList = mapper.writeValueAsString(users);
		System.out.println(jsonList);

		User jsonToUser = mapper.readValue(json, User.class);
		
		System.out.println(jsonToUser.toString());
	}
}
