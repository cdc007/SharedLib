   
  import groovy.xml.XmlUtil
   import java.io.File 

@groovy.transform.Field def config

def call (Map config){
  //  
  //  def config = [:]
   this.config=config
  
//  properties([parameters([string(defaultValue: 'fchen7274@gmail.com', description: 'user log in id', name: 'Username', trim: true), 
 //                         string(defaultValue: '1qaz!QAZ', description: 'User password', name: 'Password', trim: true)])])
 def params=[Username : "${Username}",  Password : "${Password}"]
  
//def now = LocalDateTime.now()

//println now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))

   println(new Date().getTime())
  
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
       
  ///////////////////////////////////////////////////////////////////////////     ///////////////////////// test reponse 
       
        def rp = libraryResource '/templates/response.xml'
                    
  
          writeFile file:"response.xml", text:rp
            sh "chmod 755 response.xml"      
          rpf= "${workspace}/response.xml"
       
       println(rpf)
       
      status=getApprovalStatus(rpf)
      println status
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
       println("/////////////////////////")
        println(config.approvers)
      
           def map = stringtoMap(config.approvers)
       println "test map/////////"
        println map.get(group1) +" should be Irene"
       println map.get(group2) + " should be Frank"
          println "test map///////////"

            
              println("/////////////////////////")
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////       
       // loop 2 times
       
       for(i=0;i<status.size();i++){
          println i
           println status[i] + " list value"
         println map.get(status[i])
         
       }
      
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     }
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

@NonCPS
  getApprovalStatus(String file) {
   // def list = []
   rpf1=new File(rpf).text
     listvalue=  new XmlSlurper().parseText(rpf1).'**'.findAll { it -> it.name() == 'ApproverName' }.toString()
     listvalue[1..-2].split(",").collect {it}
   //def  listApprovers = listvalue
    
   // listApprovers.each { node ->list.addAll(node.text())}
  
  //  return listApprovers
}

@NonCPS

Map stringtoMap(String param){
   // Take the String value between
    // the [ and ] brackets.
  return  param[1..-2]
        // Split on , to get a List.
        .split(', ')
        // Each list item is transformed
        // to a Map entry with key/value.
        .collectEntries { entry -> 
            def pair = entry.split(':')
            [(pair.first()): pair.last()]
        }
   
}
