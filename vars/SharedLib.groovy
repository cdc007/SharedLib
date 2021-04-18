
//@groovy.transform.Field def workspace
//   @groovy.transform.Field def path
//  @groovy.transform.Field def file

@Library('commonLib@master') _

timestamps {

       SharedLib{
           url:'https://api.beta.shipwire.com/exec/InventoryServices.ph'
       }
}
