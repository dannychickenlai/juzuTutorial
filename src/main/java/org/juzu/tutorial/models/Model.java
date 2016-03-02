package org.juzu.tutorial.models;

import java.io.Serializable;
import java.util.Date;

public class Model implements Serializable {
    private String id;
    private Date createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}