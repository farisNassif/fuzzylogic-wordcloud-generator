<h3 align="center">A multithreaded Tomcat web-app with a heuristic search that generates a word cloud from the top (n) words associated with a chosen search term and categorizes the search term using a Neural Network</h3>

## Assignment Details

|Details  |    |
| --- | --- |
| **Assignment**  | [Assignment Spec](https://learnonline.gmit.ie/pluginfile.php/176531/mod_resource/content/2/aiAssignment2020.pdf) 
| **GIF** | [TODO](https://github.com/farisNassif/FourthYear_Artificial-Intelligence)
| **Course** | BSc (Hons) in Software Development
| **Module** |  Artificial Intelligence |
| **Author** | [Faris Nassif](https://github.com/farisNassif) |
| **Lecturer** | Dr John Healy |

## Contents
* [Running the Program](#running-the-program)
* [Assignment Overview](#assignment-overview)

## Running the Program

### Requirements for Running Locally
* [Java 8](https://java.com/en/download/faq/java8.xml) (<i>Untested on alternative versions of Java</i>)

### Libraries and Development Tools
* [JFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html)
* [Jsoup 1.12.1](https://jsoup.org/)
* [Encog Core 3.4](https://github.com/jeffheaton/encog-java-core)

### How to Run
* Clone the repository
  * `git clone https://github.com/farisNassif/FourthYear_Artificial-Intelligence`
* Navigate to the [Executeables](https://github.com/farisNassif) folder
  * `cd FourthYear_Artificial-Intelligence/Executeables`
* Execute the program via the .jar
  * `TODO -> Jar thing`
  
### Alternatively
* The Program may be accessed via [Heroku](https://faris-gmit-ai-2020.herokuapp.com/)

## Assignment Overview
The goal of the assignment is to develop a multithreaded AI search application that can generate a word cloud from the top (n) words associated with an internet search term. 

The program takes a <b>Query Word</b> from the user and using [Jsoup](https://jsoup.org/), returns the top resulting web-pages associated with that word. Jsoup then scrapes the content of those pages and heuristically scores the page content using multiple scoring functions and [JFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html) allowing for smart scoring and word-frequency matching. The content contained within the top heuristically scoring pages is then processed and mapped to it's frequency of occurance.

The result of the frequency matching consists of the top (n) words associated with the user's query. The resulting top words then get returned to the user in image form, with the most frequent being the largest and the lesser words scaled representing their frequency in a spiral formation.

### Assignment Implementation
Writeup later
