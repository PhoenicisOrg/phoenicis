---
title: "Update documentation"
category: Developers
order: 7
toc: true
---

### Setup Jekyll
* Install bundler
```
gem install bundler
```
* Install Jekyll and other dependencies
```
cd phoenicis/docs
bundle install
```

### Update documentation
* Change or add .md in `_docs`

### Test
* Build html
```
cd phoenicis/docs
bundle exec jekyll serve
```
* Check in browser: [http://127.0.0.1:4000/](http://127.0.0.1:4000/)

