#########################
# CodeDuplicationFinder #
#########################

Searches for duplications in source code, and outputs matches sorted by total duplication size  
Uses ANTLR in interpreted mode (at the time of writing) to parse files using a grammar provided at runtime 

Prerequisites:
	javasdk (1.8 is good)
	gradle (2.9 is good)

run
 gradle tasks 			// to see what can be done
 gradle build   		// to make it
 gradle demo			// to run it with a sample
