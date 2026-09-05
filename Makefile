JC = javac
JFLAGS = -g

default: XOR.class TestXOR.class xor_vigenere

XOR.class: XOR.java
	$(JC) $(JFLAGS) XOR.java

TestXOR.class: TestXOR.java XOR.class
	$(JC) $(JFLAGS) TestXOR.java

xor_vigenere:
	echo '#!/bin/bash' > xor_vigenere
	echo 'java XOR "$$@"' >> xor_vigenere
	-chmod +x xor_vigenere

tests: XOR.class TestXOR.class
	java -ea TestXOR

clean:
	-rm -f *.class xor_vigenere