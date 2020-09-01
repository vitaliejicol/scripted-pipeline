node {
    stage('Pull Repo') {
        git branch: 'solution', url: 'https://github.com/ikambarov/terraform-task.git'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        stage('Terraform Init') {
            sh '''
            cd sandbox/
            terraform-0.13 init
            '''
        }
        stage('Terraform Apply') {
            sh '''
                cd sandbox/
                terraform-0.13 apply -auto-approve
            '''
        }
    }    
}   