package be.infowhere.moviebatles.dto;

import java.util.Objects;

public class RankingDto {
    private String user;
    private double numQuestionsAnswer=0.0;
    private double numQtdCorrect=0.0;
    private double percentAnswerCorrect=0.0;

    public RankingDto() {
    }

    public RankingDto(String user) {
        this.user = user;
    }

    public RankingDto(String user, double numQuestionsAnswer, double numQtdCorrect) {
        this.user = user;
        this.numQuestionsAnswer = numQuestionsAnswer;
        this.numQtdCorrect = numQtdCorrect;
    }

    public double getPercentAnswerCorrect() {
        return percentAnswerCorrect;
    }

    public void setPercentAnswerCorrect(double percentAnswerCorrect) {
        this.percentAnswerCorrect = percentAnswerCorrect;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNumQuestionsAnswer(long numQuestionsAnswer) {
        this.numQuestionsAnswer = numQuestionsAnswer;
    }

    public void setNumQtdCorrect(long numQtdCorrect) {
        this.numQtdCorrect = numQtdCorrect;
    }

    public double getNumQuestionsAnswer() {
        return numQuestionsAnswer;
    }

    public void setNumQuestionsAnswer(double numQuestionsAnswer) {
        this.numQuestionsAnswer = numQuestionsAnswer;
    }

    public double getNumQtdCorrect() {
        return numQtdCorrect;
    }

    public void setNumQtdCorrect(double numQtdCorrect) {
        this.numQtdCorrect = numQtdCorrect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankingDto that = (RankingDto) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
