package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class getInfoDetailList {
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
		private Info info;

		public Info getInfo() {
			return info;
		}

		public void setInfo(Info info) {
			this.info = info;
		}

		public static class Info {

			private String id;
			private String title;
			private String view_num;
			private String comment_num;
			private String last_modify_time;
			private String signup_endtime;
			private int collect;
			private int signup;
			private String tzno;


			public String getSignup_endtime() {
				return signup_endtime;
			}

			public void setSignup_endtime(String signup_endtime) {
				this.signup_endtime = signup_endtime;
			}

			public int getSignup() {
				return signup;
			}

			public void setSignup(int signup) {
				this.signup = signup;
			}

			private String content;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public String getView_num() {
				return view_num;
			}

			public void setView_num(String view_num) {
				this.view_num = view_num;
			}

			public String getComment_num() {
				return comment_num;
			}

			public void setComment_num(String comment_num) {
				this.comment_num = comment_num;
			}

			public String getLast_modify_time() {
				return last_modify_time;
			}

			public void setLast_modify_time(String last_modify_time) {
				this.last_modify_time = last_modify_time;
			}

			public int getCollect() {
				return collect;
			}

			public void setCollect(int collect) {
				this.collect = collect;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public String getTzno() {
				return tzno;
			}

			public void setTzno(String tzno) {
				this.tzno = tzno;
			}
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

}
