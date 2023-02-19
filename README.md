# Jenkins Plugin for DevOps Solution

## Introduction

This Jenkins plugin is designed to automate the DevOps pipeline and provide a seamless integration between Jenkins and the API/REST API. The plugin will detect the code language (e.g. Java) of the pipeline job and trigger the API/REST API based on the code language. Additionally, the plugin will check the availability of the agent node and create a new Jenkins slave node if one is not present.

## Requirements

- Jenkins installed on the server
- Maven installed on the server
- API/REST API endpoint with authentication token (JWT or any)

## Features

- Detects the code language of the pipeline job and triggers the API/REST API
- API requests are made with authentication token (JWT or any)
- Checks agent node availability and creates a new Jenkins slave node if not present
- Input box to configure agent termination time in plugin UI
- Automatically triggers the custom plugin on every build
- Input box to configure API link in plugin UI

## Installation

1. Clone or download the repository to your local machine
2. Open a terminal/command prompt in the root directory of the repository
3. Run the following command to install the plugin:

```
  mvn clean install
  mvn hpi:hpi
```

4. Log in to Jenkins as an administrator
5. Navigate to the Manage Plugins page in the Jenkins UI
6. Click on the `Advanced` tab
7. Under the `Upload Plugin` section, select the `.hpi` file from the target directory of the repository
8. Click the `Upload` button to install the plugin
9. Restart Jenkins to activate the plugin

<<<<<<< HEAD

## Usage

1. Create a new pipeline job in Jenkins
2. In the pipeline script, specify the code language for the job (e.g. Java) ##The plugin will automatically detect the code language for the job(e.g. Java).
3. Configure the API link in the plugin UI
4. The plugin will automatically trigger on every build and detect the code language to trigger the API/REST API accordingly
5. The agent node availability will be checked and a new Jenkins slave node will be created if necessary
6. The agent termination time can be configured in the plugin UI

# Usage

## Setting up a Jenkins Pipeline with Language Detector Plugin

1. Manually install the Language Detector plugin in Jenkins.

2. Create a new pipeline project in Jenkins. Go to Jenkins Dashboard > New Item > Enter a name for your pipeline project > Select "Pipeline" and click OK.

3. In the pipeline configuration page, scroll down to the "Pipeline" section and select "Pipeline script from SCM" in the Definition dropdown.

4. In the "SCM" section, select the SCM you want to use to store your pipeline script. For example, if you are using Git, select "Git".

5. In the "Script Path" field, enter the path to your pipeline script in your SCM repository. For example, if your pipeline script is in the root directory of your Git repository, you can enter "Jenkinsfile".

6. Make sure to have a Jenkinsfile that has the following code in the root of your directory:

```
pipeline {
  agent any

  stages {
    stage('Detect Language') {
      steps {
        languageDetector(
          filePattern: '**/*.java', // Path to the files you want to detect language for
          outputFile: 'languages.json' // Output file to store the detected languages
        )
      }
    }

    stage('Print Language') {
            steps {
                sh "echo Detected language: ${env.LANGUAGE}"

            }
        }
  }
}

```

7. click "Build Now" to run your pipeline. Jenkins will detect the language of your source code files and save the detected languages to the output file specified in your pipeline script. You can view the output file by clicking on the build in the Jenkins dashboard and then clicking on the "Console Output" link.
   > > > > > > > 96e192bacc349f9fc1a96e607e3358ef305d5e7c

## Conclusion

This Jenkins plugin provides a seamless integration between Jenkins and the API/REST API and automates the DevOps pipeline. With the ability to detect the code language, trigger the API/REST API, and manage the agent nodes, this plugin streamlines the CI/CD process and enhances the efficiency of the DevOps workflow.
