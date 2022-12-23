package com.example.cinematicket.Model;

import java.util.List;

public class FCMModel {
    private List<String> registration_ids;
    private NotificationModel notification;

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }

    public FCMModel() {
    }

    public FCMModel(List<String> registration_ids, NotificationModel notification) {
        this.registration_ids = registration_ids;
        this.notification = notification;
    }
}
