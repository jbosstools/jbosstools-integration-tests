#!/bin/sh
DISPLAY=:2 mvn clean verify -B -U -fae -Punified.target -Dswtbot.test.skip=false -Dtest.class=AllTests
