all: RSAKeyGen.jar RSAEnc.jar RSADec.jar

RSAKeyGen.jar: src/RSAKeyGen.class 
	jar cfe RSAKeyGen.jar src.RSAKeyGen src/RSAKeyGen.class

src/RSAKeyGen.class: src/RSAKeyGen.java
	javac -cp .:src/ src/RSAKeyGen.java

RSAEnc.jar: src/RSAEnc.class 
	jar cfe RSAEnc.jar src.RSAEnc src/RSAEnc.class

src/RSAEnc.class: src/RSAEnc.java
	javac -cp .:src/ src/RSAEnc.java

RSADec.jar: src/RSADec.class 
	jar cfe RSADec.jar src.RSADec src/RSADec.class

src/RSADec.class: src/RSADec.java
	javac -cp .:src/ src/RSADec.java

clean:
	rm -f src/*.class

scrub: clean
	rm -f *.jar

