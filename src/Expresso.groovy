import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import spock.lang.*


class ExpressoOutput extends Specification {
    def "testing for valid output to debug"() {
        def client = new RESTClient( 'http://loadus.exelator.com/load/' )
        def requestParams = [ p : '227', g : '001', dbg : 'smtdn9', response : 'json' ]
        def resp = client.get(path : '/load', query : requestParams)
        def jsonSlurper = new JsonSlurper()

        expect:
            resp.status == 200  // HTTP response code is 200
            def object = resp.getData() // Get data and set to object
            def map = jsonSlurper.parseText(object.toString()) // Json Debug
            map instanceof Map

        cleanup:
            println JsonOutput.prettyPrint(object.toString())
    }
}

