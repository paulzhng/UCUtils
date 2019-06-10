package de.fuzzlemann.ucutils.utils.noobchat;

/**
 * @author Fuzzlemann
 */
public class NoobChatAnswer {

    private final String answerKey;
    private final String[] answers;

    public NoobChatAnswer(String answerKey, String[] answers) {
        this.answerKey = answerKey;
        this.answers = answers;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public String[] getAnswers() {
        return answers;
    }
}
