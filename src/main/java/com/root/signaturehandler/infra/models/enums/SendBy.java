package com.root.signaturehandler.infra.models.enums;


public enum SendBy {
    PHONE("phone"), EMAIL("email");

    private final String sendBy;

    SendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public String getSendBy() {
        return this.sendBy;
    }
}
