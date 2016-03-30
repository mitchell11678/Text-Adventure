// Created by Mitchell Barnhart Brown attending Tarleton State University
//This is a simple text based adventure program that allows you to walk through different areas and drop and pick up things
package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text_Adventure {

    static int count = 0;
    static int number = 0;
    static String Location, Description;
    static ArrayList<String> Inventory = new ArrayList<>();
    static ArrayList<String> Things = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String Command, cmdString;
        String[] strArr;
        Scanner scanner = new Scanner(System.in);

        Game();
        Location = "Cottage";
        Read();
        Desc();
        System.out.println("You see " + Things.toString());
        System.out.println("Input 'help' at any time if you are having trouble");

        //Checks what the user input is
        while (true) {
            System.out.println();
            System.out.print("Command(picking up items is case sensitive): ");
            cmdString = scanner.nextLine();
            strArr = cmdString.split(" ", 2);
            String firstWord = strArr[0];
            firstWord = firstWord.toLowerCase();
            firstWord = firstWord.trim();

            if (null != firstWord) {
                switch (firstWord) {
                    case "quit":
                        Quit();
                    case "go":
                        try {
                            Command = strArr[1];
                            Command.toLowerCase();
                            if (Game().get(Location).toString().contains(Command)) {
                                Go(Command);
                            } else {
                                System.out.println("Not a vlid direction");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Not a valid direction");
                            break;
                        }
                        break;
                    case "pickup":
                    case "get":
                    case "drop":
                        try {
                            if (!strArr[1].isEmpty()) {
                                Object(strArr[1], strArr[0]);
                            } else {
                                System.out.println("Not a valid item");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Not a valid item");
                        }
                        break;
                    case "save":
                        try {
                            if (strArr[1].isEmpty()) {
                            } else {
                                System.out.println("Invalid Command");
                                break;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Save();
                            break;
                        }
                    case "look":
                        System.out.println(Description);
                        if (Things.isEmpty()) {
                            System.out.println("You see no items");
                        } else {
                            System.out.println("You see " + Things.toString());

                        }
                        break;
                    case "things":
                    case "inventory":
                        try {
                            Sort(firstWord, strArr[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            Sort(firstWord, "asc");
                        }
                        break;
                    case "help":
                        try {
                            Help(strArr[1]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            strArr[1] = "nothing";
                            Help(strArr[1]);
                        }
                        break;
                    default:
                        try {
                            if (Game().get(Location).toString().contains(firstWord)) {
                                Go(firstWord);
                            } else {
                                System.out.println("Invalid Command");
                            }
                        } catch (NullPointerException e) {
                            System.out.println("Invalid Command");
                        }
                        break;
                }
            }
        }
    }

    //Help Command
    public static void Help(String catagory) {
        switch (catagory) {
            case "pickup":
                System.out.println("Input 'pickup' or 'get' followed by the item you wish to pick up. Keep in mind that the items are case sensitive. Ex. 'pickup Apple' or 'get Apple'");
            case "get":
                System.out.println("Input 'pickup' or 'get' followed by the item you wish to pick up. Keep in mind that the items are case sensitive. Ex. 'pickup Apple' or 'get Apple'");
            case "drop":
                System.out.println("Input 'drop' followed by the item in your inventory. Keep in mind it that the items are case sensitive. Ex. 'drop Apple' or 'drop Candle'");
            case "look":
                System.out.println("Input 'look' this reads off the description for the location your in again, as well as the items currently in your location");
            case "things":
                System.out.println("Input 'things' to see ONLY the items in your current location");
            case "inventory":
                System.out.println("Input 'inventory' to display your current inventory");
            case "go":
                System.out.println("Input 'go' followed by a valid direction, or simply a valid direction and you will be moved to the location in said direction. Ex. 'go east' or 'east'");
            case "quit":
                System.out.println("This exits and resets the game");
            case "save:":
                System.out.println("Input 'save' followed by nothing else (this currently does nothing)");
            default:
                System.out.println("************************************-Help-***********************************************\n"
                        + "* 'pickup' or 'get' = take an items that is in your location                            *\n"
                        + "* 'drop' = drop an item from your inventory into the current location                   *\n"
                        + "* 'look' = look at your current location and see what items are on the ground           *\n"
                        + "* 'inventory' = see what you're currently holding                                       *\n"
                        + "* 'things' = see what's in the current location                                         *\n"
                        + "* 'save' = saves the game (not currently implemented)                                   *\n"
                        + "* 'go' + direction or direction = go in that direction                                  *\n"
                        + "* 'quit' = quit the game without saving                                                 *\n"
                        + "*------------------------------------Help Catagories------------------------------------*\n"
                        + "* The following is a list of help commands if you don't understand how to use the above *\n"
                        + "* 'help pickup' 'help get' 'help drop' 'help look' 'help things' 'help inventory'       *\n"
                        + "* 'help save' 'help quit'                                                               *\n"
                        + "************************************-Help-***********************************************");
        }

    }

    //Go command
    public static void Go(String Command) throws FileNotFoundException, IOException {
        String[] cmdArr;
        String thisLine, Cmd;
        BufferedReader read = new BufferedReader(new FileReader("game.txt"));

        while ((thisLine = read.readLine()) != null) {
            if (thisLine.equals("@ " + Location)) {
                while (read.readLine() != null) {
                    if ((thisLine = read.readLine()).contains("@")) {
                    } else if (thisLine.equals("! " + Command)) {
                        cmdArr = thisLine.split(" ", 2);
                        Cmd = cmdArr[1].toLowerCase();
                        if (Cmd.equals(Command)) {
                            System.out.println("Going " + Command);
                            thisLine = read.readLine();
                            cmdArr = thisLine.split(" ", 2);
                            Location = cmdArr[1];
                            Desc();
                            read.close();
                            return;
                        }
                    }
                }
            }
        }
        read.close();
    }

    //Description Command
    public static void Desc() throws FileNotFoundException, IOException {
        String[] descArr;
        String thisLine;
        BufferedReader read = new BufferedReader(new FileReader("game.txt"));

        while ((thisLine = read.readLine()) != null) {
            if (thisLine.equals("@ " + Location)) {
                thisLine = read.readLine();
                if (thisLine.charAt(0) == '%') {
                    descArr = thisLine.split(" ", 2);
                    Description = descArr[1];
                    Read();
                }
            }
        }

        read.close();
        System.out.println(Description);
    }

    //Pickup and drop commands
    public static void Object(String Item, String Command) throws IOException {
        Read();
        if (Things.contains(Item) && ("pickup".equals(Command) || "get".equals(Command))) {
            Get(Item);
            Inventory.add(Item);
            System.out.println(Item + " was picked up");
        } else if (Inventory.contains(Item) && ("drop".equals(Command))) {
            Drop(Item);
            Inventory.remove(Item);
            System.out.println(Item + " was dropped");
        } else {
            System.out.println("Not a valid item");
        }
    }

    //Drop implementation
    public static void Drop(String Item) throws FileNotFoundException, IOException {
        File inputFile = new File("game.txt");
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        String thisLine;

        BufferedReader Read = new BufferedReader(new FileReader(inputFile));
        PrintWriter Print = new PrintWriter(new FileWriter(tempFile));

        while ((thisLine = Read.readLine()) != null) {
            Print.println(thisLine);
            Outer:
            if (thisLine.contains("@ " + Location)) {
                while ((thisLine = Read.readLine()) != null) {
                    if (thisLine.isEmpty()) {
                        Print.println("# " + Item + "\r\n");
                        break Outer;
                    } else {
                        Print.println(thisLine);
                    }
                }
            }
        }

        Print.close();
        Read.close();

        if (!inputFile.delete()) {
            System.out.println("Can't delete file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Can't rename");
        }
        tempFile.renameTo(inputFile);

    }

    //Pickup implementation
    public static void Get(String Remove) throws IOException {
        File inputFile = new File("game.txt");
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        BufferedReader Read = new BufferedReader(new FileReader(inputFile));
        PrintWriter Print = new PrintWriter(new FileWriter(tempFile));

        String thisLine;

        while ((thisLine = Read.readLine()) != null) {
            if (!thisLine.trim().contains(Remove)) {
                Print.println(thisLine);
                Print.flush();
            }
        }

        Print.close();
        Read.close();

        if (!inputFile.delete()) {
            System.out.println("Can't delete file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Can't rename");
        }
        tempFile.renameTo(inputFile);
    }

    //Save command <W.I.P>
    public static void Save() {
        System.out.println("Game saved");
    }

    //Loads the game file and stores the locations into a hashtable
    public static Hashtable Game() {

        Hashtable<String, String> nodes = new Hashtable<>();
        String Location = null;
        String[] locArr, dirArr;
        String[] directionArr = new String[10];
        String dirs = "";

        try {
            StringBuilder builder = new StringBuilder();
            BufferedReader read = new BufferedReader(new FileReader("game.txt"));
            String thisLine = "";
            while ((thisLine = read.readLine()) != null) {
                for (int i = 0; i < thisLine.length(); i++) {
                    if ((thisLine.charAt(i)) == '@') {
                        locArr = thisLine.split(" ", 2);
                        Location = locArr[1];
                        count = 0;
                        for (int s = 0; s < directionArr.length; s++) {
                            directionArr[s] = null;
                        }
                    } else if ((thisLine.charAt(i)) == '!') {
                        builder.setLength(0);
                        dirArr = thisLine.split(" ", 2);
                        directionArr[count] = dirArr[1];
                        count++;
                        for (int d = 0; d < directionArr.length; d++) {
                            if (directionArr[d] != null) {
                                builder.append(directionArr[d] + " ");
                            }
                            dirs = builder.toString();
                        }
                    } else {
                        nodes.put(Location, dirs.toLowerCase());
                    }
                }
            }
            read.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Text_Adventure.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Text_Adventure.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nodes;

    }

    //Sorts the inventory
    public static void Sort(String Arg, String Cmd) {
        if (Cmd.equals("asc") || Cmd.equals("des")) {
            switch (Arg) {
                case "inventory":
                    if ("des".equals(Cmd)) {
                        Comparator comparator = Collections.reverseOrder();
                        Collections.sort(Inventory, comparator);
                        System.out.println(Inventory.toString());
                        return;
                    } else {
                        Collections.sort(Inventory);
                        System.out.println(Inventory.toString());
                        return;
                    }
                case "things":
                    if ("des".equals(Cmd)) {
                        Comparator comparator = Collections.reverseOrder();
                        Collections.sort(Things, comparator);
                        System.out.println(Things.toString());
                        return;

                    } else {
                        Collections.sort(Things);
                        System.out.println(Things.toString());
                        return;
                    }
                default:
                    System.out.println("Invalid Command");
                    return;
            }
        }
        System.out.println("Invalid Command");
        return;

    }

    //Reads the file and adds the items in the location to the 'Things' hashtable
    public static void Read() throws FileNotFoundException, IOException {
        try {
            Things.clear();
        } catch (NullPointerException e) {
        }
        String[] thingsArr;
        String thisLine, Th;
        BufferedReader read = new BufferedReader(new FileReader("game.txt"));

        while ((thisLine = read.readLine()) != null) {
            if (thisLine.equals("@ " + Location)) {
                while ((thisLine = read.readLine()) != null) {
                    if (thisLine.contains("@ ")) {
                        break;
                    } else if (thisLine.contains("# ")) {
                        thingsArr = thisLine.split(" ", 2);
                        Th = thingsArr[1];
                        Things.add(Th);
                    }
                }
            }
        }
        read.close();
    }

    //Quit command
    public static void Quit() throws IOException {
        File curFile = new File("game.txt");
        File orgFile = new File("gameOriginal.txt");
        File copyFile = new File("copy.txt");

        BufferedReader Read = new BufferedReader(new FileReader(orgFile));
        PrintWriter Print = new PrintWriter(new FileWriter(copyFile));

        String thisLine;

        while ((thisLine = Read.readLine()) != null) {
            Print.println(thisLine);
            Print.flush();
        }

        Print.close();
        Read.close();

        if (!curFile.delete()) {
            System.out.println("Can't Delete File");
            System.exit(0);
        }
        if (!orgFile.renameTo(curFile)) {
            System.out.println("Can't Rename File");
        }
        if (!copyFile.renameTo(orgFile)) {
            System.out.println("Can't Rename");
        }

        System.exit(0);
    }

}
