package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class search {
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
		@JsonProperty("tz.list")
		private List<Tz> tz;
		@JsonProperty("xw.list")
		private List<Xw> xw;
		@JsonProperty("question.list")
		private List<Question> question;

		public List<Tz> getTz() {
			return tz;
		}

		public void setTz(List<Tz> tz) {
			this.tz = tz;
		}

		public List<Xw> getXw() {
			return xw;
		}

		public void setXw(List<Xw> xw) {
			this.xw = xw;
		}

		public List<Question> getQuestion() {
			return question;
		}

		public void setQuestion(List<Question> question) {
			this.question = question;
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

	public static class Tz {
		private String title;
		private String last_modify_time;
		private String id;

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

		public String getLast_modify_time() {
			return last_modify_time;
		}

		public void setLast_modify_time(String last_modify_time) {
			this.last_modify_time = last_modify_time;
		}

	}

	public static class Xw {
		private String title;
		private String last_modify_time;
		private String id;

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

		public String getLast_modify_time() {
			return last_modify_time;
		}

		public void setLast_modify_time(String last_modify_time) {
			this.last_modify_time = last_modify_time;
		}

	}

	public static class Question {
		private String title;
		private String id;

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

	}

}
