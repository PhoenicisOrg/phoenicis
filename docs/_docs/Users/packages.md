---
title: "Packages"
category: Users
order: 2
---

The packaging is done in phoenicis-dist. 

The built packages are located in the target folder:
- .zip
- .deb


### Flatpak
```
cd phoenicis-dist/src/flatpak/
flatpak-builder build-dir org.phoenicis.javafx.json --force-clean
```
run with:
```
flatpak-builder --run build-dir org.phoenicis.javafx.json phoenicis-javafx
```
**Hint:** If you cannot compile after building the flatpak, remove `build-dir` and `.flatpak-builder`.
