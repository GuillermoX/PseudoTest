javac -d out $(find src -name "*.java")    
echo "Java compiled"
jar cfm input/PseudoTest.jar Manifest.mf -C out .
echo ".Jar created"
