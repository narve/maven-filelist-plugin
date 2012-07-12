package no.machina.filelistplugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which creates a class list.
 *
 * @goal create-list
 *
 *
 */
public class FileListMojo extends AbstractMojo {

    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}/classes"
     * @required
     */
    private File outputDirectory;
    /**
     * Name of the file.
     *
     * @parameter expression="persistent-classes.txt"
     * @required
     */
    private String outputFileName;
    /**
     * inputDirectory.
     *
     * @parameter
     * @required
     */
    private File inputDirectory;
    /**
     * includeRegExp.
     *
     * @parameter
     * @required expression=".*\\.java"
     */
    private String includeRegExp;
    /**
     * excludeRegExp.
     *
     * @parameter
     * @required expression=".*~"
     */
    private String excludeRegExp;
    /**
     * outputTemplate - example com.whatever.{2} args are fullPath, fileName, fileNameBeforeDot, extension
     *
     * @parameter
     * @required
     */
    private String outputTemplate;

    @Override
    public void execute() throws MojoExecutionException {
//        File f = outputDirectory;

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        final Pattern includePattern = Pattern.compile(includeRegExp);
        final Pattern excludePattern = Pattern.compile(excludeRegExp);
        getLog().info("Reading classes from " + inputDirectory + " using includeRegExp " + includeRegExp + " and excludeRegExp " + excludeRegExp);

        FilenameFilter fnf = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                boolean inc = includePattern.matcher(name).matches(); 
                boolean ex =  excludePattern.matcher(name).matches();
                getLog().debug( "Analyzing " + name + ": inc=" + inc + ", ex=" + ex ); 
                return inc && !ex; 
            }
        };

        File[] list = inputDirectory.listFiles(fnf);
        int unfilteredSize = inputDirectory.list().length;


        File f = new File(outputDirectory, outputFileName);
        getLog().info("Creating list of " + list.length + " files (out of total " + unfilteredSize + ") in " + outputDirectory);

        try {
            try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
                for (File s : list) {
                    String cname = cname( s ); 
                    w.write(cname);
                    w.write( "\r\n" ); 
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + f, e);
        }
    }

    private String cname(File f) {
        String[] sa = f.getName().split( "\\." ); 
        return String.format( outputTemplate, f.getAbsoluteFile().getPath(), f.getName(), sa[0], sa[1] ); 
    }
}
