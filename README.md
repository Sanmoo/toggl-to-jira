# Introduction
Simple utility CLI app that can be used to automate the logging of working hours in JIRA from a Toggl Track account.

## Motivation
At the beginning of every week I have to read my weekly summary report in Toggl Track, review it, and then create
correspondent log registries in JIRA. This takes some time since I have to open all issues in JIRA and manually log 
my work.

This utility has two main features. The command `display`, which shows a report of logged hours for the week before the
current one (or, in fact, for the n-th week before the current one if you provide an integer after 'display' in the
command line).

The other one is the command `post`, which creates all time entries from Toggl as working logs in JIRA, _if_ that entry's
description follow a given standard in its description: "PROJECT-123: Do this and that" or simply "PROJECT-123". That is,
the description must start with a JIRA project key.

## Usage
1. Firs things first: compile the project using java 11 and gradle 7.4.1 or above.
2. A "fat jar" is going to be generated at app/build/libs/app-standalone.jar. Execute `java -jar <fat_jar_path>` to see
extremely simple usage instructions.
3. Also, copy the file config.properties.tmpl into a file in the folder from which you intend to call the cli app.
Rename it to config.properties and update the content with the information asked.
4. Now run `java -jar <fat_jar_path> display` in order to see your "report". You can also see the report for the current
week with the command `java -jar <fat_jar_path> display 0`
5. If you are ok with the report, post the entries to JIRA with `java -jar <fat_jar_path> post`.
