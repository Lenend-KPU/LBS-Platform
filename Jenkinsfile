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
        sudo chown -hR root:root /var/jenkins_home/workspace/ ;
        sudo touch /var/jenkins_home/workspace/db.sqlite3
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