package pl.lodz.p.it.masi.stp.chatbot.model.collections.logging;

import java.math.BigInteger;
import java.util.List;

public class MessageLog {

    private String userInput;
    private List<String> watsonIntent;
    private List<String> watsonOutput;
    private BigInteger resultsCount;

    public MessageLog() {
    }

    public MessageLog(String userInput, List<String> watsonIntent, List<String> watsonOutput, BigInteger resultsCount) {
        this.userInput = userInput;
        this.watsonIntent = watsonIntent;
        this.watsonOutput = watsonOutput;
        this.resultsCount = resultsCount;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public List<String> getWatsonIntent() {
        return watsonIntent;
    }

    public void setWatsonIntent(List<String> watsonIntent) {
        this.watsonIntent = watsonIntent;
    }

    public List<String> getWatsonOutput() {
        return watsonOutput;
    }

    public void setWatsonOutput(List<String> watsonOutput) {
        this.watsonOutput = watsonOutput;
    }

    public BigInteger getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(BigInteger resultsCount) {
        this.resultsCount = resultsCount;
    }

    @Override
    public String toString() {
        return "MessageLog{" +
                "userInput='" + userInput + '\'' +
                ", watsonIntent='" + watsonIntent + '\'' +
                ", watsonOutput='" + watsonOutput + '\'' +
                ", resultsCount=" + resultsCount +
                '}';
    }
}

