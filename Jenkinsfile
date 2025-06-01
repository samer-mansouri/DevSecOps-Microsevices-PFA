pipeline {
    agent any

    environment {
        DEFAULT_PREV_COMMIT = 'HEAD~1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Detect Changes & Dispatch') {
            steps {
                script {
                    def previousCommit = env.GIT_PREVIOUS_SUCCESSFUL_COMMIT ?: sh(
                        script: "git rev-parse ${env.DEFAULT_PREV_COMMIT}",
                        returnStdout: true
                    ).trim()

                    def currentCommit = env.GIT_COMMIT ?: sh(
                        script: "git rev-parse HEAD",
                        returnStdout: true
                    ).trim()

                    echo "Checking changes: ${previousCommit} → ${currentCommit}"

                    def changedDirs = sh(
                        script: "git diff --name-only ${previousCommit} ${currentCommit} | cut -d/ -f1 | sort -u",
                        returnStdout: true
                    ).trim().split("\n")

                    echo "Changed top-level folders: ${changedDirs}"

                    if (changedDirs.contains("user-service")) {
                        echo "Triggering user-service pipeline..."
                        build job: 'build-user-service', wait: false
                    }

                    if (changedDirs.contains("management-service")) {
                        echo "Triggering user-service pipeline..."
                        build job: 'build-user-service', wait: false
                    }

                    if (changedDirs.contains("report-service")) {
                        echo "Triggering report-service pipeline..."
                        build job: 'build-report-service', wait: false
                    }

                    if (changedDirs.contains("api-gateway")) {
                        echo "Triggering api-gateway pipeline..."
                        build job: 'build-api-gateway', wait: false
                    }

                    if (changedDirs.contains("advertisement-service")) {
                        echo "Triggering advertisement-service pipeline..."
                        build job: 'build-advertisement-service', wait: false
                    }

                    if (changedDirs.contains("configserver")) {
                        echo "Triggering configserver pipeline..."
                        build job: 'build-configserver', wait: false
                    }

                    if (changedDirs.every { !["user-service", "management-service", "report-service", "api-gateway", "advertisement-service", "configserver"].contains(it) }) {
                        echo "No relevant microservice changed — no pipelines triggered."
                    }
                }
            }
        }
    }
}
