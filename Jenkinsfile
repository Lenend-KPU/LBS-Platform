#!groovy
node {
  environment {
    Django_secret_key = credentials('Django_secret_key')
  }

  stage("Checkout") {
    cleanWs()
    checkout scm
  }

  stage('Chown to user Jenkins'){
    sh """
        chown -R jenkins:jenkins /var/jenkins_home/workspace
    """
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
        sh '''
            docker-compose -p LBS_Platform up --build
        '''
       }
    }
  }
}