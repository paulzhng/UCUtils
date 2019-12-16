package de.fuzzlemann.ucutils.common.udf.data.supporter.violation;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@Entity
public class Violation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private String reason;
    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL)
    @Expose
    private List<Punishment> punishments;

    public Violation() {
    }

    public Violation(String reason, Punishment... punishments) {
        List<Punishment> punishmentList = new ArrayList<>();
        Collections.addAll(punishmentList, punishments);

        this.reason = reason;
        this.punishments = punishmentList;
    }

    public Violation(String reason, List<Punishment> punishments) {
        this.reason = reason;
        this.punishments = punishments;
    }

    public String getReason() {
        return reason;
    }

    public List<String> getCommands(String player) {
        return punishments.stream()
                .map(punishment -> punishment.parseCommand(player))
                .collect(Collectors.toList());
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

}
