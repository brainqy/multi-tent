package com.brainqy.hooks;
/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 01-03-2025
 */
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;

public class LoginHook {

    @Before(order = 0)  // Runs before all scenarios
    public void globalSetup() {
        System.out.println("Logging in once before all tests...");
        // Add login logic here if needed
    }

    @After(order = 0)  // Runs after all scenarios
    public void globalTeardown() {
        System.out.println("Logging out after all tests...");
        // Add logout logic here if needed
    }
}
