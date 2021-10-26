package ru.netology.domain;

import com.codeborne.selenide.Condition;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.domain.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.domain.DataGenerator.Registration.getUser;
import static ru.netology.domain.DataGenerator.getRandomLogin;
import static ru.netology.domain.DataGenerator.getRandomPassword;

public class ServiceTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=login] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id=password] input").sendKeys(registeredUser.getPassword());
        $(".button__text").click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").sendKeys(notRegisteredUser.getPassword());
        $(".button__text").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").sendKeys(blockedUser.getLogin());
        $("[data-test-id=password] input").sendKeys(blockedUser.getPassword());
        $(".button__text").click();
        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").sendKeys(wrongLogin);
        $("[data-test-id=password] input").sendKeys(registeredUser.getPassword());
        $(".button__text").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").sendKeys(registeredUser.getLogin());
        $("[data-test-id=password] input").sendKeys(wrongPassword);
        $(".button__text").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}
