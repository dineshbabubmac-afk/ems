pipeline{
    agent any
    environment{
        EMS_IMAGE_NAME = 'spring/demo'
        POSTGRES_CONTAINER = 'postgres_container'
        EMS_CONTAINER = 'reward_system_app'
        //ENVIRONMENT = 'testing'
    }
    parameters{
        //string(name: 'ENVIRONMENT', defaultValue: '', description: 'Environment name')
        // hi
        choice(name: 'ENVIRONMENT', choices:['testing', 'prod'], description: 'environment')
    }
    tools{
        maven 'mvn3.9.9'
    }
    stages {

        stage('git clone'){
            steps{
                git branch: 'main', credentialsId: 'github_ems_credenticals', url: 'https://github.com/dineshbabubmac-afk/ems.git'
            }
        }

        stage('list command'){
            steps{
                sh 'ls'
            }
        }

        stage('maven test stage'){
            steps{
                sh 'echo $PATH'
                sh 'mvn test'
            }
        }
        stage('maven build stage'){
            when {
                expression{
                    params.ENVIRONMENT == 'testing'
                }
            }
            steps{
                sh 'mvn clean install -DskipTests=true'
            }
        }

        stage('docker image build'){
            steps{
                sh 'docker build -t $EMS_IMAGE_NAME .'
            }
        }

        stage('docker run postgres'){
            steps{
                sh 'docker rm -f $POSTGRES_CONTAINER 2>/dev/null || true'
                sh 'docker run -d --name $POSTGRES_CONTAINER -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password -v /Users/macbookvig/Documents/workspace/Docker/postgres_demo:/var/lib/postgresql/ -p 5532:5432 postgres'
            }
        }

        stage('docker network connect'){
            steps{
                sh 'docker network connect reward-system-network postgres_container'
            }
        }

        stage('docker run containers'){
            steps{
                sh 'docker rm -f reward_system_app 2>/dev/null || true'
                sh 'docker run -d \
                --name reward_system_app \
                --network reward-system-network \
                -e PMS_DB_URL=jdbc:postgresql://postgres_container:5432/reward_system \
                -e PMS_DB_USERNAME=postgres \
                -e PMS_DB_PASSWORD=password \
                -p 8080:8085 \
                spring/demo'
            }
        }


    }
}