package com.example.karim.MedicalRep.model;

public class Order {
    String orderValue,placeName,location,date,time,keyPersonName,keyPersonEmail,keyPersonPhoneNumber,keyPersonComments,keyPersonTitle,
    contactPersonName,contactPersonPhoneNumber,contatTitle,contactEmail,contactComments,placState,visitState,feedback;

    Double longtuide ,lituide;

    public String getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(String orderValue) {
        this.orderValue = orderValue;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactComments() {
        return contactComments;
    }

    public void setContactComments(String contactComments) {
        this.contactComments = contactComments;
    }

    public String getVisitState() {
        return visitState;
    }

    public void setVisitState(String visitState) {
        this.visitState = visitState;
    }

    public Double getLongtuide() {
        return longtuide;
    }

    public void setLongtuide(Double longtuide) {
        this.longtuide = longtuide;
    }

    public Double getLituide() {
        return lituide;
    }

    public void setLituide(Double lituide) {
        this.lituide = lituide;
    }

    boolean seen;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getContatTitle() {
        return contatTitle;
    }

    public void setContatTitle(String contatTitle) {
        this.contatTitle = contatTitle;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyPersonName() {
        return keyPersonName;
    }

    public void setKeyPersonName(String keyPersonName) {
        this.keyPersonName = keyPersonName;
    }

    public String getKeyPersonEmail() {
        return keyPersonEmail;
    }

    public void setKeyPersonEmail(String keyPersonEmail) {
        this.keyPersonEmail = keyPersonEmail;
    }

    public String getKeyPersonPhoneNumber() {
        return keyPersonPhoneNumber;
    }

    public void setKeyPersonPhoneNumber(String keyPersonPhoneNumber) {
        this.keyPersonPhoneNumber = keyPersonPhoneNumber;
    }

    public String getKeyPersonComments() {
        return keyPersonComments;
    }

    public void setKeyPersonComments(String keyPersonComments) {
        this.keyPersonComments = keyPersonComments;
    }

    public String getKeyPersonTitle() {
        return keyPersonTitle;
    }

    public void setKeyPersonTitle(String keyPersonTitle) {
        this.keyPersonTitle = keyPersonTitle;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhoneNumber() {
        return contactPersonPhoneNumber;
    }

    public void setContactPersonPhoneNumber(String contactPersonPhoneNumber) {
        this.contactPersonPhoneNumber = contactPersonPhoneNumber;
    }

    public String getPlacState() {
        return placState;
    }

    public void setPlacState(String placState) {
        this.placState = placState;
    }



    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
