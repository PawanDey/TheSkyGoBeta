package com.global.travel.telecom.app.model;


public class RecentSetDataModel {

    private String create_time, duration, leg2, outcome, retail_charge;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLeg2() {
        return leg2;
    }

    public void setLeg2(String leg2) {
        this.leg2 = leg2;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getRetail_charge() {
        return retail_charge;
    }

    public void setRetail_charge(String retail_charge) {
        this.retail_charge = retail_charge;
    }


    public RecentSetDataModel(String create_time, String duration, String leg2, String outcome, String retail_charge) {
        this.create_time = create_time;
        this.duration = duration;
        this.leg2 = leg2;
        this.outcome = outcome;
        this.retail_charge = retail_charge;

    }
}
