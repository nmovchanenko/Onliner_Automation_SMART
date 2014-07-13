package webdriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.SkipException;


/**
 * Base business entity
 *
 */
@SuppressWarnings("serial")
public class Entity extends BaseEntity implements Serializable, Cloneable {

	@Override
	protected final String formatLogMsg(String message) {
		return getLoc("loc.entity");
	}

	/**
	 * Default contructor
	 */
	public Entity() {
		prefix = this.getClass().getSimpleName();
	}

	/**
	 * Constructor + extra prefix
	 * @param prefixExtra prefixExtra
	 */
	public Entity(String prefixExtra) {
		prefix = this.getClass().getSimpleName() + "_" + prefixExtra;
	}


	/** Getting data from object
	 * @param item field
	 * @return value
	 */
	public final String getValue(Enum<?> item){
		String value = map.get(item.toString());
		logger.info("<<<<<Getting data :" + item.toString() + ":[" + value +"]");
		return value;
	}

	private HashMap<String, String> map = new HashMap<String, String>();
	public HashMap<String, String> getMap() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public void setMap(HashMap<String, String> map) {
		this.map = (HashMap<String, String>)map.clone();
	}

	private String prefix = "";

	/**
	 * getPrefix
	 * @return String
	 */
	public final String getPrefix() {
		return prefix;
	}

	/**
	 * setPrefix
	 * @param pr pr
	 */
	public final void setPrefix(String pr) {
		prefix = pr;
	}

	/** 
	 * Set value for field.
	 * @param item item
	 * @param value value
	 */
	public final void setValue(Enum<?> item, String value){
		logger.info(">>>>>Setting data " + item.toString() + ":[" + value + "]");
		map.put(item.toString(), value);
	}

	/** Serialize of base entities
	 */
	public final void serialize(){
		try{
			FileOutputStream fileOut = new FileOutputStream(this.getPrefix() + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		}catch(IOException i){
			logger.info("Serialize failed :" + i.getStackTrace());
		}
	}

	
	/**
	 * Save of Map to property file
	 */
	public final void save(){
		try{
			Properties properties = new Properties();
			properties.putAll(getMap());
			properties.store(new FileOutputStream(String.format("%s.properties",this.getPrefix())), null);
		}catch(IOException i){
			logger.info("Saving failed :" + i.getStackTrace());
		}
	}
	
	/**
	 * Load a Map from property file
	 */
	public final void load(){
		try{
			Properties properties = new Properties();
			properties.load(new FileInputStream(getPropertyFilePath()));
			setMap(new MapAdapter().convert(new HashMap<Object, Object>(properties)));
		}catch(IOException i){
			logger.info("Loading failed :" + i.getStackTrace());
		}
	}

	
	/**
	 * Adapter map
	 */
	public class MapAdapter {
	    public HashMap<String, String> convert(Map<Object, Object> oldMap) {
	    	HashMap<String, String> ret = new HashMap<String, String>();
	        for (Object key : oldMap.keySet()) {
	            ret.put(key.toString(), oldMap.get(key).toString());
	        }
	        return ret;
	    }
	}
	
	
	/** 
	 * Deserialize map from properties file
	 * @return 
	 */
	public final void getEntityFromProperties(){
		load();
	}

	/**
	 * Get properties file path
	 * @return
	 */
	private String getPropertyFilePath() {
		String path = this.getPrefix() + ".properties";
		File file = new File(path);
		if(!file.exists()){
			path = "../" + this.getPrefix() + ".properties";
			file = new File(path);
		}
		path = file.getAbsolutePath();
		return path;
	}
	
	
	/** 
	 * Deserialize object from file
	 */
	public final void getEntity(){

		try{
			String path = this.getPrefix() + ".ser";
			path = this.getPrefix() + ".ser";
			File file = new File(path);
			if(!file.exists()){
				path = "../" + this.getPrefix() + ".ser";
				file = new File(path);
			}
			path = file.getAbsolutePath();
			FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            this.setMap(((Entity)obj).getMap());
        }catch(IOException i){
            throw new SkipException("Deserialize failed :" + i.toString());
        }catch(ClassNotFoundException c){
        	throw new SkipException("Deserialize failed (verify file is present) :" + c.toString());
        }
	}

	/** Clone object
	 * @return Clone object
	 */
	@Override
	public final Object clone(){
		try{
			Entity e = (Entity)super.clone();
			e.map = new HashMap<String, String>();
			e.map.putAll(this.getMap());
			return e;
		}catch(Exception e){
			logger.fatal("Clone failed");
			return null;
		}
	}
}
