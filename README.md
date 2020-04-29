<h3 align="center">A multithreaded Tomcat web-app multiple heuristically informed searches that generates a word cloud from the top (n) words associated with a chosen search term and categorizes the search term using a Neural Network</h3>

## Assignment Details

|Details  |    |
| --- | --- |
| **Assignment**  | [Assignment Spec](https://learnonline.gmit.ie/pluginfile.php/176531/mod_resource/content/2/aiAssignment2020.pdf) 
| **Hosted Webapp** | [Heroku Deployment](https://faris-gmit-ai-2020.herokuapp.com/)
| **Course** | BSc (Hons) in Software Development
| **Module** |  Artificial Intelligence |
| **Author** | [Faris Nassif](https://github.com/farisNassif) |
| **Lecturer** | Dr John Healy |

## Contents
* [Running the Program](#running-the-program)
* [Assignment Overview](#assignment-overview)
* [Assignment Implementation](#assignment-implementation)

## Running the Program

### Requirements for Running Locally
* [Java 8](https://java.com/en/download/faq/java8.xml) (<i>Untested on alternative versions of Java</i>)

### Libraries and Development Tools
* [JFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html)
* [Jsoup 1.12.1](https://jsoup.org/)
* [Encog Core 3.4](https://github.com/jeffheaton/encog-java-core)

### How to Run
* Project .jar files were not generated considering the project .war file is compiled and built via [Tomcat](http://tomcat.apache.org/). For information on how to compile and deploy the .war archive see [How to Deploy a WAR file to Tomcat](https://www.baeldung.com/tomcat-deploy-war)

### Alternatively
* The Program may be accessed via [Heroku](https://faris-gmit-ai-2020.herokuapp.com/). (<i>May take a couple of seconds to load initially, application is set to sleep</i>)

## Assignment Overview
The goal of the assignment is to develop a multithreaded AI search application that can generate a word cloud from the top (n) words associated with an internet search term. 

The program takes a <b>Query Word</b> from the user and using [Jsoup](https://jsoup.org/), returns the top resulting web-pages associated with that word. Jsoup then scrapes the content of those pages and heuristically scores the page content using multiple scoring functions and [JFuzzyLogic](http://jfuzzylogic.sourceforge.net/html/index.html) allowing for smart scoring and word-frequency matching. The content contained within the top heuristically scoring pages is then processed and mapped to it's frequency of occurrence.

The result of the frequency matching consists of the top (n) words associated with the user's query. The resulting top words then get returned to the user in image form, with the most frequent being the largest and the lesser words scaled representing their frequency in a spiral formation.

## Assignment Implementation
The project is driven using both a Recursive Beam Search & Recursive Best First Search, both searches being implemented as greedy searches meaning they are driven solely on nodal heuristic value.

* <b>Best First Search</b>
  * Generates and scores children nodes on the fly and places those nodes into a priority queue ordered by the score of the child nodes, meaning the best child will always be polled first. The process of scoring a node involves checking the URL, title, heading and paragraph tags for occurrences of the query word and administering appropriate scores respective of the location of the query word location within that webpage. Once the score is tallied, the logarithmic value is obtained and that gets passed into relevant [Fuzzy Inference System](https://github.com/farisNassif/FourthYear_Artificial-Intelligence/blob/master/res/BFS_Fuzzy.fcl) along with the depth level of the child where the relevance of the child is evaluated by the ruleset and defuzzifier. <br> <br>Once all children of the initial node have been scored and placed into the queue, the queue is polled and the highest scoring child removed, but not before the contents of the page are mapped and more children are generated from this node. This process repeats and the node generation function is called recursively on the new best scoring node in the queue until a stop condition is reached. A closed list is used to make sure pages aren't revisited. <br><br> Best First Search turned out to be an ideal algorithm considering the given search space, especially since more nodes than the Beam Search will be polled and mapped, allowing for a wider range of results rather than a small sample size.
<br><br>Executed in linear space with complexity of <i>O(bd)</i> and time complexity of <i>O(b<sup>d</sup>)</i>.

* <b>Beam Search </b>
  * This Beam Search implementation works by generating and scoring <i>(n)</i> nodes initially and keeping the best two nodes in a reversed priority queue, meaning unlike R-BFS, the lowest node will always be polled first. If a queue with two nodes with the values [95, 50] receives a new node with the value of [100], the smallest scoring node which will always be the last node placed in the queue is removed and discarded, the new queue is now [100, 95] giving it a LIFO structure. <br><br> Only the nodes on the queue used to generate children, if one of these nodes has a child with a score greater than that of the last node into the queue, offer it to the
queue otherwise and check the new node for any children better. Like R-BFS, this search has its own unique [Fuzzy Inference System](https://github.com/farisNassif/FourthYear_Artificial-Intelligence/blob/master/res/Beam_Fuzzy.fcl). The recursive process is repeated until either a stop condition is encountered or both nodes in the queue have been polled and neither of their children have scored high enough to be placed into the queue. Like the Best First Search, a closed list is used to keep track of visited pages. <br><br> Beam search is a very strong search, but while it can be good it does have its drawbacks. Beam can potentially chuck away a very relevant node and return sub-optimal results, but could also be fortunate enough to identify and traverse a very promising path faster than Recursive Best First Search, even if it's not the absolute optimal path. When testing Beam and Best First Search with a timer the bigger the search space the bigger the time gap between the two searches. Beam works best with an unambiguous space. <br><br> If no stop conditions were implemented, the time for Beam to complete would completely depend on the accuracy of the heuristic, giving it a worst case time complexity of going straight to the bottom of the tree <i>O(bd)</i>. Since beam only stores <i>n</i> nodes at each level, the worst case space complexity is <i>O(bd)</i>. The linear memory consumption means beam can probe fairly deep into larger spaces and find solutions other searches may not reach. <br>

* <b>Fuzzy Implementation </b>
  
<p align="center">
 <b>Recursive Best First Search</b>
  <img src = "https://i.imgur.com/OzO2HL9.png">
   <b><i>For a detailed description of the Fuzzy Implementation please see the <a href="https://github.com/farisNassif/FourthYear_Artificial-Intelligence/blob/master/res/BFS_Fuzzy.fcl">fuzzy file</a></i></b>
</p>

<p align="center">
 <b>Beam Search</b>
  <img src = "https://i.imgur.com/ECP8b6p.png">
   <b><i>For a detailed description of the Fuzzy Implementation please see the <a href="https://github.com/farisNassif/FourthYear_Artificial-Intelligence/blob/master/res/Beam_Fuzzy.fcl">fuzzy file</a></i></b>
</p>

* <b>Word Classification using Encog </b><br>
  * Using [Encog Core 3.4](https://github.com/jeffheaton/encog-java-core), a Neural Network was created and saved locally. The Neural Network was trained and tested on locally fed data that was written to ensure it yielded reasonable classification output. The Neural Network has one hidden layer and uses the resilient propagation optimization algorithm, which uses a specific update value for every neuron connection, the values are automatically determined unlike the learning rate of back-propagation algorithms. <br><br>The Neural Network allows for the categorization of the query word, the 5 most associated words with each of the 8 possible categories of classification are compared against the final word frequency map and scored depending on the occurrence of that word anywhere in the node and amplified depending on the frequency. A normalized result is generated, scaled between a value of 0 and 3, and then fed into the neural network via an 8 indexed array. Based on the data it was trained with, the Neural Network looks at the input and attempts to provide a result based on the input data and the data it was previously trained on. 

* <b>Executing the Program</b>
  * The application allows for the user to select the Branching Factor, Max Depth, Search Algorithm and the amount of words to return via the word cloud. Once a search is executed, the search for the most relevant nodes begin, once the conditions have been met and the search runs it course, the nodal content is scored, mapped and returned to the Service Handler where, using Base64 and a Logarithmic Spiral Placer, the words are placed on a canvas with sizes representing their frequencies. The image is then displayed with details about the specific search presented to the user.
