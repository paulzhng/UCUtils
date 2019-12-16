package de.fuzzlemann.ucutils.common.udf.data.supporter.beginnerchatanswer;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * @author Fuzzlemann
 */
@Entity
public class BeginnerChatAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private String answerKey;
    @Column(columnDefinition = "LONGTEXT")
    @Expose
    private String answer;

    public BeginnerChatAnswer() {
    }

    public BeginnerChatAnswer(String answerKey, String answer) {
        this.answerKey = answerKey;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String[] getLines() {
        return answer.split("\n");
    }
}
