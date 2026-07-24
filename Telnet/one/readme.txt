This is the root directory of the binary release of one.world. It is
self-contained and includes all binaries necessary to run one.world on
Windows and Linux systems.

			       Contents
			      ----------

The binary release of one.world contains all binaries to run one.world
and to develop applications for one.world. It uses the following
directory structure:

  .                The directory containing this readme file and
                   the shell scripts to start one.world
      bin          Binaries
      config       Configuration files
      data         Data to be imported into one.world
      scripts      one.world shell scripts
      tuplestore   Tuple storage

The bin directory contains the three main JAR files for one.world:
one.world.jar, one.tools.jar, and one.apps.jar. The one.world.jar file
contains the core architecture, that is, all packages starting with
one.world and the one.net and one.util packages. Regression tests and
benchmarks (classes starting with "Test" or "Benchmark") are
omitted. The one.tools.jar file contains the one.tools package. The
one.apps.jar file contains the one.radio and one.toys packages.


				Setup
			       -------

The binary release of one.world requires that a Java virtual machine
compatible with the Jave 2 standard edition version 1.3 or later is
installed on your machine. It must be possible to start Java by typing
"java" from the command shell (i.e., the java command must be in your
command path).

Otherwise, once unzipped, the binary release of one.world is ready to
go without any additional setup.

Optionally, you can perform the following. In the same directory as
this readme file, execute:

       java -jar bin/bench.jar

This performs Pendragon Software Corporation's CaffeineMark 3.0 Java
benchmarks and, after some time, will print several scores. Record the
"Overall score". Edit the text file "one.world.config" in the "config"
directory, replacing the "-1" in the line starting with
"caffeine.mark.rating" with the overall score you recorded earlier. Do
not change anything else and save the edited version.


			  Running one.world
			 -------------------

On Windows, execute the following command in the same directory as
this readme file:

       one.world

You can also double-click the "one.world.bat" or "one.world" icon in
Windows Explorer. Its name depends on whether Windows Explorer is set
up to show file name extensions or not.

On Linux, execute the following command in the same directory as this
readme file:

       source one.world.sh

After startup, one.world automatically starts the one.radio Emcee
application, which provides a graphical user interface for managing
users and their applications.


		    Running Your Own Applications
		   -------------------------------

To run your own applications in one.world, you need to make the Java
class files or JAR files accessible within one.world. This entails (a)
adding them to the classpath and (b) changing the security policy.

To add a directory with class files or a JAR file to the classpath,
you need to edit the one.world.bat (for Windows) and one.world.sh (for
Linux) files in this directory. Both files contain a line that starts
with "java -cp". You need to add the directory or JAR file to the list
of paths following the "-cp" flag.

To change the security policy, you need to edit the one.world.policy
file in the config directory. The policy contains a block of
permission statements starting with

  permission java.io.FilePermission "bin${/}one.world.jar", "read";

This is where you need to add corresponding permission statements for
the directory or JAR file. For example, to make the "bin\my-classes"
(on Windows) or "bin/my-classes" (on Linux) directory accessible, add
the following:

  permission java.io.FilePermission "bin${/}my-classes", "read";
  permission java.io.FilePermission "bin${/}my-classes${/}-", "read";

To make the "bin\my-classes.jar" (on Windows) or "bin/my-classes.jar"
(on Linux) JAR file accessible, add the following:

  permission java.io.FilePermission "bin${/}my-classes.jar", "read";

Note that the separator character, '\' on Windows and '/' on Linux, is
always replaced with "${/}".

After making the class or JAR files accessible within one.world, you
can start your application by right clicking a user's name in the
Emcee application and selecting "Run..." in the user's
popup-menu. Type the fully qualified class name in the application
combobox, followed optionally by any command line arguments. Select
an environment name (hitting the tab key automatically suggests an
appropriate environment name) and click OK.


			 Further Information
			---------------------

More information on one.world, including a source distribution, is
available at:

       http://one.cs.washington.edu

This release includes binaries for the Berkeley DB. The source
distribution for the Berkeley DB is available at:

       http://www.sleepycat.com

This release includes binaries for Markus Dahm's Byte Code Engineering
Library (BCEL). The source distribution for the BCEL is available at:

       http://bcel.sourceforge.net

This release includes a repackaged version of Pendragon Software
Corporation's CaffeineMark 3.0 Java benchmarks. More information on
CaffeineMark is available at:

       http://www.pendragon-software.com/pendragon/cm3/index.html
