package com.root.signaturehandler.domain.interfaces;

public interface MailHandlerModel {
    void sendDocumentMailMessage(String emailTo, String documentUrl);
}
