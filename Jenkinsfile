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
            touch /var/jenkins_home/workspace/db.sqlite3 ; chmod 777 /var/jenkins_home/workspace/db.sqlite3 ; ln -sfn /var/jenkins_home/workspace/db.sqlite3 $(pwd)/db.sqlite3 ; docker-compose -p LBS_Platform up --build
        '''
       }
    }
  }
}