package com.spoqa.hiringchallenge

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.Test

class ArchitectureTest {
    private val importedClasses =
        ClassFileImporter().importPackages("com.spoqa.hiringchallenge")

    @Test
    fun `layered architecture is enforced`() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Interfaces").definedBy("com.spoqa.hiringchallenge.interfaces..")
            .layer("Application").definedBy("com.spoqa.hiringchallenge.application..")
            .layer("Domain").definedBy("com.spoqa.hiringchallenge.domain..")
            .layer("Infrastructure").definedBy("com.spoqa.hiringchallenge.infrastructure..")
            .whereLayer("Interfaces").mayOnlyAccessLayers("Application", "Interfaces", "Domain")
            .whereLayer("Application").mayOnlyAccessLayers("Domain", "Application")
            .whereLayer("Domain").mayOnlyAccessLayers("Domain")
            .whereLayer("Infrastructure").mayOnlyAccessLayers("Domain", "Infrastructure")
            .check(importedClasses)
    }
}
