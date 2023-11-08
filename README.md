# molecule


# build
mvn clean compile assembly:single

# run inspector with test.in
java -jar target/sort-0.0.1-SNAPSHOT-jar-with-dependencies.jar test.in test.in