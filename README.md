# Monoliti che Evolvono!

This project is a Kotlin-based backend application using Ktor and Gradle. It is the code for the workshop _Monoliti che
Evolvono!_ workshop being conducted at the Working Software Conference on June 14, 2024 in Brescia.

## Installation

Instructions to set up your local development environment.

### Prerequisites

- [JDK 17]() - Java Development Kit
- [Gradle 8.5](https://gradle.org/install/) - Build tool

### Clone the repository

```bash
$ git clone https://github.com/bombo82/monoliti-che-evolvono-wsc2024.git
$ cd monoliti-che-evolvono-wsc2024
```

### Build the project

Run the following to build the application without running tests:

```bash
$ ./gradlew build -x test 
```

### Running the application

Run the following to run the application:

```bash
$ ./gradlew run
```

## Running the Tests

There are some kinds of test inside the repository. The more important one is the _CustomerTest_ that contains the E2E
test cases written from the customer point of view. Inside the repository are present also unit test and test for the
application APIs.

> [!IMPORTANT]
> The _Customer Test_ does not automatically run the application, so you need to run the application before launch the
_Customer Test_.

Run the following to execute all the test present:

```bash
$ ./gradlew test
```

## Built With

- [Kotlin](https://kotlinlang.org/) - The programming language used
- [Ktor](https://ktor.io/) - Asynchronous HTTP server and client framework based on coroutines
- [Gradle](https://gradle.org/) - Build tool

## Thanks to
- Marco
- Mattia
- Alessio
- Daniele
- Fabrizio
- Nicola
- Ludovico
- Andrea
- Andrea
- Matteo
- Mauro
- Alin

## Special thanks to
- Elisabetta
- Alessandro

## License

This project is licensed under the GNU General Public License v3.0 - see the LICENSE.md file for details.
