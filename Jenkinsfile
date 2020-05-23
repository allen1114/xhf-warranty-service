static def buildTime() {
    return new Date().format('yyyyMMddHHmmss') + "";
}

static def isSnapshot(String v) {
    return (v =~ "-SNAPSHOT").find();
}

def isTestPassed = false
def isSitTaged = false
def isMasterTaged = false
def ownerEmail = ""
def authorEmail = ""
def commitMsg = ""

def emailSubject = ""
def emailAttachLog = true
def emailBody = ""
def emailAttachmentsPattern = ""
def emailTo = ""

pipeline {

    options {
        skipStagesAfterUnstable()
    }

    agent any

    environment {

        BUILD_TIME = buildTime()

        PROJECT_GROUPID = "com.zkztch.xhf"
        PROJECT_ARTIFACTID = "xhf-warranty-service"
//        PROJECT_VERSION = "0.0.1-SNAPSHOT"
        PROJECT_VERSION = "1.0.0"

        GITLAB_URL = "http://gitlab.dev.zkztch.com"
        GITLAB_TOKEN = "iDwF4syhx15oTFsRyzKa"
        GITLAB_NAMESPACE = "caizl"
        GITLAB_PROJECT = "xhf-warranty-service"

        GITLAB_LASTTEST_TAG = "last_test_pass"
        GITLAB_SIT_TAG = "sit_${PROJECT_VERSION}"
        GITLAB_MASTER_TAG = "release_${PROJECT_VERSION}"
        GITLAB_MASTER_LAST_TAG = "last_release"

        DOCKER_HOST = "https://docker.dev.zkztch.com:2375"
        DOCKER_CERT_PATH = "certs"

        DOCKER_IMAGE_NAME = "${PROJECT_GROUPID}/${PROJECT_ARTIFACTID}"
        DOCKER_IMAGE_TAG = "${PROJECT_VERSION}"
        DOCKER_IMAGE = "${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"

    }

    stages {

        stage("start") {
            steps {
                script {
                    echo "build on ${BUILD_TIME}"
                    echo "GITLAB_NAMESPACE = ${GITLAB_NAMESPACE}"
                    gitlabProtectBranch branch: "${BRANCH_NAME}", level: 'NONE'
                    checkout scm
                    sh 'chmod u+x mvnw'

                    isTestPassed = "${GIT_COMMIT}" == gitlabGetTagCommitId("${GITLAB_LASTTEST_TAG}")
                    isSitTaged = "${GIT_COMMIT}" == gitlabGetTagCommitId("${GITLAB_SIT_TAG}")
                    isMasterTaged = "${GIT_COMMIT}" == gitlabGetTagCommitId("${GITLAB_MASTER_TAG}")

                    ownerEmail = gitlabGetOwnerEmail()
                    authorEmail = gitlabGetCommitField commit: "${GIT_COMMIT}", field: 'authorEmail'
                    commitMsg = gitlabGetCommitField commit: "${GIT_COMMIT}", field: 'message'

                    emailTo = authorEmail + ",cc:" + ownerEmail
                    emailBody = "项目地址: ${GITLAB_URL}/${GITLAB_NAMESPACE}/${GITLAB_PROJECT}\n " +
                            "分支: ${BRANCH_NAME} \n" +
                            "提交: ${GIT_COMMIT} \n" +
                            "修改信息: " + commitMsg + "\n"
                }
            }
        }
// dev和sit分支更新需进行单元测试，测试通过的分支打上相应标签
        stage("test") {
            when {
                allOf {
                    anyOf {
                        branch 'sit'
                        branch 'dev'
                    }
                    //已测试提交不需再次测试，防止回滚促发
                    equals expected: false, actual: isTestPassed
                    equals expected: false, actual: isSitTaged
                }
            }

            steps {
                sh "./mvnw -s settings.xml" +
                        " -DPROJECT_GROUPID='${PROJECT_GROUPID}'" +
                        " -DPROJECT_ARTIFACTID='${PROJECT_ARTIFACTID}'" +
                        " -DPROJECT_VERSION='${PROJECT_VERSION}'" +
                        " clean test"
                jacoco exclusionPattern: '**/*Test*.class,**/*Spec*.class,**/*Application.class',
                        changeBuildStatus: true,
                        maximumBranchCoverage: '80',
                        maximumClassCoverage: '80',
                        maximumComplexityCoverage: '80',
                        maximumInstructionCoverage: '80',
                        maximumLineCoverage: '80',
                        maximumMethodCoverage: '80'
            }

            post {

                success {
                    script {
                        if ("${BRANCH_NAME}" == "dev") {
                            // 打上测试通过标签，用于下次测试失败回滚
                            gitlabCreateTag force: true, tag: "${GITLAB_LASTTEST_TAG}", ref: "${GIT_COMMIT}"
                        }
                    }
                }

                unsuccessful {
                    script {
                        if ("${BRANCH_NAME}" == "dev") {
                            //开发分支测试失败进行错误备份并回滚分支
                            gitlabCreateBranch force: true, branch: "${BRANCH_NAME}_test_fail_on_${BUILD_TIME}", ref: "${BRANCH_NAME}"
                            gitlabCreateBranch force: true, branch: "${BRANCH_NAME}", ref: "${GITLAB_LASTTEST_TAG}"
                            gitlabProtectBranch branch: "${BRANCH_NAME}", level: 'NONE'
                            emailBody += "错误备份分支：${BRANCH_NAME}_test_fail_on_${BUILD_TIME} \n"
                        }
                    }
                }

                failure {
                    script {
                        sh "./mvnw -s settings.xml" +
                                " -DPROJECT_GROUPID='${PROJECT_GROUPID}'" +
                                " -DPROJECT_ARTIFACTID='${PROJECT_ARTIFACTID}'" +
                                " -DPROJECT_VERSION='${PROJECT_VERSION}'" +
                                " surefire-report:report"
                        fileOperations([fileZipOperation('target/site/junit')])
                        emailSubject = '单元测试不通过，赶紧去处理'
                        emailAttachmentsPattern = '**/junit.zip'
                    }
                }

                unstable {
                    script {
                        sh "./mvnw -s settings.xml" +
                                " -DPROJECT_GROUPID='${PROJECT_GROUPID}'" +
                                " -DPROJECT_ARTIFACTID='${PROJECT_ARTIFACTID}'" +
                                " -DPROJECT_VERSION='${PROJECT_VERSION}'" +
                                " jacoco:report"
                        fileOperations([fileZipOperation('target/site/jacoco')])
                        emailSubject = '覆盖率不达标，赶紧去处理'
                        emailAttachmentsPattern = '**/jacoco.zip'
                    }
                }
            }
        }
// sit可能从dev分支合并已测试提交，新建stage防止tag动作被忽略
        stage("sitTag") {
            when {
                allOf {
                    branch 'sit'
                    equals expected: false, actual: isSitTaged
                }
            }
            steps {
                script {
                    if (isSnapshot("${PROJECT_VERSION}")) {
                        echo "force tag"
                        gitlabCreateTag force: true, tag: "${GITLAB_SIT_TAG}", ref: "${GIT_COMMIT}"
                    } else {
                        echo "common tag"
                        gitlabCreateTag force: false, tag: "${GITLAB_SIT_TAG}", ref: "${GIT_COMMIT}"
                    }
                }
            }

            post {
                unsuccessful {
                    script {
                        emailSubject = '分支创建标签失败，赶紧去处理'
                    }
                }
            }
        }

        stage("sitPackage") {
            when {
                branch "sit"
            }

            environment {
                DOCKER_REPO_HOST = "docker.repo.sit.zkztch.com"
                DOCKER_REPO_USERNAME = "sit"
                DOCKER_REPO_PASSWORD = "sit.zkztch"
            }

            steps {
                script {
                    sh "./mvnw -s settings.xml" +
                            " -DPROJECT_GROUPID='${PROJECT_GROUPID}'" +
                            " -DPROJECT_ARTIFACTID='${PROJECT_ARTIFACTID}'" +
                            " -DPROJECT_VERSION='${PROJECT_VERSION}'" +
                            " -DDOCKER_HOST='${DOCKER_HOST}'" +
                            " -DDOCKER_CERT_PATH='${DOCKER_CERT_PATH}'" +
                            " -DDOCKER_IMAGE_NAME='${DOCKER_IMAGE_NAME}'" +
                            " -DDOCKER_IMAGE_TAG='${DOCKER_IMAGE_TAG}'" +
                            " -DskipTests docker:removeImage package"
                    dockerTag image: "${DOCKER_IMAGE}", tag: "${DOCKER_REPO_HOST}/${DOCKER_IMAGE}", force: true
                    dockerPush image: "${DOCKER_REPO_HOST}/${DOCKER_IMAGE}"
                }
            }

            post {
                unsuccessful {
                    script {
                        emailSubject = '项目打包失败，赶紧去处理'
                    }
                }
            }
        }

        stage("tagMaster") {

            when {
                allOf {
                    branch "master"
                    equals expected: false, actual: isMasterTaged
                }
            }

            steps {
                script {
                    // master 必须从已测试sit分支合并,且非快照版本
                    if (isSitTaged && !isSnapshot("${PROJECT_VERSION}")) {
                        gitlabCreateTag force: false, tag: "${GITLAB_MASTER_TAG}", ref: "${GIT_COMMIT}"
                    } else {
                        error 'master merge error!'
                    }
                }
            }

            post {

                success {
                    script {
                        gitlabCreateTag force: true, tag: "${GITLAB_MASTER_LAST_TAG}", ref: "${GIT_COMMIT}"
                    }
                }

                unsuccessful {
                    script {
                        gitlabCreateBranch force: true, branch: "${BRANCH_NAME}_tag_fail_on_${BUILD_TIME}", ref: "${BRANCH_NAME}"
                        gitlabCreateBranch force: true, branch: "${BRANCH_NAME}", ref: "${GITLAB_MASTER_LAST_TAG}"

                        emailSubject = '分支合并检查失败，已经自动回滚'
                        emailBody += "错误备份分支：${BRANCH_NAME}_tag_fail_on_${BUILD_TIME} \n"
                    }
                }
            }
        }

        stage("masterPackage") {
            when {
                branch "master"
            }

            environment {
                DOCKER_REPO_HOST = "docker.repo.pro.hdayun.com"
                DOCKER_REPO_USERNAME = "pro"
                DOCKER_REPO_PASSWORD = "pro.zkztch"
            }

            steps {
                script {
                    sh "./mvnw -s settings.xml" +
                            " -DPROJECT_GROUPID='${PROJECT_GROUPID}'" +
                            " -DPROJECT_ARTIFACTID='${PROJECT_ARTIFACTID}'" +
                            " -DPROJECT_VERSION='${PROJECT_VERSION}'" +
                            " -DDOCKER_HOST='${DOCKER_HOST}'" +
                            " -DDOCKER_CERT_PATH='${DOCKER_CERT_PATH}'" +
                            " -DDOCKER_IMAGE_NAME='${DOCKER_IMAGE_NAME}'" +
                            " -DDOCKER_IMAGE_TAG='${DOCKER_IMAGE_TAG}'" +
                            " -DskipTests docker:removeImage package"
                    dockerTag image: "${DOCKER_IMAGE}", tag: "${DOCKER_REPO_HOST}/${DOCKER_IMAGE}", force: true
                    dockerPush image: "${DOCKER_REPO_HOST}/${DOCKER_IMAGE}"
                }
            }

            post {
                unsuccessful {
                    script {
                        emailSubject = '项目打包失败，赶紧去处理'
                    }
                }
            }
        }

    }

    post {
        unsuccessful {
            emailext subject: emailSubject,
                    attachLog: emailAttachLog,
                    body: emailBody,
                    attachmentsPattern: emailAttachmentsPattern,
                    to: emailTo
        }

        cleanup {
            gitlabProtectBranch branch: "${BRANCH_NAME}", level: 'DEVELOPER'
        }
    }
}
