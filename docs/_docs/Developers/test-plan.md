---
title: "Test plan"
category: Developers
order: 6
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

## Library
* open "Library" tab
* check that "All" and "Development" categories are shown
* check that Notepad++ is listed
* check that list style can be switched
* click "Notepad++"
* check that details panel is shown
* click "run"
* check that Notepad++ opens
* click "close"
* check that Notepad++ closes
* right-click "Notepad++" and select "Edit"
* check that edit form is shown in details panel
* fill "Arguments" with "/home/<username>/.Phoenicis/shortcuts/Notepad.shortcut"
* click "save"
* run Notepad++
* check that Notepad++ opens and shows the Notepad.shortcut file
* close Notepad++
* click "Create shortcut"
* click "create"
* check that the required fields are highlighted
* fill with
    * name: "Test"
    * category: "test-category"
    * executable: "/home/<username>/.Phoenicis/containers/wineprefix/Notepad/drive_c/Program Files/Internet Explorer/iexplore.exe"
* click "create"
* check that "All", "Development" and "test-category" categories are shown
* check that "Test" shortcut is listed with default miniature
* search for "Test"
* check that categories are filtered
* check that only "Test" shortcut is listed
* run "Test" shortcut
* check that Wine Internet Explorer opens
* close Wine Internet Explorer
  
## Containers
* open "Containers" tab
* check that "All" and "wineprefix" categories are shown
* check that Notepad is listed with composed miniature (half Notepad++, half default)
* check that list style can be switched

## Library
* open "Library" tab
* uninstall "Test" shortcut
* check that "Test" shorcut is not listed anymore

## Containers
* open "Containers" tab
* click "Notepad"
* check that details panel is shown
* check that tabs exist
    * Information
    * Display
    * Input
    * Wine Tools
    * Tools
* open "Wine Tools" tab
* check that all tools open
* open "Tools" tab
* click "Run executable"
* select "/home/<username>/.Phoenicis/containers/wineprefix/Notepad/drive_c/Program Files/Notepad++/notepad++.exe"
* check that Notepad++ opens
* close Notepad++
* open "Information" tab
* click "delete container"
* approve pop-up
* check that "Notepad" is not listed anymore   
* check that Notepad has been deleted from `.Phoenicis/containers/wineprefix`
   
## Library
* open "Library" tab
* check that "Notepad++" shortcut is not listed anymore
* check that the shortcut files have been deleted from `.Phoenicis/shortcuts`

## Settings
* open "Settings" tab
### User Interface
* open "User Interface"
* select all themes and check that they are applied
* change scaling
* check that UI is scaled
* maximize window
* click "reset"
* restart Phoenicis
* check that window is not maximized and default scaling and theme are used
### Repositories
* open "Repositories"
* check that default repositories are listed
* click "add"
* select "local repository"
* use "/home/<userhome>/.Phoenicis/cache/git-XXXXXX"
* click "finish"
* check that new repository is listed
* select new repository
* click "delete"
* approve pop-up
* check that repository has been removed from list
* click "add"
* select "local repository"
* use "/home/<userhome>/.Phoenicis/cache/git-XXXXXX"
* click "finish"
* check that new repository is listed
* click "reset
* restart Phoenicis
* check that repository has been removed from list
### File Associations
* open "File Associations"
* check that it is empty
### Network
* open "Network"
* check that it is empty
### About
* open "About"
* check that shown information is correct
