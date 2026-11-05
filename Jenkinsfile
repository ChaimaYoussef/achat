pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        stage('Verification') {
            steps {
                echo 'Verification de l environnement'
                sh 'java -version'
                sh 'mvn -version'
                sh 'git --version'
            }
        }

        stage('Build') {
            steps {
                echo 'Compilation du projet'
                sh 'mvn clean compile'
            }
        }

        stage('Tests') {
            steps {
                echo 'Execution des tests unitaires'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Packaging') {
            steps {
                echo 'Generation de l artefact'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar',
                                 allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo 'Pipeline termine avec succes !'
        }

        failure {
            echo 'Le pipeline a echoue !'
        }
    }
}
