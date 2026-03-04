package com.autosales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDto {

    @NotBlank(message = "Имя обязательно")
    @Pattern(regexp = "^[A-Za-zА-Яа-я]{2,}$", message = "Имя должно содержать минимум 2 буквы")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Pattern(regexp = "^[A-Za-zА-Яа-я]{2,}$", message = "Фамилия должна содержать минимум 2 буквы")
    private String lastName;

    private String patronymic; // отчество необязательно

    @NotBlank(message = "Email обязателен")
    @Email(message = "Введите корректный email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String matchingPassword;
}