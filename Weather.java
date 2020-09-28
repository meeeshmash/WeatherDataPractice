package Weatherdata;
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

/**
 * Write a description of Weather here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Weather {
    public CSVRecord getSmallestOfTwo (CSVRecord record,CSVRecord current,String field){
        if (record == null){
                record = current;
            }else {
                double currentNum = Double.parseDouble(current.get(field));
                double recordNum = Double.parseDouble(record.get(field));
                
                if (currentNum < recordNum){
                    record = current;
                }
                                   
            }
        return record;    
    }
    
    
    public CSVRecord coldestHourInFile (CSVParser parser){
        CSVRecord coldestHourSoFar = null;
            
        for (CSVRecord currentRow : parser){
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if (currentTemp == -9999){}else{
            coldestHourSoFar = getSmallestOfTwo(coldestHourSoFar,currentRow,"TemperatureF");
        }        
        }
        return coldestHourSoFar;
    }
    
    public String fileWithColdestTemp(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord coldestHourSoFar = null;
        String coldestTempName = "";
                
        for (File f : dr.selectedFiles()){
             FileResource currentFile = new FileResource(f);
             CSVParser currentParser = currentFile.getCSVParser();
             if (coldestHourSoFar == null){
                 coldestHourSoFar = coldestHourInFile(currentParser);
                 double coldestTemp = Double.parseDouble(coldestHourSoFar.get("TemperatureF"));
                 
                 
            } else {
                 CSVRecord currentRow = coldestHourInFile(currentParser);
                 double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
                 double coldestTemp = Double.parseDouble(coldestHourSoFar.get("TemperatureF"));
                 if (currentTemp < coldestTemp){
                    coldestHourSoFar = currentRow;                    
                    coldestTempName = f.getAbsolutePath();
                    
                 }
                 
            }
        }
        
        
        return coldestTempName;
    
    }
    
    public CSVRecord lowestHumidityManyFiles(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord lowestHSoFar = null;
                        
        for (File f : dr.selectedFiles()){
             FileResource currentFile = new FileResource(f);
             CSVParser currentParser = currentFile.getCSVParser();
             if (lowestHSoFar == null){
                 lowestHSoFar = lowestHumidityInFile(currentParser);
                 double lowestH = Double.parseDouble(lowestHSoFar.get("Humidity"));
                 
                 
            } else {
                 CSVRecord currentRow = lowestHumidityInFile(currentParser);
                 double currentH = Double.parseDouble(currentRow.get("Humidity"));
                 double lowestH = Double.parseDouble(lowestHSoFar.get("Humidity"));
                 if (currentH < lowestH){
                    lowestHSoFar = currentRow;                    
                                       
                 }
                 
            }
        }
        
        
        return lowestHSoFar;
    
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestHSoFar = null;
            
        for (CSVRecord currentRow : parser){
            String currentHumidity = currentRow.get("Humidity");
            if (currentHumidity.indexOf("N/A") == -1){
               lowestHSoFar = getSmallestOfTwo(lowestHSoFar,currentRow,"Humidity");
                        
            }          
                  
        }
        return lowestHSoFar;
    
    }
    
    public double averageTempInFile (CSVParser parser){
        double count = 0;
        double totalTemp = 0;
        
        for (CSVRecord currentRow: parser){
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if (currentTemp == -9999){} else{
            totalTemp += currentTemp;         
            count +=1;
           }
        }
        
        double avgTemp = totalTemp/count;
        return avgTemp;
               
    }
    
    public double avgTempHighHumidity (CSVParser parser, int value){
        double count = 0;
        double totalTemp = 0;
        
        for (CSVRecord currentRow: parser){
            double currentH = Double.parseDouble(currentRow.get("Humidity"));
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if (currentTemp!= -9999 && currentH >= value){            
            count +=1;
            totalTemp += currentTemp;         
        }
       }
    
       if (count == 0){
          return Double.NEGATIVE_INFINITY;
        }else{
          double avgTemp = totalTemp/count;
          return avgTemp;
       }
    }
    
    public void testAvgTempWithHHumidity(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double avgTemp = avgTempHighHumidity(parser, 80);
        if (avgTemp == Double.NEGATIVE_INFINITY){
            System.out.println("No temperatures with that humidity");
        }else {
            System.out.println("Average Temp when high Humidity is "+avgTemp);        
        }
           
    }
    
    public void testcoldestHourInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord coldestHourInFile = coldestHourInFile(parser);
        System.out.println(coldestHourInFile.get("TemperatureF"));
    }
    
    public void testFileWithColdestHour(){
        String fileName = fileWithColdestTemp();
        //String fileName = "weather-2014-01-03.csv";
        //String filePath = ("C:\\Users\\manmiche\\Documents\\ATA\\Weatherdata\\nc_weather\\2014\\" + fileName);
        String filePath = (fileName);
        FileResource fr = new FileResource(filePath);
        CSVParser parser = fr.getCSVParser();
        CSVRecord coldestHourInFile = coldestHourInFile(parser);
        String coldTemp = coldestHourInFile.get("TemperatureF");
        System.out.println("Coldest day was in file w"+ fileName);
        System.out.println("Coldest temperature on that day was "+coldTemp);
        
        parser = fr.getCSVParser();
        for (CSVRecord currentRow : parser){
            System.out.print(currentRow.get("DateUTC") + " ");
            System.out.println(currentRow.get("TemperatureF"));
            
        }
    
    }
    
    public void testLowestH(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowestHInFile = lowestHumidityInFile(parser);
        System.out.println("Lowest Humidity was "+lowestHInFile.get("Humidity")+" at "+lowestHInFile.get("DateUTC"));
    
    }
    
    public void testLowestHumidityInManyFiles(){
        CSVRecord lowestHFile = lowestHumidityManyFiles();
        System.out.println("Lowest Humidity was "+lowestHFile.get("Humidity")+" at "+lowestHFile.get("DateUTC"));
    
    
    } 
    
    public void testAverageTempInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double avgTemp = averageTempInFile(parser);
        System.out.println("Average temperature in file is "+avgTemp);
        
    }
}
