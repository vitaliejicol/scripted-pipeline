properties([
    parameters([
        string(defaultValue: '', description: 'Please enter the NodeIp!', name: 'NodeIp', trim: true),
        string(defaultValue: '', description: 'Please enter the Branch Name!', name: 'BranchName', trim: true)
        ])
    ])
if  (NodeIp?.trim()) {
    node {
        withCredentials([sshUserPrivateKey(credentialsId: 'ssh-key', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
           stage('Pull Repo') {
                git branch: '${BranchName}', changelog: false, poll: false, url: 'git@github.com:vitaliejicol/scripted-pipeline.git'  
            }
            stage('Install Mvn') {
                ansiblePlaybook credentialsId: 'ssh-key', inventory: '167.71.188.57,', playbook: 'mvninstall.yaml' 
            }
        }
    }
}

