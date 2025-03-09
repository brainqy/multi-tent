package com.brainqy.api;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 10-03-2025
 */
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.brainqy.api.services.ServiceImpls")
@IncludePackages("com.brainqy.api.services.ServiceImpls")
public class TestRunner {
}
