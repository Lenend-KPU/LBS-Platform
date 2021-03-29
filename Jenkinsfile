#!groovy
node {
  stage("Checkout") {
    checkout scm
  }

  stage('Stop previous containers') {
    dir('backend') {
       withEnv(["PATH=$PATH:~/.local/bin"]){
        sh """
            docker-compose down -p LBS_Platform
        """
       }
    }
  }
  stage('Run current containers') {
    dir('backend') {
       withEnv(["PATH=$PATH:~/.local/bin"]){
        sh """
            docker-compose up -p LBS_Platform
        """
       }
    }
  }
}