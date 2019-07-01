pipeline {
    agent any

    tools {
        maven "MAVEN"
    }
    
    stages {
        stage("Build"){
            steps {
                sh "mvn clean package -Dmaven.test.skip=true -Pwww2"
                sh "printenv"
            }
        }
    }
}
