build:
	javac src/*.java -d .;
	javac -cp classes src/*.java -d .;
clean:
	rm -rf ./*.class
