package me.sent1nel.criminalintent.app;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID id;
    private String title;
    private Date date;
    private boolean solved;


    public Crime() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void solve() {
        this.solved = true;
    }

    public void unsolve() {
        this.solved = false;
    }

    public boolean isSolved() {
        return solved;
    }
}
