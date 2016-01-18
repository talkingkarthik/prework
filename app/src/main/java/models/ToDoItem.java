package models;

import java.util.Date;

/**
 * Created by kartikkulkarni on 1/16/16.
 */
public class ToDoItem {
    private String title;
    private String text;
    private Date timestamp;
    private boolean highpri;
    private String docid;
    private boolean dirty;
    private boolean newly;

    public ToDoItem(String title, String text, Date ts, boolean highpri, boolean isnew, String docid) {
        this.title = title;
        this.text = text;
        this.timestamp = ts;
        this.highpri = highpri;
        if (isnew) {
            this.newly = true;
            this.dirty = true;
        } else {
            this.newly = false;
            this.dirty = false;
            this.docid = docid;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        setDirty(true);

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setDirty(true);

    }

    public boolean isHighpri() {
        return highpri;
    }

    public void setHighpri(boolean highpri) {
        this.highpri = highpri;
        setDirty(true);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        setDirty(true);

    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public boolean isNewly() {
        return newly;
    }

    public void setNewly(boolean newly) {
        this.newly = newly;
    }
}
