package itmo.apkvt.ruchyov.smartboard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TableRow {

    public TableRow() {

    }

    public TableRow(String rowText, Entry entry) {
        this.rowText = rowText;
        this.entry = entry;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String rowText;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "entryId")
    private Entry entry;

    public long getId() {
        return id;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
