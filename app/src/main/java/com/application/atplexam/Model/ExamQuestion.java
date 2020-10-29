package com.application.atplexam.Model;

public class ExamQuestion {
    public final Integer examid;
    public final Integer questionid;
    public final Integer subjectid;

    public ExamQuestion(Integer examid,
                        Integer questionid,
                        Integer subjectid
    ) {
        this.examid = examid;
        this.questionid = questionid;
        this.subjectid = subjectid;
    }

    public ExamQuestion(Integer questionid,
                        Integer subjectid
    ) {
        this.examid = 0;
        this.questionid = questionid;
        this.subjectid = subjectid;
    }
}