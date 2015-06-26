#!/bin/bash

clear
export MAIN_DIALOG='

<window title="CRYPTO">
  
  <notebook labels="Encrypt|Decrypt">
    
    <vbox>
    
    <frame Select Input File>
	  <hbox>
	    <entry>
	      <variable>FILE</variable>
	    </entry>
	    <button>
	      <input file stock="gtk-open"></input>
	      <variable>FILE_BROWSE</variable>
	      <action type="fileselect">FILE</action>
	    </button>
	  </hbox>
     </frame>
    
      <frame Select Output Directory>
	<hbox>
	  <entry accept="directory">
	    <variable>FILE_DIRECTORY</variable>
	  </entry>
	  <button>
	    <input file stock="gtk-open"></input>
	    <variable>FILE_BROWSE_DIRECTORY</variable>
	    <action type="fileselect">FILE_DIRECTORY</action>
	  </button>
	</hbox>
      </frame>
    
      <vbox>
	<button>
	  <height>20</height>
	  <width>40</width>
	  <label>Encrypt</label>
	  <action signal="clicked">clear; java docrypto.UserInput "$FILE" "$FILE_DIRECTORY"</action>
	  <variable>"flag"</variable>
	</button>
      </vbox>
    
    </vbox>
    
    <vbox>         
      <expander label="Decryption" expanded="true">
	<vbox>
	  <frame Choose File to be Decrypted>
	    <hbox>
	      <entry>
		<variable>XFILE</variable>
	      </entry>
	      <button>
		<input file stock="gtk-open"></input>
		<variable>xfile</variable>
		<action type="fileselect">XFILE</action>
	      </button>
	    </hbox>
	  </frame>  
	  
    
	  <frame Choose Appropriate Key>
	    <hbox>
	      <entry>
		<variable>KEY</variable>
	      </entry>
	      <button>
		<input file stock="gtk-open"></input>
		<variable>key</variable>
		<action type="fileselect">KEY</action>
	      </button>
	    </hbox>
	  </frame>
	  
	 <frame Select Output Directory>
	<hbox>
	  <entry accept="directory">
	    <variable>DIR</variable>
	  </entry>
	  <button>
	    <input file stock="gtk-open"></input>
	    <variable>DIR_BROWSE</variable>
	    <action type="fileselect">DIR</action>
	  </button>
	</hbox>
      </frame>
    
	  <vbox>
	    <button>
	      <height>20</height>
	      <width>40</width>
	      <label>Decrypt</label>
	      <action signal="clicked">clear; java docrypto.Decrypt "$XFILE" "$KEY" "$DIR"</action>
	    </button>
	  </vbox>
	 </vbox>
      </expander>
    
    </vbox>
   
   </notebook>
   
</window>

'
gtkdialog --program=MAIN_DIALOG
