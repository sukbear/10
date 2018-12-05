package jsontest;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Serializable {
	@JsonProperty("UserName") // 属性序列化时的名字
	private String name;
	private Integer age;
	//@JsonIgnore // 序列化时忽略该属性
	private Role role;

	@JsonFormat(pattern = "yyyy年MM月dd日") // 序列化格式
	private Date birthday;
	private String email;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "name :" 
	+ this.name + "age:" 
				+ this.age 
				+ "email :" 
				+ this.email+ "birthday: "
				+this.birthday
				+"role:"+this.role.getName();
	}
}
