package com.oivato.dscatalog.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchUnitTest {

    @Test
    void servicesShouldNotAccessControllers() {
        JavaClasses importedClasses = new ClassFileImporter()
                .importPackages("com.oivato.dscatalog");
        ArchRule rule = noClasses()
                .that().resideInAPackage("..services..")
                .should().accessClassesThat().resideInAPackage("..controllers..");
        rule.check(importedClasses);
    }

    @Test
    void controllersShouldNotAccessRepositories() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = noClasses()
                .that().resideInAPackage("..controllers..")
                .should().accessClassesThat().resideInAPackage("..repositories..");
        rule.check(importedClasses);
    }

    @Test
    void servicesShouldBeInServicePackage() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..services..");
        rule.check(importedClasses);
    }

    @Test
    void repositoriesShouldBeInterfaces() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = classes()
                .that().resideInAPackage("..repositories..")
                .should().beInterfaces();
        rule.check(importedClasses);
    }

    @Test
    void controllersShouldBeAnnotatedWithRestController() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = classes()
                .that().resideInAPackage("..controllers..")
                .should().beAnnotatedWith(RestController.class);
        rule.check(importedClasses);
    }

    @Test
    void internalPackageShouldNotBeAccessedFromOutside() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..internal..")
                .should().accessClassesThat().resideInAPackage("..internal..");
        rule.check(importedClasses);
    }

    @Test
    void domainShouldNotDependOnFrameworks() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.oivato.dscatalog");
        ArchRule rule = noClasses()
                .that().resideInAPackage("..entities..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "javax..");
        rule.check(importedClasses);
    }
}
