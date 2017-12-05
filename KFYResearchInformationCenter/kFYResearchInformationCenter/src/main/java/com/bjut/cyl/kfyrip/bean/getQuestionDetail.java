package com.bjut.cyl.kfyrip.bean;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class getQuestionDetail {
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
		@JsonProperty("answer_vip.list")
		private List<Datavip> datavip;
		@JsonProperty("answer_normal.list")
		private List<Datanormal> datanormal;
		private Question question;

		public Question getQuestion() {
			return question;
		}

		public void setQuestion(Question question) {
			this.question = question;
		}

		

		public List<Datavip> getDatavip() {
			return datavip;
		}

		public void setDatavip(List<Datavip> datavip) {
			this.datavip = datavip;
		}

		public List<Datanormal> getDatanormal() {
			return datanormal;
		}

		public void setDatanormal(List<Datanormal> datanormal) {
			this.datanormal = datanormal;
		}



		public static class Question {
			private String id;
			private String title;
			private String content;
			private String view_num;
			private String answer_num;
			private String last_modify_time;
			private String user_id;
			private String user_nickname;
			private int collect;

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

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
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

			public String getLast_modify_time() {
				return last_modify_time;
			}

			public void setLast_modify_time(String last_modify_time) {
				this.last_modify_time = last_modify_time;
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

			public int getCollect() {
				return collect;
			}

			public void setCollect(int collect) {
				this.collect = collect;
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

	public static class Datavip {
		private String id;
		private String content;
		private String last_modify_time;
		private String user_id;
		private String user_nickname;
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

		public String getLast_modify_time() {
			return last_modify_time;
		}

		public void setLast_modify_time(String last_modify_time) {
			this.last_modify_time = last_modify_time;
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

		public String getUser_vip() {
			return user_vip;
		}

		public void setUser_vip(String user_vip) {
			this.user_vip = user_vip;
		}

	}
	
	public static class Datanormal {
		private String id;
		private String content;
		private String last_modify_time;
		private String user_id;
		private String user_nickname;
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

		public String getLast_modify_time() {
			return last_modify_time;
		}

		public void setLast_modify_time(String last_modify_time) {
			this.last_modify_time = last_modify_time;
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

		public String getUser_vip() {
			return user_vip;
		}

		public void setUser_vip(String user_vip) {
			this.user_vip = user_vip;
		}

	}
}
