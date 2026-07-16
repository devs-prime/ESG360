package com.esg360.archtests;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import java.util.Set;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

/**
 * CI-enforced architecture rules. ADR-0004: "Architecture tests are what makes this real. Without
 * a failing build, module boundaries decay in weeks."
 *
 * <p>Scope is production code only ({@link ImportOption.DoNotIncludeTests}); every new feature
 * module must be added to this module's POM to be visible here.
 */
@AnalyzeClasses(packages = "com.esg360", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureRulesTest {

    // ---------------------------------------------------------------------
    // ADR-0006 / BR-002: all arithmetic is decimal — never binary float.
    // Fast-feedback copy lives in Checkstyle (IllegalType); this is authoritative.
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule binary_floating_point_is_banned = classes()
            .that()
            .resideInAPackage("com.esg360..")
            .should(notUseBinaryFloatingPoint())
            .because("quantities and money are exact decimals (ADR-0006, BR-001/BR-002); "
                    + "use the shared-kernel Quantity type");

    // ---------------------------------------------------------------------
    // ADR-0004: the shared kernel depends on nothing but the JDK.
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule shared_kernel_depends_only_on_jdk = classes()
            .that()
            .resideInAPackage("com.esg360.sharedkernel..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage("com.esg360.sharedkernel..", "java..")
            .because("the shared kernel is the small FK-able core every module may use "
                    + "(ADR-0004); it must never acquire framework or module dependencies");

    // ---------------------------------------------------------------------
    // ADR-0004: no feature module reads another module's internals.
    // Feature modules live under com.esg360.modules.<name>; interface calls or
    // domain events only. When the first legitimate cross-module API call appears,
    // refine this rule to ignore ..api.. packages rather than deleting it.
    // ---------------------------------------------------------------------

    @ArchTest
    static final ArchRule feature_modules_do_not_depend_on_each_other = slices().matching("com.esg360.modules.(*)..")
            .should()
            .notDependOnEachOther()
            .because("modules own their data and communicate via interfaces or domain events " + "(ADR-0004, §36)");

    private static ArchCondition<JavaClass> notUseBinaryFloatingPoint() {
        Set<String> banned = Set.of("float", "double", "java.lang.Float", "java.lang.Double");
        return new ArchCondition<>("not declare fields, parameters or return types of " + "float/double/Float/Double") {
            @Override
            public void check(JavaClass clazz, ConditionEvents events) {
                clazz.getFields().stream()
                        .filter(field -> banned.contains(field.getRawType().getName()))
                        .forEach(field -> events.add(SimpleConditionEvent.violated(
                                field,
                                "%s declares banned type %s"
                                        .formatted(
                                                field.getFullName(),
                                                field.getRawType().getName()))));
                clazz.getMethods().forEach(method -> {
                    if (banned.contains(method.getRawReturnType().getName())) {
                        events.add(SimpleConditionEvent.violated(
                                method,
                                "%s returns banned type %s"
                                        .formatted(
                                                method.getFullName(),
                                                method.getRawReturnType().getName())));
                    }
                    method.getRawParameterTypes().stream()
                            .filter(parameter -> banned.contains(parameter.getName()))
                            .forEach(parameter -> events.add(SimpleConditionEvent.violated(
                                    method,
                                    "%s takes banned parameter type %s"
                                            .formatted(method.getFullName(), parameter.getName()))));
                });
                clazz.getConstructors().forEach(constructor -> constructor.getRawParameterTypes().stream()
                        .filter(parameter -> banned.contains(parameter.getName()))
                        .forEach(parameter -> events.add(SimpleConditionEvent.violated(
                                constructor,
                                "%s takes banned parameter type %s"
                                        .formatted(constructor.getFullName(), parameter.getName())))));
            }
        };
    }
}
