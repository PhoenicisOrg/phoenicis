#!/bin/bash
diffs=$(git diff --name-only | grep -v ^i18n/ | wc -l)
if [ $diffs != 0 ]; then
  echo "Some files are not formatted!"
  git diff --name-only | grep -v ^i18n/
  exit 1
fi
exit 0
