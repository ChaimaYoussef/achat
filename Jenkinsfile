pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ChaimaYoussef/achat.git'
            }
        }

        stage('Build et Tests') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw -B clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh './mvnw -B org.sonarsource.scanner.maven:sonar-maven-plugin:sonar'
                }
            }
        }

        stage('OWASP Dependency-Check') {
            steps {
                sh './mvnw -B org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=8 -DskipTestScope=true || true'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-credentials',
                    usernameVariable: 'NEXUS_USER',
                    passwordVariable: 'NEXUS_PASSWORD'
                )]) {
                    sh '''
                        set +x
                        ./mvnw -B -s settings.xml deploy
                    '''
                }
            }
        }

        stage('Publication des rapports') {
            steps {
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('Archivage') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar, target/dependency-check-report.*',
                                 fingerprint: true,
                                 allowEmptyArchive: true
            }
        }
    }
}

