all: Tables.class

clean:
	$(RM) Tables.class

Tables.class: Tables.java
	javac Tables.java

run: Tables.class
	java Tables
