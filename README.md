My DSL for cron has to be as close to real meaning and natural language as possible. That is why I have taken examples of possible cron and meanings from official quartz site and try to create my DSL based on their meaning examples:


*Expression** 	**Meaning**

0 0 12 * * ? 	      Fire at 12pm (noon) every day

0 15 10 ? * * 	    Fire at 10:15am every day

0 15 10 * * ? 	    Fire at 10:15am every day

0 15 10 * * ? * 	  Fire at 10:15am every day

0 15 10 * * ? 2005 	Fire at 10:15am every day during the year 2005

0 * 14 * * ? 	      Fire every minute starting at 2pm and ending at 2:59pm, every day

0 0/5 14 * * ? 	    Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day

0 0/5 14,18 * * ? 	Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day

0 0-5 14 * * ? 	    Fire every minute starting at 2pm and ending at 2:05pm, every day

0 10,44 14 ? 3 WED 	Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.

0 15 10 ? * MON-FRI 	Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday

0 15 10 15 * ? 	    Fire at 10:15am on the 15th day of every month

0 15 10 L * ? 	    Fire at 10:15am on the last day of every month

0 15 10 L-2 * ?     Fire at 10:15am on the 2nd-to-last last day of every month

0 15 10 ? * 6L 	    Fire at 10:15am on the last Friday of every month

0 15 10 ? * 6L 	    Fire at 10:15am on the last Friday of every month

0 15 10 ? * 6L 2002-2005 	Fire at 10:15am on every last friday of every month during the years 2002, 2003, 2004 and 2005

0 15 10 ? * 6#3 	  Fire at 10:15am on the third Friday of every month

0 0 12 1/5 * ? 	    Fire at 12pm (noon) every 5 days every month, starting on the first day of the month.

0 11 11 11 11 ? 	  Fire every November 11th at 11:11am.


Implementation is based on couple of rules. 
First I have defined some word as key words that will be used to define meaning of particular part of sentence.
For example I have put some keywords in brackets:

Fire at 10:15(am) every (day) during the (year) 2005 

Then I scan for some additional numeric values or additional keywords like "every","last","from","and","on" or "," and translate them to their meaning  accordingly.

Rules of my DSL will be defined in tests in details, but I use above examples as my initial set of rules.


