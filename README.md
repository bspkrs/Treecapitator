Treecapitator
=================
DaftPVF's Treecapitator mod for Minecraft.  Takes trees down in one blow.  Most popular mods supported by default.
This repo contains source files for Forge (dependant on bspkrsCore).

## Links of Interest
 - [Official Minecraft Forum Thread](http://www.minecraftforum.net/topic/1009577-)
 - [Old ModLoader Downloads](http://bspk.rs/MC/TreeCapitator_ML/index.html)
 - [Forge Player Downloads](http://bspk.rs/MC/Treecapitator/index.html) - for Minecraft players to use
 - [Developer Downloads](http://bspk.rs/MC/Treecapitator/deobf/index.html) - for developers to use in an IDE such as Eclipse
 - [Treecapitator Wiki](https://github.com/bspkrs/Treecapitator/wiki)
 - [Issue Tracking System](https://github.com/bspkrs/Treecapitator/issues)
 - [Contributing](#contributing) - for those that want to help out  
 - [License](https://raw.github.com/bspkrs/Treecapitator/master/src/main/resources/LICENSE)
  
### Technical Info
 - [Setup Java](#setup-java)
 - [Setup Gradle](#setup-gradle)
 - [Setup Git](#setup-git)
 - [Setup Treecapitator](#setup-treecapitator)
 - [Compile Treecapitator](#compile-treecapitator)
 - [Updating Your Repository](#updating-your-repository)

#### Setup Java
The Java JDK is used to compile Treecapitator.

1. Download and install the Java JDK.
    * [Windows/Mac download link](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html). Scroll down, accept the `Oracle Binary Code License Agreement for Java SE`, and download it (if you have a 64-bit OS, please download the 64-bit version).
    * Linux: Installation methods for certain popular flavors of Linux are listed below. If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html).
        * Gentoo: `emerge dev-java/oracle-jdk-bin`
        * Archlinux: `pacman -S jdk7-openjdk`
        * Ubuntu/Debian: `apt-get install openjdk-7-jdk`
        * Fedora: `yum install java-1.7.0-openjdk`
2. Set up the environment.
    * Windows: Set environment variables for the JDK.
        1. Go to `Control Panel\System and Security\System`, and click on `Advanced System Settings` on the left-hand side.
        2. Click on `Environment Variables`.
        3. Under `System Variables`, click `New`.
        4. For `Variable Name`, input `JAVA_HOME`.
        5. For `Variable Value`, input something similar to `C:\Program Files\Java\jdk1.7.0_45` exactly as shown (or wherever your Java JDK installation is), and click `Ok`.
        6. Scroll down to a variable named `Path`, and double-click on it.
        7. Append `;%JAVA_HOME%\bin` EXACTLY AS SHOWN and click `Ok`. Make sure the location is correct; double-check just to make sure.
3. Open up your command line and run `javac`. If it spews out a bunch of possible options and the usage, then you're good to go. If not try the steps again.

#### Setup Gradle
Gradle is used to execute the various build tasks when compiling Treecapitator.

1. Download and install Gradle.
    * [Windows/Mac download link](http://www.gradle.org/downloads). You only need the binaries, but choose whatever flavor you want.
        * Unzip the package and put it wherever you want, eg `C:\Gradle`.
    * Linux: Installation methods for certain popular flavors of Linux are listed below. If your distribution is not listed, follow the instructions specific to your package manager or install it manually [here](http://www.gradle.org/downloads).
        * Gentoo: `emerge dev-java/gradle-bin`
        * Archlinux: You'll have to install it from the [AUR](https://aur.archlinux.org/packages/gradle).
        * Ubuntu/Debian: `apt-get install gradle`
        * Fedora: Install Gradle manually from its website (see above), as Fedora ships a "broken" version of Gradle. Use `yum install gradle` only if you know what you're doing.
2. Set up the environment.
    * Windows: Set environment variables for Gradle.
        1. Go back to `Environment Variables` and then create a new system variable.
        2. For `Variable Name`, input `GRADLE_HOME`.
        3. For `Variable Value`, input something similar to `C:\Gradle-1.10` exactly as shown (or wherever your Gradle installation is), and click `Ok`.
        4. Scroll down to `Path` again, and append `;%GRADLE_HOME%\bin` EXACTLY AS SHOWN and click `Ok`. Once again, double-check the location.
3. Open up your command line and run `gradle`. If it says "Welcome to Gradle [version].", then you're good to go. If not try the steps again.

#### Setup Git
Git is used to clone Treecapitator and update your local copy.

1. Download and install Git [here](http://git-scm.com/download/).
2. *Optional* Download and install a Git GUI client, such as Github for Windows/Mac, SmartGitHg, TortoiseGit, etc. A nice list is available [here](http://git-scm.com/downloads/guis).

#### Setup Treecapitator
This section assumes that you're using the command-line version of Git.

1. Open up your command line.
2. Navigate to a place where you want to download Treecapitator's source (eg `C:\Development\Github\Minecraft\`) by executing `cd [folder location]`. This location is known as `mcdev` from now on.
3. Execute `git clone git@github.com:bspkrs/Treecapitator.git`. This will download Treecapitator's source into `mcdev`.
4. Right now, you should have a directory that looks something like:

***
    mcdev
    \-Treecapitator
        \-Treecapitator's files (should have build.gradle)
***

#### Compile Treecapitator
1. Execute `gradle setupDecompWorkspace`. This sets up Forge and downloads the necessary libraries to build Treecapitator. This might take some time, be patient.
    * You will generally only have to do this once until the Forge version in `build.properties` changes.
2. Execute `gradle build`. If you did everything right, `BUILD SUCCESSFUL` will be displayed after it finishes. This should be relatively quick.
    * If you see `BUILD FAILED`, check the error output (it should be right around `BUILD FAILED`), fix everything (if possible), and try again.
3. Go to `mcdev\Treecapitator\build\libs`.
    * You should see a `.jar` file named `[#.#.#]Treecapitator-<version>.jar`.
4. Copy the jar into your Minecraft mods folder, and you are done!

#### Updating Your Repository
In order to get the most up-to-date builds, you'll have to periodically update your local repository.

1. Open up your command line.
2. Navigate to `mcdev` in the console.
3. Make sure you have not made any changes to the local repository, or else there might be issues with Git.
    * If you have, try reverting them to the status that they were when you last updated your repository.
4. Execute `git pull master`. This pulls all commits from the official repository that do not yet exist on your local repository and updates it.

### Contributing
#### Submitting a Pull Request (PR)
So you found a bug in the code? Think you can make it more efficient? Want to help in general? Great!

1. If you haven't already, create a [GitHub account](https://github.com/signup/free).
2. Click the `Fork` icon located at the top-right of this page (below your username).
3. Make the changes that you want to and commit them.
    * If you're making changes locally, you'll have to do `git add -A`, `git commit` and `git push` in your command line.
4. Click `Pull Request` at the right-hand side of the gray bar directly below your fork's name.
5. Click `Click to create a pull request for this comparison`, enter your pull request title, and create a detailed description explaining what you changed.
6. Click `Send pull request`, and wait for feedback!

#### Creating an Issue
Crashing? Have a suggestion? Found a bug? Create an issue now!

1. Make sure your issue hasn't already been answered or fixed. Also think about whether your issue is a valid one before submitting it.
2. Go to the issues page.
3. Click `New Issue` right below `Star` and `Fork`.
4. Enter your issue title (something that summarizes your issue), and then add a detailed description ("Hey, could you add/change xxx?" or "Hey, found an exploit: stuff").
    * If you are reporting a bug, make sure you include the following:
        * Version (can be found in the mcmod.info file or in the mod list)
        * ForgeModLoader log (please use [gists](https://gist.github.com/) for large amounts of text!)
        * Detailed description of the bug
5. Click `Submit new issue`, and wait for feedback!

This README is shamelessly based off of [pahimar's version](https://github.com/pahimar/Equivalent-Exchange-3).

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License</a>.
