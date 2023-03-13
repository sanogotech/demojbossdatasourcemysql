
## Sample code
- https://github.com/sanogotech/demojbossdatasourcemysql


## Docs

- [View the guide](https://www.squins.com/knowledge/jboss-cli-mysql-datasource-howto/)
- https://www.techgalery.com/2019/09/add-mysql-driver-to-wildfly.html

## Sample MySQL database schema
This example uses the following mysql schema:

```
    CREATE TABLE IF NOT EXISTS `books` (
      `id` int(11) NOT NULL,
      `title` varchar(255) NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

    -- Some sample data.
    INSERT INTO `books` (`id`, `title`) VALUES
    (1, 'Java is cool.'),
    (2, 'Clean Code');
```
To run the example, create a mysql database schema with following configuration:

Username: root
Password: 
Schema/DB: books


## Manually Creating Data Source

1. Download MySQL connector from Maven central

You can download with this link :

Download : mysql-connector-java-8.0.17.jar

https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.17/mysql-connector-java-8.0.17.jar


And then put the file under WILDFLY_HOME\modules\system\layers\base\com\mysql\main

2. Create a file  module.xml

Using any kind of text editor, create file inside your Wildfly path, WILDFLY_HOME\modules\system\layers\base\com\mysql\main, and this is the XML file contents of it:

```
<module name="com.mysql" xmlns="urn:jboss:module:1.5">
    <resources>
        <resource-root path="mysql-connector-java-8.0.17.jar">
    </resource-root></resources>
    <dependencies>
        <module name="javax.api">
        <module name="javax.transaction.api">
    </module></module></dependencies>
</module>
```

If the folders didn't exist, create it by yourself.

3. Add MySQL connector to the driver list

Open WILDFLY_HOME\standalone\configuration\standalone.xml, and then find <drivers> tag, inside that tag, put these lines to add MySQL driver:

```
<driver name="mysql" module="com.mysql">
 <driver-class>com.mysql.cj.jdbc.Driver</driver-class>
 <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
```

Now you can restart Wildfly and expect that new driver will be inside the available list driver.

## Using CLI Terminal 

Try creating the Module itself using the   jboss-cli.sh command rather than manually writing the module.xml file. This is because when we use some text editors, they might append some hidden chars to our files. (Specially when we do a copy & paste in such editors)

[standalone@localhost:9990 /]  module add --name=com.mysql.driver  --dependencies=javax.api,javax.transaction.api --resources=/PATH/TO/mysql-connector-java-8.0.17.jar

[standalone@localhost:9990 /] :reload

{
    "outcome" => "success",
    "result" => undefined
}

After running above command you should see the module.xml generated in the following location:  "wildfly-8.2.1.Final/modules/com/mysql/driver/main/module.xml"

Now create DataSource:

[standalone@localhost:9990 /] /subsystem=datasources/jdbc-driver=mysql/:add(driver-module-name=com.mysql.driver,driver-name=mysql,jdbc-compliant=false,driver-class-name=com.mysql.jdbc.Driver)
{"outcome" => "success"} 

source: https://developer.jboss.org/thread/266472

## Alternative 2: CLI  JBOSS Installing the MySQL driver

To install the driver, run the command below:

deploy /path-to-driver/mysql-connector-java-8.0.17.jar

To check if the driver installation was successful, run the following command:

/subsystem=datasources:installed-drivers-list

**  CLI  Add Datasource to JBoss Wildfly using JBoss/ CLI

Sample application that uses a Datasouce in Wildfly.


data-source add --name=books-datasource --jndi-name=java:/jdbc/books-database --driver-name=mysql-connector-java-8.0.17.jar --connection-url=jdbc:mysql://127.0.0.1:3306/books --user-name=root --password=


To check whether the datasource is able to connect to the database, run the following command:

/subsystem=datasources/data-source=books-datasource:test-connection-in-pool
When all is OK, it should say:

{
    "outcome" => "success",
    "result" => [true]
}
If it says “Outcome failed”, check the connection details. To remove the faulty datasource, run:

data-source remove --name=books-datasource
Then try again.

##  To deploy the sample war, build the maven project and execute the following command in JBoss CLI:

deploy /path/to/project/target/demo.war
When deployed successfully, open a browser and go to http://localhost:8080/demo/test-datasource. it should output:

Hello world!

You have 2 record(s) in your table.
Congratulations, you have successfully configured your MySQL datasource in the JBoss CLI!

## Code Sample 
