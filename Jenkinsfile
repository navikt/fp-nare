import groovy.json.JsonSlurperClassic


timestamps {
    def skipITests = '-DskipITs'
    def skipUTests = '-DskipUTs'

    properties([disableConcurrentBuilds(), parameters([
            booleanParam(defaultValue: true, description: '', name: 'build'),
            booleanParam(defaultValue: false, description: '', name: 'skip_UTests'),
            booleanParam(defaultValue: false, description: '', name: 'skip_ITests'),]),
                pipelineTriggers([pollSCM('')])
    ])

    if (!params.skip_UTests) {
        skipUTests = ''
    }

    if (!params.skip_ITests) {
        skipITests = ''
    }

    node () {
        try {
            env.LANG = "nb_NO.UTF-8"

            stage("Init") {
                printStage("Init")
                env.JAVA_HOME = "${tool 'jdk-1.8'}"
                env.PATH = "${tool 'default-maven'}/bin:${env.PATH}"
                step([$class: 'WsCleanup'])
                checkout scm
            }

            if (params.build) {

                stage("Build") {

                    printStage("Build")
                    configFileProvider(
                            [configFile(fileId: 'navMavenSettingsUtenProxy', variable: 'MAVEN_SETTINGS')]) {
                        sh 'mvn -s $MAVEN_SETTINGS ' + skipUTests + ' ' + skipITests + ' clean deploy'
                    }

                    if (!skipITests) {
                        publishHTML(target: [
                                allowMissing         : true,
                                alwaysLinkToLastBuild: false,
                                keepAll              : true,
                                reportDir            : '**/target/failsafe-reports',
                                reportFiles          : '*.html',
                                reportName           : "Failsafe Report"
                        ])
                    }
                }

                info("Build")
            }


        } catch(error) {
            emailext (
                    subject: "[AUTOMAIL] Feilet jobb ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                    body: "<p>Hei,<br><br>har du til til å ta en titt på hva som kan være feil?<br>" +
                            "<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a><br><br>" +
                            "Tusen takk på forhånd,<br>Miljø</p>",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
            throw error
        }
    }
}


void info(msg) {
    ansiColor('xterm') {
        println "\033[45m\033[37m " + msg + " \033[0m"
    }
    currentBuild.description = msg
}
void printStage(stage) {
    ansiColor('xterm') {
        println "\033[46m Entered stage " + stage + " \033[0m"
    }
}