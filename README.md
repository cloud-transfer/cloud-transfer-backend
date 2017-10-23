
<h1 align="center">
  Save to Google Drive
  <br>
</h1>

<h4 align="center">A web application to download file directly to google drive from URL.<h4>

<div align="center">

  <!-- Build Status -->
  <a href="https://travis-ci.org/dhavalmehta1997/url-to-google-drive">
    <img src="https://travis-ci.org/dhavalmehta1997/url-to-google-drive.svg?branch=master"
      alt="Build Status" />
  </a>
 
  <!-- Contributions -->
  <a href="">
    <img src="https://img.shields.io/badge/contributions-welcome-orange.svg"
      alt="Contributions welcome" />
  </a>

 <!-- issues -->
  <a href="https://github.com/dhavalmehta1997/url-to-google-drive/issues">
    <img src="https://img.shields.io/github/issues/dhavalmehta1997/url-to-google-drive.svg"
      alt="Issues" />
  </a>
  
 <!-- License --> 
  <a href="https://github.com/dhavalmehta1997/url-to-google-drive/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/dhavalmehta1997/url-to-google-drive.svg"
      alt="License MIT" />
  </a>
</div>
  
## Table of Content  
- [Features](#features)
- [Live Demo](#live-demo)
- [Build Process](#build-process)
- [Built With](#built-with)
- [Bug Report](#bug-report)
- [Feature Request](#feature-request)
- [Team](#team)
- [Links](#links)
- [Contributing](#contributing)
- [License](#license)

## Features

* OAuth - Say good bye to registration and login
  - Use Google account to login
* No upload size limit
* No access to your personal files
* No Concurrent Upload limit
* Automatically extraction of filename from URL
* Small API: with only 6 methods there's not much to learn

## Live Demo

Here is a working live demo: [https://savetogoogledrive.herokuapp.com](https://savetogoogledrive.herokuapp.com)

## Build Process

```bash
# Clone this repository
$ git clone https://github.com/dhavalmehta1997/url-to-google-drive

# Go into the repository
$ cd url-to-google-drive

# Build the app
$ mvnw package

# Run the app
$ mvnw spring-boot:run
```

## Built With

This application uses several open source packages to run.

- [Spring](https://spring.io/)
- [Gson](https://github.com/google/gson)
- [Swagger](http://springfox.github.io/springfox/)
- [Apache commons email](https://commons.apache.org/proper/commons-email/)
- [Apache commons io](https://commons.apache.org/proper/commons-io/)
- [Apache Tomcat](http://tomcat.apache.org/)

## Bug Report

If you find a bug (the website couldn't handle the upload request and / or gave undesired results), kindly open an issue [here](https://github.com/dhavalmehta1997/url-to-google-drive/issues/new) by including your upload URL.

## Feature Request

Feature requests are always welcome. If you'd like to request a new function, feel free to do so by opening an issue [here](https://github.com/dhavalmehta1997/url-to-google-drive/issues/new). Please include sample queries and their corresponding results.

## Team

- [Dhaval Mehta](https://github.com/dhavalmehta1997)
- [Aditya Krishnakumar](https://github.com/beingadityak)

## Links

* [Web site](https://savetogoogledrive.herokuapp.com)
* [Documentation](https://savetogoogledrive.herokuapp.com/swagger-ui.html)
* [Bug Report](https://savetogoogledrive.herokuapp.com/bug_report.jsp)
* [Issue tracker](https://github.com/dhavalmehta1997/url-to-google-drive/issues)
* [Source code](https://github.com/dhavalmehta1997/url-to-google-drive/)

## Contributing

We always welcome new contributors. If you wish to contribute, please take a quick look at the [guidelines](./CONTRIBUTING.md)!

## License

MIT © Dhaval Mehta
