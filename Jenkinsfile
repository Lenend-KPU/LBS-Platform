#!groovy
node {
  wrap([$class: 'AnsiColorBuildWrapper', colorMapName: 'xterm']) {
    stage("Checkout") {
      checkout scm
    }
    // CI TODO
  }
  post {
    success {
      stage('Stop previous containers') {
        sh """
          docker-compose down -p LBS_Platform
        """
      }
      stage('Run current containers') {
        sh """
          docker-compose up -p LBS_Platform
        """
      }
    }
  }
}