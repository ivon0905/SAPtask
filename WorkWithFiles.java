import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class WorkWithFiles {
    static File file;
    static String filePath;
    static Scanner scan;
    public static void main(String[] args){
        try {
            scan = new Scanner(System.in);
            int lines = 0;
            System.out.println("Welcome to java file manipulator\nYou should provide the path of your file...");
            while(true) {
                filePath = checkFileExistence();
                lines = checkFileContent(filePath);
                if(lines!=0)
                    break;
            }
            menu(lines, filePath);
        }catch(IOException e){
            e.getMessage();
        }catch(InputMismatchException e){
            System.err.println("Only numbers required!");
        }finally{
            scan.close();
        }
    }
    public static String checkFileExistence(){
        scan = new Scanner(System.in);
        while(true) {
            filePath = scan.nextLine();
            file = new File(filePath);
            if(file.exists()){
                System.out.println("File found");
                break;
            }else {
                System.err.println("Non existing file");
                System.out.println("Enter new path...");
            }
        }
        return filePath;
    }
    public static int checkFileContent(String filePath) throws IOException {
        BufferedReader bf = null;
        int numLines = 0;
        try {
            file = new File(filePath);
            bf = new BufferedReader(new FileReader(file));
            String line;
            int i=0;
           if(file.length()==0){
               System.err.println("Empty file! You cannot make any operations...\nEnter new path....");
           }else{
               System.out.println("        File content:");
                while ((line = bf.readLine()) != null) {
                    numLines++;
                    System.out.print("Line "+numLines+": ");
                    System.out.println(line);
                }
            }
        }finally{
            if(bf!=null)
                bf.close();
        }
        return numLines;
    }
    public static void menu(int numLines, String filePath) throws InputMismatchException, IOException{
        System.out.println("              ----------MENU----------");
        System.out.println("     You can choose an option as you enter its code");
        System.out.println("     (1)Swap two lines as you enter their indexes");
        System.out.println("(2)Swap two words from different lines by providing indexes");
        int option = scan.nextInt();
        boolean isTrue = false;
        do {
            if (option == 1) {
                option1(numLines, filePath);
                isTrue=true;
                break;
            }
            else if (option == 2) {
                option2(numLines);
                isTrue = true;
                break;
            }else {
                System.err.println("You should choose between 1 or 2");
                option = scan.nextInt();
            }
        }while(isTrue);
    }
    public static int checkLines(int numLines){
        int index = 0;
        scan=new Scanner(System.in);
        while(index<1 || index>numLines) {
            System.out.println("Index of line: ");
            index = scan.nextInt();
            if (index < 1 || index > numLines)
                System.err.println("Index out of boundaries. Choose between 1 to " + numLines);
        }
        return index;
    }
    public static void option1(int numLines, String filePath) throws IOException{
        System.out.println("Enter the indexes of the lines you want to switch");
        int index1 = 0, index2 = 0;
        scan=new Scanner(System.in);
        index1= checkLines(numLines);
        index2= checkLines(numLines);
        if(index1==index2){
            System.err.println("You cannot choose the same index");
            index2 = checkLines(numLines);
        }
        String[] fileContent = getData(filePath, numLines);
        String[] newFileContent = new String[numLines];
        for(int j=0;j<numLines;j++){
            if((index1-1)==j)
                newFileContent[j]=fileContent[(index2-1)];
            else if((index2-1)==j)
                newFileContent[j]=fileContent[(index1-1)];
            else
                newFileContent[j]=fileContent[j];
        }
        writeToFile(numLines, newFileContent);
    }
    public static void option2(int numLines) throws IOException{
        System.out.println("Enter the indexes of the lines you want to switch");
        int indexLine1=0, word1, indexLine2=0, word2;
        scan=new Scanner(System.in);
        String[] fileContent = getData(filePath, numLines);
        indexLine1 = checkLines(numLines);
        String[] line1 = fileContent[indexLine1-1].split("[ |\t]");
        word1 = getWordIndex(line1);
        indexLine2 = checkLines(numLines);
        if(indexLine1==indexLine2){
            System.err.println("You cannot choose the same index...");
            indexLine2= checkLines(numLines);
        }
        String[] line2 = fileContent[indexLine2-1].split("[ |\t]");
        word2 = getWordIndex(line2);
        String[] newFileContent = new String[numLines];
        String word = line1[word1-1];
        for(int q=0; q<line1.length; q++){
            if((word1-1)==q)
                line1[q]=line2[word2-1];
        }
        for(int k=0; k<line2.length; k++){
            if((word2-1)==k)
                line2[k]=word;
        }
        for(int j=0;j<numLines;j++){
            if((indexLine1-1)==j)
                newFileContent[j]= toString((line1));
            else if((indexLine2-1)==j)
                newFileContent[j]= toString((line2));
            else
                newFileContent[j]=fileContent[j];
        }
        writeToFile(numLines, newFileContent);
    }

    public static void writeToFile(int numLines, String[] newFileContent) throws IOException{
        file = new File(filePath);
        FileWriter fw = null;
        try{
            fw = new FileWriter(file, false);
            for(int i=0;i<numLines;i++) {
                fw.write(newFileContent[i]+"\n");
            }
        }finally{
            if(fw!=null)
                fw.close();
        }
        checkFileContent(filePath);
    }

    public static int getWordIndex(String[] line){
        scan=new Scanner(System.in);
        int word = 0;
        while(true) {
            System.out.println("Index of word: ");
            word = scan.nextInt();
            if(line.length>=word){
                break;
            }else
                System.err.println("Index out of boundaries. Choose between 1 to "+line.length);
        }
        return word;
    }

    private static String toString(String[] line) {
        String str = "";
        for(int i=0; i<line.length;i++)
            str +=  line[i] + " ";
        return str;
    }

    public static String[] getData(String filePath, int numLines) throws IOException{
        String[] fileContent = new String[numLines];
        BufferedReader bf = null;
        String line;
        int i = 0;
        try{
            file = new File(filePath);
            bf = new BufferedReader(new FileReader(file));
            while ((line = bf.readLine()) != null){
                fileContent[i]=line;
                i++;
            }
        }finally{
            if(bf!=null)
                bf.close();
        }
        return fileContent;
    }
}
