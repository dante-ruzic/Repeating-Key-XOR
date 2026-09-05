# Repeating Key XOR Assignment
Name: 
* Dante Ruzic

Language Used: 
* Java (OpenJDK 26.0.2)

 Build/Test: 
* Makefile is used by running "make tests"
* To test the code without makefile simply run "javac XOR.java" then type "java XOR encrypt/decrypt/keygen [along with arguments required for each such as --length, --text, --key, and --ciphertext]. 

 Example:
```Example
C:\Users\[user]\Documents\GitHub\Repeating-Key-XOR> javac XOR.java
C:\Users\[user]\Documents\GitHub\Repeating-Key-XOR> java XOR keygen --length 10
44b8c9edd4f7b46d9b02

C:\Users\[user]\Documents\GitHub\Repeating-Key-XOR> java XOR encrypt --key 44b8c9edd4f7b46d9b02 --text "Hello world"
0cdda581bbd7c302e96e20

C:\Users\[user]\Documents\GitHub\Repeating-Key-XOR> java XOR decrypt --key 44b8c9edd4f7b46d9b02 --ciphertext 0cdda581bbd7c302e96e20
Hello world
```

 Limitations: 
* System used for this was Windows, therefore any examples or ways to run the code have only been tested on Powershell and Command Line.

 Disclosure:
* I used A.I. to help with mathematical proof understanding if anything did not make sense, such as using Mark Down for mathematics (as finding XOR symbols, etc).
* I verified the Mark Down using the following site "https://markdownviewer.org/".
