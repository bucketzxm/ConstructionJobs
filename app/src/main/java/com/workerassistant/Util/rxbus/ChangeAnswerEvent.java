package com.workerassistant.Util.rxbus;

/**
 * Created on 16/7/19.
 *
 * @author ice
 */
public class ChangeAnswerEvent {
    private String answer;
    private String target;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String mAnswer) {
        this.answer = mAnswer;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }
}
