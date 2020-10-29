package com.application.atplexam.Model;

public class Exam {
    public final Integer id;
    public final String name;
    public final Integer qscount;
    public Integer answercount;
    public final Integer totaltime;
    public final String createdtime;
    public final Integer mode;
    public final Integer subjectid;
    public final Integer unviewedqs;
    public final Integer favorite;
    public final Integer timer;
    public Integer remainingtime;
    public Integer isEnded;

    public Exam(Integer id,
                String name,
                Integer qscount,
                Integer answercount,
                Integer totaltime,
                String createdtime,
                Integer mode,
                Integer subjectid,
                Integer unviewedqs,
                Integer favorite,
                Integer timer,
                Integer remainingtime,
                Integer isEnded
    ) {
        this.id = id;
        this.name = name;
        this.qscount = qscount;
        this.answercount = answercount;
        this.totaltime = totaltime;
        this.createdtime = createdtime;
        this.mode = mode;
        this.subjectid = subjectid;
        this.unviewedqs = unviewedqs;
        this.favorite = favorite;
        this.timer = timer;
        this.remainingtime = remainingtime;
        this.isEnded = isEnded;
    }

    public Exam(Integer id,
                Integer answercount,
                Integer unviewedqs,
                Integer remainingtime
    ) {
        this.id = id;
        this.answercount = answercount;
        this.unviewedqs = unviewedqs;
        this.remainingtime = remainingtime;
        name = null;
        qscount = 0;
        totaltime = 0;
        createdtime = null;
        mode = 0;
        subjectid = 0;
        favorite = 0;
        timer = 0;
    }

    public Exam(Integer id,
                Integer qscount
    ) {
        this.id = id;
        this.qscount = qscount;
        this.answercount = 0;
        this.unviewedqs = 0;
        this.remainingtime = 0;
        name = null;
        totaltime = 0;
        createdtime = null;
        mode = 0;
        subjectid = 0;
        favorite = 0;
        timer = 0;
    }
}