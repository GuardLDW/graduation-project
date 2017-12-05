package com.bjut.cyl.kfyrip.bean;

import java.util.List;

/**
 * Created by Che on 16/6/21.
 */
public class OfficeBean {
    private int code;
    private String message;
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result{
        public List<Model> yld;
        public List<Model> zhsw;
        public List<Model> zx;
        public List<Model> jskf;
        public List<Model> jg;
        public List<Model> xtcx;
        public List<Model> rwsk;
        public List<Model> kx;
        public List<Model> zhcs;
        public List<Model> zscq;

        public static class Model{
            public String name;
            public String comment;
            public String location;
            public String tel;
            public String email;
            public String office;

        }
    }
}
