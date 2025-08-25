package com.pragma.powerup.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class HexagonalArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void loadClasses() {
        classes = new ClassFileImporter().importPackages("com.pragma.powerup");
    }

    @Test
    @DisplayName("Domain must not depend on application or infrastructure")
    void domainDoesNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..application..", "..infrastructure..", "..configuration..")
                .because("Domain must be pure and independent");
        rule.check(classes);
    }

    @Test
    @DisplayName("Application must not depend on infrastructure")
    void applicationDoesNotDependOnInfrastructure() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..")
                .because("Application orchestrates domain and should not know infrastructure");
        rule.check(classes);
    }

    @Test
    @DisplayName("No cyclic dependencies between packages")
    void noCyclicDependencies() {
        ArchRule rule = slices().matching("com.pragma.powerup.(*)..")
                .should().beFreeOfCycles();
        rule.check(classes);
    }
}
