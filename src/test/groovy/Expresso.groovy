import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import spock.lang.*


import java.util.concurrent.TimeUnit


class ExpressoCall {
    /*TODO Implement POST for batch calls */

    private String uri
    private String path
    private Map<String,?> requestParams
    private client = new RESTClient()
    private Object restResponse
    private int responseCode
    private Object response



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

@Title("Testing for valid expresso debug output")
@Narrative("Test and arbitrary expresso call to see if the debug returns in time with the proper keys")
class ExpressoOutputSpec extends Specification implements  AppConfiguration.PropertyKeys{

    @Shared appConfiguration = AppConfiguration.getInstance()

    @Shared expCall = new ExpressoCall(getUri(), [p: '227', g: '001', dbg: 'smtdn9', response: 'json' ], '/load')

    def getUri () {
        return appConfiguration.getProperty(BASE_URL)
    }

    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
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

