package com.oivato.dscatalog.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchUnitTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setup() {
        importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
    }

    @Test
    void servicesShouldNotAccessControllers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..services..")
                .should().accessClassesThat().resideInAPackage("..controllers..");
        rule.check(importedClasses);
    }

    @Test
    void controllersShouldNotAccessRepositories() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..controllers..")
                .should().accessClassesThat().resideInAPackage("..repositories..");
        rule.check(importedClasses);
    }

    @Test
    void servicesShouldBeInServicePackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..services..");
        rule.check(importedClasses);
    }

    @Test
    void repositoriesShouldBeInterfaces() {
        ArchRule rule = classes()
                .that().resideInAPackage("..repositories..")
                .should().beInterfaces();
        rule.check(importedClasses);
    }

    @Test
    void controllersShouldBeAnnotatedWithRestController() {
        ArchRule rule = classes()
                .that().resideInAPackage("..controllers..")
                .should().beAnnotatedWith(RestController.class);
        rule.check(importedClasses);
    }

    @Test
    void internalPackageShouldNotBeAccessedFromOutside() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..internal..")
                .should().accessClassesThat().resideInAPackage("..internal..");
        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotDependOnFrameworks() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..entities..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "javax..");
        rule.check(importedClasses);
    }
}
