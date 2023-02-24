# java-files-and-links-manager
Multipurpose files and links manager that is created by me using java swing package.

Watch this [**video**](https://youtu.be/SvH74gjn25g) to see this app in action.

This app can help you share multiple non-copyrighted books and tutorials to online forums. This app generates pre-formatted html that is ready to be posted in forums, insert links in that html, create dummy files to increase your zip file size and many more. The only thing you need to do is fill up the pre-formatted html with necessary information.

I may update this project in the feature. However, that may not happen soon because I'm focusing on web development right now. I created and halted this project somewhere in 2021. Note, this project is pretty much complete. However, there are few features in this project that are left unfinished.  

I didn't use IDE when I created this app. You can try importing it in your favorite IDE if you want. If you wanna compile this via terminal/command prompt, here's how I do it.

### Compiling  
create classes directory adjacent to 'packages' directory. Open terminal/cmd in the 'packages' directory and enter this command:  
**javac -d ../classes/ main/Fmanager.java -Xlint:unchecked**

### Running  
Once you compiled the project successfully, open terminal/cmd in the 'classes' director and enter this command:  
**java main/Fmanager**

### Building
If you want to pack this project in .jar file, create build director adjacent to 'classes' directory and open terminal/cmd in the 'classes' directory and enter this command:  
**jar cfe ../build/Fmanager.jar main.Fmanager main/*.class gui/*.class gui/events/*.class gui/gui_related_events/*.class gui/subpanels/*.class gui/subpanels/filetree/*.class gui/dialogs/*.class gui/dialogs/subpanels/prefs_subpanel/*.class fileoperations/*.class fileoperations/approvedopfunctions/*.class**

After finishing this project, I realized that IDEs are precious tools :satisfied:

# Manual
This app has lots of functionalities. You can watch the video link above to give you an idea how this app works. In here, I'm going to list the functionalities of this app:

## File Generation
**Create post.txt** you can create a pre-formatted html in .txt file by clicking the 'create post.txt' button. You can change the format type by choosing one of the formats in 'post type' dropdown list. Also, you can modify the pre-formatted html to a 'collection' format by checking the 'use collection template' checkbox.

**Create/Delete Dummy Files** To create a dummy file, click the 'Create dummy_file' button. Click the 'Delete dummy_file' button to delete a dummy file. The 'threshold' textbox evaluates the total size of a directory where a dummy file will be placed. 

If the total file size of a directory is greater then the threshold, no dummy file will be created. Otherwise, a dummy file will be created. The 'include sub-directories' checkbox is an option for 'delete dummy_file' button. 

If checked, all dummy files in every directory in a top-level directory will be deleted. Otherwise, only the dummy files in the top-level directory will be deleted.

## Logs
This panel gives information about the operations that has happened in the app. Clear the 'logs' textbox by clicking the 'clear' button.

## Link Operations
This panel handles the links that is going to be put in post.txt. There are two types of how this panel handles that distribution of links:

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

Other functionalities will be written soon...

