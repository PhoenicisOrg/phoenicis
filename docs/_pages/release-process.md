---
title: "Release process"
permalink: /release-process/
toc: false
---

The following steps must be executed to release a new version of Phoenicis:
* Create release branches (e.g. "5.0-alpha") for phoenicis and scripts
* Protect release branch
    * Require pull request reviews before merging
    * Require status checks to pass before merging (Travis CI and Codacy)
* on the release branch:
    * Add release branch to `branches` section of `.travis.yml` such that Travis CI executes checks for the branch
    * Specify scripts release branch in configuration (`application.repository.default.git.url`)
    * Set release version for Maven in `pom.xml` files
    * Set release version in .deb control files
* Test
* Create GitHub release
* Announce release on phoenicis.org
