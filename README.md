# SmartCanteen
## How to use git


Install git at [https://git-scm.com/](https://git-scm.com/)

Open up git bash and type the following.
```
$ git config --global user.name "John Doe"
$ git config --global user.email johndoe@example.com
```

Clone the repository to your desired folder.(open git bash on your desired folder)

```
$ git clone https://github.com/garyongguanjie/SmartCanteen.git
```

## Making Changes for the first time
Open up the project on Android Studio and make the changes.

Provide sufficient comments if code is complex.

### Commit

Android Studio -> vcs -> commit 

Note commit is **local**.

commit to master branch unless you have something crazy that might break the whole thing

Enter a commit message explaining what you have done.


**Important:** ensure your code works by running.

### Push

Push pushes the code onto gihub

**DO NOT PUSH** if it fails to run.

vcs->git->push

## Making changes from second time onwards

### Pull

**ALWAYS PULL** before doing anything. vcs->git->pull 

This is done to get the latest copy from github.

Commit and push after changes

## Handling conflicts

Conflicts occur when 2 or more user make changes that affect each other.

Eg. deleting code from another person

Android Studio will give you a warning. And a popup will ask you to pick and choose which code to keep.

## Guidelines for editing code

1.*Try* not to hardcode the following
  
  * Strings. Use Strings Resource for strings
  
  * Colors. Use Colors resource for colors
  
  * Drawables
  
2.Try to create helper classes for complex code separate from the activity
  * Eg. Firebase

3.**DO NOT** put continuous tasks in the UI other than asynctasks and firebase
  * Eg. Thread.sleep(1)
  
  
  HAVE FUN







