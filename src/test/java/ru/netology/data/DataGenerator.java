package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendRequest(DataGenerator.RegistrationDTo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when().log().all()
                .post("/api/system/users")
                .then().log().all()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return faker.name().username();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDTo getUser(String status) {
            return new RegistrationDTo(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDTo getRegisteredUser(String status) {
            var user = getUser(status);
            sendRequest(user);
            return user;
        }
    }

    @Value
    public static class RegistrationDTo {
        String login;
        String password;
        String status;
    }
}
