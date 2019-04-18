---
title: "Translation"
category: Developers
order: 7
---

1. run `mvn package` to update the `.pot` and `.po` files
2. create `po` per language (if it doesn't exist yet): `msginit -i keys.pot -o de.po` (for German)
3. translate (e.g. with poedit)
4. run `mvn package` again to generate the `.properties` with the translated messages
5. create a pull request containing the `.pot` and `.po` files
