---
title: "Packages"
category: Users
order: 2
---

The packaging is done in phoenicis-dist. 

The built packages are located in the target/packages folder:
- .zip
- .deb


### Flatpak
Required extensions:
- org.freedesktop.Platform.Compat.i386
- org.freedesktop.Platform.GL32.nvidia-410-73

```
cd phoenicis-dist/src/flatpak/
flatpak-builder build-dir org.phoenicis.playonlinux.json --force-clean --user --install
```
run with:
```
flatpak run org.phoenicis.playonlinux phoenicis-playonlinux
```
**Hint:** If you cannot compile after building the flatpak, remove `build-dir` and `.flatpak-builder`.
