package org.teste.casetecnico.form;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(@NotBlank
                        String login,
                        @NotBlank
                        String password) {
}
