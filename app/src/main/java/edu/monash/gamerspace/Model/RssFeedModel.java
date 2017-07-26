/*
Project: GameSpace
Project Description: Create a space which displays available games for each of the registered vendors
File name: RssFeedModel.java
Description: Contains the information of a rss feed information. As this is retrieved online, little operations are being implemented into this class
Author: Ha Nam Anh Pham
Last Modified Date: 09-06-2017
*/

//Implements all relevant libraries/packages

package edu.monash.gamerspace.Model;

/**
 * Created by MSI on 21-May-17.
 */

public class RssFeedModel {

    //Variables for storing the feed's title, description and link
    public String title;
    public String link;
    public String description;

    //Standard Constructor
    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }
}
