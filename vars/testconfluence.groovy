@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper

def confluenceURL = "https://mangeshbangale.atlassian.net"
def accessToken = "excKBCr7rFgrerpnFIsC6888"
def spaceKey = "MANGESH"
def pageTitle = "demo"

def http = new HTTPBuilder(confluenceURL)

// Get page ID
def pageIdResponse = http.get(path: "/rest/api/content", headers: [
    'Content-Type': 'application/json',
    'Authorization': "Bearer ${accessToken}"
], query: [
    title: pageTitle,
    spaceKey: spaceKey
])
def pageId = new JsonSlurper().parseText(pageIdResponse.getData()).results[0].id

// Update page with new line
def updateResponse = http.put(path: "/rest/api/content/${pageId}", headers: [
    'Content-Type': 'application/json',
    'Authorization': "Bearer ${accessToken}"
], json: [
    body: [
        storage: [
            value: '<p>Line 1</p>', // replace "Line 1" with the actual line you want to add
            representation: 'storage'
        ]
    ]
])

if (updateResponse.status == 200) {
    println 'Line added successfully'
} else {
    println 'Error adding line: ' + updateResponse.status
}

