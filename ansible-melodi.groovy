properties([
    parameters([
        string(defaultValue: '', description: 'Please enter the NodeIp!', name: 'NodeIp', trim: true),
        string(defaultValue: '', description: 'Please enter the Branch Name!', name: 'BranchName', trim: true)
        ])
    ])
if  (NodeIp?.trim()) {
    node {
    withCredentials([sshUserPrivateKey(credentialsId: 'master-jenkins-private', keyFileVariable: 'SSHKEY', passphraseVariable: '', usernameVariable: 'SSHUSERNAME')]) {
        stage('Pull Repo') {
            git branch: '${BranchName}', changelog: false, poll: false, url: 'https://github.com/ikambarov/melodi'  
       }
       stage('Install Apache') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} yum install httpd -y'
        }
        stage('Install git') {
            sh 'scp -r -o StrictHostKeyChecking=no -i $SSHKEY * $SSHUSERNAME@${NodeIp}:/var/www/html/ '
        }
        stage('Change Ownership') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} chown -R apache:apache /var/www/html/'
        }
        stage('Start Apache') {
            sh 'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp}  "systemctl start httpd && systemctl enable httpd"'
        }
        stage('Install Ansible') {
            'ssh -o StrictHostKeyChecking=no -i $SSHKEY $SSHUSERNAME@${NodeIp} yum install ansible -y'
        }      
    }
}
    println('Not Empty')
}
else {
    error 'Please enter a valid IP address'
}   
