import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import java.util.*
import java.text.SimpleDateFormat


void call (Map map = [ : ]){
    String ENVIRONMET = null;
    String STATUS = null;
    String SOURCE_BRANCH = null;
    String IMAGE_TAG = null;
    String CRED_ID = null;
    String CRED_USERNAME = null;
    String CRED_PASSWORD = null;
   

if (map.ENVIRONMET?.trim() && map.STATUS?.trim() && map.SOURCE_BRANCH?.trim() && map.IMAGE_TAG?.trim() && map.CRED_USERNAME?.trim() && map.CRED_PASSWORD?.trim()){
    ENVIRONMET = "${map.ENVIRONMET}".trim()
    STATUS = "${map.STATUS}".trim()
    SOURCE_BRANCH = "${map.SOURCE_BRANCH}".trim()
    IMAGE_TAG = "${map.IMAGE_TAG}".trim()
    CRED_USERNAME = "${map.CRED_USERNAME}".trim()
    CRED_PASSWORD = "${map.CRED_PASSWORD}".trim()
    ENVIRONMET = ENVIRONMET.toUpperCase()
} else {
    error("Some of values are missing : \n ENVIRONMET : ${map.ENVIRONMET}, STATUS : ${map.STAUS}, SOURCE_BRANCH : ${map.SOURCE_BRANCH}, IMAGE_TAG : ${map.IMAGE_TAG}, CRED_USERNAME : ${map.CRED_USERNAME}, CRED_PASSWORD: ${map.CRED_PASSWORD}")
}


  withCredentials([usernamePassword(credentialsId: 'confluenseCred', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    int statusUpdate = 100
    int i = 0
    while (statusUpdate != 200 && i <= 5) {
        def siteConnection = new URL ("https://mangeshbangale.atlassian.net/wiki/rest/api/content/65661?expand=body.storage,version").openConnection()
        def credentials = USERNAME + ":" + PASSWORD
        println(USERNAME)
        println(PASSWORD)
        println(SOURCE_BRANCH)
        println(ENVIRONMET)
        siteConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())) ) 
        def responseCode = siteConnection.getResponseCode()
        println(responseCode)

        def date = new Date()
        def DEPLOY_TIME = date.format("dd/MM/yyyy HH:mm")
        def jsonData = new JsonSlurper().parseText(siteConnection.getInputStream().getText())

        def matcher = jsonData.body.storage.value =~ /<td colspan="1"><strong>${ENVIRONMET}<\/strong><\/td><td colspan="1">(.*?)<\/td><td colspan="1">(.*?)<\/td><td colspan="1">(.*?)<\/td><td colspan="1">(.*?)<\/td>/
        
        if (STATUS.isEmpty()) {
            STATUS = matcher[0][1]
        }

        if ( SOURCE_BRANCH.isEmpty() ) {
            SOURCE_BRANCH = matcher[0][2]
        }
        
        jsonData.body.storage.value = jsonData.body.storage.value.replaceFirst("<td colspan=\"1\"><strong>${ENVIRONMET}</strong><td><td colspan=\"1\">(.*?)</td><td colspan=\"1\">(.*?)</td><td colspan=\"1\">(.*?)</td><td colspan=\"1\">(.*?)</td>", "<td colspan=\"1\"><strong>${ENVIRONMET}</strong><td><td colspan=\"1\">${STATUS}</td><td colspan=\"1\">${SOURCE_BRANCH}</td><td colspan=\"1\">${IMAGE_TAG}</td><td colspan=\"1\">${DEPLOY_TIME}</td>" )
        jsonData.version.number += 1

        // save new confluense page
        println("saving new confluence page...")
        siteConnection = new URL("https://mangeshbangale.atlassian.net/wiki/rest/api/content/65661").openConnection()
        siteConnection.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes())))
        siteConnection.setRequestMethod("PUT")
        siteConnection.setDoOutput(true)
        siteConnection.setRequestProperty("content-type", "application/json")
        siteConnection.getOutputStream().write(new JsonBuilder(jsonData).toPrettyString().getBytes("UTF-8"))
        statusUpdate = siteConnection.getResponseCode();
        println("page updated successfully...")

        if (statusUpdate != 200) {
            sleep(1000)  // 1 sec delay - may be another process try to update confluense page
        }
        i++

        if (statusUpdate != 200 && i == 5) {
            throw new Exception(" Coudn't obtain confluense page")
        }

    }
}


}
