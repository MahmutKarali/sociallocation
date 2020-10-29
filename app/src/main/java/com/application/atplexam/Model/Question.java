package com.application.atplexam.Model;

public class Question {
    public final Integer autoid;
    public final Integer id;
    public final Integer orjid;
    public final Integer ans;
    public final Integer grp;
    public final String src;
    public final Integer sbjid;
    public final String qs;
    public final String a;
    public final String b;
    public final String c;
    public final String d;
    public final String figure;

    public Question(Integer autoid,
                    Integer id,
                    Integer orjid,
                    Integer ans,
                    Integer grp,
                    String src,
                    Integer sbjid,
                    String qs,
                    String a,
                    String b,
                    String c,
                    String d,
                    String figure
    ) {
        this.autoid = autoid;
        this.id = id;
        this.orjid = orjid;
        this.ans = ans;
        this.grp = grp;
        this.src = src;
        this.sbjid = sbjid;
        this.qs = qs;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.figure = figure;
    }
}