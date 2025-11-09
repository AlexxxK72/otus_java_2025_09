import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("org.ow2.asm:asm-commons")
    implementation ("ch.qos.logback:logback-classic")
    //lombok
    implementation("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
}

tasks {
    create<ShadowJar>("asmJar") {
        archiveBaseName.set("asmDemo")
        archiveVersion.set("")
        archiveClassifier.set("")
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "ru.otus.aop.asm.AsmDemo",
                    "Premain-Class" to "ru.otus.aop.asm.Agent"
                )
            )
        }
        from(sourceSets.main.get().output)
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }
}