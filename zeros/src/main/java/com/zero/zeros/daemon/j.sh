cd /zero/manager
rm -f ZeroManager.class
javac ZeroManager.java
#java version 확인
java -cp .:/usr/java14_64/lib/*.jar:/usr/java14_64/jre/lib/*.jar:ojdbc14.jar:jtds-1.2.2.jar Manager