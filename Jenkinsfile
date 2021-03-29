#!groovy
node {
  steps {
    stage("Checkout") {
      checkout scm
    }
  }
  post {
    success {
      stage('Stop previous containers') {
        dir('backend') {
          sh """
            docker-compose down -p LBS_Platform
          """
        }
      }
      stage('Run current containers') {
        dir('backend') {
          sh """
            docker-compose up -p LBS_Platform
          """
        }
      }
    }
  }
}