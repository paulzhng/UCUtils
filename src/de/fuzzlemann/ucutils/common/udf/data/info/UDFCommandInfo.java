package de.fuzzlemann.ucutils.common.udf.data.info;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@Entity
public class UDFCommandInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private Set<UDFCommandDescription> commandDescriptions = new HashSet<>();

    public UDFCommandInfo() {
    }

    public UDFCommandInfo(Set<UDFCommandDescription> commandDescriptions) {
        this.commandDescriptions = commandDescriptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<UDFCommandDescription> getCommandDescriptions() {
        return commandDescriptions;
    }

    public void setCommandDescriptions(Set<UDFCommandDescription> commandDescriptions) {
        this.commandDescriptions = commandDescriptions;
    }
}
