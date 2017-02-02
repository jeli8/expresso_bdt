import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import spock.lang.*


class ExpressoOutput extends Specification {
        @Shared exp_uri = 'http://nyjexp001.exelator.com:8080'
        @Shared client = new RESTClient()
        @Shared requestParams = [p: '227', g: '001', dbg: 'smtdn9', response: 'json' ]
        @Shared objResp = client.get(path : '/load', query : requestParams, uri: exp_uri)
    def "response is 200"() {
        expect:
            objResp.status == 200  // HTTP response code is 200
    }
    def "testing for valid json output to debug"() {
        setup:
            def object = objResp.getData()
            def jsonSlurper = new JsonSlurper()
            def resp_map = jsonSlurper.parseText(object.toString()) // Json Debug

        expect:
            resp_map instanceof Map

        cleanup:
            println JsonOutput.prettyPrint(object.toString())
    }
    def "test for all required parts of the debug object"() {
        setup:
            def object = objResp.getData()
            def jsonSlurper = new JsonSlurper()
            def resp_map = jsonSlurper.parseText(object.toString()) // Json Debug

        expect:
            resp_map.containsKey("flowStatus")
            resp_map.containsKey("logsMap")
            resp_map.containsKey("kafkaMap")
    }
}

