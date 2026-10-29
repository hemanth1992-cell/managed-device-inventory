plugins {
    id 'java-library'
}

dependencies {
    implementation project(':agent-core')
    implementation project(':receiver')
    testImplementation platform('org.junit:junit-bom:5.10.2')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}
