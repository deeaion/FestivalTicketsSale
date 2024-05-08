
echo "Generating Java classes"
protoc -I=. --java_out=. --csharp_out=. festival.proto
