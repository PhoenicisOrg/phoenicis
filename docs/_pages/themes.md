---
title: "Themes"
permalink: /docs/themes/
excerpt: "About themes"
toc: true
---

The JavaFX GUI can be customized with CSS themes. The themes are located in
```
phoenicis-javafx/src/main/resources/com/playonlinux/javafx/themes/
```

## CSS Elements

### Main Window Scene
- mainWindowScene
[[img/themes/mainWindowScene.png]]

### Logo
- logoText
[[img/themes/logo.png]]

### Menu
- menuPane
- menuPane > .tab-header-area .tab
- menuPane > .tab-header-area .tab-label
- menuPane > .tab-header-area .tab-header-background
- menuPane > .tab-header-area .tab:selected
- menuPane > .tab-header-area .tab:selected .focus-indicator
- menuPane > .tab-header-area .tab:disabled
[[img/themes/menuPane.png]]

### Left
- leftPane
- leftPaneScrollbar
- leftPaneScrollbar > .viewport
- leftPaneScrollbar .scroll-bar:vertical
- leftPaneScrollbar .increment-button, .leftPaneScrollbar .decrement-button
- leftPaneScrollbar .scroll-bar:vertical .track
- leftPaneScrollbar .scroll-bar:vertical .thumb
- leftPaneInside
- leftSpacer

[[img/themes/leftPane.png]]
- searchBar
- searchBar:hover
- searchCleanButton

[[img/themes/searchBar.png]]
- leftBarTitle

[[img/themes/leftBarTitle.png]]
- leftButton
- leftButton:hover, .leftButton:selected

[[img/themes/leftButton.png]]
- listChooser
  - listIcon
  - compactList
  - detailsList
  - iconsList

[[img/themes/listChooser.png]]

### Right
- rightPane
- rightPane > .viewport
[[img/themes/rightPane.png]]

### Apps
- appPresentation
- appPresentation > *
- appPanelMiniaturesPane
- appPanelMiniaturesPaneWrapper
- appPanelMiniaturesPaneWrapper > .viewport
[[img/themes/apps.png]]

#### App Description
- descriptionTitle
[[img/themes/appDescriptionTitle.png]]

The app description text is HTML in a WebView. It can be customized in the file description.css.
[[img/themes/appDescription.png]]

- appMiniature
[[img/themes/appDescriptionMiniature.png]]

### Containers
- containerConfigurationPane
- containerConfigurationPane > .grid
[[img/themes/containers.png]]

### Miniature
- rightPane .miniatureList
- miniatureListElement
- miniatureListElement:hover
- miniatureImage
- miniatureText
[[img/themes/miniature.png]]

### Installation Wizard
#### Presentation
- presentationBackground
- presentationScrollPane
- presentationTextTitle
- presentationText
<figure>
  <img src="{{ '/assets/images/themes/presentation.png' | absolute_url }}" alt="presentation">
</figure>


#### Step
- panelForTopheader
- header
[[img/themes/header.png]]
- footer
[[img/themes/footer.png]]
- stepScrollPane
- stepText
- dragAndDropBox

### Console
- console
- console .viewport
- consoleCommandType
- consoleCommandType:focused
- consoleText
- consoleText.normal
- consoleText.default
- consoleText.error
[[img/themes/console.png]]

### Wine Tools
- wineToolButton
[[img/themes/wineToolButton.png]]
- wineToolCaption
[[img/themes/wineToolCaption.png]]

## Icons
To add custom icons to your theme, just place them in an "icons" folder in your theme directory. For a list of icons, please have a look at the [default theme](https://github.com/PlayOnLinux/POL-POM-5/tree/master/phoenicis-javafx/src/main/resources/org/phoenicis/javafx/themes/default). You do not have to use a special resolution, as the icons will be resized automatically. Just make sure the resolution is high enough to work on high DPI displays.
