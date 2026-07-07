package app.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class InputValidator {
    public static void numberOnly(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void letterOnly(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z]*")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void letterWithNumber(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9]*")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void letterWithNumberWithDotUnderLine(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9._]*")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void letterWithSpace(TextField field) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z ]*")) {
                return change;
            }
            return null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }

    public static boolean isThisAPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            return false;
        }
        return phoneNumber.startsWith("09");
    }
}
