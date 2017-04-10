#COMPILE
echo "Building project..."

#COMPILE JAVA
mkdir -p ./bin
javac -d "bin" source/**/*.java

echo "Project built in \"bin\" folder."
