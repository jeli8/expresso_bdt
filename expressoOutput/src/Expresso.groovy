import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import spock.lang.*
import spock.util.mop.Use

class ExpressoCall {
    /*TODO Implement POST for batch calls */
    private String uri
    private String path
    private Map<String,?> requestParams
    private client = new RESTClient()
    private Object restResponse
    private int responseCode
    private Map<String, ?> response

    ExpressoCall(String uri, Map requestParams, String path) {
        this.uri = uri
        this.requestParams = requestParams
        this.path = path
    }

    def getCall(){
        this.restResponse = this.client.get(path : this.path, query : this.requestParams, uri: this.uri)
        this.responseCode = this.restResponse.status
        this.getJsonMap()
    }

    def getJsonMap(){
        def call = this.restResponse.getData()
        def jsonSlurper = new JsonSlurper()
        this.response = jsonSlurper.parseText(call.toString())
    }

}


class ExpressoOutput extends Specification {
    @Shared expCall = new ExpressoCall(
            'http://nyjexp010.exelator.com:8080',
            [p: '227', g: '001', dbg: 'smtdn9', response: 'json' ],
            '/load')

    def expressoHelper(expCall){
        expCall.getCall()
    }

    def "response is 200"() {
        setup:
            expressoHelper(expCall)
        expect:
            expCall.responseCode == 200  // HTTP response code is 200
    }
    def "testing for valid json output to debug"() {
        expect:
            expCall.response instanceof Map

    }
    def "test for all required parts of the debug object"() {
        expect:
            expCall.response.containsKey("flowStatus")
            expCall.response.containsKey("logsMap")
            expCall.response.containsKey("kafkaMap")
    }

}

