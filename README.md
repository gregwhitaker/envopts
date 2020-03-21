# envopts
[![Build Status](https://travis-ci.org/gregwhitaker/envopts.svg?branch=master)](https://travis-ci.org/gregwhitaker/envopts)

Parses an environment variable containing multiple system parameters and sets the parameters for use in a Java application.

This is useful for when you want to supply a bunch of system parameters to a Docker container.

## Importing
The library is available on JCenter and Maven Central:

* Maven

        <dependency>
            <groupId>com.github.gregwhitaker</groupId>
            <artifactId>envopts</artifactId>
            <version>1.0.0</version>
        </dependency>
        
* Gradle

        implementation 'com.github.gregwhitaker:envopts:1.0.0'

## Usage
Simply supply an environment variable named `ENV_OPTS` with a comma-delimited string of system parameters you would like
to set on the JVM:

    ENV_OPTS=-Dspring.profiles.active='local,dev',-Dlog4j.configurationFile=log4j2.xml
    
Then in your application startup add the following:

    EnvOpts.parse();
    
Once the environment variable is parsed you will be able to access the system parameters like so:

    System.getParameter("spring.profiles.active")

## Building from Source
Run the following command to build EnvOpts from source:

    ./gradlew clean build

## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/gregwhitaker/envopts/issues).

## License
MIT License

Copyright (c) 2020 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.