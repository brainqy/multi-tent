package com.brainqy.steps;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 27-02-2025
 */
import com.brainqy.api.YtmsApplication;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
//
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.en.*;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = YtmsApplication.class)
public class UserSteps {


    @Given("a user with email exists")
    public void aUserWithEmailExists() {
        boolean test = true;

        assertTrue(test);
    }

    @When("I search for the user with email")
    public void iSearchForUserWithEmail() {
//write code here that turns the phrase above into concrete actions
        assertTrue(true);
    }

    @Then("I should get the user details with name")
    public void iShouldGetUserDetailsWithName() {
        assertTrue(true);
          }
}

