package itmo.apkvt.ruchyov.smartboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Entry {

    public Entry() {
    }

    public Entry(String name, String content, String contentType, long entryPosition, Project project) {
        this.name = name;
        if (contentType.equals("TEXT")) {
            this.entryText = content;
        } else if (contentType.equals("IMAGE")) {
            this.imagePath = content;
        }
        this.contentType = contentType;
        this.entryPosition = entryPosition;
        this.project = project;
    }

    public Entry(String name, String contentType, long entryPosition, String css, Project project) {
        this.name = name;
        this.contentType = contentType;
        this.entryPosition = entryPosition;
        this.css = css;
        this.project = project;
        this.tableRowList = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entryId;

    @Column
    private String name;

    @Column
    private String imagePath;

    @Column
    private String entryText;

    @Column
    private String contentType;

    @Column
    private long entryPosition;

    @Column
    private String css;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @OneToMany(mappedBy = "entry")
    private List<TableRow> tableRowList;

    public long getEntryId() {
        return entryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEntryText() {
        return entryText;
    }

    public void setEntryText(String entryText) {
        this.entryText = entryText;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getEntryPosition() {
        return entryPosition;
    }

    public void setEntryPosition(long entryPosition) {
        this.entryPosition = entryPosition;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    public List<TableRow> getTableRowList() {
        return tableRowList;
    }

    public void setTableRowList(List<TableRow> tableRowList) {
        this.tableRowList = tableRowList;
    }


    @Override
    public String toString() {
        return "Entry{" +
                "entryId=" + entryId +
                ", name='" + name + '\'' +
                ", entryPosition=" + entryPosition;
    }
}
