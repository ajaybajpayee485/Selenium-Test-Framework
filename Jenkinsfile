pipeline{
	agent any 
	
	tools{
		maven '3.9.12'
	}
	environment {
		COMPOSE_PATH="${WORKSPACE}/DOCKER"
		SELENIUM_GRID="true"
	}
	
	stages{
		
		
		stage('Start Selenium grid with docker compose'){
			steps{
				script{
					echo "starting selenium grid"
					bat "docket compose -f ${COMPOSE_PATH}\\docker-compose.yml up -d"
					sleep 30
				}
			}
		}
		
		stage('Checkout'){
			steps{
				git branch: 'main' , url: 'https://github.com/ajaybajpayee485/Selenium-Test-Framework'
			}
		}
		
		stage('build'){
			steps{
				bat 'mvn clean install -DseleniumGrid=true'
			}
		}
		
		stage('Test'){
			steps{
				bat 'mvn test -DseleniumGrid=true'
			}
		}
		
		stage('Reports'){
			steps{
				publishHTML(target:[
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportName: 'Extent Spark Report'
				])
			}
		}
	}
	
	post{
		always{
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html',fingerprint:true
			junit 'target/surefire-reports/*.xmll'
		}
		
		success{
			emailtext{
				//to: 'bajpayeea1@gmail.com',
				subject:"Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
			}
		}
		failure{
			emailtext{
				//to: 'bajpayeea1@gmail.com',
				subject:"Build failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}".
				mimeType: 'text/html',
				attachLog: true
			}
		}
		
	}
	
}