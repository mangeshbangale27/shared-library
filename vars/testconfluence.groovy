@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
import groovyx.net.http.HTTPBuilder
import groovy.json.JsonSlurper

def confluenceURL = "https://mangeshbangale.atlassian.net"
def accessToken = "excKBCr7rFgrerpnFIsC6888"
def spaceKey = "MANGESH"
def pageTitle = "demo"

def http = new HTTPBuilder(confluenceURL)

// Create table
def createTableResponse = http.post(path: '/rest/api/content', headers: [
    'Content-Type': 'application/json',
    'Authorization': "Bearer ${accessToken}"
], json: [
    type: 'page',
    title: pageTitle,
    space: [
        key: spaceKey
    ],
    body: [
        storage: [
            value: '<table><tbody><tr><th>Column 1</th><th>Column 2</th></tr></tbody></table>',
            representation: 'storage'
        ]
    ]
])

// Get page ID
def pageId = new JsonSlurper().parseText(createTableResponse.getData()).id

// Add row to table
def addRowResponse = http.put(path: "/rest/api/content/${pageId}/child/table", headers: [
    'Content-Type': 'application/json',
    'Authorization': "Bearer ${accessToken}"
], json: [
    table: [
        rows: [
            [
                cells: [
                    [
                        contents: [
                            [
                                text: [
                                    value: 'Row 1 Column 1'
                                ]
                            ]
                        ]
                    ],
                    [
                        contents: [
                            [
                                text: [
                                    value: 'Row 1 Column 2'
                                ]
                            ]
                        ]
                    ]
                ]
            ]
        ]
    ]
])

if (addRowResponse.status == 200) {
    println 'Row added successfully'
} else {
    println 'Error adding row: ' + addRowResponse.status
}
