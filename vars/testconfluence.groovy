import groovyx.net.http.RESTClient

// Set up the REST client
def confluenceUrl = "mangeshbangale.atlassian.net"
def client = new RESTClient(confluenceUrl)
def username = "mangeshbangale27@gmail.com"
def password = "excKBCr7rFgrerpnFIsC6888"
// Set up the authentication
def auth = "Basic " + "${username}:${password}".bytes.encodeBase64().toString()

// Set up the JSON payload for the table
def tableJson = """
{
    "type": "table",
    "content": [
        {
            "type": "tableRow",
            "content": [
                {
                    "type": "tableCell",
                    "content": [
                        {
                            "type": "paragraph",
                            "text": "Column 1"
                        }
                    ]
                },
                {
                    "type": "tableCell",
                    "content": [
                        {
                            "type": "paragraph",
                            "text": "Column 2"
                        }
                    ]
                }
            ]
        },
        {
            "type": "tableRow",
            "content": [
                {
                    "type": "tableCell",
                    "content": [
                        {
                            "type": "paragraph",
                            "text": "Row 1, Column 1"
                        }
                    ]
                },
                {
                    "type": "tableCell",
                    "content": [
                        {
                            "type": "paragraph",
                            "text": "Row 1, Column 2"
                        }
                    ]
                }
            ]
        }
    ]
}
"""

// Send the POST request to create the table
def pageId = "1507329" // replace with the ID of the page you want to add the table to
def response = client.post(path: "/rest/api/content/${pageId}/child/table", headers: [Accept: "application/json", Authorization: auth], json: tableJson)

// Check the response status code
if (response.status != 201) {
    println "Error creating table: ${response.status} ${response.statusLine}"
} else {
    println "Table created successfully!"
}
