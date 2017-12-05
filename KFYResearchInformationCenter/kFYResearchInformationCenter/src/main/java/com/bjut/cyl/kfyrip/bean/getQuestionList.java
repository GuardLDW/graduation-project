package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class getQuestionList {
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
		@JsonProperty("question.list")
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
		private String title;
		private String last_modify_time;
		private String view_num;
		private String answer_num;
		private Answer answer;

		public Answer getAnswer() {
			return answer;
		}

		public void setAnswer(Answer answer) {
			this.answer = answer;
		}

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

		public static class Answer {
			private String answer_userid;
			private String answer_nickname;
			private String answer_uservip;
			private String answer_usericon;
			private String answer_content;
			private String answer_time;

			

			public String getAnswer_userid() {
				return answer_userid;
			}

			public void setAnswer_userid(String answer_userid) {
				this.answer_userid = answer_userid;
			}

			public String getAnswer_nickname() {
				return answer_nickname;
			}

			public void setAnswer_nickname(String answer_nickname) {
				this.answer_nickname = answer_nickname;
			}

			public String getAnswer_uservip() {
				return answer_uservip;
			}

			public void setAnswer_uservip(String answer_uservip) {
				this.answer_uservip = answer_uservip;
			}

			public String getAnswer_usericon() {
				return answer_usericon;
			}

			public void setAnswer_usericon(String answer_usericon) {
				this.answer_usericon = answer_usericon;
			}

			public String getAnswer_content() {
				return answer_content;
			}

			public void setAnswer_content(String answer_content) {
				this.answer_content = answer_content;
			}

			public String getAnswer_time() {
				return answer_time;
			}

			public void setAnswer_time(String answer_time) {
				this.answer_time = answer_time;
			}

		}

	}
}
