---
title: "Packages"
category: Developers
order: 2
---

Run the build before packaging.

### deb
Install required packages:
```
sudo apt install -y fakeroot openjdk-11-jdk-headless
```

build with:
```
cd phoenicis-dist/src/scripts
bash phoenicis-create-package.sh
```

**NOTE:** Currently, the native Ubuntu OpenJDK has issues with differing hashes. Use e.g. [SDKMAN!](https://sdkman.io/) to install a different JDK for packaging:
```
cd
sudo apt install -y curl
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 11.0.8.hs-adpt
export JAVA_HOME=~/.sdkman/candidates/java/current/
```

### Flatpak
Required extensions:
- org.freedesktop.Platform.Compat.i386
- org.freedesktop.Platform.GL32.nvidia-415-25
- org.freedesktop.Sdk.Extension.openjdk11

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
