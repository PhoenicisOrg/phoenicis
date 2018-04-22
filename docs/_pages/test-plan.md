---
title: "Test plan"
permalink: /test-plan/
toc: true
---

This page describes the test plan which is used to verify that Phoenicis works as expected.

Before you start, ensure that you are in a clean environment:
* move `.Phoenicis` to `.Phoenicis_bak`
* checkout and pull the desired branch/revision from upstream `phoenicis`
* do a clean build (`mvn clean package`)

## Applications
* open "Applications" tab
* check that several categories are shown
* check that applications are listed
* check that list style can be switched
* click a category
* check that apps are filtered
* check "testing"
* check that "testing" apps are shown (if there are any in the scripts repository)
* search for "notep"
* check that categories are filtered
* check that apps are filtered
* click "Notepad++"
* check that details panel opens
* if you don't use the English version: check that description is translated
* click "install"
* check that "Installations" tab is opened

# Engines
* open "Engines" tab
* check that Wine categoriy is shown
* check that engines are listed
* check that list style can be switched
* search for "3.0"
* check that engines are filtered
* open "Upstream linux x86"
* click "3.0"
* check that details panel opens
* click "install"
* approve pop-up
* check that "Installations" tab is opened

## Installations
* check that "Applications" and "Engines" categories are shown
* check that installations of Notepad++ and Wine 3.0 are listed
* search for "notep"
* check that categories are filtered
* check that installations are filtered
* click "Notepad++"
* check that details panel is shown
* click Wine 3.0 installation
* check that details panel is shown
* wait until Wine 3.0 installation is finished
* check that Wine 3.0 is installed in `.Phoenicis/engines`

## Engines
* open "Engines" tab
* check that Wine 3.0 is marked as installed
* delete Wine 3.0
* check that Wine 3.0 is deleted from `.Phoenicis/engines`
* check that Wine 3.0 is marked as not installed

## Installations
* open "Installations" tab
* click "Notepad++"
* follow installation wizard and finish installation

