package com.madcatworld.e_riqabguru.model;

import androidx.annotation.NonNull;

public class SubjectModelEdit {

    private int id;
    private String subject;

    public SubjectModelEdit() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return subject;
    }
}
