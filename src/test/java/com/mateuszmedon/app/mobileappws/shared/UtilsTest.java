package com.mateuszmedon.app.mobileappws.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.rmi.CORBA.Util;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testGenerateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue( !userId.equalsIgnoreCase(userId2) );
    }

    @Test
    final void testHasTokenNotExpired() {
        String token = utils.generateVerificationToken("2345jkk2l554kjl");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    //only if have a token expired
//    @Test
//    final void testHasTokenExpired() {
//
//        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MUB0ZXN0LmNvbSIsImV4cCI6MTUzMjc3Nzc3NX0.cdudUo3pwZLN9UiTuXiT7itpaQs6BgUPU0yWbNcz56-l1Z0476N3H_qSEHXQI5lUfaK2ePtTWJfROmf0213UJA";
//
//        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
//
//        assertTrue(hasTokenExpired);
//    }
}