package service;

import java.time.LocalDate;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static String requireSafeText(String fieldName, String value, boolean required) {
        String normalized = value == null ? "" : value.trim();
        if (required && normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " é obrigatório.");
        }
        if (normalized.indexOf(';') >= 0 || normalized.indexOf('\r') >= 0 || normalized.indexOf('\n') >= 0) {
            throw new IllegalArgumentException(fieldName + " não pode conter ponto e vírgula ou quebra de linha.");
        }
        return normalized;
    }

    public static double parseNonNegativeFinite(String fieldName, String value) {
        String normalized = requireSafeText(fieldName, value, false);
        if (normalized.isEmpty()) {
            return 0.0;
        }
        try {
            double parsed = Double.parseDouble(normalized.replace(',', '.'));
            if (!Double.isFinite(parsed) || parsed < 0) {
                throw new NumberFormatException("non-finite or negative");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " deve ser um número finito e não negativo.");
        }
    }

    public static void validateContact(String phone, String email) {
        String normalizedPhone = phone == null ? "" : phone.replaceAll("\\D", "");
        String normalizedEmail = requireSafeText("E-mail", email, false);
        if (normalizedPhone.isEmpty() && normalizedEmail.isEmpty()) {
            throw new IllegalArgumentException("Informe um telefone completo ou um e-mail.");
        }
        if (!normalizedPhone.isEmpty() && normalizedPhone.length() != 11) {
            throw new IllegalArgumentException("O telefone deve conter 11 dígitos.");
        }
    }

    public static void validateLoanDates(LocalDate loanDate, LocalDate dueDate) {
        if (loanDate == null || dueDate == null) {
            throw new IllegalArgumentException("As datas de empréstimo e prevista são obrigatórias.");
        }
        if (dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("A data prevista não pode ser anterior à data do empréstimo.");
        }
    }
}
