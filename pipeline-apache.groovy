properties([
    parameters([
        string(defaultValue: '', description: 'Please enter the NodeIp!', name: 'NodeIp', trim: true)
        ])
    ])
if  (NodeIp?.trim()) {
    node {
    withCredentials([sshUserPrivateKey(credentialsId: 'master-jenkins-private', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
        stage('Pull repo') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} git clone https://github.com/ikambarov/melodi.git'
        }
        stage('Install Apache') {
            'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} yum install httpd -y'
        }
        stage('Start Apache') {
            'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} sytemctl starte httpd && systemctl enable httpd'
        }
        stage('Copy files') {
            'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} cp -r melodi/* /var/www/html/ && rm -rf melodi'
        }
        stage('Clean Workspace') {
            cleanWs()
        }
        stage('Send Message to Slack') {
            slackSend channel: 'apr_devops_2020', message: 'Task #1 is done. Vitalie. '
        }
    }
}
    println('Not Empty')
}
else {
    error 'Please enter a valid IP address'
}   
