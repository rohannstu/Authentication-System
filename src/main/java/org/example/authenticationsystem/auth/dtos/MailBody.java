package org.example.authenticationsystem.auth.dtos;

public record MailBody(String to, String subject, String text) {
}