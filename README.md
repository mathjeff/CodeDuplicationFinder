#########################
# CodeDuplicationFinder #
#########################

Searches for duplications in source code, and outputs matches sorted by total duplication size
Currently uses ANTLR in interpreted mode to parse files using a grammar provided at runtime

Prerequisites:
	javasdk (1.8 is good)
	gradle (2.9 is good)

run
 gradle tasks           // to see what can be done
 gradle build           // to make it
 gradle demo            // to run it with a sample
 gradle idea            // to generate project files for Intellij
 gradle eclipse         // to generate project files for Eclipse