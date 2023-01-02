#!/usr/bin/env groovy

void call(Map map = [ : ]){

String NAME = null;
String PASS = null;
 withCredentials([usernamePassword(credentialsId: 'fullName', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
  NAME = USERNAME;
  PASS = PASSWORD;
}
println(NAME)
println(PASS)

}
