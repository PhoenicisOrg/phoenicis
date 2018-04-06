---
title: "Repository"
permalink: /repository/
toc: true
---

The repository contains the scripts for applications, engines, verbs etc.

There are several possible repository types, which can be set in the `repositories.json` file in your `.Phoenicis` folder:
- Java classpath
- Git
- local directory

The content inside the `repositories.json` file is a json array of `repository` objects.

For example:
```JSON
[    
    {
        "type":"git",
        "gitRepositoryUri":"https://github.com/PlayOnLinux/Scripts",
        "branch":"master"
    }
]
```

When multiple repositories are specified and a script is available in more than one repository, the first entry in the list will be selected (i.e. repository hierarchy is from left to right).

### Java classpath
A Phoenicis Java classpath repository is a repository contained inside the Java classpath of Phoenicis.

To add a repository contained inside the Java classpath, please add the object
```json
{
   "type":"classpath",
   "packagePath":"<classpath to repository>"
}
```
to the `repositories.json` file.
Inside this object `<classpath to repository>` needs to be substituted by the correct path to the repository folder inside the classpath.

An example for a Java classpath repository object would be:
```json
{
   "type":"classpath",
   "packagePath":"/org/phoenicis/repository"
}
```

### Git
A Phoenicis Git repository is a repository, which is located inside a Git repository, which can be found on GitHub for example.

To add a Git repository to POL 5, please add the object
```json
{
   "type":"git",
   "gitRepositoryUri":"<path to git repository>",
   "branch":"<branch>"
}
```
to the `repositories.json` file, where `<path to git repository>` needs to be substituted by an URL leading to the wanted git repository and `<branch>` by the name of the branch containing the repository.

An example for a Git repository object would be:
```json
{
   "type":"git",
   "gitRepositoryUri":"https://github.com/PlayOnLinux/Scripts",
   "branch":"master"
}
```

The git repository is cloned to a folder in `application.user.cache` on first access. Later on, this folder is updated via `git pull` (every time you start Phoenicis).

### Local directory
Another type of Phoenicis repository is a local repository.
A local repository is located in a directory on your computer.

To add a local repository to Phoenicis, please add the object
```json
{
   "repositoryLocation":"<path to local directory>",
   "type":"local"
}
```
to the `repositories.json` file, where `<path to local directory>` describes the path to the local directory of the user inside which the repository is located.

An example for a local repository object would be:
```json
{
   "repositoryLocation":"/home/marc/POL-5-repository",
   "type":"local"
}
```
