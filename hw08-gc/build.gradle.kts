dependencies {
    implementation("ch.qos.logback:logback-classic")
}

tasks.register<JavaExec>("runCalcDemo") {
    mainClass.set("ru.otus.homework.CalcDemo")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "-Xms128m",
        "-Xmx128m",
        "-XX:+HeapDumpOnOutOfMemoryError",
        "-XX:HeapDumpPath=logs/heapdump.hprof",
        "-XX:+UseG1GC",
        "-Dfile.encoding=UTF-8"
    )
}