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
<figure>
  <img src="{{ '/assets/images/themes/mainWindowScene.png' | absolute_url }}" alt="mainWindowScene">
</figure>

### Logo
- logoText
<figure>
  <img src="{{ '/assets/images/themes/logo.png' | absolute_url }}" alt="logo">
</figure>

### Menu
- menuPane
- menuPane > .tab-header-area .tab
- menuPane > .tab-header-area .tab-label
- menuPane > .tab-header-area .tab-header-background
- menuPane > .tab-header-area .tab:selected
- menuPane > .tab-header-area .tab:selected .focus-indicator
- menuPane > .tab-header-area .tab:disabled
<figure>
  <img src="{{ '/assets/images/themes/menuPane.png' | absolute_url }}" alt="menuPane">
</figure>

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

<figure>
  <img src="{{ '/assets/images/themes/leftPane.png' | absolute_url }}" alt="leftPane">
</figure>
- searchBar
- searchBar:hover
- searchCleanButton

<figure>
  <img src="{{ '/assets/images/themes/searchBar.png' | absolute_url }}" alt="searchBar">
</figure>
- leftBarTitle

<figure>
  <img src="{{ '/assets/images/themes/leftBarTitle.png' | absolute_url }}" alt="leftBarTitle">
</figure>
- leftButton
- leftButton:hover, .leftButton:selected

<figure>
  <img src="{{ '/assets/images/themes/leftButton.png' | absolute_url }}" alt="leftButton">
</figure>
- listChooser
  - listIcon
  - compactList
  - detailsList
  - iconsList

<figure>
  <img src="{{ '/assets/images/themes/listChooser.png' | absolute_url }}" alt="listChooser">
</figure>

### Right
- rightPane
- rightPane > .viewport
<figure>
  <img src="{{ '/assets/images/themes/rightPane.png' | absolute_url }}" alt="rightPane">
</figure>

### Apps
- appPresentation
- appPresentation > *
- appPanelMiniaturesPane
- appPanelMiniaturesPaneWrapper
- appPanelMiniaturesPaneWrapper > .viewport
<figure>
  <img src="{{ '/assets/images/themes/apps.png' | absolute_url }}" alt="apps">
</figure>

#### App Description
- descriptionTitle
<figure>
  <img src="{{ '/assets/images/themes/appDescriptionTitle.png' | absolute_url }}" alt="appDescriptionTitle">
</figure>

The app description text is HTML in a WebView. It can be customized in the file description.css.
<figure>
  <img src="{{ '/assets/images/themes/appDescription.png' | absolute_url }}" alt="appDescription">
</figure>

- appMiniature
<figure>
  <img src="{{ '/assets/images/themes/appDescriptionMiniature.png' | absolute_url }}" alt="appDescriptionMiniature">
</figure>

### Containers
- containerConfigurationPane
- containerConfigurationPane > .grid
<figure>
  <img src="{{ '/assets/images/themes/containers.png' | absolute_url }}" alt="containers">
</figure>

### Miniature
- rightPane .miniatureList
- miniatureListElement
- miniatureListElement:hover
- miniatureImage
- miniatureText
<figure>
  <img src="{{ '/assets/images/themes/miniature.png' | absolute_url }}" alt="miniature">
</figure>

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
<figure>
  <img src="{{ '/assets/images/themes/header.png' | absolute_url }}" alt="header">
</figure>
- footer
<figure>
  <img src="{{ '/assets/images/themes/footer.png' | absolute_url }}" alt="footer">
</figure>
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
<figure>
  <img src="{{ '/assets/images/themes/console.png' | absolute_url }}" alt="console">
</figure>

### Wine Tools
- wineToolButton
<figure>
  <img src="{{ '/assets/images/themes/wineToolButton.png' | absolute_url }}" alt="wineToolButton">
</figure>
- wineToolCaption
<figure>
  <img src="{{ '/assets/images/themes/wineToolCaption.png' | absolute_url }}" alt="wineToolCaption">
</figure>

## Icons
To add custom icons to your theme, just place them in an "icons" folder in your theme directory. For a list of icons, please have a look at the [default theme](https://github.com/PlayOnLinux/POL-POM-5/tree/master/phoenicis-javafx/src/main/resources/org/phoenicis/javafx/themes/default). You do not have to use a special resolution, as the icons will be resized automatically. Just make sure the resolution is high enough to work on high DPI displays.
