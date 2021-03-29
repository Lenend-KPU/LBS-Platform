#!groovy
node {
  stage("Checkout") {
    checkout scm
  }

  stage('Stop previous containers') {
    dir('backend') {
       withEnv(["PATH=$PATH:/usr/local/bin"]){
        sh """
            docker-compose -p LBS_Platform down
        """
       }
    }
  }
  stage('Run current containers') {
    dir('backend') {
       withEnv(["PATH=$PATH:/usr/local/bin"]){
        sh """
            docker-compose -p LBS_Platform up 
        """
       }
    }
  }
}