#!/usr/bin/python

import string, sys, os, re, subprocess, shutil, time, datetime, zipfile, xml.etree.ElementTree as ET, httplib, urllib, json

def main(*args):
   if len(args)<2:
      print 'PRINT USAGE HERE'
      return 0

   if not os.path.isdir(args[1]):
      print 'Provided path is not a directory!'
      return 0

   if len(args)==3:
      sURL = args[2]
   else:
      sURL = "localhost:8080/logdump/staticimporter"

   sHost = sURL[:sURL.find("/")]
   sHostEndpoint = sURL[sURL.find("/"):]
   
   sJettyPath = args[1]
   sWebappPath = args[1] + "/webapps/"

   aWebappList = os.listdir(sWebappPath)

   print 'Found %s directories. Starting analysis...' % (len(aWebappList))
   
   mWebapp = []
   
   for webapp in aWebappList:
      sFullPath = os.path.join(sWebappPath,webapp)
      print 'Analyzing %s' % (sFullPath)
      
      if not os.path.isdir(sFullPath):
         print 'Not a directory. Skipping...'
         continue

      info = extractInfo(sJettyPath, sFullPath)

      if info:
         mWebapp.append(info)
      else:
         print 'No info could be extracted. Skipping...'
         continue

   params = urllib.urlencode({"data":json.dumps(mWebapp)})
   
   conn = httplib.HTTPConnection(sHost)
   
   headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}

   conn.request("POST", sHostEndpoint, params, headers)

   response = conn.getresponse()

   if response.status != 200:
      print "Could not send data to server!"
   
   return 0

# Extracts an associative array with info about a service
# based on its path. Checks the jars for static info.
def extractInfo(sJettyPath, webappPath): 
   service = {}

   sLibPath = webappPath+"/WEB-INF/lib/"
   
   # Webapp is probably not a turmeric service
   if not os.path.isdir(sLibPath): return
   
   # List all jars
   aJarList = os.listdir(sLibPath);
   
   # The Impl jar provides the name of the service
   oMatcher = re.compile("(?P<serviceName>.*)Impl\-(?P<serviceVersion>[0-9]?\.[0-9]?\.[0-9]?)\.jar$")

   aImplJars = filter(oMatcher.search, aJarList)

   # More than one Impl jar! Should never happen... quit.
   if len(aImplJars) > 1:
      print "Found more than 1 implementation jar for "+webappPath+"! Skipping..."
      return

   result = oMatcher.match(aImplJars[0])
   
   service["name"] = result.group("serviceName")
   service["version"] = result.group("serviceVersion")
   service["endpoint"] = getLocalEndpoint(sJettyPath, webappPath, webappPath+"/WEB-INF/web.xml")
   service["dependencies"] = getDependencyList(sLibPath+aImplJars[0])

   conn = httplib.HTTPConnection(service["endpoint"]["host"]+":"+service["endpoint"]["port"])
   conn.request("GET", service["endpoint"]["url"]+"?wsdl")
   resp = conn.getresponse()

   if resp.status == 200:
      parsedOperations = []
      
      sWSDL = resp.read()

      tree = ET.fromstring(sWSDL)

      ns = "{http://schemas.xmlsoap.org/wsdl/}"
      soapns = "{http://schemas.xmlsoap.org/wsdl/soap/}"
      
      aOperations = tree.findall("%sbinding/%soperation/%soperation" % (ns,ns,soapns))

      for operation in aOperations:
         soapOperation = operation.attrib["soapAction"]
         soapOperation = soapOperation[soapOperation.rfind("/")+1:]

         parsedOperations.append(soapOperation)

      service["operations"] = parsedOperations
         
   return service

def getLocalEndpoint(sJettyPath, sWebappPath, sWebXMLPath):
   tree = ET.parse(sJettyPath+"/etc/jetty.xml")
   doc = tree.getroot()

   sPort = doc.find("Call/Arg/New/Set[@name='port']/Property").attrib["default"]
   
   tree = ET.parse(sWebXMLPath)
   doc = tree.getroot()

   ns = '{http://java.sun.com/xml/ns/j2ee}'

   sPattern = doc.find("%sservlet-mapping/%surl-pattern" % (ns,ns)).text

   return {"host":"localhost","port":sPort, "url":"/"+sWebappPath[sWebappPath.rfind("/")+1:]+sPattern}

def getDependencyList(sImplJar):
   aDependencies = []
   
   with zipfile.ZipFile(sImplJar, 'r') as jar:
      aContents = jar.namelist()

      oMatcher = re.compile(".*\/ClientConfig.xml$")

      aConfigs = filter(oMatcher.search, aContents)

      for confFile in aConfigs:
         fp = jar.open(confFile)

         # Not very efficient to load to memory but alas...
         #xmlConf = ''.join(fp.readlines())

         tree = ET.parse(fp)
         doc = tree.getroot()

         ns = '{http://www.ebayopensource.org/turmeric/common/config}'

         sClassName = doc.find("%sclient-config/%sservice-interface-class-name" % (ns,ns)).text
         sClassName = sClassName[sClassName.rfind(".")+1:]

         #dependencies[] = {}
         sLocation = doc.find("%sclient-config/%sservice-location" % (ns,ns)).text
         
         aDependencies.append({"serviceName":sClassName, "serviceLocation":sLocation})
         
         #print doc.find("{http://www.ebayopensource.org/turmeric/common/config}client-config")
         #print xmlConf
         #print doc.tag
         # TODO: Can't seem to get elements from document...
         
      #print aConfigs
    
   return aDependencies
 
if __name__ == '__main__':
    sys.exit(main(*sys.argv))
