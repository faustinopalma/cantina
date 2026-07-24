package xmltree;
import org.w3c.dom.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class xml_brain {
 int Numero;
 String outHtml="";
public Document xmlData;
       public xml_brain(Document xmlData){
       this.xmlData=xmlData;
       }
public String retHtml(){

String first="";
String mid="";
String after="";
first=first();
first+="</head><body>"+"\n";
first+="<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">"+
		"<tr><td>"+"\n";
first+="<form name=\"frmMenu\" action=\"_self\" method=\"post\">"+"\n";


int iTotal = 0;
Numero=iTotal;
String sLeftIndent = "";
//mid= DisplayNode( xmlData.getChildNodes(), iTotal, sLeftIndent);
DisplayNode( xmlData.getChildNodes(), iTotal, sLeftIndent);
mid=outHtml;
after+="<input type=\"hidden\" name=\"hdnOpenFolders\" value=\"1\"></form>"+"\n";

after+="</td></tr></table>"+"\n";
after+="<SCRIPT LANGUAGE=\"Javascript\">"+"\n";
after+="	<!--"+"\n";
after+="		"+"\n";
after+="		var arClickedElementID = new Array(";
    for (int i = 1;i<=Numero;i++){
     after+=""+"\""+i+"\"";
       if (i < Numero) {
       after+=",";
       }
     }
after+=");"+"\n";
after+="		var arAffectedMenuItemID = new Array(";
    for (int i = 1;i<=Numero;i++){
     after+=""+"\""+(i+1)+"\"";
       if (i < Numero) {
       after+=",";
       }
     }
after+=");"+"\n";

after+="	//-->"+"\n";
after+="</SCRIPT>"+"\n";


after+="</body>"+"\n"+"</html>";
return first+mid+after;
}

public void DisplayNode(NodeList objNodes, int iElement, String sLeftIndent) {

    //String outHtml="";
    Numero=iElement;
    iElement = iElement + 1;
    for (int k=0;k<objNodes.getLength();k++){


    boolean	bHasChildren = objNodes.item(k).hasChildNodes();
    boolean bIsLast;
    boolean bIsRoot;

    /*try{
    Node test=objNodes.item(k).getNextSibling();
            bIsLast = false;

    }catch(NullPointerException e){bIsLast = true;}
    */
    if (k==objNodes.getLength()){
    bIsLast = true;
    }
    else
    {
    bIsLast = false;
    }


            String sNodeName = objNodes.item(k).getNodeName();
            Element tempNode=(Element) objNodes.item(k);
            String sAttrValue = tempNode.getAttribute("value");
            String sNodeType = tempNode.getAttribute("type").toLowerCase();
            String sURL = tempNode.getAttribute("url");
            String sTempLeft="";


            if (sNodeType == "root") {
                    bIsRoot = true;}
            else{
                    bIsRoot = false;}




            if (sNodeType == "document") {
                            outHtml+=
                            "<table border=0"+
                             " cellspacing=0 cellpadding=0><tr>\n"+
                             sLeftIndent+
                            "<td height=\"16\"><img src=img/";
                            outHtml+= fnChooseIcon(bIsLast, bIsRoot, sNodeType, bHasChildren, false);

                           outHtml+= " width=31 height=16 border=0></td><td>\n"+
                            "<img src=img/pixel.gif width=2 height=1></td>\n"+
                            "<td nowrap class=node><img src=img/pixel.gif width=2 height=1>\n"+
                            "<a href=\""+sURL+"\" target=basefrm onClick=objPreviousLink="+
                            "fnSelectItem(this,objPreviousLink)>\n"+sAttrValue+
                            "</a><img src=img/pixel.gif width=2 height=1></td></tr></table>\n";
            }
            else{
                            outHtml+="<table border=0 cellspacing=0 cellpadding=0><tr>\n";
                            boolean bShowOpen;
                            if (sNodeType == "root") {
                                            bShowOpen = true;

                                    }else{
                                            bShowOpen = false;
                            }



                            outHtml+= sLeftIndent;
                            outHtml+="<td height=\"16\">\n<img onclick=\"doChangeTree(this"+
                            ", arClickedElementID, arAffectedMenuItemID);\" "+
                            "class=LEVEL"+iElement+" src=img/"+
                            fnChooseIcon(bIsLast, bIsRoot, sNodeType, bHasChildren, bShowOpen)+
                            " id="+iElement+" width=31 height=16 border=0 name=\""+
                            "\"></td><td><img src=img/pixel.gif width=2 height=1></td>\n"+
                            "<td nowrap class=node><img src=img/pixel.gif width=2 height=1>\n"+
                            "<a href="+sURL+" target=basefrm onClick=\"objPreviousLink="+
                            "fnSelectItem(this,objPreviousLink)\">"+sAttrValue+
                            "</a><img src=img/pixel.gif width=2 height=1></td></tr></table>\n";

                                                            iElement = iElement + 1;

                            if (bHasChildren) {
                            outHtml+="<table border=0 cellspacing=0 cellpadding=0>\n"+
                            "<tr class=LEVEL"+iElement+" id="+iElement+
                            " style=display:";
                            if (bShowOpen==false) outHtml+="none><td>\n";
                            if (bShowOpen==true) outHtml+="><td>\n";
                            }
                            sTempLeft = sLeftIndent;

                            if (iElement > 1) {
                                    sLeftIndent = fnBuildLeftIndent(bIsLast, sLeftIndent);
                            }

                            //Numero=iElement;
                            DisplayNode(tempNode.getChildNodes(), iElement, sLeftIndent);

                            sLeftIndent = sTempLeft;
                            outHtml+="</td></tr></table>\n";
                            //return outHtml;
                    }
            }
        //return outHtml;
}

public String fnBuildLeftIndent(boolean bIsLast, String sLeftIndent){

	if (bIsLast == false) {
		sLeftIndent = sLeftIndent + "<td><img src=img/line.gif width=18 height=16></td>\n";
	}
        else{
		sLeftIndent = sLeftIndent + "<td><img src=img/pixel.gif width=20 height=1 border=0></td>\n";
	}

	return sLeftIndent;
}

public String fnChooseIcon(boolean bIsLast, boolean bIsRoot, String sNodeType,
       boolean bHasChildren, boolean bShowOpen){
       String sIcon;

	sIcon = "";


	if (sNodeType == "document") {
		if (bIsLast == false) {
			sIcon = "docjoin.gif";
                   }
		else
                {
			sIcon = "doc.gif";
		}
            }
	else{
		if (bIsRoot == true) {

			if (bShowOpen == true) {
				sIcon = "minusonly.gif";}
			else{
				sIcon = "plusonly.gif";
			}
                 }
		else if  (bHasChildren == true) {

			if (bShowOpen == true) {
				sIcon = "folderopen.gif";}
			else{
				sIcon = "folderclosed.gif";
			}
                  }
		else if (bHasChildren == false) {

			if (bIsLast == false) {

				sIcon = "folderclosedjoin-empty.gif";}
			else{

				sIcon = "folderclosed-empty.gif";
			}
		}
	}

	return sIcon;
}
public String first(){
String temp="";

temp+="<html><head>\n"+
	"<title>Network Analysis (by Faustino Palma)</title>\n"+
	"<STYLE TYPE=\"text/css\">\n"+
	"<!--\n"+
		".node { color: black;\n"+
			"font-family : \"Helvetica\", \"Arial\", \"MS Sans Serif\", sans-serif;\n"+
			"font-size : 9pt;}\n"+
		".node A:link { color: black; text-decoration: none; }\n"+
		".node A:visited { color: black; text-decoration: none; }\n"+
		".node A:active { color: black; text-decoration: none; }\n"+
		".node A:hover { color: black; text-decoration: none; }\n"+
	"-->\n"+
	"</STYLE>\n"+
	"<script language=\"javascript\">\n"+

        "var objPreviousLink = null;\n"+

"//The following lines preload the menu img"+"\n"+
"var imgPixel = new Image(31,16);"+"\n"+
"var imgLine = new Image(31,16);"+"\n"+
"var imgDocJoin = new Image(31,16);"+"\n"+
"var imgDoc = new Image(31,16);"+"\n"+
"var imgPlusOnly = new Image(31,16);"+"\n"+
"var imgMinusOnly = new Image(31,16);"+"\n"+
"var imgFolderOpen = new Image(31,16);"+"\n"+
"var imgFldrClosed = new Image(31,16);"+"\n"+
"var imgFldrClosedJoinempty = new Image(31,16);"+"\n"+
"var imgFldrClosedempty = new Image(31,16);"+"\n"+

"imgPixel.src = \"img/pixel.gif\";"+"\n"+
"imgLine.src = \"img/line.gif\";"+"\n"+
"imgDocJoin.src = \"img/docjoin.gif\";"+"\n"+
"imgDoc.src = \"img/doc.gif\";"+"\n"+
"imgPlusOnly.src = \"img/plusonly.gif\";"+"\n"+
"imgMinusOnly.src = \"img/minusonly.gif\";"+"\n"+
"imgFolderOpen.src =\"img/folderopen.gif\";"+"\n"+
"imgFldrClosed.src = \"img/folderclosed.gif\";"+"\n"+
"imgFldrClosedJoinempty = \"img/folderclosedjoin-empty.gif\";"+"\n"+
"imgFldrClosedempty = \"img/folderclosed-empty.gif\";  "+"\n"+

"//This function queries the arClickedElementID[] and arAffectedMenuItemID[] arrays"+"\n"+
"//to get an object reference to the appropriate menu element to show or hide."+"\n"+
"function fnLookupElementRef(sID, arClickedElementID, arAffectedMenuItemID)"+"\n"+
"{"+"\n"+
"	var i;"+"\n"+
"	for (i=0;i<arClickedElementID.length;i++)"+"\n"+
"		if (arClickedElementID[i] == sID)"+"\n"+
"			return document.getElementById(arAffectedMenuItemID[i]);"+"\n"+
"			"+"\n"+
"	return null;"+"\n"+
"}"+"\n"+

"//This function is responsible for showing/hiding the menu items.  It"+"\n"+
"//also switches the img accordingly"+"\n"+
"function doChangeTree(e, arClickedElementID, arAffectedMenuItemID)"+"\n"+
"{"+"\n"+
"	var targetID, srcElement, targetElement;"+"\n"+
"	srcElement = e;"+"\n"+

"	if (srcElement != null)			"+"\n"+
"		//Only work with elements that have LEVEL in the classname"+"\n"+
"		if(srcElement.className.substr(0,5) == \"LEVEL\")"+"\n"+
"		{"+"\n"+
"			//Using the ID of the item that was clicked, we look up"+"\n"+
"			//and retrieve an object reference to the menu item that"+"\n"+
"			//should be shown or hidden"+"\n"+
"			targetElement = fnLookupElementRef(srcElement.id, arClickedElementID, arAffectedMenuItemID)"+"\n"+
""+"\n"+
"			if (targetElement != null)"+"\n"+
"			{"+"\n"+
"				fnChangeFolderStatus(srcElement, targetElement);"+"\n"+

"				//If we have a value in the MODE field, it means we are clicking"+"\n"+
"				//on a site.  We should submit the menu so we can retrieve the"+"\n"+
"				//data for that site and rebuild the tree "+"\n"+
"				if (srcElement.name == 'LoadOnDemand')"+"\n"+
"				{"+"\n"+
"					//We submit the menu only if the tree is being expanded.  "+"\n"+
"					if (targetElement.style.display == \"\")"+"\n"+
"						document.frmMenu.submit();"+"\n"+
"				}"+"\n"+
"			}"+"\n"+
"		}"+"\n"+
"}"+"\n"+

"//Adds the current element ID to a string stored in hidden HTML field."+"\n"+
"//Only adds the ID if it is not already in there"+"\n"+
"function fnAddItem(objField, sElementID)"+"\n"+
"{"+"\n"+
"	var sCurrValue = objField.value;"+"\n"+
""+"\n"+
"	if (sCurrValue.indexOf(sElementID) == -1)"+"\n"+
"		objField.value = objField.value + ',' + sElementID;"+"\n"+
"}"+"\n"+

"//Removes a specific element ID from a string stored in hidden HTML field."+"\n"+
"function fnRemoveItem(objField, sElementID)"+"\n"+
"{"+"\n"+
"	var sCurrValue = objField.value;"+"\n"+
"	var arValues = sCurrValue.split(',');"+"\n"+
"	var arNewValues = new Array(0);"+"\n"+
"	var x=0;"+"\n"+

"	for (i=0;i<arValues.length;i++)"+"\n"+
"		if (arValues[i] != sElementID)"+"\n"+
"		{"+"\n"+
"			arNewValues[x] = arValues[i];"+"\n"+
"			x++;"+"\n"+
"		}"+"\n"+
"	"+"\n"+
"	sCurrValue = arNewValues.join(',');"+"\n"+
"	objField.value = sCurrValue;"+"\n"+
"}"+"\n"+

"//Opens a closed folder and closes an open folder.  This function"+"\n"+
"//is responsible for all aspects of changing the folder status."+"\n"+
"//Attributes are as follows:"+"\n"+
"//-------------------------------"+"\n"+
"//srcElement : Object reference to the folder that should be expanded/contracted"+"\n"+
"//targetElement : Object reference to the subfolder that should be displayed/hidden"+"\n"+
"function fnChangeFolderStatus(srcElement, targetElement)"+"\n"+
"{"+"\n"+
"	if (srcElement != null)"+"\n"+
"	{"+"\n"+
"		//First find out if the current folder is empty"+"\n"+
"		//We find out based on the name of the image used"+"\n"+
"		if (srcElement.tagName == 'IMG')"+"\n"+
"		{"+"\n"+
"			var simgource = srcElement.src;"+"\n"+
"			if (simgource.indexOf(\"empty\") == -1)"+"\n"+
"			{"+"\n"+
"				if (targetElement.style.display == \"none\")"+"\n"+
"				{"+"\n"+
"					//Our menu item is currently hidden, so display it"+"\n"+
"					targetElement.style.display = \"\";"+"\n"+
"									"+"\n"+
"					if (srcElement.className == \"LEVEL1\")"+"\n"+
"						//Set a special open-folder graphic for the root folder"+"\n"+
"						srcElement.src = imgMinusOnly.src;"+"\n"+
"					else"+"\n"+
"						//Otherwise, just show the standard icon"+"\n"+
"						srcElement.src = imgFolderOpen.src;"+"\n"+
"							"+"\n"+
"					fnAddItem(document.frmMenu.hdnOpenFolders, srcElement.id);"+"\n"+
"				}"+"\n"+
"				else"+"\n"+
"				{"+"\n"+
"					//Our menu item is currently visible, so hide it"+"\n"+
"					targetElement.style.display = \"none\";"+"\n"+
"						"+"\n"+
"					if (srcElement.className == \"LEVEL1\")"+"\n"+
"						//Set a special closed-folder graphic for the root folder"+"\n"+
"						srcElement.src = imgPlusOnly.src;"+"\n"+
"					else"+"\n"+
"						//Otherwise, just show the standard icon"+"\n"+
"						srcElement.src = imgFldrClosed.src;"+"\n"+
"						"+"\n"+
"					fnRemoveItem(document.frmMenu.hdnOpenFolders, srcElement.id);"+"\n"+
"				}"+"\n"+
"			}"+"\n"+
"		}"+"\n"+
"	}"+"\n"+
"}"+"\n"+

"//This function highlights the text of a menu item."+"\n"+
"//It also deselects the previously"+"\n"+
"//selected menu item.  It takes three parameters: 1) an"+"\n"+
"//object reference to the selected link, and 2) an "+"\n"+
"//object reference to the previously selected link.  The"+"\n"+
"//function returns a reference to the currently selected link."+"\n"+
"function fnSelectItem(objSelectedLink, objPreviousLink)"+"\n"+
"{	"+"\n"+
"	var bFound = false;"+"\n"+
"				"+"\n"+
"	//If we have previously selected a menu item, deselect it"+"\n"+
"	if (objPreviousLink != null)"+"\n"+
"		fnDeselectItem(objPreviousLink);"+"\n"+
"					"+"\n"+
"	//Find an object reference for our TD tag"+"\n"+
"	var objTD = objSelectedLink;"+"\n"+
"	while (objTD.tagName!=\"TD\")"+"\n"+
"	{"+"\n"+
"		objTD=objTD.parentElement;"+"\n"+
"					"+"\n"+
"		if (objTD.tagName == \"TD\")"+"\n"+
"			bFound = true;"+"\n"+
"	}"+"\n"+
"					"+"\n"+
"	//Got the TD tag reference, so now highlight the cell"+"\n"+
"	if (bFound == true)"+"\n"+
"	{"+"\n"+
"		objTD.className = \"selected\";"+"\n"+
"	}"+"\n"+
"					"+"\n"+
"	//Return reference to our selected item"+"\n"+
"	return objSelectedLink;"+"\n"+
"}"+"\n"+

"//This function removes the highlight from a"+"\n"+
"//previously selected menu item.  It takes an"+"\n"+
"//object reference to the item that needs deselecting."+"\n"+
"function fnDeselectItem(objPreviousLink)"+"\n"+
"{"+"\n"+
"	if (objPreviousLink !=  null)"+"\n"+
"	{"+"\n"+
"		//Find an object reference for our TD tag"+"\n"+
"		var objTD = objPreviousLink;"+"\n"+
"		while (objTD.tagName!=\"TD\")"+"\n"+
"			objTD=objTD.parentElement;"+"\n"+
"					"+"\n"+
"		//Change the style class for the TD tag"+"\n"+
"		//back to normal"+"\n"+
"		objTD.className = \"node\";"+"\n"+
"	}"+"\n"+
"}"+"\n"+


        "</script>";

return temp;


}

}