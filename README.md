# java-files-and-links-manager
Multipurpose files and links manager that is created by me using java swing package.

Watch this [**video**](https://youtu.be/SvH74gjn25g) to see this app in action.

This app can help you share multiple non-copyrighted books and tutorials to online forums. This app generates pre-formatted html that is ready to be posted in forums, insert links in that html, create dummy files to increase your zip file size and many more. The only thing you need to do is fill up the pre-formatted html with necessary information.

I may update this project in the feature. However, that may not happen soon because I'm focusing on web development right now. I created and halted this project somewhere in 2021. Note, this project is pretty much complete. However, there are few features in this project that are left unfinished.

## Java Version
If I remember correctly, I created this using java11(I'm not sure about this). However, I used this app in java17(OpenJDK)(Eclipse Temurin) environment for a long time. Thus, I recommend users to use java17 or higher version if you wanna run this app properly.

## Building the App
I didn't use IDE when I created this app. You can try importing it in your favorite IDE if you want. If you wanna compile this via terminal/command prompt, here's how I do it.

### Compiling  
create classes directory adjacent to 'packages' directory. Open terminal/cmd in the 'packages' directory and enter this command:  
**javac -d ../classes/ main/Fmanager.java -Xlint:unchecked**

### Running  
Once you compiled the project successfully, open terminal/cmd in the 'classes' directory and enter this command:  
**java main/Fmanager**

### Building
If you want to pack this project in .jar file, create build directory adjacent to 'classes' directory and open terminal/cmd in the 'classes' directory and enter this command:  
**jar cfe ../build/Fmanager.jar main.Fmanager main/\*.class gui/\*.class gui/events/\*.class gui/gui_related_events/\*.class gui/subpanels/\*.class gui/subpanels/filetree/\*.class gui/dialogs/\*.class gui/dialogs/subpanels/prefs_subpanel/\*.class fileoperations/\*.class fileoperations/approvedopfunctions/\*.class**

After finishing this project, I realized that IDEs are precious tools :satisfied:

## SerialVersionUID and raw types warnings
When I compiled and run this in terminal/cmd using java17(Eclipse Temurin), I didn't see these warnings because I think I already suppressed them. However, some IDEs may still complain about these stuff. 

IDEs may complain that 'SerialVersionUID' is unused. That's alright because the purpose of that ID is to give a hint to JVM that this class will be serialized using the ID. Explicitly setting this ID improves the serialization/deserialization performance.

IDEs may complain about raw types in this codebase. It's alright because I intentionally made those objects raw types and I put some measures to make sure that using these raw types are safe. This raw types are a product of deserialization process.

If you know java generics. The generic type, the type in the diamond(<>) operator doesn't exist in runtime. Thus, if we serialize and object during runtime, the generic type of that object won't be included in the serialization process.

Now, once we deserialize that object, we can only convert it to its raw type. For example, we serialize ArrayList&lt;Integer&gt;. Once we deserialize it, we can only convert it to ArrayList because &lt;Integer&gt; doesn't exist in runtime.

## OS compatibility
This application is fully tested in windows 7. I had used this app for months in windows 7. Other OS can use this app. However, problems may arise. Make sure to test this app first in your system before using it as part of your work. I'm not responsile for any damage that this app produces.

## Problems with windows 7
There two 'unfixable' problems that I encountered during my use of this app in windows 7:

**File name limitation:** When you drag a file in file viewer and put that file in a directory, the file won't be moved if its file name longer than 200-250 characters. I think this is OS limitation, thus, I can't fix this.  
**Symlinks:** If you use 'File Randomizer' and want to create a symlink for each output, you need to be logged in as main administrator or the default admin in windows 7. User admin or admins that are created by users won't be allowed to create symlinks.

# Manual
This app has lots of functionalities. You can watch the video link above to give you an idea how this app works. In here, I'm going to list the functionalities of this app:

# Panels
These are the panels that you see right away once you open the app. There are six panels: **File Generation**, **Logs**, **Link Operations**, **Compress & Decompress**, **Stopwatch**, and **File Viewer**.

## File Generation
**Create post.txt** you can create a pre-formatted html in .txt file by clicking the 'create post.txt' button. You can change the format type by choosing one of the formats in 'post type' dropdown list. Also, you can modify the pre-formatted html to a 'collection' format by checking the 'use collection template' checkbox.

**Create/Delete Dummy Files** To create a dummy file, click the 'Create dummy_file' button. Click the 'Delete dummy_file' button to delete a dummy file. The 'threshold' textbox evaluates the total size of a directory where a dummy file will be placed. 

If the total file size of a directory is greater then the threshold, no dummy file will be created. Otherwise, a dummy file will be created. The 'include sub-directories' checkbox is an option for 'delete dummy_file' button. 

If checked, all dummy files in every directory in a top-level directory will be deleted. Otherwise, only the dummy files in the top-level directory will be deleted.

## Logs
This panel gives information about the operations that has happened in the app. Clear the 'logs' textbox by clicking the 'clear' button.

## Link Operations
This panel validates the links that is going to be put in post.txt is a valid link or not. There are two types of how this panel handles that distribution of links:

**Single:** All links in the 'links' textbox will be put to a single post.txt of a selected directory.  
**Multiple:** Links are distributed in each selected directories. If the number of directories are less than the number of links, the remaining links will be left out.

The 'include sub-directories' checkbox is an option available to 'multiple' link distribution type. If checked, links are distributed to sub-directories.

## Compress & Decompress
This panel handles compressing and decompressing of zip files. Note, this panel only supports single zip file. Zip with multiple parts are not supported. To compress a directory, click the 'compress file' button. To decompress zip, click the 'decompress file' button.

In compression operation, there are two types of packaging:

**Per Selected Directory:** This packaging type compress each directory in a zip file.  
**Single Package:** This packaging type compress all directories into one zip file.

In decompression operation, there are two types of selection:

**Directory:** This selection type selects all zip files in a directory and extracts them one by one. If 'include sub-directories' is checked, all zip files in sub-directories will also be selected.  
**Zip File:** This selection type extracts selected zip files.

**'Exclude Collection Directory' checkbox** exclude any 'collection' directory from compression. Go to File Generation section to know about 'collection' directory.    
**'Exclude post.txt' checkbox** exclude any 'post.txt' from compression. Go to File Generation section to know about post.txt

## Stopwatch
Sometimes, forums implement a delay in-between post to prevent spamming posts. This stopwatch can help us set timing to each of post we make in forums. Right now, the default is 3 mins. You can put your desired mins/seconds in the textboxes in this panel. Setting default time will be implemented in the future.

**'No Interruptions' checkbox** disables all the functionalities except for stopwatch itself.

## File Hierarchy Viewer
This panel helps us visualize files in cascade view. This panel has popup and drag-n-drop functionality. For drag-n-drop, you can move a file in the panel to a selected directory by dragging the file out of the app.

For popup functionality, this panel has multiple popup options:  
**Delete Selected File...** option has multiple sub-options that lets you delete zip, dummy files with 'dummy_file' name and files in general.  
**Delete Selected File** has one sub-option where you can specifically select zip files and deselect everything that is not a zip file.  
**Expand Selected Directories** lets you expand all selected directories.  
**Collapse Selected Directories** lets you collapse all selected directories.  
**Create Directories For File** This only works for files. Creates a directory per selected file and put them in.  
**Replace Text** lets you replace a character in a file name with another character. You put a character that you wanna replace in the 'Text' textbox and put a replacement for that character in 'Replace with' textbox. You can choose which position the replacement will take place by checking one of the checkboxes:

**Leading:** Only the first character is subjected to replacement.  
**Trailing:** Only the last character is subjected to replacement.  
**Replace All:** Every character in a filename is subjected to replacement.

# Settings Menu
This menu has two sub-menus: **Preferences** and **Exit**. 'Exit' menu terminates the application. 'Preferences' menu lets you set up preferences that is needed for the functionalities in the panels. Right now, 2 out of 6 preferences options are supported. More may come in the future.

Alright, let's enumerate the two supported options:

**File Generation:** This is a preference settings for the 'File Generation' panel. We can create a preset here that the File Generation panel will use. To add preset click the 'Add Preset' button. To edit existing preset, click the 'Edit Preset' button. To delete an active preset, click the 'Delete Preset' button.

A file generation preset has these properties:  
**Preset Name:** Name of the preset. I usually put the forum domain here.  
**Supported Hosts:** Supported hosts of the preset. When you generate a post.txt, these supported hosts will have space in the download section of your post.txt

**Link Operations:** This is a preference settings for the 'Link Operations' panel. The presets and UI structure here are pretty similar to the File Generation preference settings. However, Their functionalities are different.

A link operations preset has these properties:  
**Host Name:** Name of the host that the link operations will support.  
**Alternative Host Name:** Alternative name of the host name. For example, 'example.com' has another name which is called 'exmpl.com'. 'example.com' is the host name and 'exmpl.com' is its alternative host name.

Note, if the host in your File Generation preference is not supported in the list of supported host in link operations preference, that host will be deemed as invalid. Also, link operations doesn't check if a link is dead or not. That's the job of the 'Link Checker' dialog. Right now, the UI of that dialog is done but it has no functionality. I may add its functionality in the future.

# Tools Menu
Tools menu has two sub-menus: **File Randomizer** and **Link Checker**. 'Link Checker' is still not functioning. Thus, I'm gonna skip that and discuss the 'File Randomizer'.

**File Randomizer**
This dialog helps us choose which file we wanna upload in a random manner. This one has lots of properties and I'm gonna explain them all.

**Add Source/s:** This button lets you choose directories that we wanna add as candidates for randomization.  
**Add Destination:** This button lets you choose a directory where the randomized file will be placed.  
**Add Reference:** This button lets you choose directories that is going to be compared with the source directories. If the directory name of a source directory and a reference directory is equal, that source directory won't become a candidate for randomization.  
**Add Link Destination:** This button lets you choose a directory where 'symlinks' will be placed. **Symlinks** is just a shortcut for a file, to put it simply.

**# of directories:** This textbox sets the number of random directories in the output. If the number in this textbox is greater than the number of source directory candidates, the number of random directories in the output will be equal to the number of candidates.

**Settings:** This button opens up a new dialog which contains these properties:

**Randomed File/s Operation:** This property describes how the randomized output is handled. If the 'Copy' selection is selected, source directories will be copied to the destination directory. If the 'Move' selection is selected, source directories will be moved to the destination directory.  
**Created Symlink/s:** This property describes how the symlinks of the randomized output are handled. If the 'From Source' selection is selected, symlinks will be created from the source of the output directories. If the 'From Copy' selection is selected, symlinks will be created from the output directories.  
**Saved Presets:** This property lets us load a saved configuration of the properties of settings dialog. Just select a preset in the dropdown and click the 'Load Preset' button to load the selected preset.

**Save/Delete Preset:** This button opens up a dialog that lets us add a new 'settings' preset and delete an existing one. 'settings' preset includes the source, destination, records and symlinks directories and the properties in the 'settings' dialog. 

Type the preset name that you wanna add/delete in the textbox and click 'Save' button to save the preset. The preset won't be added if it already exists. You need to delete the existing preset first before you can add it again in the 'settings' preset record. click 'Delete' button to delete the preset in the record if it's existing.

**Randomize:** Once all the properties are set, click this button to start the randomization process. Source directories and a destination directory are required. Otherwise, randomization process won't start. Record directories and a symlinks destination directory are optional.

# Edit Menu
This menu has one sub-menu which is the 'Post File editor' which edits post.txt. Unfortunately, this sub-menu doesn't have UI and functionalities yet. I may work on this in the future.