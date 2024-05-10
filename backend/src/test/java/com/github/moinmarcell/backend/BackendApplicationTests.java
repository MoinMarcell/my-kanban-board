package com.github.moinmarcell.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the application context loads successfully
    }

    @Test
    void testMain() {
        // This test ensures that the main method runs successfully
        BackendApplication.main(new String[]{});
        // No assertions are needed as we are just verifying the main method does not throw any exceptions
    }

}
