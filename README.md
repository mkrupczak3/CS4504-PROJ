# CS4504 PROJ 1 P1

## Members:

### **documentation report working group**
* Thomas
* Mark
* Leiko
* Matthew

### **benchmarking working group**
* Brandon
* Sahan
* Dillon
* Matthew


## Description

Enclosed in this repository is the code and the project documents we will be using for this project. See project specification [part 1](./doc/Project-Specification-Part1.pdf) and [part 2](./doc/Project-Specification-Part2.pdf) for details

## Operation Guide

Clone this repository to your disk, then enter the project directory.

### Run the **Router** program:
```bash
javac src/*.java
java src/TCPServeRouter.java
```

**OR**

```bash
docker-compose up --force-recreate -t 1
```

### Run the **Server** program:
```bash
javac src/*.java
java src/TCPServer.java <router IP> <client IP> <port number>
```

### Run the **Client** program:

#### TXT mode:
```bash
javac src/*.java
java src/TCPClient.java localfilename.txt <router IP> <server IP> <port number>
```

#### base64payload mode (any filetype other than .txt):
```bash
javac src/*.java
java src/TCPClient.java song.mp3 <router IP> <server IP> <port number>
```

### **(Alternative)** Run all parts locally with docker-compose:

Running all parts locally requires [Docker](https://www.docker.com/) and [docker-compose](https://docs.docker.com/compose/install/) to be installed first.

#### Router only (and publish 5555 on host):
```bash
docker-compose up -d --force-recreate -t 1
```

#### All parts, txt mode:
```
docker-compose -f docker-compose-test-local-txt-mode.yml up --force-recreate -t 1
```

#### All parts, base64 mode:
```
docker-compose -f docker-compose-test-local-base64-mode.yml up --force-recreate -t 1
```

### Docker Troubleshooting
If your shell complains about a network address not being available, run the following:

```bash
docker-compose -f docker-compose-test-local-txt-mode.yml down -t 1
docker network prune
```

If containers are erroring out, rebuild them from source (bypassing the stale cache):
```bash
docker-compose -f docker-compose-test-local-txt-mode.yml build --no-cache
```

## Project Responsibilities

### Documentation Working Group

The responsibilities of the **documentation working group** include:

1. Audit the codebase and add inline comments ([Javadoc](https://www.baeldung.com/javadoc) or otherwise)

   Fork this repository to your account, add your comments, then commit, push, and create a pull request from your fork to this repository. See [this link detailing the process](https://reflectoring.io/github-fork-and-pull/)

2. Provide the benchmarking working group with actionable insights for standing up a lab environment

3. Create a user guide document describing the process for running the lab environment (see sample document [here](./doc/Part1-UserGuide%20-%20Sample.pdf))

4. Write a report based on the benchmarking data from the lab environment, according to the [project specification](./doc/Project-Specification-Part1.pdf)

5. Assemble the final deliverable as a .zip archive, due Mon 02/13 at 11:59pm

### Benchmarking Working Group

The responsibilities of the **benchmarking working group** include:

1. Fix bugs and make the project executable in a lab environment

2. Containerize and automate the build process

3. Make a process which can be run entirely local for testing

4. Bring up a lab environment and test using multiple computers

5. Benchmark performance and statistics in a lab environment using multiple computers

6. Provide context and assistance to the documentation working group as they assemble the report and user guide


# TODO

- [x] Each member of the documentation report working group must make a fork, add comments, and create a pull request
- [x] make TCPClient and TCPServer send and receive binary data
- [x] parameterize IP and ports for all programs (make it not hardcoded - default values are ok)
- [ ] After doing a close reading of the code, estimate the results of the lab benchmarking and begin to stub out a report and user guide (use [this Google Doc](https://docs.google.com/document/d/1GBz-MCbro0JrvfaEXTlpnCDcOlLi4INHzIb_OOtZvw8/edit?usp=sharing))
