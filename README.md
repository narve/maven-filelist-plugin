maven-filelist-plugin
=====================

A simple maven plugin to generate a text-file containing a directory listing. 
I didn't find any plugin that did what I wanted, so I made my own. We use it in 
a project to generate a list of JPA-classes. 

If anybody actually uses this and have suggestions/improvements/bug reports, send me a 
pull request. I promise not to be picky :)

Sample usage (this will cause the plugin to generate the list before compilation/unit testing): 

            <plugin>
                <groupId>no.machina</groupId>
                <artifactId>filelist-plugin</artifactId>
                <version>1.2-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>create-list</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <inputDirectory>src/main/java/com/yourcompany/model</inputDirectory>
                    <includeRegExp>.*\.java</includeRegExp>                    
                    <excludeRegExp>Test</excludeRegExp>
                    <outputTemplate>%3$s</outputTemplate>
                </configuration>
            </plugin>
            
Configuration: 

 * inputDirectory: What it seems. 
 * includeRegExp:  What it seems. Follows Java regexp syntax (se example above)
 * excludeRegExp:  What it seems. Follows Java regexp syntax (se example above)
 * outputTemplate: Choose what to write for each file. Follows normal String.format syntax. Available vars are listed below. 
 * outputFileName: What it seems

Output template variables: 
 1. Absolute path of file
 2. Filename
 3. Filename without extension
 4. Extension