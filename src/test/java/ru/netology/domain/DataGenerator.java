package ru.netology.domain;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        // спецификация нужна для того, чтобы переиспользовать настройки в разных запросах
        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        @BeforeAll
        static void setUpAll(RegistrationData registrationData) {
            given() // "дано"java -jar app-ibank.jar -P:profile=test
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(registrationData) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 OK
        }

        public static RegistrationData getValidActiveUser() {
            Faker faker = new Faker(new Locale("en"));
            RegistrationData registrationData = new RegistrationData(
                    faker.name().firstName(),
                    faker.internet().password(),
                    "active");
            setUpAll(registrationData);
            return registrationData;
        }

        public static RegistrationData getValidBlockedUser() {
            Faker faker = new Faker(new Locale("en"));
            RegistrationData registrationData = new RegistrationData(
                    faker.name().firstName(),
                    faker.internet().password(),
                    "blocked");
            setUpAll(registrationData);
            return registrationData;
        }

        public static RegistrationData getUserWithoutRegistration() {
            Faker faker = new Faker(new Locale("en"));
            RegistrationData registrationData = new RegistrationData(
                    faker.name().firstName(),
                    faker.internet().password(),
                    "active");
            return registrationData;
        }
    }
}
