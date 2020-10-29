package com.application.atplexam.Model;

public class UserLog {
    public final Integer id;
    public final Integer examid;
    public final Integer questionid;
    public final String useranswer;
    public final Integer iscorrect;

    public UserLog(Integer id,
                   Integer examid,
                   Integer questionid,
                   String useranswer,
                   Integer iscorrect
    ) {
        this.id = id;
        this.questionid = questionid;
        this.useranswer = useranswer;
        this.examid = examid;
        this.iscorrect = iscorrect;
    }
}