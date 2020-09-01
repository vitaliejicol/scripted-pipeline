node {
    stage('Pull Repo') {
        git branch: 'solution', url: 'https://github.com/vitaliejicol/terraform-vpc.git'
    }
    withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        stage('Terraform Init') {
            sh '''
            bash setenv.sh dev.tfvars
            terraform-0.13 init
            terraform-0.13 plan -var-file dev.tfvars
            '''
        }
        stage('Terraform Apply') {
            sh '''
        
                terraform-0.13 apply -var-file def.tfvars -auto-approve
            '''
        }
    }    
}   