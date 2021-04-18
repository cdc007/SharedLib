@groovy.transform.Field def config

def call (config){
  //  
  //  def config = [:]
    this.config=config
    node{
        stage('prepare'){
            deleteDir()
            checkout scm
            println sh(script: 'pwd', returnStdout: true).trim()
            println sh(script: 'ls -lrt', returnStdout: true).trim()

            workspace = env.WORKSPACE
            sh "chmod 755 ${workspace}"
            echo "Current workspace is ${env.WORKSPACE}"
        //    path= "${workspace}/RestfulXML"
            sourceFile= "${workspace}/RestfulXML/test.xml"
       //     final String content = readFile("${path}/test.xml")

        //    echo "${file} eeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
       //   config.each{println(it)}
        //  config.each{entry -> println "$entry.key: $entry.value"}
          def path=${config.url}
            println(path + "dddddddddddddddddddd")
            response = sh (
                    script: "curl --location --request POST ${path} \
--header 'Content-Type: application/xml' \
           -d  @${sourceFile} ",
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




