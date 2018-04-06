---
title: "Run"
permalink: /run/
toc: true
---

## JavaFX
```
phoenicis-javafx
```

## CLI
```
phoenicis-cli -install Notepad++
```
```
phoenicis-cli -run Notepad++
```

## Show games in native Steam
- add phoenicis-cli as [Non-Steam Game](https://support.steampowered.com/kb_article.php?ref=2219-YDJV-5557)
- rename it
- set launch parameters to `-run "<your_game>"`

## Troubleshooting
### Missing libudev.so.0
On some Linux distributions (e.g. Ubuntu 16.04) only libudev1 is available. However, this can also be used for Wine.
```bash
sudo ln -sf /lib/i386-linux-gnu/libudev.so.1 /lib/i386-linux-gnu/libudev.so.0
```
