@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')

import groovyx.net.http.HTTPBuilder

def http = new HTTPBuilder("https://mangeshbangale.atlassian.net")
def auth = 'Basic ' + "mangeshbangale27@gmail.com:excKBCr7rFgrerpnFIsC6888".bytes.encodeBase64().toString()

http.request(Method.PUT, ContentType.JSON) { req ->
    headers.'Authorization' = auth
    body = [
        id: pageId,
        type: 'page',
        title: 'New Page Title',
        body: [
            storage: [
                value: 'New page content',
                representation: 'storage'
            ]
        ]
    ]
    response.success = { resp, json ->
        println 'Page Updated Successfully'
    }
    response.failure = { resp ->
        println 'Page Update Failed'
    }
}
