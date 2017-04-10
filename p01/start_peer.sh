#start Peer	
echo "Starting peer..."
java -cp ./bin systems.Peer 1.0 1 accessPoint 224.0.0.1 8000 224.0.0.2 8001 224.0.0.3 8002

