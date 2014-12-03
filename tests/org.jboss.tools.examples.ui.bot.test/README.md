Executing tests:

mvn clean install -DexamplesFolder=${examplesFolder} -DquickstartsURL={quickstartsURL} -Dswtbot.test.skip=true

Where
  ${examplesFolder} is name of directory to which will be examples extracted
  ${quickstartsURL} url from where quickstarts should be downloaded
  
  
Errors and Warnings will be written to standard output after executing tests.
Also All errors will be written to files inside target/reports/<exampleName>.txt
