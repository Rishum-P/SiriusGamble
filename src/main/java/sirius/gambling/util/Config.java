package sirius.gambling.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sirius.gambling.SiriusGambling;

import java.io.File;
import java.io.IOException;

public class Config {


    private static File file;
    private static FileConfiguration Config;

    public static void setup(){

        //Finds the Config File or Generates if it does not exist.
        file = new File(SiriusGambling.plugin.getDataFolder(), "config.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            }
            catch (IOException e){

            }
        }
        Config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return Config;
    }

    public static void save(){
        try {
            Config.save(file);
        }
        catch (IOException e){
        }
    }

    public static void reload(){
        Config = YamlConfiguration.loadConfiguration(file);
    }
}