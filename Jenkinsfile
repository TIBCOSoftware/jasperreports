@Library('jenkins-pipeline-jrs@xss-lint-changes') _

timestamps {
  throttle(['jr']) {
    node {
      properties([
          parameters([
              choice(
                  name: 'jdk_version',
                  choices: 'jdk-default\njdk-7u80\njdk-9.0.1',
                  description: 'JDK Version'
              ),
              choice(
                  name: 'ant_version',
                  choices: 'ant-default\nant-latest',
                  description: 'Ant Version'
              ),
              choice(
                  name: 'mvn_version',
                  choices: 'mvn-default\nmvn-latest',
                  description: 'Maven Version'
              ),
              choice(
                  name: 'node_version',
                  choices: 'nodejs-lts',
                  description: 'NodeJS Version'
              )
          ])
      ])
      stage('Checkout') {
        checkout scm
      }

      stage('Lint') {
        withJrsEnv(params.jdk_version, params.mvn_version, params.ant_version) {
          withFafEnv(params.node_version) {
            sh "./node_modules/.bin/grunt eslint:ci"
          }
        }
      }
      stage('Compile') {
        withJrsEnv(params.jdk_version, params.mvn_version, params.ant_version) {
          sh "ant compile"
        }
      }
    }
  }
}
