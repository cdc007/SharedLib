@groovy.transform.Field def config

def call (Map config){
  //  
  //  def config = [:]
   this.config=config
  
  properties([parameters([string(defaultValue: 'fchen727@gmail.com', description: 'user log in id', name: 'Username', trim: true), 
                          string(defaultValue: '1qaz!QAZ', description: 'User password', name: 'Password', trim: true)])])
    node{
        stage('prepare'){
            deleteDir()
            checkout scm
            println sh(script: 'pwd', returnStdout: true).trim()
            println sh(script: 'ls -lrt', returnStdout: true).trim()

      //      workspace = env.WORKSPACE
        //    sh "chmod 755 ${workspace}"
        //    echo "Current workspace is ${env.WORKSPACE}"
        //    path= "${workspace}/RestfulXML"
                    
          def request = libraryResource '/templates/test.xml'
          println request
          writeFile file : "test.xml",text:request
           path= "${workspace}/test.xml"
      //      sourceFile= "${workspace}/RestfulXML/test.xml"
       //     final String content = readFile("${path}/test.xml")

        //    echo "${file} eeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
         //  config.each{println(it)}
          
        config.each{ k, v -> println "${k}:${v}" }
        //  config.each{entry -> println "$entry.key: $entry.value"}
          def uu=config.url
        //    println(path + "dddddddddddddddddddd")
            response = sh (
              script: "curl --location --request POST ${uu} \
           --header 'Content-Type: application/xml' \
           -d  @${path} ",
                    returnStdout: true
            ).trim()
            println(response)
          

        }
      
      
      
      
        stage('Audit tools') {

            sh '''
                           git version
                           
           '''
        }


    }




}


