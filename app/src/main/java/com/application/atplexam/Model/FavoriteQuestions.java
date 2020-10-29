package com.application.atplexam.Model;

public class FavoriteQuestions {
    public final Integer id;
    public final Integer subjectid;
    public final Integer questionid;

    public FavoriteQuestions(Integer id,
                             Integer subjectid,
                             Integer questionid
    ) {
        this.id = id;
        this.subjectid = subjectid;
        this.questionid = questionid;
    }

    public FavoriteQuestions(Integer subjectid,
                             Integer questionid
    ) {
        this.id = 0;
        this.subjectid = subjectid;
        this.questionid = questionid;
    }
}