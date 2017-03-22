# Java-Shell
Shell
It is a bash-like shell written in Java

BUILDING AND LAUNCHING 

Is done in IntelliJIdea. If you don't have it, please check the current version on the https://www.jetbrains.com/idea/. Then
download the project and press the green triangle in the top-left corner.

USAGE

The list of following commands is supported natively:


1) echo : 
allows to display the text in console.

Example usage : echo HelloJavashell!


2) cat : allows to view the content of the file.
    
Example usage: cat /Users/lara/Downloads/4.sh. 

Be careful when writing the path: the format should be correct. 

In case you don't have this file, you'll see "file not found" in console 


3) pwd :
shows where you are now. 

Example usage: pwd OUTPUT: /Users/lara/Desktop/Shell-2/Shell


4) exit : 
allows to shut down the shell


5) wc :
word counting

In case you want to run a command not presented in the list, it is recognised as exterior command in system shell environment

FEATURES

PIPE : you can provide the output of the previous command to the next one
EXAMPLE: cat /Users/lara/Downloads/4.sh| wc

WEAK and STRONG quoting

VARIABLES: you can store the names of the variables during the current Shell session and assign the values by doing:
a=7 (without whitespaces)

$ : you can view and use the value of the variable by performing $.
Example:
a=7
echo $a

IS IT BASH?
No. The functionality is to be enlarged(the release of the grep command is coming soon). Pipes are not lazy. 
