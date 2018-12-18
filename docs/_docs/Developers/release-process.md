---
title: "Release process"
category: Developers
order: 5
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
    * Set release version in Flatpak manifest (`phoenicis-dist/src/flatpak/org.phoenicis.javafx.json`)
    * Set release version in .deb control files
* [Test]({{ site.baseurl }}{% link _docs/Developers/test-plan.md %})
* Create GitHub release from the release branches for phoenicis and scripts
    * attach .zip
    * attach .deb
    * attach .dmg
    * attach .flatpak (see [single-file bundle](http://docs.flatpak.org/en/latest/single-file-bundles.html))
* Announce release on phoenicis.org
    * Showcase new features
    * List major changes/fixed bugs
