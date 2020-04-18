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
- org.freedesktop.Sdk.Extension.openjdk11

**NOTE:** Newer versions of the OpenJDK extension cause issues with Graal.
Downgrade to the OpenJDK 11.0.6 version with:
```
sudo flatpak update --commit c554d684bdfc4ab5f09a99b554c3c9b219770416e4af257c2715b2810df2b850 org.freedesktop.Sdk.Extension.openjdk11//19.08
```
build with:
```
cd phoenicis-dist/src/flatpak/
flatpak-builder build-dir org.phoenicis.playonlinux.yml --force-clean --user --install
```
run with:
```
flatpak run org.phoenicis.playonlinux phoenicis-playonlinux
```
**Hint:** If you cannot compile after building the flatpak, remove `build-dir` and `.flatpak-builder`.
