# Homework Assignment for NoviLabs
## Assignment 1
### Overview
The CSV file processing part of the assignment is a simple CLI Java app, using the Univocity CSV parser.
The overall design approach was to fulfill the requirements based on my personal interpretation (I tried to
get clarifications from Kevin, but according to him making these choices is part of the challenge), while
trying to keep the design flexible enough to accommodate different interpretations and possible improvements
and extensions without overengineering. 

### Design Decisions
- When calculating a replacement value for a missing value, I decided to only consider values from the
  same column. I really cannot think of any reason why a column would want to include values from other,
  unrelated columns.
- When discarding an invalid value (e.g. "foo" when an integer is expected), we set the processed value
  to `null`, but that cell will not participate in the replacement value resolution process. This is what
  the documentation suggests, but it'd be easy to adjust.
- Any runtime exception other than the exception signaling missing required value (i.e. for the ID column)
  will stop the processing. 
- I included 6 different column data types based on the sample data: 
  - ID
  - String
  - Integer
  - Decimal
  - Date
  - Category

  More can be easily added as needed.

### Technology Stack
- Java 21
- Maven 3.9.x
- Univocity parser
- Slf4j + Logback for logging
- JUnit 5 for testing
- Docker (for Assignment 2)

### Build
The following command builds an uber-jar with all dependencies under `target/nl-home-assignment-1.0-SNAPSHOT.jar`:
```
mvn clean install
```
(alternatively, it can be built and packaged into a Docker container in one step, see below in Assignment 2)

## Run
To execute the application from the command line, run
```
java -jar target/nl-home-assignment-1.0-SNAPSHOT.jar
```
If no additional arguments are provided, the default input file will be `novi-labs-java-assignment-data.csv`,
as specified in the requirements. If extra arguments are specified, the first one will become the name of the
input file, while the second becomes the name of the output file. E.g.
```
java -jar target/nl-home-assignment-1.0-SNAPSHOT.jar in.csv out.csv
```
will read the input from `in.csv` and write into `out.csv`.

## Assignment 2

### Overview
It was somewhat difficult to come to terms with a Docker application operating on files and file names, as 
for example using the standard input and output would have been more natural, but eventually I came up with
a Docker volume-based approach (see `run_from_docker.sh`), where both the input file and the output directory
are mounted as volumes.

Due to time constraints, I did not spend an overwhelming amount of time trying to make the scripts super
flexible, but they can be spruced up a bit (e.g. adding proper command-line option support, reading files
from any directory, error checking on files/file permissions, etc.).

### Build
The included Dockerfile does a multi-stage build so it can be built anywhere, even without Java or Maven installed 
on the computer. This can effectively replace the local Maven build, although it is much slower due to the
Maven cache always starting from empty, however using a volume would solve this issue.
The name of the produced image is `nv-home-assignment`. You can run the convenience `build_image.sh`
script or execute:
```
docker build -t nv-home-assignment:latest .
```

### Run
As mentioned above the `run_from_docker.sh` script can be used to mount the default input file as volume,
and produce the result on the local machine in the `out-dir` directory. If an extra argument is provided,
for example:
```
run_from_docker.sh foo.csv
```
then that will be used as input file name (assumed to be in the current directory), and the output will be
produced in `out-dir/foo-out.csv`

## Bonus Assignments

[See in this repository.](https://github.com/good2fly/novilabs-bonus-assignment)
