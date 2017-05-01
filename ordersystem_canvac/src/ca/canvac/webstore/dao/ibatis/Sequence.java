package ca.canvac.webstore.dao.ibatis;

import java.io.Serializable;

public class Sequence implements Serializable {

    /* Private Fields */

    private String name;
    private int nextId;

    /* Constructors */

    public Sequence() {
    }

    public Sequence(String name, int nextId) {
        this.name = name;
        this.nextId = nextId;
    }

    /* JavaBeans Properties */

    public String getName() {
        return name;
    }

    public int getNextId() {
        return nextId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

}
