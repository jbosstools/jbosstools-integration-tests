# The JBoss Tools Integration Tests project

## Summary

JBoss Tools Integration Test contains SWT Bot test plugins for overall integration testing of JBoss Tools. 

## Install

_JBoss Tools Integration Tests_ is part of [JBoss Tools](http://jboss.org/tools) from
which it can be [downloaded and installed](http://jboss.org/tools/download)
on its own or together with the full JBoss Tools distribution.

## Get the code

The easiest way to get started with the code is to [create your own fork](http://help.github.com/forking/), 
and then clone your fork:

    $ git clone git@github.com:<you>/jbosstools-integration-tests.git
    $ cd jbosstools-integration-tests
    $ git remote add upstream git://github.com/jbosstools/jbosstools-integration-tests.git
	
At any time, you can pull changes from the upstream and merge them onto your master:

    $ git checkout master               # switches to the 'master' branch
    $ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
    $ git push origin                   # pushes all the updates to your fork, which should be in-sync with 'upstream'

The general idea is to keep your 'master' branch in-sync with the
'upstream/master'.

## Building JBoss Tools Integration Tests

To build _JBoss Tools Integration Tests_ requires specific versions of Java and
Maven. Also, there is some Maven setup. The [How to Build JBoss Tools with Maven 3](https://community.jboss.org/wiki/HowToBuildJBossToolsWithMaven3)
document will guide you through that setup.

This command will run the build:

    $ mvn clean verify

If you just want to check if things compiles/builds you can run:

    $ mvn clean verify -DskipTest=true

But *do not* push changes without having the new and existing unit tests pass!
 
## Contribute fixes and features

_JBoss Tools Integration Tests_ is open source, and we welcome anybody that wants to
participate and contribute!

If you want to fix a bug or make any changes, please log an issue in
the [JBoss Tools JIRA](https://issues.jboss.org/browse/JBIDE)
describing the bug or new feature and give it a component type of
`QA`. Then we highly recommend making the changes on a
topic branch named with the JIRA issue number. For example, this
command creates a branch for the JBIDE-1234 issue:

	$ git checkout -b jbide-1234

After you're happy with your changes and a full build (with unit
tests) runs successfully, commit your changes on your topic branch
(with good comments). Then it's time to check for any recent changes
that were made in the official repository:

	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
	$ git checkout jbide-1234           # switches to your topic branch
	$ git rebase master                 # reapplies your changes on top of the latest in master
	                                      (i.e., the latest from master will be the new base for your changes)

If the pull grabbed a lot of changes, you should rerun your build with
tests enabled to make sure your changes are still good.

You can then push your topic branch and its changes into your public fork repository:

	$ git push origin jbide-1234         # pushes your topic branch into your public fork of JBoss Tools Integration Tests

And then [generate a pull-request](http://help.github.com/pull-requests/) where we can
review the proposed changes, comment on them, discuss them with you,
and if everything is good merge the changes right into the official
repository.

## Branching process check-list

Integration Tests have different life cycle than other repos in JBT - we typically 
don’t need to develop tests for next versions of JBT until the current version under 
development is released. So while we’re testing the current version, we’re using master.

Once a major version of JBT is released (e.g. JBT 4.1.1.Final), create a branch, 
e.g. jbosstools-4.1.x

In the newly created maintenance branch, these steps are needed:

*   Fix RedDeer repo url in root pom.xml to point to something stable rather then the ever-moving RedDeer master repo

*   Update integration tests repo url in root pom.xml to point to a maintenance repo, e.g.:
    
         http://download.jboss.org/jbosstools/updates/nightly/integrationtests/4.1.kepler/

In the master branch, these steps are needed:

*   Update parent pom dependency to new version, e.g. 4.2.0.Alpha1-SNAPSHOT

*   Update integration tests repo url in root pom.xml 

*   Update all plugins/tests/poms version to the new version:

        $ mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=<version> 

    where version is e.g. 4.2.0-SNAPSHOT

*   Update range for bot.ext in all manifests to the new version, e.g.:

        $ find . -iname manifest.mf|xargs perl -pi -e 's/\[4.1.0,4.2.0\)/[4.2.0,4.3.0)/' 

## Known Problems 

*   Ubuntu vncviewer skips 's' during input (see https://bugs.launchpad.net/ubuntu/+source/vnc4/+bug/658723 for workaround).
