package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class getAnswerList {
	private int code;
	private String message;
	private Result result;

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public static class Result {
		@JsonProperty("answer.list")
		private List<Data> data;

		public List<Data> getData() {
			return data;
		}

		public void setData(List<Data> data) {
			this.data = data;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class Data {
		private String id;
		private String content;
		private String user_id;
		private String user_nickname;
		private String last_modify_time;
		private String user_vip;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getUser_nickname() {
			return user_nickname;
		}

		public void setUser_nickname(String user_nickname) {
			this.user_nickname = user_nickname;
		}

		public String getLast_modify_time() {
			return last_modify_time;
		}

		public void setLast_modify_time(String last_modify_time) {
			this.last_modify_time = last_modify_time;
		}

		public String getUser_vip() {
			return user_vip;
		}

		public void setUser_vip(String user_vip) {
			this.user_vip = user_vip;
		}
	}
}
