properties([
    parameters([string(defaultValue: '', description: 'Please enter the NodeIp!', name: 'NodeIp', trim: true)
    ])
])
node {
    withCredentials([sshUserPrivateKey(credentialsId: 'jenkins-master', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
        stage('Init') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@{NodeIp} yum install openjdk git -y'
        }
        stage ('Install git') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@{NodeIp} yum install git -y'
        }
    }
}