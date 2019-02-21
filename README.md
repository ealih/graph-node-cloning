# Graph Node Cloning

This is a solution for assignment (check Assignment.md).

## Running the precompiled app

cd to project root directory and run

`java -jar app.jar json-file entityID`

e.g.

`java -jar app.jar data/valid.json 5`

## Building using gradle

Requires JDK to be installed on the machine.

cd to project root directory and run

`./gradlew buildJar`

And after that run

`java -jar app.jar json-file entityID`

e.g.

`java -jar app.jar data/valid.json 5`

## Running tests
In project root directory type

`./gradlew clean test`

### Notes
`data` folder contains some example JSON files as well as the original file that came with the assignment `valid.json`