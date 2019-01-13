pipeline{

	agent {
		label 'Slave_Induccion'
	}

	options {
		buildDiscarder(logRotator(numToKeepStr:'3'))
		disableConcurrentBuilds()
	}
	tools{
		jdk 'JDK8_Centos'
		gradle 'Gradle4.5_Centos'
	}



	stages {


		stage('Checkout'){
			steps{
				echo "------------>Checkout<------------"
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], gitTool: 'Git_Centos', submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'GitHub_julianyepes', url: 'https://github.com/julianyepesR/estacionamiento.git']]])
			}
		}

		stage('Unit Tests') {
			steps {
				echo "------------>Unit Tests<------------"
				sh 'gradle --b ./build.gradle test'
			}
		}

		stage('Static Code Analysis') {
			steps{
				echo '------------>Análisis de código estático<------------'
				withSonarQubeEnv('Sonar') {
					 sh "${tool name: 'SonarScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'}/bin/sonar-scanner -Dproject.settings=./sonar.properties"
				}
			}
		}

		stage('Build') {
			steps {
				echo "------------>Build<------------"
				sh 'gradle --b ./build.gradle build -x test'
			}
		}

	}

	post {
    	always {
                archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
                junit 'build/reports/**/*.xml'
            }

		failure {
			echo 'This will run only if failed'
			mail( to: 'julian.yepes@ceiba.com.co' ,
				body: "Build failed in Jenkins: Project: ${env.JOB_NAME} Build /n Number: ${env.BUILD_NUMBER} URL de build: ${env.BUILD_NUMBER}/n/n Please go to ${env.BUILD_URL} and verify the build",
				subject: "ERROR CI: Project name → ${env.JOB_NAME}")
			}

		success {
			echo 'This will run only if successful'
			junit '**/build/jacoco/test-results/*.xml'
		}
	}
}