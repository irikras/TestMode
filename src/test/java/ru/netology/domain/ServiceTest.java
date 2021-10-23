package ru.netology.domain;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ServiceTest {
    @BeforeEach
    void Setup() {
       open("http://localhost:9999");
    }

    @Test
    void shouldSubmitActiveUser() {
        RegistrationData validActiveUser = DataGenerator.Registration.getValidActiveUser();
        $("[data-test-id=login] input").setValue(validActiveUser.getLogin());
        $("[data-test-id=password] input").setValue(validActiveUser.getPassword());
        $(".button__text").click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitBlockedUser() {
        RegistrationData validBlockedUser = DataGenerator.Registration.getValidBlockedUser();
        $("[data-test-id=login] input").setValue(validBlockedUser.getLogin());
        $("[data-test-id=password] input").setValue(validBlockedUser.getPassword());
        $(".button__text").click();
        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible,Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitIncorrectLogin() {
        RegistrationData userWithoutRegistration = DataGenerator.Registration.getUserWithoutRegistration();
        $("[data-test-id=login] input").setValue("Логин");
        $("[data-test-id=password] input").setValue(userWithoutRegistration.getPassword());
        $(".button__text").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldSubmitIncorrectPassword() {
        RegistrationData userWithoutRegistration = DataGenerator.Registration.getUserWithoutRegistration();
        $("[data-test-id=login] input").setValue(userWithoutRegistration.getLogin());
        $("[data-test-id=password] input").setValue("Пароль");
        $(".button__text").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
