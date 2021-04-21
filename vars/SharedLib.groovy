   
  import groovy.xml.XmlUtil

@groovy.transform.Field def config

def call (Map config){
  //  
  //  def config = [:]
   this.config=config
  
//  properties([parameters([string(defaultValue: 'fchen7274@gmail.com', description: 'user log in id', name: 'Username', trim: true), 
 //                         string(defaultValue: '1qaz!QAZ', description: 'User password', name: 'Password', trim: true)])])
 def params=[Username : "${Username}",  Password : "${Password}"]
  

  
    node{
        stage('prepare'){
            deleteDir()
            checkout scm
        //    println sh(script: 'pwd', returnStdout: true).trim()
       //     println sh(script: 'ls -lrt', returnStdout: true).trim()

      //      workspace = env.WORKSPACE
        //    sh "chmod 755 ${workspace}"
        //    echo "Current workspace is ${env.WORKSPACE}"
        //    path= "${workspace}/RestfulXML"
                    
   
      //      sourceFile= "${workspace}/RestfulXML/test.xml"
       //     final String content = readFile("${path}/test.xml")

        //    echo "${file} eeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
         //  config.each{println(it)}
          
          
          def request = libraryResource '/templates/test.xml'
                    
          println request
         
          request1 =extractFromXml(request, params)
       
          writeFile file:"test.xml", text:request1
            sh "chmod 755 test.xml"
          
          sourceFile= "${workspace}/test.xml"
          
        

     //    config.each{ k, v -> println "${k}:${v}" }
         def uu=config.url

         response = sh (
              script: "curl --location --request POST ${uu} \
           --header 'Content-Type: application/xml' \
           -d  @${sourceFile} ",
                    returnStdout: true
            ).trim()
            println(response)
        }
}
}


private  replaceXMLvalues (xmlContent,before, after){
 

        def newContent = file.replaceAll(before, after)
    }


@NonCPS
String extractFromXml(String request, Map params) {
     def test = new XmlSlurper().parseText(request)
          
            println(params)
          
          println(test)
         
          params.each{ key, value ->
            test.'**'.findAll{ if (it.name() ==key) it.replaceBody value}
          }
           
           println(test)

XmlUtil xmlUtil = new XmlUtil()

xmlString = xmlUtil.serialize(test)
     println "String:\n${xmlString}"
   return xmlString

}
//InventoryUpdate
