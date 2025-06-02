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

          def tagName = sh(
            script: "git describe --tags --exact-match ${currentCommit} || true",
            returnStdout: true
          ).trim()

          def isTag = tagName?.trim()
          echo isTag ? "Git tag detected: ${tagName}" : "No tag detected (regular commit)"

          def knownServices = ["user-service", "management-service", "report-service", "api-gateway", "advertisement-service", "configserver"]

          for (service in knownServices) {
            if (changedDirs.contains(service)) {
              echo "Changes detected in ${service}"
              def waitForBuild = isTag ? true : false
              def buildParams = isTag ? [string(name: 'GIT_TAG_NAME', value: tagName)] : []

              echo "Triggering build-${service} (wait: ${waitForBuild})"
              def buildResult = build job: "build-${service}", wait: waitForBuild, parameters: buildParams

              if (isTag) {
                echo "Triggering deploy-${service} with tag ${tagName}"
                build job: "deploy-${service}", wait: false, parameters: [
                  string(name: 'GIT_TAG_NAME', value: tagName)
                ]
              }
            }
          }

          if (changedDirs.every { !knownServices.contains(it) }) {
            echo "No relevant microservice changed — skipping pipelines."
          }
        }
      }
    }
  }
}
