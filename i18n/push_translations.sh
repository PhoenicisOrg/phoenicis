#!/bin/bash
git config --global user.email "travis@travis-ci.org"
git config --global user.name "Travis CI"

head_ref=$(git rev-parse HEAD)
if [[ $? -ne 0 || ! $head_ref ]]; then
    echo "could not retrieve HEAD reference"
    exit 1
fi
branch_ref=$(git rev-parse "$TRAVIS_BRANCH")
if [[ $? -ne 0 || ! $branch_ref ]]; then
    echo "could not retrieve $TRAVIS_BRANCH reference"
    exit 1
fi
if [[ $head_ref != $branch_ref ]]; then
    echo "HEAD ref ($head_ref) does not match $TRAVIS_BRANCH ref ($branch_ref)"
    echo "new commits have been added before this build cloned the repository"
    exit 0
fi
if [[ $TRAVIS_BRANCH != master ]]; then
    echo "will not push translation updates to branch $TRAVIS_BRANCH"
    exit 1
fi
git checkout master

# check if anything except "POT creation date" has changed
git diff --numstat i18n/keys.pot |  awk '{ if($1 == 1 && $2 == 1) { exit 1 } else exit 0 }'
if [ $? == 0 ];  then
    git add *.pot
    git commit --message "Update translations"
    if ! git push https://$GH_TOKEN@github.com/PhoenicisOrg/phoenicis.git > /dev/null 2>&1; then
        echo "could not push translation updates"
        exit 1
    fi
fi
