#!groovy
node {
  environment {
    Django_secret_key = credentials('Django_secret_key')
  }

  stage("Checkout") {
    cleanWs()
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
  stage('Chown to user Jenkins'){
    sh """ 
        chown -hR jenkins:jenkins /var/jenkins_home/workspace/
    """
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