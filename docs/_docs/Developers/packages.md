---
title: "Packages"
category: Developers
order: 2
---

The packaging is done in phoenicis-dist. 

The built packages are located in the target/packages folder:
- .zip
- .deb


### Flatpak
Required extensions:
- org.freedesktop.Platform.Compat.i386
- org.freedesktop.Platform.GL32.nvidia-415-25

```
cd phoenicis-dist/src/flatpak/
flatpak-builder build-dir org.phoenicis.playonlinux.yml --force-clean --user --install
```
run with:
```
flatpak run org.phoenicis.playonlinux phoenicis-playonlinux
```
**Hint:** If you cannot compile after building the flatpak, remove `build-dir` and `.flatpak-builder`.
