@Library('vl-jenkins')_

import no.nav.jenkins.*

pipeline {
    agent none

    stages {
        stage("Bygg og deploy") {
            agent {
                node {
                    label 'DOCKER'
                }
            }
            when { branch 'master' }
            steps {
                checkout scm
                script {
                    Date date = new Date()
                    maven = new maven()
                    def GIT_COMMIT_HASH = sh(script: "git log -n 1 --pretty=format:'%h'", returnStdout: true)
                    def changelist = "_" + date.format("YYYYMMDDHHmmss") + "_" + GIT_COMMIT_HASH
                    def mRevision = maven.revision()
                    def tagName = mRevision + changelist
                    //def committer = sh(script: 'git log -1 --pretty=format:"%an (%ae)"', returnStdout: true).trim()
                    //def committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
                    //def changelog = sh(script: 'git log `git describe --tags --abbrev=0`..HEAD --oneline', returnStdout: true)
                    currentBuild.displayName = tagName

                    configFileProvider(
                            [configFile(fileId: 'navMavenSettings', variable: 'MAVEN_SETTINGS')]) {

                        buildEnvironment = new buildEnvironment()
                        if(maven.javaVersion() != null) {
                            buildEnvironment.overrideJDK(maven.javaVersion())
                        }

                        sh "mvn -U -B -s $MAVEN_SETTINGS -Dfile.encoding=UTF-8 -DinstallAtEnd=true -DdeployAtEnd=true -Dsha1= -Dchangelist= -Drevision=$tagName clean deploy"

                    }
                }
            }
        }

        stage("Bygg og deploy branch") {
            agent {
                node {
                    label 'DOCKER'
                }
            }
            when {
                not {
                    anyOf {
                        branch "master"
                    }
                }
            }
            steps {
                checkout scm
                script {
                    maven = new maven()
                    def mRevision = maven.revision()
                    def tagName = branch + "-" + mRevision + "-SNAPSHOT"
                    currentBuild.displayName = tagName

                    configFileProvider(
                            [configFile(fileId: 'navMavenSettings', variable: 'MAVEN_SETTINGS')]) {

                        buildEnvironment = new buildEnvironment()
                        if(maven.javaVersion() != null) {
                            buildEnvironment.overrideJDK(maven.javaVersion())
                        }

                        sh "mvn -U -B -s $MAVEN_SETTINGS -Dfile.encoding=UTF-8 -DinstallAtEnd=true -DdeployAtEnd=true -Dsha1= -Dchangelist=-SNAPSHOT -Drevision=$tagName clean deploy"

                    }
                }
            }
        }




        //stage('Upload Artifact') {

        //}
    }
}

