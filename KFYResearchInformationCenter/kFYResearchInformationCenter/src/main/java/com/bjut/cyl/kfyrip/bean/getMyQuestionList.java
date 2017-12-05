package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class getMyQuestionList {
	private int code;
	private String message;
	private List<Result> result;

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

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public static class Result {
		private String id;
		private String title;
		private String last_modify_time;
		private String view_num;
		private String answer_num;

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

		public String getView_num() {
			return view_num;
		}

		public void setView_num(String view_num) {
			this.view_num = view_num;
		}

		public String getAnswer_num() {
			return answer_num;
		}

		public void setAnswer_num(String answer_num) {
			this.answer_num = answer_num;
		}

	}
}
