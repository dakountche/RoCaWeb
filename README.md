# RoCaWeb (Reverse Proxy based on Contract for Web protection)

* [Introduction](#introduction)
* [Web Application Firewall](#waf)
* [Approach of the RoCaWeb Project](#approach)
  * [Learning](#learning)
  * [Datasets](#datasets)
  * [Algorithms](#algos)
* [Web UI](#webui)
* [Agent](#agent)
* [Installation](#installation)
* [Contribution](#contribution)
* [Documentation](#doc)


## <a name="introduction"></a> Introduction

RoCaWeb was a project between IMT-Atlantique (then Télécom-Bretagne) and [Keraval](http://kereval.com).
It was funded by a grant called RAPID (régime d’appui à l’innovation duale). The project leads to the development of the software named RoCaWeb.

RoCaWeb is the descendant of other projects launch to study the application of the notion of smart contract to Web application protection.
These projects are:
  - [DALI](http://dali.kereval.com/)
  - DIDON
 

## <a name="waf"></a> Web application Firewall

As defined by [OWASP](https://www.owasp.org/index.php/Main_Page): "A web application Firewall (WAF) is an application Firewall for HTTP applications. It applies a set of rules to an HTTP conversation. Generally, these rules cover common attacks such as cross-site scripting (XSS) and SQL injection."
One such application is ModSecurity. A WAF can be deploy as reserve proxy to filter all the traffic in both direction. It can also function as:

    - passive mode also known as IDS (Intrusion detection System)
    - active mode also known as IPS  (Intrusion Prevention System)
    - combination of the two modes as an IDPS



## <a name="approach"></a> The chosen approach in the RoCaWeb project

RoCaWeb is composed by three modules: 

    - Learning
    - Web User Interface
    - Agent
An illustration of RoCaWeb protecting Hackazon is given by the figure below. 

![RoCaWeb protecting Hackazon](./documentation/images/rocaweb-archi.png)

### <a name="learning"></a> Learning

The RoCaWeb Learning module is illustrated on the following figure:
![RoCaWeb Learning process](./documentation/images/learning.png)

### <a name="datasets"></a> Data sets used for the learning process
An important remark is that: We are assuming that the data sets used for the learning 
process describe the **normal behavior** of the application.  Also no learning is made when the  application is on 
*production mode*.
These data can be obtained using:

    - Wireshark, for example during functional tests, to capture a normal traffic and then export it as a PDML
    - the ELK stack (provided in a beta version). 
Other data sources can be also implemented. 

### <a name="algos"></a> Algorithms implemented
The learning module is composed by the following algorithms: 

- Bio-informatic algorithms:
    - AMAA (Another Multiple sequence Alignment Algorithm). This algorithm was developed during this project. 
    - CSR (Contiguous Substring Reward)
    - Needleman-Wunsch
    - Smith-Waterman

- Grammatical algorithms:
    - Trie
    - Dictionary methods
    - Probabilistic grammar inference


- Statistical algorithms
    - Chebychev test
    - Chi-squarre
    - etc.
  
- Machine learning
    - Naive Bayesian
    - Once class SVM
    - Decision trees
    - etc.


These algorithms are implemented  in Java when a suitable version from a library is not used.
Also, for each algorithm a validation version needs to be implemented in Lua for ModSecurity.
This allows to extend ModSecurity to support other types of rules than the regular expression. 


### <a name="webui"></a> Web User Interface
RoCaWeb offers a user interface developed using the [Play Framework](https://www.playframework.com/).
It allows the configuration of all the learning process and also the state of the rules.

#### Generating firewall rules with Webui
Now, here's how to import the PDML file using Webui and then generate the firewall rules. Webui
can be accessed by connecting to http://localhost:9001 in a browser.
The Data page should be displayed, with three main blocks: sources, preprocessed, visualization. In
the sources block, we are going to import the PDML file, by clicking on the import icon.

![Importing PDML](./documentation/images/import.png)

Once the file is uploaded, it should appear in the sources window. We then need to cluster the data:
to do so, we need to select the file in the sources window and click on the 'clusterize' button:
![Clusterize](./documentation/images/clusterize.png)

Once the data is ready, it should appear in the preprocessed block:

![Preprocessing](./documentation/images/preprocessed.png)

We can now move on to the Learning page. We should see localhost in the data block. To visualize it,
we are going to select localhost and click on the visualization button:

![Visualize](./documentation/images/visualize.png)

We should now be able to see the details of our data in the visualization block. 
To create a rule for a given node, we can select it (```/path/to/node```) and generate the rule by clicking on the Run button. <br/>
In the running block, we should see the information of what job is running. Once it is finished, the running block should look like this (green border):

![Learning](./documentation/images/learnwebui.png)


Once the job is finished, we can go to the Profiles page to download the rule. To do so, we just need
to select it and click on the import button. It is downloaded as a text file (.txt), so we will need to
change it to a .conf file by renaming it name-of-the-rule.conf.


### <a name="agent"></a> Agent

The agent is composed by ModSecurity, Torch7 and OSBF-Lua to provide the reverse-proxy.
The detection phase is illustrated on the figure below. 
![RoCaWeb detection phase](./documentation/images/detection.png)


## <a name="installation"></a> Installation 

### Using Docker

After downloading this repository, change to the docker directory and type

```bash
docker-compose up
```

This will launch the:

   - web user interface available at http://localhost:9001

   - hackazon vulnerable web site at http://localhost
   
   - Webgoat available at http://localhost:8080/WebGoat

   - two instances of the  agent (Apache/Modsecurity/Torch/OSBFLua) for Hackzon and Webgoat. Each functioning as a separate reverse proxy. 

   - ELK stack
   
      - Elasticsearch: http://localhost:9200/_cat/indices?v
      - Logstash
      - Kibana is accessible at http://localhost:5601
     
   - The status of the agent is given at:
   
       - http://localhost/server-status

### From this Git repository
After installing the dependencies, downloading this repository from Github by:

```bash
$ git clone https://github.com/dakountche/RoCaWeb.git rocaweb  
```

 Then change to the directory rocaweb and:

- Install the dependencies:
   - Java 8
   - Maven 3
   - Scala
   - SBT
   - Add the name of your machine to /etc/hosts
   - NodeJS and add the following to your .bashrc
   - Docker and docker-compose

```bash
$ export SBT_OPTS="${SBT_OPTS} -Dsbt.jse.engineType=Node -Dsbt.jse.command=$(which node)"
```
   
```bash
$ mvn clean install -DskipTests
```
We are skipping the tests from now. 

- Change to the webui folder and type:

```bash
$sbt run
```
This command will launch the web user interface. 


## <a name="contribution"></a> Contributing to this project
RoCaWeb is still under development. And your contribution is welcome. 


## <a name="doc"></a> RoCaWeb Documentation

This ```./documentation``` folder is divided into three subfolders: papers, presentations and reports.

### Papers

**Specification-based intrusion detection using Sequence alignment and Data clustering**
* Authors: Djibrilla Amadou Kountche, Sylvain Gombault
* Language: English
* Location: ```./documentation/papers/writen/fnss2015/```
* Abstract: In this paper, we present our work on specification-based intrusion detection. Our goal is to build a web application firewall which is able to learn the normal behavior of an application (and/or the user) from the traffic between a client and a server.
* Format: PDF, LaTeX

### Presentations

**Specification-based intrusion detection using Sequence alignment and Data clustering**
* Authors: Djibrilla Amadou Kountche, Sylvain Gombault
* Language: English
* Location: ```./documentation/presentations/fnss2015/```
* Abstract: 
* Format: PDF, LaTeX

**RoCaWeb: Choix algorithmiques et Questions d'implémentation**
* Author: Djibrilla Amadou Kountche
* Language: French
* Location: ```./documentation/presentations/jr2015/``` 
* Abstract: 
* Format: PDF, LaTeX

### Reports

**Manuel d'utilisation de RoCaWeb**
* Author: Team RoCaWeb
* Language: French
* Location: ```./documentation/reports/userGuide/```
* Abstract: Dans ce petit guide, nous allons expliquer le fonctionnement de la version courante de Rocaweb. Nous informons dès à présent le lecteur que RoCaWeb est toujours en développement. Il est à la version 3 sur quatre  prévues. 
* Format: LaTeX

