package com.vivaldibank;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Testes de arquitetura que garantem as fronteiras da Arquitetura Hexagonal.
 *
 * <p>A regra central é a Regra de Dependência: as dependências de código-fonte
 * só podem apontar para dentro — em direção ao domínio. Nenhuma camada interna
 * pode conhecer camadas externas.</p>
 *
 * <pre>
 *  ┌─────────────────────────────────────────┐
 *  │            infrastructure               │
 *  │   ┌─────────────────────────────────┐   │
 *  │   │          application            │   │
 *  │   │   ┌─────────────────────────┐   │   │
 *  │   │   │         domain          │   │   │
 *  │   │   └─────────────────────────┘   │   │
 *  │   └─────────────────────────────────┘   │
 *  └─────────────────────────────────────────┘
 *  Dependências permitidas: de fora para dentro.
 *  Dependências proibidas: de dentro para fora.
 * </pre>
 */
@AnalyzeClasses(
    packages = "com.vivaldibank",
importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAnyPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..");

    @ArchTest
    static final ArchRule application_should_not_depend_on_infrastructure =
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");


    @ArchTest
    static final ArchRule application_should_not_depend_on_spring =
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "org.springframework..",
                "jakarta.persistence..",
                "jakarta.transaction.."
                );

    @ArchTest
    static final ArchRule web_should_not_depend_on_persistence_adapter =
        noClasses()
            .that().resideInAPackage("..adapters.in.web..")
            .should().dependOnClassesThat().resideInAPackage("..adapters.out.persistence..");
}
