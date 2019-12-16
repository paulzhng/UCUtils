package de.fuzzlemann.ucutils.common.udf.data.info;

import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@Entity
public class UDFCommandDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private String command;
    @Expose
    private String description;

    public UDFCommandDescription() {
    }

    public UDFCommandDescription(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UDFCommandDescription that = (UDFCommandDescription) o;
        return id == that.id &&
                Objects.equals(command, that.command) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, command, description);
    }
}
